create table npos.adresy_dodavatelu
(
	dodavatel integer not null
		constraint adresy_dodavatelu_pkey
			primary key
		constraint fk_adresy_dodavatelu_dodavatele
			references npos.dodavatele,
	adresa integer not null
		constraint fk_adresy_dodavatelu_adresy
			references npos.adresy,
	hlavni boolean default true not null
);

alter table npos.adresy_dodavatelu owner to "nPos";

