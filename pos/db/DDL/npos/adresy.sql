create table npos.adresy
(
	id serial
		constraint uq_adresy
			primary key,
	druhy_radek varchar(100),
	cp varchar(10),
	cor varchar(10),
	ulice varchar(100),
	mesto varchar(100) not null,
	stat varchar(3) not null
		constraint fk_adresy_staty
			references npos.staty,
	telefon varchar(25),
	mobil varchar(25),
	fax varchar(25),
	email varchar(100),
	web varchar(100),
	psc varchar(15)
);

alter table npos.adresy owner to "nPos";

