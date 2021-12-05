create function npos.gen_random_bytes(integer) returns bytea
	strict
	parallel safe
	language c
as $$
begin
-- missing source code
end;
$$;

alter function npos.gen_random_bytes(integer) owner to postgres;

