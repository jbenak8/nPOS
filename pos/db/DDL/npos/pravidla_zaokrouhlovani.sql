create table npos.pravidla_zaokrouhlovani
(
	cislo_pravidla serial
		constraint pravidla_zaokrouhlovani_pk
			primary key,
	id_platebniho_prostredku varchar(5) not null
		constraint fk_pravidla_zaokrouhlovani_platebni_prostredky
			references npos.platebni_prostredky,
	iso_kod_meny varchar(3) not null
		constraint fk_pravidla_zaokrouhlovani_mena
			references npos.mena,
	hodnota_od numeric(10,5) not null,
	hodnota_do numeric(10,5) not null,
	hodnota_zaokrouhleni numeric(10,5) not null,
	smer varchar(6) default 'NAHORU'::character varying not null
);

comment on column npos.pravidla_zaokrouhlovani.smer is 'NAHORU nebo DOLU';

alter table npos.pravidla_zaokrouhlovani owner to "nPos";

