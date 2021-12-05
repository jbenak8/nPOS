create table npos.platby
(
	cislo_platby serial
		constraint platby_pk
			primary key,
	doklad uuid not null
		constraint fk_platby_doklady
			references npos.doklady,
	cislo_polozky integer not null,
	vraceno boolean default false not null,
	mena varchar(3) not null
		constraint fk_platby_meny
			references npos.mena,
	castka numeric(10,5) not null,
	castka_v_kmenove_mene numeric(10,5),
	platebni_prostredek varchar(5) not null
		constraint fk_platby_platebni_prostredky
			references npos.platebni_prostredky,
	kurz integer
		constraint fk_platby_kurzy
			references npos.kurzy_men
);

alter table npos.platby owner to "nPos";

create unique index platby_cislo_platby_uindex
	on npos.platby (cislo_platby);

