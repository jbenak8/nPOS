create table npos.kurzy_men
(
	cislo_kurzu integer default nextval('npos.kurzy_men_cislo_kurzu_seq'::regclass) not null
		constraint kurzy_men_pk
			primary key,
	iso_kod_meny varchar(3) not null
		constraint fk_kurz_mena
			references npos.mena,
	platnost_od date not null,
	platnost_do date,
	nakup numeric(10,5) not null,
	prodej numeric(10,5) not null
);

alter table npos.kurzy_men owner to "nPos";

