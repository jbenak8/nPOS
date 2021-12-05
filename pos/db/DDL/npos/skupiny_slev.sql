create table npos.skupiny_slev
(
	cislo integer default nextval('npos.skupiny_slev_cislo_seq'::regclass) not null
		constraint skupiny_slev_pkey
			primary key,
	nazev varchar(255) not null,
	typ_skupiny_slev varchar(45) not null,
	typ_slevy varchar(45) not null,
	hodnota_slevy numeric(10,5) not null,
	id_cile_slevy varchar(45),
	rozsah_pouziti varchar(45) not null,
	okamzik_uplatneni varchar(45) not null,
	platne boolean not null
);

alter table npos.skupiny_slev owner to "nPos";

