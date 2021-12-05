create table npos.staty
(
	iso_kod varchar(3) not null
		constraint staty_pkey
			primary key,
	bezny_nazev varchar(45) not null,
	uplny_nazev varchar(150) not null,
	hlavni boolean not null,
	mena varchar not null
		constraint fk_staty_meny
			references npos.mena,
	constraint uq_staty
		unique (iso_kod, bezny_nazev, uplny_nazev)
);

alter table npos.staty owner to "nPos";

