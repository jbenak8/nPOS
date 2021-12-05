create function npos.hmac(text, text, text) returns bytea
	immutable
	strict
	parallel safe
	language c
as $$
begin
-- missing source code
end;
$$;

alter function npos.hmac(text, text, text) owner to postgres;

create function npos.hmac(bytea, bytea, text) returns bytea
	immutable
	strict
	parallel safe
	language c
as $$
begin
-- missing source code
end;
$$;

alter function npos.hmac(bytea, bytea, text) owner to postgres;

