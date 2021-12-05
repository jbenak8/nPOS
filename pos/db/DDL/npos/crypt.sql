create function npos.crypt(text, text) returns text
	immutable
	strict
	parallel safe
	language c
as $$
begin
-- missing source code
end;
$$;

alter function npos.crypt(text, text) owner to postgres;

