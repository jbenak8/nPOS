create function public.bytea_import(p_path text, OUT p_result bytea) returns bytea
	language plpgsql
as $$
declare
  l_oid oid;
begin
  select lo_import(p_path) into l_oid;
  select lo_get(l_oid) INTO p_result;
  perform lo_unlink(l_oid);
end;
$$;

alter function public.bytea_import(text, out bytea) owner to postgres;

