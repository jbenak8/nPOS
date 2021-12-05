create function npos.armor(bytea) returns text
	immutable
	strict
	parallel safe
	language c
as $$
begin
-- missing source code
end;
$$;

alter function npos.armor(bytea) owner to postgres;

create function npos.armor(bytea, text[], text[]) returns text
	immutable
	strict
	parallel safe
	language c
as $$
begin
-- missing source code
end;
$$;

alter function npos.armor(bytea, text[], text[]) owner to postgres;

