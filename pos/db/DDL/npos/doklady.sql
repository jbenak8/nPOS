create table npos.doklady
(
	id_dokladu uuid not null
		constraint doklady_pk
			primary key,
	cislo varchar(25) not null,
	poradove_cislo serial,
	typ varchar(20) not null,
	vratka boolean default false not null,
	storno boolean default false,
	datum_vystaveni timestamp not null,
	cislo_pokladni integer not null
		constraint fk_doklady_pokladni
			references npos.uzivatele,
	cislo_pokladny integer not null,
	cislo_smeny varchar(25) not null
		constraint fk_dooklady_smeny
			references npos.smeny,
	id_sparovaneho_dokladu uuid,
	castka_bez_dph numeric(10,5) not null,
	castka_vcetne_dph numeric(10,5) not null,
	filialka varchar(10) not null
		constraint fk_doklady_filialka
			references npos.filialka,
	zakaznik varchar(45)
		constraint fk_doklad_zakaznik
			references npos.zakaznici,
	zaplaceno numeric(10,5),
	vraceno numeric(10,5),
	replikace_ok boolean default false not null
);

alter table npos.doklady owner to "nPos";

create unique index doklady_cislo_uindex
	on npos.doklady (cislo);

