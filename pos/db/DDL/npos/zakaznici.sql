create table npos.zakaznici
(
	id varchar(45) not null
		constraint zakaznici_pkey
			primary key
		constraint uq_id_zakaznika
			unique,
	nazev varchar(254),
	doplnek_nazvu varchar(254),
	ic varchar(15),
	dic varchar(20),
	blokovan boolean not null,
	duvod_blokace text,
	odebira_na_dl boolean not null,
	manualni_sleva_povolena boolean not null,
	skupina_slev integer
		constraint fk_zakaznici_skupiny_slev
			references npos.skupiny_slev,
	jmeno varchar(100),
	prijmeni varchar(100)
);

alter table npos.zakaznici owner to "nPos";

