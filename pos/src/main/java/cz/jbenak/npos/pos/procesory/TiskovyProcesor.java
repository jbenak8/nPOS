package cz.jbenak.npos.pos.procesory;

import cz.jbenak.npos.pos.gui.Pomocnici;
import cz.jbenak.npos.pos.objekty.doklad.Doklad;
import cz.jbenak.npos.pos.objekty.doklad.RekapitulaceDPH;
import cz.jbenak.npos.pos.objekty.filialka.Filialka;
import cz.jbenak.npos.pos.objekty.tiskoveSestavy.paragon.ModelPolozkyParagonu;
import cz.jbenak.npos.pos.objekty.tiskoveSestavy.paragon.ModelPolozkyPlatby;
import cz.jbenak.npos.pos.objekty.tiskoveSestavy.paragon.ModelRekapitulaceDPH;
import cz.jbenak.npos.pos.objekty.tiskoveSestavy.paragon.ModelUplatnenePoukazy;
import cz.jbenak.npos.pos.system.Pos;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;

public class TiskovyProcesor {

    public void vytiskniDoklad() {
        try {
            Filialka filialka = Pos.getInstance().getFilialka();
            Doklad doklad = DokladProcesor.getInstance().getDoklad();
            Map<String, Object> parametry = new HashMap<>();
            File logo = new File(Pos.getInstance().getNastaveni().getProperty("tisk.paragony.logo"));
            parametry.put("logo", logo.getAbsolutePath());
            parametry.put("filialka_nazev", filialka.getNazev());
            parametry.put("filialka_ulice", filialka.getUlice());
            parametry.put("filialka_psc", filialka.getPsc());
            parametry.put("filialka_cp", filialka.getCp_cor());
            parametry.put("filialka_mesto", filialka.getMesto());
            parametry.put("filialka_ic", filialka.getIc());
            parametry.put("filialka_dic", filialka.getDic());
            parametry.put("cislo_dokladu", doklad.getCislo());
            parametry.put("cislo_pokladny", Pos.getInstance().getCisloPokladny());
            parametry.put("datum_cas", Pomocnici.formatujDatumACas(doklad.getDatumVytvoreni()));
            parametry.put("kopie", false);
            parametry.put("url", filialka.getUrl());
            parametry.put("mail", filialka.getEmail());
            parametry.put("hl_mena_symbol", Pos.getInstance().getHlavniMena().getNarodniSymbol());
            if (doklad.getZakaznik() != null) {
                parametry.put("zakaznik_cislo", doklad.getZakaznik().getCislo());
                String nazev = doklad.getZakaznik().getNazev() == null ? (doklad.getZakaznik().getJmeno() + " " + doklad.getZakaznik().getPrijmeni()) : doklad.getZakaznik().getNazev();
                parametry.put("zakaznik_nazev", nazev);
            }
            final List<ModelPolozkyParagonu> polozky = new ArrayList<>();
            doklad.getPolozky().forEach(pol -> {
                ModelPolozkyParagonu model = new ModelPolozkyParagonu();
                model.setRegistr(pol.getRegistr());
                model.setNazev(pol.getNazev());
                model.setCena_celkem(pol.getCenaCelkemBezeSlev());
                model.setSazba_dph(pol.getDph().getSazba());
                model.setJ_cena_mj(pol.getJednotkovaCena(), pol.getMnozstvi(), pol.getMj().getJednotka());
                model.setVratka(pol.isVracena());
                model.setOrig_doklad(doklad.getSparovanyDokladCislo());
                model.setSlevy(pol);
                polozky.add(model);
            });
            final List<ModelRekapitulaceDPH> dph = new ArrayList<>();
            doklad.getTabulkaDPH().sort(Comparator.comparing(RekapitulaceDPH::getDph));
            doklad.getTabulkaDPH().forEach(pol -> {
                ModelRekapitulaceDPH model = new ModelRekapitulaceDPH();
                model.setSazba(pol.getDph().getSazba());
                model.setZaklad(pol.getZaklad());
                model.setDan(pol.getDan());
                model.setCelkem(pol.getCelkem());
                dph.add(model);
            });
            parametry.put("RekapitulaceDPH", new JRBeanCollectionDataSource(dph));
            List<ModelPolozkyPlatby> platby = new ArrayList<>();
            doklad.getPlatby().forEach(pol -> {
                ModelPolozkyPlatby model = new ModelPolozkyPlatby();
                model.setTyp(pol.getPlatebniProstredek().getTyp());
                model.setZaplaceno(pol.getCastka());
                model.setMena(pol.getMena());
                model.setKurz(pol.getKurzMeny());
                model.setHodnota_platby(pol.getCastkaVKmenoveMene());
                platby.add(model);
            });
            parametry.put("SeznamPlateb", new JRBeanCollectionDataSource(platby));
            BigDecimal celkem = PlatebniProcesor.getInstance().jePosledniPlatbaHotovost() ? PlatebniProcesor.getInstance().zaokrouhliVeVybraneMene(Pos.getInstance().getHlavniMena(), doklad.getCenaDokladuCelkem()) : doklad.getCenaDokladuCelkem();
            parametry.put("celkem_doklad", Pomocnici.formatujNaDveMista(celkem) + " " + Pos.getInstance().getHlavniMena().getNarodniSymbol()); // Platba hotovostí se zaokrouhlením
            parametry.put("zaplaceno_celkem", Pomocnici.formatujNaDveMista(doklad.getCelkemZaplaceno()));
            parametry.put("vraceno", Pomocnici.formatujNaDveMista(doklad.getCastkaKVraceni()));
            if (PlatebniProcesor.getInstance().jePosledniPlatbaHotovost()) {
                parametry.put("hodnota_zaokrouhleni", Pomocnici.formatujNaDveMista(doklad.getCenaDokladuCelkem().subtract(PlatebniProcesor.getInstance().zaokrouhliVeVybraneMene(Pos.getInstance().getHlavniMena(), doklad.getCenaDokladuCelkem())).abs()));
            }
            parametry.put("data_EFT", """
                    A00000000000031010
                    Visa Contactless    (CL)
                         **** **** **** 1737
                              PRODEJ
                    Částka CZK       123,456
                              PIN OK
                    Autorizační kód:  123456                  
                    """);
            List<ModelUplatnenePoukazy> poukazy = new ArrayList<>();
            doklad.getUplatnenePoukazy().forEach(pol -> {
                ModelUplatnenePoukazy model = new ModelUplatnenePoukazy();
                model.setCislo(pol.getCisloPoukazu());
                model.setCastka(pol.getTypHodnoty(), pol.getUplatnenaHodnota());
                poukazy.add(model);
            });
            parametry.put("UplatnenePoukazy", new JRBeanCollectionDataSource(poukazy));
            parametry.put("provozovna", "EET provozovna");
            parametry.put("rezim_trzby", "Běžný");
            parametry.put("bkp", "BKP: už nebude");
            parametry.put("pkp_fik", "FIK: už nebude");
            parametry.put("tisk_reklamy", true);
            parametry.put("tisk_eet", true);
            parametry.put("paticka_1radek", "Děkujeme Vám za návštěvu");
            parametry.put("paticka_2radek", "a přejeme Vám příjemný den.");
            parametry.put("carovy_kod", doklad.getCislo());
            JasperPrint print = JasperFillManager.fillReport("c:/IDEA_PROJEKTY/nPOS/pos/src/main/zdroje/sestavy/paragon.jasper", parametry, new JRBeanCollectionDataSource(polozky));
            JasperPrintManager.printReport(print, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
