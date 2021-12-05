create table npos.pravidla_skupin_slev
(
	cislo_pravidla integer not null
		constraint uq_cislo_pravidla
			primary key,
	cislo_skupiny_slev integer not null
		constraint fk_pravidla_slev_skupiny_slev
			references npos.skupiny_slev,
	poradove_cislo integer not null,
	typ_podminky varchar(45) not null,
	operator varchar(45) not null,
	hodnota_podminky numeric(10,5),
	id_polozky_podminky varchar(45)
);

alter table npos.pravidla_skupin_slev owner to "nPos";

