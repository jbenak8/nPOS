create table npos.platebni_prostredky
(
	id varchar(5) not null
		constraint platebni_prostredky_pk
			primary key,
	typ varchar(20) not null,
	oznaceni varchar(255) not null,
	akceptovatelny boolean default true not null,
	cizi_mena_povolena boolean default true not null,
	zaokrouhlit_dle_denominace boolean default false not null,
	miminalni_castka numeric(20,5),
	maximalni_castka numeric(20,5),
	povoleno_pro_vratku boolean default true not null,
	povoleno_pro_storno boolean default true not null,
	evidence_v_zasuvce boolean not null,
	evidence_v_trezoru boolean not null,
	otevrit_zasuvku boolean not null,
	pocitani_nutne boolean not null,
	preferovany boolean default false not null
);

alter table npos.platebni_prostredky owner to "nPos";

create unique index platebni_prostredky_id_uindex
	on npos.platebni_prostredky (id);

create unique index platebni_prostredky_typ_uindex
	on npos.platebni_prostredky (typ);

