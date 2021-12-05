create function npos.decrypt(bytea, bytea, text) returns bytea
	immutable
	strict
	parallel safe
	language c
as $$
begin
-- missing source code
end;
$$;

alter function npos.decrypt(bytea, bytea, text) owner to postgres;

