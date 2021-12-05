create table npos.obrazky_platidel_na_tlacitka
(
	cislo_zaznamu serial,
	denominace integer not null
		constraint fk_obrazky_platidel_na_tlacitka_denominace
			references npos.denominace,
	obrazek bytea not null
);

alter table npos.obrazky_platidel_na_tlacitka owner to "nPos";

