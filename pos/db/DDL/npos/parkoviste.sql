create table npos.parkoviste
(
	uid uuid not null
		constraint parkoviste_pkey
			primary key,
	vytvoreno timestamp not null,
	doklad bytea not null
);

alter table npos.parkoviste owner to "nPos";

