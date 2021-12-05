create table npos.rekapitulace_dph
(
	cislo_zaznamu serial
		constraint rekapitulace_dph_pk
			primary key,
	doklad uuid not null
		constraint fk_dph_doklady
			references npos.doklady,
	cislo_dph integer not null
		constraint fk_dph_dan
			references npos.dph,
	zaklad numeric(10,5) not null,
	dan numeric(10,5) not null,
	celkem numeric(10,5) not null
);

alter table npos.rekapitulace_dph owner to "nPos";

create unique index rekapitulace_dph_cislo_zaznamu_uindex
	on npos.rekapitulace_dph (cislo_zaznamu);

