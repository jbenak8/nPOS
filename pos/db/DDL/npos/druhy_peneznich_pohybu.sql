create table npos.druhy_peneznich_pohybu
(
	cislo_druhu_pohybu serial
		constraint druhy_peneznich_pohybu_pk
			primary key,
	pohyb varchar(5) default 'VKLAD'::character varying not null,
	typ_dokladu varchar(20) not null,
	nazev varchar(100) not null,
	predkontace varchar(10)
);

comment on column npos.druhy_peneznich_pohybu.pohyb is 'VKLAD nebo VYBER';

alter table npos.druhy_peneznich_pohybu owner to "nPos";

create unique index druhy_peneznich_pohybu_nazev_uindex
	on npos.druhy_peneznich_pohybu (nazev);

