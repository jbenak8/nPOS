create table npos.smeny
(
	cislo_smeny varchar(20) not null
		constraint smeny_pk
			primary key,
	otevrena boolean default true not null,
	datum_otevreni timestamp not null,
	datum_uzavreni timestamp,
	pokladna integer not null,
	filialka varchar(10) not null
		constraint fk_smeny_filialka
			references npos.filialka
);

alter table npos.smeny owner to "nPos";

create unique index smeny_cislo_smeny_uindex
	on npos.smeny (cislo_smeny);

