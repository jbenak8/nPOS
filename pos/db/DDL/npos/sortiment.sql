create table npos.sortiment
(
	registr varchar(45) not null
		constraint uq_sortiment
			primary key,
	skupina varchar(45) not null
		constraint fk_sortiment_skupiny_sortimentu
			references npos.skupiny_sortimentu,
	nazev varchar(255) not null,
	plu varchar(5),
	mj varchar(5) not null
		constraint fk_sortiment_mj
			references npos.merne_jednotky,
	dph integer not null
		constraint fk_sortiment_dph
			references npos.dph,
	minimalni_prodejne_mnozstvi numeric(10,5) default 1 not null,
	maximalni_prodejne_mnozstvi numeric(10,5),
	prodej_povolen boolean default true not null,
	jednotkova_cena numeric(10,5) not null,
	dodavatel integer
		constraint fk_sortiment_dodavatele
			references npos.dodavatele,
	sleva_povolena boolean default true not null,
	zmena_ceny_povolena boolean default true not null,
	neskladova boolean default false not null,
	sluzba boolean default false not null,
	nutno_zadat_cenu boolean default false not null,
	platebni_poukaz boolean default false not null,
	vratka_povolena boolean default true not null,
	nedelitelne boolean default false not null,
	popis text,
	obrazek bytea,
	nutno_zadat_popis boolean default false not null,
	obsah_baleni numeric(10,5),
	obsahova_mj varchar(5)
		constraint fk_sortiment_obsahova_mj
			references npos.merne_jednotky,
	pomerne_mnozstvi numeric(10,5) default 1,
	zakladni_obsahova_mj varchar(5)
		constraint fk_sortiment_zakladni_obsahova_mj
			references npos.merne_jednotky,
	zakladni_obsahove_mnozstvi numeric(10,5) default 1,
	nutno_zadat_mnozstvi boolean default false not null,
	sleva_v_akci_povolena boolean default true not null
);

alter table npos.sortiment owner to "nPos";

