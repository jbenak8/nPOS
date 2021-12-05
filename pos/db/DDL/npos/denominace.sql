create table npos.denominace
(
	cislo serial
		constraint denominace_pk
			primary key,
	typ_platidla varchar(8) default 'MINCE'::character varying not null,
	iso_kod_meny varchar(3) not null
		constraint fk_denominace_meny
			references npos.mena,
	poradi integer not null,
	jednotka integer not null,
	hodnota numeric(10,2) not null,
	nazev_jednotky varchar(15) not null,
	ks_balicek integer,
	akceptovatelne boolean default true not null,
	zobrazit_na_tlacitku boolean default false
);

comment on column npos.denominace.typ_platidla is 'MINCE nebo BANKOVKA';

comment on column npos.denominace.jednotka is 'Velikost (hodnota) daného platidla:
např. 1 haléř - nominál 1, velikost 0.01';

comment on column npos.denominace.hodnota is 'hodnota vůči základní jednotce';

comment on column npos.denominace.ks_balicek is 'Obvyklý počet kusů platidla v balíčku/ruličce';

alter table npos.denominace owner to "nPos";

