create view npos.seznam_sortimentu(registr, skupina, nazev_skupiny, nazev, jednotkova_cena, kod, prodej_povolen) as
	SELECT sortiment.registr,
       sortiment.skupina,
       skupiny_sortimentu.nazev AS nazev_skupiny,
       sortiment.nazev,
       sortiment.jednotkova_cena,
       carove_kody.kod,
       sortiment.prodej_povolen
FROM npos.sortiment
         JOIN npos.skupiny_sortimentu ON skupiny_sortimentu.id::text = sortiment.skupina::text
         LEFT JOIN npos.carove_kody ON carove_kody.registr::text = sortiment.registr::text
ORDER BY sortiment.registr;

alter table npos.seznam_sortimentu owner to "nPos";

