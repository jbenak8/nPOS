create table npos.dph
(
	id serial
		constraint dph_pkey
			primary key,
	typ varchar(45) not null,
	sazba real not null,
	oznaceni varchar(45) not null,
	platnost_od date not null,
	platnost_do date,
	constraint uq_dph
		unique (id, typ, oznaceni, sazba)
);

alter table npos.dph owner to "nPos";

