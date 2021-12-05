create table npos.mena
(
	iso_kod varchar(3) not null
		constraint mena_pkey
			primary key,
	oznaceni varchar(45) not null,
	narodni_symbol varchar(3) not null,
	akceptovatelna boolean not null,
	kmenova boolean not null,
	constraint uq_meny
		unique (iso_kod, oznaceni, narodni_symbol)
);

alter table npos.mena owner to "nPos";

