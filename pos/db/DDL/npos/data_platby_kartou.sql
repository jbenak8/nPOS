create table npos.data_platby_kartou
(
	cislo_zaznamu serial
		constraint data_platby_kartou_pk
			primary key,
	doklad uuid not null
		constraint fk_data_platby_kartou_doklady
			references npos.doklady,
	cislo_platby integer not null,
	cislo_karty varchar(16) not null,
	cislo_transakce varchar(20) not null,
	vratka boolean default false not null,
	cislo_terminalu varchar(20) not null,
	autorizacni_id varchar(20) not null,
	stav_transakce varchar(15),
	datum_a_cas timestamp not null,
	castka numeric(10,5) not null,
	vybrana_mena varchar(3) not null
		constraint fk_data_platby_kartou_meny
			references npos.mena,
	data_terminalu text not null,
	druh_karty varchar(25) not null
);

comment on column npos.data_platby_kartou.stav_transakce is 'stavy SCHVALENO, ZAMITNUTO, CHYBA';

alter table npos.data_platby_kartou owner to "nPos";

create unique index data_platby_kartou_cislo_zaznamu_uindex
	on npos.data_platby_kartou (cislo_zaznamu);

