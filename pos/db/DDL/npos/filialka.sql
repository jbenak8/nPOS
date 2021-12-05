create table npos.filialka
(
	oznaceni varchar(10) not null
		constraint uq_filialka
			primary key,
	nazev varchar(60) not null,
	popis varchar(60),
	cp_cor varchar(10),
	ulice varchar(45),
	mesto varchar(45) not null,
	psc varchar(10) not null,
	stat varchar(3) not null
		constraint fk_filialka_staty
			references npos.staty,
	telefon varchar(30),
	mobil varchar(30),
	email varchar(60),
	odpovedny_vedouci varchar(60),
	ic varchar(15) not null,
	dic varchar(15),
	danovy_kod_dph varchar(45),
	nazev_spolecnosti varchar(45) not null,
	adresa_spolecnosti text,
	url text,
	zapis_or text,
	eet_rezim integer default 0,
	eet_id_provozovny integer,
	eet_poverujici_dic varchar(15)
);

alter table npos.filialka owner to "nPos";

