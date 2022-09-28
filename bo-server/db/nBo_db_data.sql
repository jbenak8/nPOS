--
-- PostgreSQL database dump
--

-- Dumped from database version 14.2
-- Dumped by pg_dump version 14.2

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

ALTER TABLE IF EXISTS ONLY nbo.measure_units DROP CONSTRAINT IF EXISTS ifk_measure_units_base_units;
ALTER TABLE IF EXISTS ONLY nbo.users_mapping DROP CONSTRAINT IF EXISTS fk_users_mapping_users;
ALTER TABLE IF EXISTS ONLY nbo.users_mapping DROP CONSTRAINT IF EXISTS fk_users_mapping_user_groups;
ALTER TABLE IF EXISTS ONLY nbo.functions_mapping DROP CONSTRAINT IF EXISTS fk_functions_mapping_user_groups;
ALTER TABLE IF EXISTS ONLY nbo.functions_mapping DROP CONSTRAINT IF EXISTS fk_functions_mapping_functions;
ALTER TABLE IF EXISTS ONLY nbo.user_groups DROP CONSTRAINT IF EXISTS uk_user_groups_name;
ALTER TABLE IF EXISTS ONLY nbo.functions DROP CONSTRAINT IF EXISTS uk_function_class;
ALTER TABLE IF EXISTS ONLY nbo.users_mapping DROP CONSTRAINT IF EXISTS pk_users_mapping;
ALTER TABLE IF EXISTS ONLY nbo.users DROP CONSTRAINT IF EXISTS pk_users;
ALTER TABLE IF EXISTS ONLY nbo.user_groups DROP CONSTRAINT IF EXISTS pk_user_groups;
ALTER TABLE IF EXISTS ONLY nbo.functions_mapping DROP CONSTRAINT IF EXISTS pk_functions_mapping;
ALTER TABLE IF EXISTS ONLY nbo.functions DROP CONSTRAINT IF EXISTS pk_functions;
ALTER TABLE IF EXISTS ONLY nbo.measure_units DROP CONSTRAINT IF EXISTS measure_units_pk;
ALTER TABLE IF EXISTS nbo.users_mapping ALTER COLUMN id DROP DEFAULT;
ALTER TABLE IF EXISTS nbo.user_groups ALTER COLUMN id DROP DEFAULT;
ALTER TABLE IF EXISTS nbo.functions_mapping ALTER COLUMN id DROP DEFAULT;
DROP SEQUENCE IF EXISTS nbo.users_mapping_id_seq;
DROP TABLE IF EXISTS nbo.users_mapping;
DROP TABLE IF EXISTS nbo.users;
DROP SEQUENCE IF EXISTS nbo.user_groups_id_seq;
DROP TABLE IF EXISTS nbo.user_groups;
DROP TABLE IF EXISTS nbo.measure_units;
DROP SEQUENCE IF EXISTS nbo.functions_mapping_id_seq;
DROP TABLE IF EXISTS nbo.functions_mapping;
DROP TABLE IF EXISTS nbo.functions;
DROP EXTENSION IF EXISTS pgcrypto;
DROP SCHEMA IF EXISTS nbo;
--
-- Name: nbo; Type: SCHEMA; Schema: -; Owner: nBo
--

CREATE SCHEMA nbo;


ALTER SCHEMA nbo OWNER TO "nBo";

--
-- Name: pgcrypto; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS pgcrypto WITH SCHEMA nbo;


--
-- Name: EXTENSION pgcrypto; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION pgcrypto IS 'cryptographic functions';


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: functions; Type: TABLE; Schema: nbo; Owner: nBo
--

CREATE TABLE nbo.functions (
    id character varying(16) NOT NULL,
    description character varying(256) NOT NULL,
    component character varying(256),
    key_component character varying(3) DEFAULT 'BOC'::character varying NOT NULL
);


ALTER TABLE nbo.functions OWNER TO "nBo";

--
-- Name: COLUMN functions.component; Type: COMMENT; Schema: nbo; Owner: nBo
--

COMMENT ON COLUMN nbo.functions.component IS 'Component or class or reference name. Is used for management of an access to each function. Can be null - in this case, this is a parent node in the visualization tree.';


--
-- Name: functions_mapping; Type: TABLE; Schema: nbo; Owner: nBo
--

CREATE TABLE nbo.functions_mapping (
    id integer NOT NULL,
    group_id integer NOT NULL,
    function_id character varying(16) NOT NULL
);


ALTER TABLE nbo.functions_mapping OWNER TO "nBo";

--
-- Name: functions_mapping_id_seq; Type: SEQUENCE; Schema: nbo; Owner: nBo
--

CREATE SEQUENCE nbo.functions_mapping_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE nbo.functions_mapping_id_seq OWNER TO "nBo";

--
-- Name: functions_mapping_id_seq; Type: SEQUENCE OWNED BY; Schema: nbo; Owner: nBo
--

ALTER SEQUENCE nbo.functions_mapping_id_seq OWNED BY nbo.functions_mapping.id;


--
-- Name: measure_units; Type: TABLE; Schema: nbo; Owner: nBo
--

CREATE TABLE nbo.measure_units (
    unit character varying(5) NOT NULL,
    name character varying(45) NOT NULL,
    base_unit character varying(5),
    ratio numeric(5,3)
);


ALTER TABLE nbo.measure_units OWNER TO "nBo";

--
-- Name: COLUMN measure_units.unit; Type: COMMENT; Schema: nbo; Owner: nBo
--

COMMENT ON COLUMN nbo.measure_units.unit IS 'Unit ID short';


--
-- Name: user_groups; Type: TABLE; Schema: nbo; Owner: nBo
--

CREATE TABLE nbo.user_groups (
    id integer NOT NULL,
    name character varying(128) NOT NULL,
    description text NOT NULL
);


ALTER TABLE nbo.user_groups OWNER TO "nBo";

--
-- Name: user_groups_id_seq; Type: SEQUENCE; Schema: nbo; Owner: nBo
--

CREATE SEQUENCE nbo.user_groups_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE nbo.user_groups_id_seq OWNER TO "nBo";

--
-- Name: user_groups_id_seq; Type: SEQUENCE OWNED BY; Schema: nbo; Owner: nBo
--

ALTER SEQUENCE nbo.user_groups_id_seq OWNED BY nbo.user_groups.id;


--
-- Name: users; Type: TABLE; Schema: nbo; Owner: nBo
--

CREATE TABLE nbo.users (
    id integer NOT NULL,
    user_name character varying(40) NOT NULL,
    user_surname character varying(40) NOT NULL,
    password text NOT NULL,
    group_id integer NOT NULL,
    mail character varying(128),
    phone character varying(20),
    rest_login_attempts integer DEFAULT 3 NOT NULL,
    locked boolean DEFAULT false NOT NULL,
    last_login_timestamp timestamp without time zone,
    note text
);


ALTER TABLE nbo.users OWNER TO "nBo";

--
-- Name: COLUMN users.id; Type: COMMENT; Schema: nbo; Owner: nBo
--

COMMENT ON COLUMN nbo.users.id IS 'User ID is used as a login name/number.';


--
-- Name: users_mapping; Type: TABLE; Schema: nbo; Owner: nBo
--

CREATE TABLE nbo.users_mapping (
    id integer NOT NULL,
    user_id integer NOT NULL,
    group_id integer NOT NULL
);


ALTER TABLE nbo.users_mapping OWNER TO "nBo";

--
-- Name: TABLE users_mapping; Type: COMMENT; Schema: nbo; Owner: nBo
--

COMMENT ON TABLE nbo.users_mapping IS 'Mapping users to user groups';


--
-- Name: users_mapping_id_seq; Type: SEQUENCE; Schema: nbo; Owner: nBo
--

CREATE SEQUENCE nbo.users_mapping_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE nbo.users_mapping_id_seq OWNER TO "nBo";

--
-- Name: users_mapping_id_seq; Type: SEQUENCE OWNED BY; Schema: nbo; Owner: nBo
--

ALTER SEQUENCE nbo.users_mapping_id_seq OWNED BY nbo.users_mapping.id;


--
-- Name: functions_mapping id; Type: DEFAULT; Schema: nbo; Owner: nBo
--

ALTER TABLE ONLY nbo.functions_mapping ALTER COLUMN id SET DEFAULT nextval('nbo.functions_mapping_id_seq'::regclass);


--
-- Name: user_groups id; Type: DEFAULT; Schema: nbo; Owner: nBo
--

ALTER TABLE ONLY nbo.user_groups ALTER COLUMN id SET DEFAULT nextval('nbo.user_groups_id_seq'::regclass);


--
-- Name: users_mapping id; Type: DEFAULT; Schema: nbo; Owner: nBo
--

ALTER TABLE ONLY nbo.users_mapping ALTER COLUMN id SET DEFAULT nextval('nbo.users_mapping_id_seq'::regclass);


--
-- Data for Name: functions; Type: TABLE DATA; Schema: nbo; Owner: nBo
--

COPY nbo.functions (id, description, component, key_component) FROM stdin;
2	BO	cz.jbenak.npos.boClient.gui.dialogs.login	BOC
1	POS	cz.jbenak.npos.pos.gui.prihlaseni	POS
\.


--
-- Data for Name: functions_mapping; Type: TABLE DATA; Schema: nbo; Owner: nBo
--

COPY nbo.functions_mapping (id, group_id, function_id) FROM stdin;
1	1	1
2	1	2
\.


--
-- Data for Name: measure_units; Type: TABLE DATA; Schema: nbo; Owner: nBo
--

COPY nbo.measure_units (unit, name, base_unit, ratio) FROM stdin;
ks	Kus	\N	\N
l	Litr	\N	\N
ml	Mililitr se strašně dlouhým názvem	l	0.001
\.


--
-- Data for Name: user_groups; Type: TABLE DATA; Schema: nbo; Owner: nBo
--

COPY nbo.user_groups (id, name, description) FROM stdin;
1	globalAdmin	Skupina s nejvyšším oprávněním pro správu celého systému.
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: nbo; Owner: nBo
--

COPY nbo.users (id, user_name, user_surname, password, group_id, mail, phone, rest_login_attempts, locked, last_login_timestamp, note) FROM stdin;
1	Admin	Administrátor	$2a$06$r49YuB8b5h/EjjNCkS78xuuWnvlQWM0S3ZSJJyHw0fKM0QSVWgIjW	1	admin@npos.com	+420 123 456 789	3	f	2022-09-25 00:07:44.957137	Globální administrátor.
\.


--
-- Data for Name: users_mapping; Type: TABLE DATA; Schema: nbo; Owner: nBo
--

COPY nbo.users_mapping (id, user_id, group_id) FROM stdin;
1	1	1
\.


--
-- Name: functions_mapping_id_seq; Type: SEQUENCE SET; Schema: nbo; Owner: nBo
--

SELECT pg_catalog.setval('nbo.functions_mapping_id_seq', 1, false);


--
-- Name: user_groups_id_seq; Type: SEQUENCE SET; Schema: nbo; Owner: nBo
--

SELECT pg_catalog.setval('nbo.user_groups_id_seq', 1, false);


--
-- Name: users_mapping_id_seq; Type: SEQUENCE SET; Schema: nbo; Owner: nBo
--

SELECT pg_catalog.setval('nbo.users_mapping_id_seq', 1, false);


--
-- Name: measure_units measure_units_pk; Type: CONSTRAINT; Schema: nbo; Owner: nBo
--

ALTER TABLE ONLY nbo.measure_units
    ADD CONSTRAINT measure_units_pk PRIMARY KEY (unit);


--
-- Name: functions pk_functions; Type: CONSTRAINT; Schema: nbo; Owner: nBo
--

ALTER TABLE ONLY nbo.functions
    ADD CONSTRAINT pk_functions PRIMARY KEY (id);


--
-- Name: functions_mapping pk_functions_mapping; Type: CONSTRAINT; Schema: nbo; Owner: nBo
--

ALTER TABLE ONLY nbo.functions_mapping
    ADD CONSTRAINT pk_functions_mapping PRIMARY KEY (id);


--
-- Name: user_groups pk_user_groups; Type: CONSTRAINT; Schema: nbo; Owner: nBo
--

ALTER TABLE ONLY nbo.user_groups
    ADD CONSTRAINT pk_user_groups PRIMARY KEY (id);


--
-- Name: users pk_users; Type: CONSTRAINT; Schema: nbo; Owner: nBo
--

ALTER TABLE ONLY nbo.users
    ADD CONSTRAINT pk_users PRIMARY KEY (id);


--
-- Name: users_mapping pk_users_mapping; Type: CONSTRAINT; Schema: nbo; Owner: nBo
--

ALTER TABLE ONLY nbo.users_mapping
    ADD CONSTRAINT pk_users_mapping PRIMARY KEY (id);


--
-- Name: functions uk_function_class; Type: CONSTRAINT; Schema: nbo; Owner: nBo
--

ALTER TABLE ONLY nbo.functions
    ADD CONSTRAINT uk_function_class UNIQUE (component);


--
-- Name: user_groups uk_user_groups_name; Type: CONSTRAINT; Schema: nbo; Owner: nBo
--

ALTER TABLE ONLY nbo.user_groups
    ADD CONSTRAINT uk_user_groups_name UNIQUE (name);


--
-- Name: functions_mapping fk_functions_mapping_functions; Type: FK CONSTRAINT; Schema: nbo; Owner: nBo
--

ALTER TABLE ONLY nbo.functions_mapping
    ADD CONSTRAINT fk_functions_mapping_functions FOREIGN KEY (function_id) REFERENCES nbo.functions(id);


--
-- Name: functions_mapping fk_functions_mapping_user_groups; Type: FK CONSTRAINT; Schema: nbo; Owner: nBo
--

ALTER TABLE ONLY nbo.functions_mapping
    ADD CONSTRAINT fk_functions_mapping_user_groups FOREIGN KEY (group_id) REFERENCES nbo.user_groups(id);


--
-- Name: users_mapping fk_users_mapping_user_groups; Type: FK CONSTRAINT; Schema: nbo; Owner: nBo
--

ALTER TABLE ONLY nbo.users_mapping
    ADD CONSTRAINT fk_users_mapping_user_groups FOREIGN KEY (group_id) REFERENCES nbo.user_groups(id);


--
-- Name: users_mapping fk_users_mapping_users; Type: FK CONSTRAINT; Schema: nbo; Owner: nBo
--

ALTER TABLE ONLY nbo.users_mapping
    ADD CONSTRAINT fk_users_mapping_users FOREIGN KEY (user_id) REFERENCES nbo.users(id);


--
-- Name: measure_units ifk_measure_units_base_units; Type: FK CONSTRAINT; Schema: nbo; Owner: nBo
--

ALTER TABLE ONLY nbo.measure_units
    ADD CONSTRAINT ifk_measure_units_base_units FOREIGN KEY (base_unit) REFERENCES nbo.measure_units(unit);


--
-- Name: CONSTRAINT ifk_measure_units_base_units ON measure_units; Type: COMMENT; Schema: nbo; Owner: nBo
--

COMMENT ON CONSTRAINT ifk_measure_units_base_units ON nbo.measure_units IS 'Internal foreign key for base measure units in ratio.';


--
-- PostgreSQL database dump complete
--

