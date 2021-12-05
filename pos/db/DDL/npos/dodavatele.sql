create table npos.dodavatele
(
	id serial
		constraint pk_dodavatele
			primary key,
	nazev varchar(80) not null,
	doplnek_nazvu varchar(100),
	ic varchar(15) not null,
	dic varchar(15),
	constraint uq_dodavatele
		unique (id, ic)
);

alter table npos.dodavatele owner to "nPos";

