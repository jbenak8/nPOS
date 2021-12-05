create table npos.skupiny_sortimentu
(
	id varchar(45) not null
		constraint uq_skupiny_sortimentu
			primary key,
	nazev varchar(80) not null,
	id_nadrazene_skupiny varchar(45)
		constraint fk_skupiny_sortimentu
			references npos.skupiny_sortimentu,
	sleva_povolena boolean default true not null,
	max_sleva numeric(10,5)
);

alter table npos.skupiny_sortimentu owner to "nPos";

