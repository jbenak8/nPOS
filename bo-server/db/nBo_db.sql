--
-- PostgreSQL database dump
--

-- Dumped from database version 14.2
-- Dumped by pg_dump version 14.2

-- Started on 2023-03-24 23:50:17

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
-- TOC entry 3435 (class 1262 OID 40961)
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
-- TOC entry 6 (class 2615 OID 40962)
-- Name: nbo; Type: SCHEMA; Schema: -; Owner: nBo
--

CREATE SCHEMA nbo;


ALTER SCHEMA nbo OWNER TO "nBo";

--
-- TOC entry 2 (class 3079 OID 49152)
-- Name: pgcrypto; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS pgcrypto WITH SCHEMA nbo;


--
-- TOC entry 3436 (class 0 OID 0)
-- Dependencies: 2
-- Name: EXTENSION pgcrypto; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION pgcrypto IS 'cryptographic functions';


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 221 (class 1259 OID 49213)
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
-- TOC entry 220 (class 1259 OID 49200)
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
-- TOC entry 213 (class 1259 OID 40980)
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
-- TOC entry 3437 (class 0 OID 0)
-- Dependencies: 213
-- Name: COLUMN functions.component; Type: COMMENT; Schema: nbo; Owner: nBo
--

COMMENT ON COLUMN nbo.functions.component IS 'Component or class or reference name. Is used for management of an access to each function. Can be null - in this case, this is a parent node in the visualization tree.';


--
-- TOC entry 214 (class 1259 OID 40989)
-- Name: functions_mapping; Type: TABLE; Schema: nbo; Owner: nBo
--

CREATE TABLE nbo.functions_mapping (
    id integer NOT NULL,
    group_id integer NOT NULL,
    function_id character varying(16) NOT NULL
);


ALTER TABLE nbo.functions_mapping OWNER TO "nBo";

--
-- TOC entry 216 (class 1259 OID 41021)
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
-- TOC entry 3438 (class 0 OID 0)
-- Dependencies: 216
-- Name: functions_mapping_id_seq; Type: SEQUENCE OWNED BY; Schema: nbo; Owner: nBo
--

ALTER SEQUENCE nbo.functions_mapping_id_seq OWNED BY nbo.functions_mapping.id;


--
-- TOC entry 219 (class 1259 OID 49190)
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
-- TOC entry 3439 (class 0 OID 0)
-- Dependencies: 219
-- Name: COLUMN measure_units.unit; Type: COMMENT; Schema: nbo; Owner: nBo
--

COMMENT ON COLUMN nbo.measure_units.unit IS 'Unit ID short';


--
-- TOC entry 225 (class 1259 OID 57460)
-- Name: numbering_series; Type: TABLE; Schema: nbo; Owner: nBo
--

CREATE TABLE nbo.numbering_series (
    number integer NOT NULL,
    definition character varying(64) NOT NULL,
    document_type character varying(20) NOT NULL,
    sequence_number_length integer DEFAULT 0 NOT NULL,
    valid_from date NOT NULL,
    start_serial_from integer DEFAULT 1 NOT NULL,
    label character varying(64) NOT NULL
);


ALTER TABLE nbo.numbering_series OWNER TO "nBo";

--
-- TOC entry 3440 (class 0 OID 0)
-- Dependencies: 225
-- Name: COLUMN numbering_series.document_type; Type: COMMENT; Schema: nbo; Owner: nBo
--

COMMENT ON COLUMN nbo.numbering_series.document_type IS 'value from INVOICE, RECEIPT, DELIVERY_NOTE, CREDIT_NOTE';


--
-- TOC entry 224 (class 1259 OID 57459)
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
-- TOC entry 3441 (class 0 OID 0)
-- Dependencies: 224
-- Name: numbering_series_number_seq; Type: SEQUENCE OWNED BY; Schema: nbo; Owner: nBo
--

ALTER SEQUENCE nbo.numbering_series_number_seq OWNED BY nbo.numbering_series.number;


--
-- TOC entry 212 (class 1259 OID 40973)
-- Name: user_groups; Type: TABLE; Schema: nbo; Owner: nBo
--

CREATE TABLE nbo.user_groups (
    id integer NOT NULL,
    name character varying(128) NOT NULL,
    description text NOT NULL
);


ALTER TABLE nbo.user_groups OWNER TO "nBo";

--
-- TOC entry 217 (class 1259 OID 41023)
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
-- TOC entry 3442 (class 0 OID 0)
-- Dependencies: 217
-- Name: user_groups_id_seq; Type: SEQUENCE OWNED BY; Schema: nbo; Owner: nBo
--

ALTER SEQUENCE nbo.user_groups_id_seq OWNED BY nbo.user_groups.id;


--
-- TOC entry 211 (class 1259 OID 40963)
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
-- TOC entry 3443 (class 0 OID 0)
-- Dependencies: 211
-- Name: COLUMN users.id; Type: COMMENT; Schema: nbo; Owner: nBo
--

COMMENT ON COLUMN nbo.users.id IS 'User ID is used as a login name/number.';


--
-- TOC entry 215 (class 1259 OID 41004)
-- Name: users_mapping; Type: TABLE; Schema: nbo; Owner: nBo
--

CREATE TABLE nbo.users_mapping (
    id integer NOT NULL,
    user_id integer NOT NULL,
    group_id integer NOT NULL
);


ALTER TABLE nbo.users_mapping OWNER TO "nBo";

--
-- TOC entry 3444 (class 0 OID 0)
-- Dependencies: 215
-- Name: TABLE users_mapping; Type: COMMENT; Schema: nbo; Owner: nBo
--

COMMENT ON TABLE nbo.users_mapping IS 'Mapping users to user groups';


--
-- TOC entry 218 (class 1259 OID 41025)
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
-- TOC entry 3445 (class 0 OID 0)
-- Dependencies: 218
-- Name: users_mapping_id_seq; Type: SEQUENCE OWNED BY; Schema: nbo; Owner: nBo
--

ALTER SEQUENCE nbo.users_mapping_id_seq OWNED BY nbo.users_mapping.id;


--
-- TOC entry 223 (class 1259 OID 49251)
-- Name: vat; Type: TABLE; Schema: nbo; Owner: nBo
--

CREATE TABLE nbo.vat (
    id integer NOT NULL,
    vat_type character varying(9) NOT NULL,
    percentage numeric(5,2) NOT NULL,
    label character varying(45) NOT NULL,
    valid_from date NOT NULL
);


ALTER TABLE nbo.vat OWNER TO "nBo";

--
-- TOC entry 3446 (class 0 OID 0)
-- Dependencies: 223
-- Name: COLUMN vat.vat_type; Type: COMMENT; Schema: nbo; Owner: nBo
--

COMMENT ON COLUMN nbo.vat.vat_type IS 'Available VAT types: BASE, LOWERED_1, LOWERED_2, ZERO';


--
-- TOC entry 222 (class 1259 OID 49250)
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
-- TOC entry 3447 (class 0 OID 0)
-- Dependencies: 222
-- Name: vat_id_seq; Type: SEQUENCE OWNED BY; Schema: nbo; Owner: nBo
--

ALTER SEQUENCE nbo.vat_id_seq OWNED BY nbo.vat.id;


--
-- TOC entry 3246 (class 2604 OID 41022)
-- Name: functions_mapping id; Type: DEFAULT; Schema: nbo; Owner: nBo
--

ALTER TABLE ONLY nbo.functions_mapping ALTER COLUMN id SET DEFAULT nextval('nbo.functions_mapping_id_seq'::regclass);


--
-- TOC entry 3252 (class 2604 OID 57463)
-- Name: numbering_series number; Type: DEFAULT; Schema: nbo; Owner: nBo
--

ALTER TABLE ONLY nbo.numbering_series ALTER COLUMN number SET DEFAULT nextval('nbo.numbering_series_number_seq'::regclass);


--
-- TOC entry 3244 (class 2604 OID 41024)
-- Name: user_groups id; Type: DEFAULT; Schema: nbo; Owner: nBo
--

ALTER TABLE ONLY nbo.user_groups ALTER COLUMN id SET DEFAULT nextval('nbo.user_groups_id_seq'::regclass);


--
-- TOC entry 3247 (class 2604 OID 41026)
-- Name: users_mapping id; Type: DEFAULT; Schema: nbo; Owner: nBo
--

ALTER TABLE ONLY nbo.users_mapping ALTER COLUMN id SET DEFAULT nextval('nbo.users_mapping_id_seq'::regclass);


--
-- TOC entry 3251 (class 2604 OID 49254)
-- Name: vat id; Type: DEFAULT; Schema: nbo; Owner: nBo
--

ALTER TABLE ONLY nbo.vat ALTER COLUMN id SET DEFAULT nextval('nbo.vat_id_seq'::regclass);


--
-- TOC entry 3276 (class 2606 OID 49218)
-- Name: countries countries_pkey; Type: CONSTRAINT; Schema: nbo; Owner: nBo
--

ALTER TABLE ONLY nbo.countries
    ADD CONSTRAINT countries_pkey PRIMARY KEY (iso_code);


--
-- TOC entry 3272 (class 2606 OID 49206)
-- Name: currencies currency_pk; Type: CONSTRAINT; Schema: nbo; Owner: nBo
--

ALTER TABLE ONLY nbo.currencies
    ADD CONSTRAINT currency_pk PRIMARY KEY (iso_code);


--
-- TOC entry 3274 (class 2606 OID 49212)
-- Name: currencies currency_uq; Type: CONSTRAINT; Schema: nbo; Owner: nBo
--

ALTER TABLE ONLY nbo.currencies
    ADD CONSTRAINT currency_uq UNIQUE (iso_code, name, symbol);


--
-- TOC entry 3270 (class 2606 OID 49194)
-- Name: measure_units measure_units_pk; Type: CONSTRAINT; Schema: nbo; Owner: nBo
--

ALTER TABLE ONLY nbo.measure_units
    ADD CONSTRAINT measure_units_pk PRIMARY KEY (unit);


--
-- TOC entry 3282 (class 2606 OID 57466)
-- Name: numbering_series numbering_series_pk; Type: CONSTRAINT; Schema: nbo; Owner: nBo
--

ALTER TABLE ONLY nbo.numbering_series
    ADD CONSTRAINT numbering_series_pk PRIMARY KEY (number);


--
-- TOC entry 3284 (class 2606 OID 57468)
-- Name: numbering_series numbering_series_uq_definition; Type: CONSTRAINT; Schema: nbo; Owner: nBo
--

ALTER TABLE ONLY nbo.numbering_series
    ADD CONSTRAINT numbering_series_uq_definition UNIQUE (definition);


--
-- TOC entry 3262 (class 2606 OID 40986)
-- Name: functions pk_functions; Type: CONSTRAINT; Schema: nbo; Owner: nBo
--

ALTER TABLE ONLY nbo.functions
    ADD CONSTRAINT pk_functions PRIMARY KEY (id);


--
-- TOC entry 3266 (class 2606 OID 40993)
-- Name: functions_mapping pk_functions_mapping; Type: CONSTRAINT; Schema: nbo; Owner: nBo
--

ALTER TABLE ONLY nbo.functions_mapping
    ADD CONSTRAINT pk_functions_mapping PRIMARY KEY (id);


--
-- TOC entry 3258 (class 2606 OID 40977)
-- Name: user_groups pk_user_groups; Type: CONSTRAINT; Schema: nbo; Owner: nBo
--

ALTER TABLE ONLY nbo.user_groups
    ADD CONSTRAINT pk_user_groups PRIMARY KEY (id);


--
-- TOC entry 3256 (class 2606 OID 40972)
-- Name: users pk_users; Type: CONSTRAINT; Schema: nbo; Owner: nBo
--

ALTER TABLE ONLY nbo.users
    ADD CONSTRAINT pk_users PRIMARY KEY (id);


--
-- TOC entry 3268 (class 2606 OID 41008)
-- Name: users_mapping pk_users_mapping; Type: CONSTRAINT; Schema: nbo; Owner: nBo
--

ALTER TABLE ONLY nbo.users_mapping
    ADD CONSTRAINT pk_users_mapping PRIMARY KEY (id);


--
-- TOC entry 3264 (class 2606 OID 40988)
-- Name: functions uk_function_class; Type: CONSTRAINT; Schema: nbo; Owner: nBo
--

ALTER TABLE ONLY nbo.functions
    ADD CONSTRAINT uk_function_class UNIQUE (component);


--
-- TOC entry 3260 (class 2606 OID 40979)
-- Name: user_groups uk_user_groups_name; Type: CONSTRAINT; Schema: nbo; Owner: nBo
--

ALTER TABLE ONLY nbo.user_groups
    ADD CONSTRAINT uk_user_groups_name UNIQUE (name);


--
-- TOC entry 3278 (class 2606 OID 49220)
-- Name: countries uq_country; Type: CONSTRAINT; Schema: nbo; Owner: nBo
--

ALTER TABLE ONLY nbo.countries
    ADD CONSTRAINT uq_country UNIQUE (full_name, common_name);


--
-- TOC entry 3280 (class 2606 OID 49256)
-- Name: vat vat_pkey; Type: CONSTRAINT; Schema: nbo; Owner: nBo
--

ALTER TABLE ONLY nbo.vat
    ADD CONSTRAINT vat_pkey PRIMARY KEY (id);


--
-- TOC entry 3290 (class 2606 OID 49221)
-- Name: countries fk_countries_currencies; Type: FK CONSTRAINT; Schema: nbo; Owner: nBo
--

ALTER TABLE ONLY nbo.countries
    ADD CONSTRAINT fk_countries_currencies FOREIGN KEY (currency_code) REFERENCES nbo.currencies(iso_code);


--
-- TOC entry 3285 (class 2606 OID 40994)
-- Name: functions_mapping fk_functions_mapping_functions; Type: FK CONSTRAINT; Schema: nbo; Owner: nBo
--

ALTER TABLE ONLY nbo.functions_mapping
    ADD CONSTRAINT fk_functions_mapping_functions FOREIGN KEY (function_id) REFERENCES nbo.functions(id);


--
-- TOC entry 3286 (class 2606 OID 40999)
-- Name: functions_mapping fk_functions_mapping_user_groups; Type: FK CONSTRAINT; Schema: nbo; Owner: nBo
--

ALTER TABLE ONLY nbo.functions_mapping
    ADD CONSTRAINT fk_functions_mapping_user_groups FOREIGN KEY (group_id) REFERENCES nbo.user_groups(id);


--
-- TOC entry 3288 (class 2606 OID 41014)
-- Name: users_mapping fk_users_mapping_user_groups; Type: FK CONSTRAINT; Schema: nbo; Owner: nBo
--

ALTER TABLE ONLY nbo.users_mapping
    ADD CONSTRAINT fk_users_mapping_user_groups FOREIGN KEY (group_id) REFERENCES nbo.user_groups(id);


--
-- TOC entry 3287 (class 2606 OID 41009)
-- Name: users_mapping fk_users_mapping_users; Type: FK CONSTRAINT; Schema: nbo; Owner: nBo
--

ALTER TABLE ONLY nbo.users_mapping
    ADD CONSTRAINT fk_users_mapping_users FOREIGN KEY (user_id) REFERENCES nbo.users(id);


--
-- TOC entry 3289 (class 2606 OID 49195)
-- Name: measure_units ifk_measure_units_base_units; Type: FK CONSTRAINT; Schema: nbo; Owner: nBo
--

ALTER TABLE ONLY nbo.measure_units
    ADD CONSTRAINT ifk_measure_units_base_units FOREIGN KEY (base_unit) REFERENCES nbo.measure_units(unit);


--
-- TOC entry 3448 (class 0 OID 0)
-- Dependencies: 3289
-- Name: CONSTRAINT ifk_measure_units_base_units ON measure_units; Type: COMMENT; Schema: nbo; Owner: nBo
--

COMMENT ON CONSTRAINT ifk_measure_units_base_units ON nbo.measure_units IS 'Internal foreign key for base measure units in ratio.';


-- Completed on 2023-03-24 23:50:17

--
-- PostgreSQL database dump complete
--

