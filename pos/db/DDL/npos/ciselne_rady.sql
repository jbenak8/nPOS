create table npos.ciselne_rady
(
	rada varchar(45) not null
		constraint ciselne_rady_pk
			primary key,
	typ_dokladu varchar(20) not null,
	platnost_od date not null,
	platnost_do date,
	delka_poradoveho_cisla integer default 5 not null
);

comment on column npos.ciselne_rady.typ_dokladu is 'PARAGON, FAKTURA, DODACI_LIST, DOBROPIS, VKLAD, VYBER';

alter table npos.ciselne_rady owner to "nPos";

create unique index ciselne_rady_rada_uindex
	on npos.ciselne_rady (rada);

