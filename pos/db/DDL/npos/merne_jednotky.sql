create table npos.merne_jednotky
(
	mj varchar(5) not null
		constraint uq_mj
			primary key,
	oznaceni varchar(45) not null
		constraint uq_mj_oznaceni
			unique,
	zakladni_mj varchar(5)
		constraint fk_mj
			references npos.merne_jednotky,
	pomer real
);

alter table npos.merne_jednotky owner to "nPos";

