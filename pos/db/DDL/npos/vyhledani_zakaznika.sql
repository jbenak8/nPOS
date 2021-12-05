create view npos.vyhledani_zakaznika(id, nazev, doplnek_nazvu, jmeno, prijmeni, ic, dic, blokovan, duvod_blokace, odebira_na_dl, manualni_sleva_povolena, skupina_slev, druhy_radek, cp, cor, ulice, mesto, stat, telefon, mobil, fax, email, web, psc, dodaci, id_adresy) as
	SELECT zakaznici.id,
       zakaznici.nazev,
       zakaznici.doplnek_nazvu,
       zakaznici.jmeno,
       zakaznici.prijmeni,
       zakaznici.ic,
       zakaznici.dic,
       zakaznici.blokovan,
       zakaznici.duvod_blokace,
       zakaznici.odebira_na_dl,
       zakaznici.manualni_sleva_povolena,
       zakaznici.skupina_slev,
       adresy.druhy_radek,
       adresy.cp,
       adresy.cor,
       adresy.ulice,
       adresy.mesto,
       adresy.stat,
       adresy.telefon,
       adresy.mobil,
       adresy.fax,
       adresy.email,
       adresy.web,
       adresy.psc,
       adresy_zakazniku.dodaci,
       adresy.id AS id_adresy
FROM npos.zakaznici
         JOIN npos.adresy_zakazniku ON adresy_zakazniku.zakaznik::text = zakaznici.id::text
         JOIN npos.adresy ON adresy.id = adresy_zakazniku.adresa
WHERE adresy_zakazniku.hlavni = true
ORDER BY zakaznici.id;

alter table npos.vyhledani_zakaznika owner to "nPos";

