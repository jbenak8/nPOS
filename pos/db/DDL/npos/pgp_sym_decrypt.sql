create function npos.pgp_sym_decrypt(bytea, text) returns text
	immutable
	strict
	parallel safe
	language c
as $$
begin
-- missing source code
end;
$$;

alter function npos.pgp_sym_decrypt(bytea, text) owner to postgres;

create function npos.pgp_sym_decrypt(bytea, text, text) returns text
	immutable
	strict
	parallel safe
	language c
as $$
begin
-- missing source code
end;
$$;

alter function npos.pgp_sym_decrypt(bytea, text, text) owner to postgres;

