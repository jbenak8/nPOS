package cz.jbenak.npos.pos.procesy.platba;

import cz.jbenak.npos.pos.objekty.doklad.ChybyEET;
import cz.jbenak.npos.pos.objekty.doklad.DataEET;
import cz.jbenak.npos.pos.objekty.doklad.Doklad;
import cz.jbenak.npos.pos.procesory.DokladProcesor;
import cz.jbenak.npos.pos.system.Pos;
import cz.jbenak.npos.pos.system.util.Sifrovani;
import javafx.concurrent.Task;
import openeet.lite.EetRegisterRequest;
import openeet.lite.EetResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.net.URL;

public class EvidenceEET extends Task<Boolean> {

    private static final Logger LOGER = LogManager.getLogger(EvidenceEET.class);
    private final boolean prvotniZaslani;
    private String bkp = "";
    private String pkp = "";

    public EvidenceEET(boolean prvotniZaslani) {
        this.prvotniZaslani = prvotniZaslani;
    }

    @Override
    protected Boolean call() throws Exception {
        pripravHlavicku();
        try {
            EetRegisterRequest request = pripravOdeslani();
            if (request != null) {
                LOGER.info("Datová zpráva bude nyní odeslána na servery daňové správy.");
                final String response = request.sendRequest(request.generateSoapRequest(), new URL(Pos.getInstance().getNastaveni().getProperty("fiskalizace.eet.url")));
                if (response != null && !response.isEmpty()) {
                    LOGER.error("Data ze serverů finanční správy přijata.");
                    EetResponse odpoved = new EetResponse(response);
                    zpracujOdpoved(odpoved);
                } else {
                    LOGER.error("Servery finanční správy nevrátily žádnou odpověď.");
                    DataEET eet = DokladProcesor.getInstance().getDoklad().getDataEET();
                    eet.setOdeslaniOK(false);
                    eet.setBkp(request.formatBkp());
                    eet.setPkp(request.formatPkp());
                    ChybyEET chyby = new ChybyEET();
                    chyby.setCislo(1000);
                    chyby.setPopis("Servery finanční správy nevrátily žádnou odpověď. - možný problém se spojením.");
                    eet.getChybyEET().add(chyby);
                }
            }
        } catch (Exception e) {
            LOGER.error("Nastala chyba zpracování při evidenci EET.", e);
            DataEET eet = DokladProcesor.getInstance().getDoklad().getDataEET();
            eet.setOdeslaniOK(false);
            eet.setBkp(bkp);
            eet.setPkp(pkp);
            ChybyEET chyby = new ChybyEET();
            chyby.setCislo(-1);
            chyby.setPopis("Nastala obecná chyba zpracování při evidenci EET.");
            eet.getChybyEET().add(chyby);
        }
        return true;
    }

    private void pripravHlavicku() {
        Doklad doklad = DokladProcesor.getInstance().getDoklad();
        DataEET eet = new DataEET();
        eet.setPrvniOdeslani(prvotniZaslani);
        eet.setDatum(doklad.getDatumVytvoreni());
        eet.setCelkovaCastka(doklad.getCenaDokladuCelkem());
        eet.setRezim(Pos.getInstance().getFilialka().getEetRezim());
        eet.setIdProvozovny(Pos.getInstance().getFilialka().getEetIdProvozovny());
        eet.setDic(Pos.getInstance().getFilialka().getDic());
        eet.setPoverujiciDic(Pos.getInstance().getFilialka().getEetPoverujiciDic());
        DokladProcesor.getInstance().getDoklad().setDataEET(eet);
    }

    private EetRegisterRequest pripravOdeslani() throws Exception {
        EetRegisterRequest request;
        try (FileInputStream fis = new FileInputStream(Pos.getInstance().getNastaveni().getProperty("fiskalizace.eet.certifikat"))) {
            Doklad doklad = DokladProcesor.getInstance().getDoklad();
            final EetRegisterRequest.Builder builder = EetRegisterRequest.builder();
            builder.pkcs12(EetRegisterRequest.loadStream(fis));
            builder.pkcs12password(Sifrovani.decodeString(Pos.getInstance().getNastaveni().getProperty("fiskalizace.eet.heslo")));
            builder.rezim(doklad.getDataEET().getRezim());
            builder.overeni(false);
            builder.celk_trzba(doklad.getDataEET().getCelkovaCastka().doubleValue());
            builder.porad_cis(doklad.getCislo());
            builder.dic_popl(doklad.getDataEET().getDic());
            if (doklad.getDataEET().getPoverujiciDic() != null && !doklad.getDataEET().getPoverujiciDic().isEmpty()) {
                builder.dic_poverujiciho(doklad.getDataEET().getPoverujiciDic());
            }
            builder.id_pokl(Integer.toString(Pos.getInstance().getCisloPokladny()));
            builder.id_provoz(Integer.toString(doklad.getDataEET().getIdProvozovny()));
            doklad.getTabulkaDPH().forEach(pol -> {
                switch (pol.getDph().getTyp()) {
                    case NULOVA -> builder.zakl_nepodl_dph(pol.getZaklad().doubleValue());
                    case ZAKLADNI -> {
                        builder.zakl_dan1(pol.getZaklad().doubleValue());
                        builder.dan1(pol.getDan().doubleValue());
                    }
                    case SNIZENA_1 -> {
                        builder.zakl_dan2(pol.getZaklad().doubleValue());
                        builder.dan2(pol.getDan().doubleValue());
                    }
                    case SNIZENA_2 -> {
                        builder.zakl_dan3(pol.getZaklad().doubleValue());
                        builder.dan3(pol.getDan().doubleValue());
                    }
                }
            });
            request = builder.build();
            bkp = request.formatBkp();
            pkp = request.formatPkp();
        }
        return request;
    }

    private void zpracujOdpoved(EetResponse odpoved) {
        LOGER.info("Přijatá EET zpráva bude nyní zpracována.");
        DataEET eet = DokladProcesor.getInstance().getDoklad().getDataEET();
        eet.setBkp(odpoved.getBkp());
        if (odpoved.isError()) {
            LOGER.error("Bylo vráceno chybové hlášení - kód chyby {}, zpráva: {}. Podání nebylo provedeno.", odpoved.getErrCode(), odpoved.getErrText());
            ChybyEET chyba = new ChybyEET();
            chyba.setCislo(Integer.parseInt(odpoved.getErrCode()));
            chyba.setPopis(odpoved.getErrText());
            eet.getChybyEET().add(chyba);
            eet.setPkp(pkp);
            eet.setOdeslaniOK(false);
        } else if (odpoved.hasWarnings()) {
            LOGER.warn("Podání EET bylo sice přijato, avšak s chybami.");
            String[] chyby = odpoved.getWarnings().split("\n");
            for (String radka : chyby) {
                ChybyEET chyba = new ChybyEET();
                String[] radkaDeleno = radka.split("]");
                if (radkaDeleno.length == 2) {
                    chyba.setCislo(Integer.parseInt(radkaDeleno[0].substring(1).trim()));
                    chyba.setPopis(radkaDeleno[1].trim());
                } else {
                    chyba.setPopis(radka);
                    chyba.setCislo(0);
                }
                eet.getChybyEET().add(chyba);
            }
            eet.setFik(odpoved.getFik());
            eet.setOdeslaniOK(true);
        } else {
            LOGER.info("Podání EET bylo přijato v pořádku.");
            eet.setFik(odpoved.getFik());
            eet.setOdeslaniOK(true);
        }
    }
}
