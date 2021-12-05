create table npos.eet
(
	id_zaznamu serial
		constraint eet_pk
			primary key,
	doklad uuid not null
		constraint fk_eet_doklady
			references npos.doklady,
	prvni_odeslani boolean default true not null,
	odeslani_ok boolean not null,
	datum_odeslani timestamp not null,
	bkp varchar(60) not null,
	pkp text,
	fik varchar(60),
	celkova_trzba numeric(10,5) not null,
	cislo_pokladny integer not null,
	id_provozovny integer not null,
	dic varchar(15),
	poverujici_dic varchar(15),
	chyby text,
	rezim integer default 0 not null,
	cislo_dokladu varchar(25) not null
);

alter table npos.eet owner to "nPos";

create unique index eet_id_zaznamu_uindex
	on npos.eet (id_zaznamu);

