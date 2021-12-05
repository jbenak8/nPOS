create table npos.hlavicka_faktury
(
	cislo_zaznamu serial
		constraint hlavicka_faktury_pk
			primary key,
	doklad uuid not null
		constraint fk_hlavicka_faktury_doklady
			references npos.doklady,
	nazev varchar(255) not null,
	ulice varchar(100),
	mesto varchar(100),
	psc varchar(15),
	cp varchar(10),
	cor varchar(10),
	stat varchar(3) not null
		constraint fk_hlavicka_faktury_staty
			references npos.staty,
	ic varchar(15),
	dic varchar(20),
	nazev_druhy_radek varchar(100),
	jmeno varchar(100),
	prijmeni varchar,
	id_zakaznika varchar(45)
		constraint fk_hlavicka_faktury_zakaznici
			references npos.zakaznici
);

alter table npos.hlavicka_faktury owner to "nPos";

create unique index hlavicka_faktury_cislo_zaznamu_uindex
	on npos.hlavicka_faktury (cislo_zaznamu);

