create function npos.pgp_pub_encrypt(text, bytea) returns bytea
	strict
	parallel safe
	language c
as $$
begin
-- missing source code
end;
$$;

alter function npos.pgp_pub_encrypt(text, bytea) owner to postgres;

create function npos.pgp_pub_encrypt(text, bytea, text) returns bytea
	strict
	parallel safe
	language c
as $$
begin
-- missing source code
end;
$$;

alter function npos.pgp_pub_encrypt(text, bytea, text) owner to postgres;

