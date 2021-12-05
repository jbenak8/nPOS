create table npos.slevy_na_doklad
(
	cislo_zaznamu serial
		constraint slevy_na_doklad_pk
			primary key,
	doklad uuid not null
		constraint fk_slevy_na_doklad_doklady
			references npos.doklady,
	cislo_skupiny_slev integer not null
		constraint fk_slevy_na_doklad_skupiny_slev
			references npos.skupiny_slev,
	hodnota numeric(10,5) not null
);

alter table npos.slevy_na_doklad owner to "nPos";

create unique index slevy_na_doklad_cislo_zaznamu_uindex
	on npos.slevy_na_doklad (cislo_zaznamu);

