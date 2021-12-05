create table npos.uzivatele
(
	id_uzivatele integer not null
		constraint uq_id_uzivatele
			primary key,
	skupina integer not null
		constraint fk_uzivatele_skupiny
			references npos.uzivatelske_skupiny,
	jmeno varchar(50),
	prijmeni varchar(50),
	blokovany boolean not null,
	heslo varchar(50) not null,
	pocet_prihlasovacich_pokusu integer not null
);

alter table npos.uzivatele owner to "nPos";

