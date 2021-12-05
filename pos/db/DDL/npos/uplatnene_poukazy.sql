create table npos.uplatnene_poukazy
(
	cislo_zaznamu serial
		constraint uplatnene_poukazy_pk
			primary key,
	doklad uuid not null
		constraint fk_uplatnene_poukazy_doklady
			references npos.doklady,
	cislo_poukazu varchar(25) not null,
	hodnota_poukazu numeric(10,5) not null,
	castecne_uplatneni boolean default false not null,
	vazana_polozka_platby integer not null
		constraint fk_uplatnene_poukazy_platby
			references npos.platby,
	vazana_skupina_slev integer
		constraint fk_uplatnene_poukazy_skupiny_slev
			references npos.skupiny_slev,
	typ_hodnoty varchar(10) default 'HODNOTOVY'::character varying not null,
	uplatnena_hodnota numeric(10,5) not null
);

comment on column npos.uplatnene_poukazy.typ_hodnoty is 'PROCENTNI, HODNOTOVY';

alter table npos.uplatnene_poukazy owner to "nPos";

create unique index uplatnene_poukazy_cislo_zaznamu_uindex
	on npos.uplatnene_poukazy (cislo_zaznamu);

