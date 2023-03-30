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

DROP DATABASE nbo;
--
-- Name: nbo; Type: DATABASE; Schema: -; Owner: nBo
--

CREATE DATABASE nbo WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE = 'Czech_Czechia.1250';


ALTER DATABASE nbo OWNER TO "nBo";

\connect nbo

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
-- Name: countries; Type: TABLE; Schema: nbo; Owner: nBo
--

CREATE TABLE nbo.countries (
    iso_code character varying(3) NOT NULL,
    common_name character varying(45) NOT NULL,
    full_name character varying(150) NOT NULL,
    currency_code character varying(3) NOT NULL,
    main boolean DEFAULT false NOT NULL
);


ALTER TABLE nbo.countries OWNER TO "nBo";

--
-- Name: currencies; Type: TABLE; Schema: nbo; Owner: nBo
--

CREATE TABLE nbo.currencies (
    iso_code character varying(3) NOT NULL,
    name character varying(45) NOT NULL,
    symbol character varying(3) NOT NULL,
    acceptable boolean DEFAULT false NOT NULL,
    main boolean DEFAULT false NOT NULL
);


ALTER TABLE nbo.currencies OWNER TO "nBo";

--
-- Name: finance_operations; Type: TABLE; Schema: nbo; Owner: nBo
--

CREATE TABLE nbo.finance_operations (
    id integer NOT NULL,
    operation_type character varying(20) NOT NULL,
    document_type character varying(20) NOT NULL,
    operation_name character varying(128) NOT NULL,
    account character varying(10)
);


ALTER TABLE nbo.finance_operations OWNER TO "nBo";

--
-- Name: COLUMN finance_operations.operation_type; Type: COMMENT; Schema: nbo; Owner: nBo
--

COMMENT ON COLUMN nbo.finance_operations.operation_type IS 'Values according to enum cz.jbenak.npos.api.shared.enums.FinanceOperationType';


--
-- Name: COLUMN finance_operations.document_type; Type: COMMENT; Schema: nbo; Owner: nBo
--

COMMENT ON COLUMN nbo.finance_operations.document_type IS 'Values according to enum cz.jbenak.npos.api.shared.enums.FinanceOperationTypeValues according to enum cz.jbenak.npos.api.shared.enums.DocumentType';


--
-- Name: finance_operations_id_seq; Type: SEQUENCE; Schema: nbo; Owner: nBo
--

CREATE SEQUENCE nbo.finance_operations_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE nbo.finance_operations_id_seq OWNER TO "nBo";

--
-- Name: finance_operations_id_seq; Type: SEQUENCE OWNED BY; Schema: nbo; Owner: nBo
--

ALTER SEQUENCE nbo.finance_operations_id_seq OWNED BY nbo.finance_operations.id;


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
-- Name: numbering_series; Type: TABLE; Schema: nbo; Owner: nBo
--

CREATE TABLE nbo.numbering_series (
    id integer NOT NULL,
    definition character varying(64) NOT NULL,
    document_type character varying(20) NOT NULL,
    sequence_number_length integer DEFAULT 0 NOT NULL,
    valid_from date NOT NULL,
    start_serial_from integer DEFAULT 1 NOT NULL
);


ALTER TABLE nbo.numbering_series OWNER TO "nBo";

--
-- Name: COLUMN numbering_series.document_type; Type: COMMENT; Schema: nbo; Owner: nBo
--

COMMENT ON COLUMN nbo.numbering_series.document_type IS 'value from INVOICE, RECEIPT, DELIVERY_NOTE, CREDIT_NOTE';


--
-- Name: numbering_series_number_seq; Type: SEQUENCE; Schema: nbo; Owner: nBo
--

CREATE SEQUENCE nbo.numbering_series_number_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE nbo.numbering_series_number_seq OWNER TO "nBo";

--
-- Name: numbering_series_number_seq; Type: SEQUENCE OWNED BY; Schema: nbo; Owner: nBo
--

ALTER SEQUENCE nbo.numbering_series_number_seq OWNED BY nbo.numbering_series.id;


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
-- Name: vat; Type: TABLE; Schema: nbo; Owner: nBo
--

CREATE TABLE nbo.vat (
    id integer NOT NULL,
    vat_type character varying(9) NOT NULL,
    percentage numeric(5,2) NOT NULL,
    valid_from date NOT NULL
);


ALTER TABLE nbo.vat OWNER TO "nBo";

--
-- Name: COLUMN vat.vat_type; Type: COMMENT; Schema: nbo; Owner: nBo
--

COMMENT ON COLUMN nbo.vat.vat_type IS 'Available VAT types: BASE, LOWERED_1, LOWERED_2, ZERO';


--
-- Name: vat_id_seq; Type: SEQUENCE; Schema: nbo; Owner: nBo
--

CREATE SEQUENCE nbo.vat_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE nbo.vat_id_seq OWNER TO "nBo";

--
-- Name: vat_id_seq; Type: SEQUENCE OWNED BY; Schema: nbo; Owner: nBo
--

ALTER SEQUENCE nbo.vat_id_seq OWNED BY nbo.vat.id;


--
-- Name: finance_operations id; Type: DEFAULT; Schema: nbo; Owner: nBo
--

ALTER TABLE ONLY nbo.finance_operations ALTER COLUMN id SET DEFAULT nextval('nbo.finance_operations_id_seq'::regclass);


--
-- Name: functions_mapping id; Type: DEFAULT; Schema: nbo; Owner: nBo
--

ALTER TABLE ONLY nbo.functions_mapping ALTER COLUMN id SET DEFAULT nextval('nbo.functions_mapping_id_seq'::regclass);


--
-- Name: numbering_series id; Type: DEFAULT; Schema: nbo; Owner: nBo
--

ALTER TABLE ONLY nbo.numbering_series ALTER COLUMN id SET DEFAULT nextval('nbo.numbering_series_number_seq'::regclass);


--
-- Name: user_groups id; Type: DEFAULT; Schema: nbo; Owner: nBo
--

ALTER TABLE ONLY nbo.user_groups ALTER COLUMN id SET DEFAULT nextval('nbo.user_groups_id_seq'::regclass);


--
-- Name: users_mapping id; Type: DEFAULT; Schema: nbo; Owner: nBo
--

ALTER TABLE ONLY nbo.users_mapping ALTER COLUMN id SET DEFAULT nextval('nbo.users_mapping_id_seq'::regclass);


--
-- Name: vat id; Type: DEFAULT; Schema: nbo; Owner: nBo
--

ALTER TABLE ONLY nbo.vat ALTER COLUMN id SET DEFAULT nextval('nbo.vat_id_seq'::regclass);


--
-- Name: countries countries_pkey; Type: CONSTRAINT; Schema: nbo; Owner: nBo
--

ALTER TABLE ONLY nbo.countries
    ADD CONSTRAINT countries_pkey PRIMARY KEY (iso_code);


--
-- Name: currencies currency_pk; Type: CONSTRAINT; Schema: nbo; Owner: nBo
--

ALTER TABLE ONLY nbo.currencies
    ADD CONSTRAINT currency_pk PRIMARY KEY (iso_code);


--
-- Name: currencies currency_uq; Type: CONSTRAINT; Schema: nbo; Owner: nBo
--

ALTER TABLE ONLY nbo.currencies
    ADD CONSTRAINT currency_uq UNIQUE (iso_code, name, symbol);


--
-- Name: finance_operations finance_operations_pk; Type: CONSTRAINT; Schema: nbo; Owner: nBo
--

ALTER TABLE ONLY nbo.finance_operations
    ADD CONSTRAINT finance_operations_pk PRIMARY KEY (id);


--
-- Name: finance_operations finance_operations_uq_name; Type: CONSTRAINT; Schema: nbo; Owner: nBo
--

ALTER TABLE ONLY nbo.finance_operations
    ADD CONSTRAINT finance_operations_uq_name UNIQUE (operation_name);


--
-- Name: measure_units measure_units_pk; Type: CONSTRAINT; Schema: nbo; Owner: nBo
--

ALTER TABLE ONLY nbo.measure_units
    ADD CONSTRAINT measure_units_pk PRIMARY KEY (unit);


--
-- Name: numbering_series numbering_series_pk; Type: CONSTRAINT; Schema: nbo; Owner: nBo
--

ALTER TABLE ONLY nbo.numbering_series
    ADD CONSTRAINT numbering_series_pk PRIMARY KEY (id);


--
-- Name: numbering_series numbering_series_uq_definition; Type: CONSTRAINT; Schema: nbo; Owner: nBo
--

ALTER TABLE ONLY nbo.numbering_series
    ADD CONSTRAINT numbering_series_uq_definition UNIQUE (definition);


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
-- Name: countries uq_country; Type: CONSTRAINT; Schema: nbo; Owner: nBo
--

ALTER TABLE ONLY nbo.countries
    ADD CONSTRAINT uq_country UNIQUE (full_name, common_name);


--
-- Name: vat vat_pkey; Type: CONSTRAINT; Schema: nbo; Owner: nBo
--

ALTER TABLE ONLY nbo.vat
    ADD CONSTRAINT vat_pkey PRIMARY KEY (id);


--
-- Name: countries fk_countries_currencies; Type: FK CONSTRAINT; Schema: nbo; Owner: nBo
--

ALTER TABLE ONLY nbo.countries
    ADD CONSTRAINT fk_countries_currencies FOREIGN KEY (currency_code) REFERENCES nbo.currencies(iso_code);


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

