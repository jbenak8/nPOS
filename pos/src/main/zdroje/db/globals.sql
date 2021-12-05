--
-- PostgreSQL database cluster dump
--

-- Started on 2021-01-24 23:29:38

SET default_transaction_read_only = off;

SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;

--
-- Roles
--

CREATE ROLE "nPos";
ALTER ROLE "nPos" WITH NOSUPERUSER INHERIT NOCREATEROLE NOCREATEDB LOGIN NOREPLICATION NOBYPASSRLS PASSWORD 'SCRAM-SHA-256$4096:h6YCGX2/5VUI0UX5mF+kGA==$U2TuNPPAEQCpfVyQNXc4UP2sPe+LdlxxjDqDEK51fSM=:dT/wkfOmCb2CJfkP/PTfzhOiG8Mm5nnKq8zu/P7dBsA=';
CREATE ROLE postgres;
ALTER ROLE postgres WITH SUPERUSER INHERIT CREATEROLE CREATEDB LOGIN REPLICATION BYPASSRLS PASSWORD 'SCRAM-SHA-256$4096:vbbG+qa1y81u8ncQohUHUQ==$62ojI2ZC6yOIpIrFZt5g3is0o53gjBC+QJWfbm+vYhI=:Gd+2mJS30SKx1fwDpArAN1IMJ2db9DmSoBhJEaz5Les=';






-- Completed on 2021-01-24 23:29:38

--
-- PostgreSQL database cluster dump complete
--

