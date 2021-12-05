create table npos.adresy_zakazniku
(
	zakaznik varchar(60) not null
		constraint fk_zakaznici_adresy_zakazniu
			references npos.zakaznici,
	adresa integer not null
		constraint fk_adresy_adresy_zakazniku
			references npos.adresy,
	hlavni boolean not null,
	dodaci boolean default false not null
);

alter table npos.adresy_zakazniku owner to "nPos";

