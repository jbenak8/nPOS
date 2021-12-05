create table npos.slevy_na_polozku
(
	cislo_zaznamu integer default nextval('npos.slevy_na_polozku_cislo_zaznamu_seq'::regclass) not null
		constraint slevy_na_polozku_pkey
			primary key,
	cislo_polozky integer not null
		constraint fk_slevy_na_polozku_polozky
			references npos.polozky_prodejniho_dokladu,
	doklad uuid not null
		constraint fk_slevy_na_polozku_doklady
			references npos.doklady,
	cislo_skupiny_slev integer not null
		constraint fk_slevy_na_polozku_skupiny_slev
			references npos.skupiny_slev,
	hodnota numeric(10,5) not null,
	registr varchar(45) not null
		constraint fk_slevy_na_polozku_sortiment
			references npos.sortiment
);

alter table npos.slevy_na_polozku owner to "nPos";

