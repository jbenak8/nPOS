create table npos.polozky_prodejniho_dokladu
(
	cislo_polozky serial
		constraint polozky_prodejniho_dokladu_pk
			primary key,
	doklad uuid not null
		constraint fk_polozky_doklady
			references npos.doklady,
	cislo_radku integer not null,
	registr varchar(45) not null
		constraint pk_polozky_sortiment
			references npos.sortiment,
	vraceno boolean default false not null,
	mnozstvi numeric(10,5) not null,
	jednotkova_cena numeric(10,5) not null,
	cena_polozky_celkem numeric(10,5) not null,
	originalni_doklad uuid,
	cena_polozky_celkem_s_dph numeric(10,5) not null,
	hodnota_dph numeric(10,5) not null
);

alter table npos.polozky_prodejniho_dokladu owner to "nPos";

create unique index polozky_prodejniho_dokladu_cislo_polozky_uindex
	on npos.polozky_prodejniho_dokladu (cislo_polozky);

