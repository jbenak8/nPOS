create table npos.uzivatelske_skupiny
(
	id_uzivatelske_skupiny integer not null
		constraint uzivatelske_skupiny_pkey
			primary key
		constraint uq_id_uzivatelske_skupiny
			unique,
	oznaceni_skupiny varchar(45) not null
		constraint uq_oznaceni_uzivatelske_skupiny
			unique,
	poznamka text
);

alter table npos.uzivatelske_skupiny owner to "nPos";

