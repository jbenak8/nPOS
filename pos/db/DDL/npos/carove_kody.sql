create table npos.carove_kody
(
	kod varchar(255) not null
		constraint uq_carove_kody
			primary key,
	registr varchar(45) not null
		constraint fk_carove_kody_sortiment
			references npos.sortiment,
	hlavni boolean default true not null,
	mnozstvi numeric(10,5) default 1 not null,
	cena numeric(10,5),
	typ varchar(20) not null,
	cena_pozice integer,
	cena_delka integer,
	cena_desetinnych_mist integer,
	mnozstvi_pozice integer,
	mnozstvi_delka integer,
	mnozstvi_desetinnych_mist integer
);

alter table npos.carove_kody owner to "nPos";

