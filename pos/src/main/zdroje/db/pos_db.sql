--
-- PostgreSQL database dump
--

-- Dumped from database version 13.1
-- Dumped by pg_dump version 13.1

-- Started on 2021-01-25 22:48:41

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

DROP DATABASE npos;
--
-- TOC entry 3262 (class 1262 OID 16454)
-- Name: npos; Type: DATABASE; Schema: -; Owner: nPos
--

CREATE DATABASE npos WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE = 'Czech_Czechia.1250';


ALTER DATABASE npos OWNER TO "nPos";

\connect npos

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
-- TOC entry 7 (class 2615 OID 16457)
-- Name: npos; Type: SCHEMA; Schema: -; Owner: nPos
--

CREATE SCHEMA npos;


ALTER SCHEMA npos OWNER TO "nPos";

--
-- TOC entry 2 (class 3079 OID 16502)
-- Name: pgcrypto; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS pgcrypto WITH SCHEMA npos;


--
-- TOC entry 3263 (class 0 OID 0)
-- Dependencies: 2
-- Name: EXTENSION pgcrypto; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION pgcrypto IS 'cryptographic functions';


--
-- TOC entry 281 (class 1255 OID 16539)
-- Name: bytea_import(text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.bytea_import(p_path text, OUT p_result bytea) RETURNS bytea
    LANGUAGE plpgsql
    AS $$
declare
  l_oid oid;
begin
  select lo_import(p_path) into l_oid;
  select lo_get(l_oid) INTO p_result;
  perform lo_unlink(l_oid);
end;$$;


ALTER FUNCTION public.bytea_import(p_path text, OUT p_result bytea) OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 202 (class 1259 OID 16540)
-- Name: adresy; Type: TABLE; Schema: npos; Owner: nPos
--

CREATE TABLE npos.adresy (
    id integer NOT NULL,
    druhy_radek character varying(100),
    cp character varying(10),
    cor character varying(10),
    ulice character varying(100),
    mesto character varying(100) NOT NULL,
    stat character varying(3) NOT NULL,
    telefon character varying(25),
    mobil character varying(25),
    fax character varying(25),
    email character varying(100),
    web character varying(100),
    psc character varying(15)
);


ALTER TABLE npos.adresy OWNER TO "nPos";

--
-- TOC entry 203 (class 1259 OID 16546)
-- Name: adresy_dodavatelu; Type: TABLE; Schema: npos; Owner: nPos
--

CREATE TABLE npos.adresy_dodavatelu (
    dodavatel integer NOT NULL,
    adresa integer NOT NULL,
    hlavni boolean DEFAULT true NOT NULL
);


ALTER TABLE npos.adresy_dodavatelu OWNER TO "nPos";

--
-- TOC entry 204 (class 1259 OID 16550)
-- Name: adresy_id_seq; Type: SEQUENCE; Schema: npos; Owner: nPos
--

CREATE SEQUENCE npos.adresy_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE npos.adresy_id_seq OWNER TO "nPos";

--
-- TOC entry 3264 (class 0 OID 0)
-- Dependencies: 204
-- Name: adresy_id_seq; Type: SEQUENCE OWNED BY; Schema: npos; Owner: nPos
--

ALTER SEQUENCE npos.adresy_id_seq OWNED BY npos.adresy.id;


--
-- TOC entry 205 (class 1259 OID 16552)
-- Name: adresy_zakazniku; Type: TABLE; Schema: npos; Owner: nPos
--

CREATE TABLE npos.adresy_zakazniku (
    zakaznik character varying(60) NOT NULL,
    adresa integer NOT NULL,
    hlavni boolean NOT NULL,
    dodaci boolean DEFAULT false NOT NULL
);


ALTER TABLE npos.adresy_zakazniku OWNER TO "nPos";

--
-- TOC entry 206 (class 1259 OID 16556)
-- Name: carove_kody; Type: TABLE; Schema: npos; Owner: nPos
--

CREATE TABLE npos.carove_kody (
    kod character varying(255) NOT NULL,
    registr character varying(45) NOT NULL,
    hlavni boolean DEFAULT true NOT NULL,
    mnozstvi numeric(10,5) DEFAULT 1 NOT NULL,
    cena numeric(10,5),
    typ character varying(20) NOT NULL,
    cena_pozice integer,
    cena_delka integer,
    cena_desetinnych_mist integer,
    mnozstvi_pozice integer,
    mnozstvi_delka integer,
    mnozstvi_desetinnych_mist integer
);


ALTER TABLE npos.carove_kody OWNER TO "nPos";

--
-- TOC entry 228 (class 1259 OID 16955)
-- Name: denominace; Type: TABLE; Schema: npos; Owner: nPos
--

CREATE TABLE npos.denominace (
    cislo integer NOT NULL,
    typ_platidla character varying(8) DEFAULT 'MINCE'::character varying NOT NULL,
    iso_kod_meny character varying(3) NOT NULL,
    poradi integer NOT NULL,
    jednotka integer NOT NULL,
    hodnota numeric(10,2) NOT NULL,
    nazev_jednotky character varying(15) NOT NULL,
    ks_balicek integer,
    akceptovatelne boolean DEFAULT true NOT NULL,
    zobrazit_na_tlacitku boolean DEFAULT false
);


ALTER TABLE npos.denominace OWNER TO "nPos";

--
-- TOC entry 3265 (class 0 OID 0)
-- Dependencies: 228
-- Name: COLUMN denominace.typ_platidla; Type: COMMENT; Schema: npos; Owner: nPos
--

COMMENT ON COLUMN npos.denominace.typ_platidla IS 'MINCE nebo BANKOVKA';


--
-- TOC entry 3266 (class 0 OID 0)
-- Dependencies: 228
-- Name: COLUMN denominace.jednotka; Type: COMMENT; Schema: npos; Owner: nPos
--

COMMENT ON COLUMN npos.denominace.jednotka IS 'Velikost (hodnota) daného platidla:
např. 1 haléř - nominál 1, velikost 0.01';


--
-- TOC entry 3267 (class 0 OID 0)
-- Dependencies: 228
-- Name: COLUMN denominace.hodnota; Type: COMMENT; Schema: npos; Owner: nPos
--

COMMENT ON COLUMN npos.denominace.hodnota IS 'hodnota vůči základní jednotce';


--
-- TOC entry 3268 (class 0 OID 0)
-- Dependencies: 228
-- Name: COLUMN denominace.ks_balicek; Type: COMMENT; Schema: npos; Owner: nPos
--

COMMENT ON COLUMN npos.denominace.ks_balicek IS 'Obvyklý počet kusů platidla v balíčku/ruličce';


--
-- TOC entry 227 (class 1259 OID 16953)
-- Name: denominace_cislo_seq; Type: SEQUENCE; Schema: npos; Owner: nPos
--

CREATE SEQUENCE npos.denominace_cislo_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE npos.denominace_cislo_seq OWNER TO "nPos";

--
-- TOC entry 3269 (class 0 OID 0)
-- Dependencies: 227
-- Name: denominace_cislo_seq; Type: SEQUENCE OWNED BY; Schema: npos; Owner: nPos
--

ALTER SEQUENCE npos.denominace_cislo_seq OWNED BY npos.denominace.cislo;


--
-- TOC entry 207 (class 1259 OID 16561)
-- Name: dodavatele; Type: TABLE; Schema: npos; Owner: nPos
--

CREATE TABLE npos.dodavatele (
    id integer NOT NULL,
    nazev character varying(80) NOT NULL,
    doplnek_nazvu character varying(100),
    ic character varying(15) NOT NULL,
    dic character varying(15)
);


ALTER TABLE npos.dodavatele OWNER TO "nPos";

--
-- TOC entry 208 (class 1259 OID 16564)
-- Name: dodavatele_id_seq; Type: SEQUENCE; Schema: npos; Owner: nPos
--

CREATE SEQUENCE npos.dodavatele_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE npos.dodavatele_id_seq OWNER TO "nPos";

--
-- TOC entry 3270 (class 0 OID 0)
-- Dependencies: 208
-- Name: dodavatele_id_seq; Type: SEQUENCE OWNED BY; Schema: npos; Owner: nPos
--

ALTER SEQUENCE npos.dodavatele_id_seq OWNED BY npos.dodavatele.id;


--
-- TOC entry 209 (class 1259 OID 16566)
-- Name: dph; Type: TABLE; Schema: npos; Owner: nPos
--

CREATE TABLE npos.dph (
    id integer NOT NULL,
    typ character varying(45) NOT NULL,
    sazba real NOT NULL,
    oznaceni character varying(45) NOT NULL,
    platnost_od date NOT NULL,
    platnost_do date
);


ALTER TABLE npos.dph OWNER TO "nPos";

--
-- TOC entry 210 (class 1259 OID 16569)
-- Name: dph_id_seq; Type: SEQUENCE; Schema: npos; Owner: nPos
--

CREATE SEQUENCE npos.dph_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE npos.dph_id_seq OWNER TO "nPos";

--
-- TOC entry 3271 (class 0 OID 0)
-- Dependencies: 210
-- Name: dph_id_seq; Type: SEQUENCE OWNED BY; Schema: npos; Owner: nPos
--

ALTER SEQUENCE npos.dph_id_seq OWNED BY npos.dph.id;


--
-- TOC entry 211 (class 1259 OID 16571)
-- Name: filialka; Type: TABLE; Schema: npos; Owner: nPos
--

CREATE TABLE npos.filialka (
    oznaceni character varying(10) NOT NULL,
    nazev character varying(60) NOT NULL,
    popis character varying(60),
    cp_cor character varying(10),
    ulice character varying(45),
    mesto character varying(45) NOT NULL,
    psc character varying(10) NOT NULL,
    stat character varying(3) NOT NULL,
    telefon character varying(30),
    mobil character varying(30),
    email character varying(60),
    odpovedny_vedouci character varying(60),
    ic character varying(15) NOT NULL,
    dic character varying(15),
    danovy_kod_dph character varying(45),
    nazev_spolecnosti character varying(45) NOT NULL,
    adresa_spolecnosti text,
    url text,
    zapis_or text,
    eet_rezim integer DEFAULT 0,
    eet_id_provozovny integer,
    eet_poverujici_dic character varying(15)
);


ALTER TABLE npos.filialka OWNER TO "nPos";

--
-- TOC entry 229 (class 1259 OID 16965)
-- Name: kurzy_men_cislo_kurzu_seq; Type: SEQUENCE; Schema: npos; Owner: postgres
--

CREATE SEQUENCE npos.kurzy_men_cislo_kurzu_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE npos.kurzy_men_cislo_kurzu_seq OWNER TO postgres;

--
-- TOC entry 226 (class 1259 OID 16945)
-- Name: kurzy_men; Type: TABLE; Schema: npos; Owner: nPos
--

CREATE TABLE npos.kurzy_men (
    cislo_kurzu integer DEFAULT nextval('npos.kurzy_men_cislo_kurzu_seq'::regclass) NOT NULL,
    iso_kod_meny character varying(3) NOT NULL,
    platnost_od date NOT NULL,
    platnost_do date,
    nakup numeric(10,5) NOT NULL,
    prodej numeric(10,5) NOT NULL
);


ALTER TABLE npos.kurzy_men OWNER TO "nPos";

--
-- TOC entry 212 (class 1259 OID 16578)
-- Name: mena; Type: TABLE; Schema: npos; Owner: nPos
--

CREATE TABLE npos.mena (
    iso_kod character varying(3) NOT NULL,
    oznaceni character varying(45) NOT NULL,
    narodni_symbol character varying(3) NOT NULL,
    akceptovatelna boolean NOT NULL,
    kmenova boolean NOT NULL
);


ALTER TABLE npos.mena OWNER TO "nPos";

--
-- TOC entry 213 (class 1259 OID 16581)
-- Name: merne_jednotky; Type: TABLE; Schema: npos; Owner: nPos
--

CREATE TABLE npos.merne_jednotky (
    mj character varying(5) NOT NULL,
    oznaceni character varying(45) NOT NULL,
    zakladni_mj character varying(5),
    pomer real
);


ALTER TABLE npos.merne_jednotky OWNER TO "nPos";

--
-- TOC entry 233 (class 1259 OID 17010)
-- Name: obrazky_platidel_na_tlacitka; Type: TABLE; Schema: npos; Owner: nPos
--

CREATE TABLE npos.obrazky_platidel_na_tlacitka (
    cislo_zaznamu integer NOT NULL,
    denominace integer NOT NULL,
    obrazek bytea NOT NULL
);


ALTER TABLE npos.obrazky_platidel_na_tlacitka OWNER TO "nPos";

--
-- TOC entry 232 (class 1259 OID 17008)
-- Name: obrazky_platidel_na_tlacitka_cislo_zaznamu_seq; Type: SEQUENCE; Schema: npos; Owner: nPos
--

CREATE SEQUENCE npos.obrazky_platidel_na_tlacitka_cislo_zaznamu_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE npos.obrazky_platidel_na_tlacitka_cislo_zaznamu_seq OWNER TO "nPos";

--
-- TOC entry 3272 (class 0 OID 0)
-- Dependencies: 232
-- Name: obrazky_platidel_na_tlacitka_cislo_zaznamu_seq; Type: SEQUENCE OWNED BY; Schema: npos; Owner: nPos
--

ALTER SEQUENCE npos.obrazky_platidel_na_tlacitka_cislo_zaznamu_seq OWNED BY npos.obrazky_platidel_na_tlacitka.cislo_zaznamu;


--
-- TOC entry 214 (class 1259 OID 16584)
-- Name: parkoviste; Type: TABLE; Schema: npos; Owner: nPos
--

CREATE TABLE npos.parkoviste (
    uid uuid NOT NULL,
    vytvoreno timestamp without time zone NOT NULL,
    doklad bytea NOT NULL
);


ALTER TABLE npos.parkoviste OWNER TO "nPos";

--
-- TOC entry 215 (class 1259 OID 16590)
-- Name: platebni_prostredky; Type: TABLE; Schema: npos; Owner: nPos
--

CREATE TABLE npos.platebni_prostredky (
    id character varying(5) NOT NULL,
    typ character varying(20) NOT NULL,
    oznaceni character varying(255) NOT NULL,
    akceptovatelny boolean DEFAULT true NOT NULL,
    cizi_mena_povolena boolean DEFAULT true NOT NULL,
    zaokrouhlit_dle_denominace boolean DEFAULT false NOT NULL,
    miminalni_castka numeric(20,5),
    maximalni_castka numeric(20,5),
    povoleno_pro_vratku boolean DEFAULT true NOT NULL,
    povoleno_pro_storno boolean DEFAULT true NOT NULL,
    evidence_v_zasuvce boolean NOT NULL,
    evidence_v_trezoru boolean NOT NULL,
    otevrit_zasuvku boolean NOT NULL,
    pocitani_nutne boolean NOT NULL,
    preferovany boolean DEFAULT false NOT NULL
);


ALTER TABLE npos.platebni_prostredky OWNER TO "nPos";

--
-- TOC entry 216 (class 1259 OID 16598)
-- Name: pravidla_skupin_slev; Type: TABLE; Schema: npos; Owner: nPos
--

CREATE TABLE npos.pravidla_skupin_slev (
    cislo_pravidla integer NOT NULL,
    cislo_skupiny_slev integer NOT NULL,
    poradove_cislo integer NOT NULL,
    typ_podminky character varying(45) NOT NULL,
    operator character varying(45) NOT NULL,
    hodnota_podminky numeric(10,5),
    id_polozky_podminky character varying(45)
);


ALTER TABLE npos.pravidla_skupin_slev OWNER TO "nPos";

--
-- TOC entry 231 (class 1259 OID 16992)
-- Name: pravidla_zaokrouhlovani; Type: TABLE; Schema: npos; Owner: nPos
--

CREATE TABLE npos.pravidla_zaokrouhlovani (
    cislo_pravidla integer NOT NULL,
    id_platebniho_prostredku character varying(5) NOT NULL,
    iso_kod_meny character varying(3) NOT NULL,
    hodnota_od numeric(10,5) NOT NULL,
    hodnota_do numeric(10,5) NOT NULL,
    hodnota_zaokrouhleni numeric(10,5) NOT NULL,
    smer character varying(6) DEFAULT 'NAHORU'::character varying NOT NULL
);


ALTER TABLE npos.pravidla_zaokrouhlovani OWNER TO "nPos";

--
-- TOC entry 3273 (class 0 OID 0)
-- Dependencies: 231
-- Name: COLUMN pravidla_zaokrouhlovani.smer; Type: COMMENT; Schema: npos; Owner: nPos
--

COMMENT ON COLUMN npos.pravidla_zaokrouhlovani.smer IS 'NAHORU nebo DOLU';


--
-- TOC entry 230 (class 1259 OID 16990)
-- Name: pravidla_zaokrouhlovani_cislo_pravidla_seq; Type: SEQUENCE; Schema: npos; Owner: nPos
--

CREATE SEQUENCE npos.pravidla_zaokrouhlovani_cislo_pravidla_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE npos.pravidla_zaokrouhlovani_cislo_pravidla_seq OWNER TO "nPos";

--
-- TOC entry 3274 (class 0 OID 0)
-- Dependencies: 230
-- Name: pravidla_zaokrouhlovani_cislo_pravidla_seq; Type: SEQUENCE OWNED BY; Schema: npos; Owner: nPos
--

ALTER SEQUENCE npos.pravidla_zaokrouhlovani_cislo_pravidla_seq OWNED BY npos.pravidla_zaokrouhlovani.cislo_pravidla;


--
-- TOC entry 217 (class 1259 OID 16601)
-- Name: skupiny_sortimentu; Type: TABLE; Schema: npos; Owner: nPos
--

CREATE TABLE npos.skupiny_sortimentu (
    id character varying(45) NOT NULL,
    nazev character varying(80) NOT NULL,
    id_nadrazene_skupiny character varying(45),
    sleva_povolena boolean DEFAULT true NOT NULL,
    max_sleva numeric(10,5)
);


ALTER TABLE npos.skupiny_sortimentu OWNER TO "nPos";

--
-- TOC entry 218 (class 1259 OID 16605)
-- Name: sortiment; Type: TABLE; Schema: npos; Owner: nPos
--

CREATE TABLE npos.sortiment (
    registr character varying(45) NOT NULL,
    skupina character varying(45) NOT NULL,
    nazev character varying(255) NOT NULL,
    plu character varying(5),
    mj character varying(5) NOT NULL,
    dph integer NOT NULL,
    minimalni_prodejne_mnozstvi numeric(10,5) DEFAULT 1 NOT NULL,
    maximalni_prodejne_mnozstvi numeric(10,5),
    prodej_povolen boolean DEFAULT true NOT NULL,
    jednotkova_cena numeric(10,5) NOT NULL,
    dodavatel integer,
    sleva_povolena boolean DEFAULT true NOT NULL,
    zmena_ceny_povolena boolean DEFAULT true NOT NULL,
    neskladova boolean DEFAULT false NOT NULL,
    sluzba boolean DEFAULT false NOT NULL,
    nutno_zadat_cenu boolean DEFAULT false NOT NULL,
    platebni_poukaz boolean DEFAULT false NOT NULL,
    vratka_povolena boolean DEFAULT true NOT NULL,
    nedelitelne boolean DEFAULT false NOT NULL,
    popis text,
    obrazek bytea,
    nutno_zadat_popis boolean DEFAULT false NOT NULL,
    obsah_baleni numeric(10,5),
    obsahova_mj character varying(5),
    pomerne_mnozstvi numeric(10,5) DEFAULT 1,
    zakladni_obsahova_mj character varying(5),
    zakladni_obsahove_mnozstvi numeric(10,5) DEFAULT 1,
    nutno_zadat_mnozstvi boolean DEFAULT false NOT NULL,
    sleva_v_akci_povolena boolean DEFAULT true NOT NULL
);


ALTER TABLE npos.sortiment OWNER TO "nPos";

--
-- TOC entry 219 (class 1259 OID 16626)
-- Name: seznam_sortimentu; Type: VIEW; Schema: npos; Owner: nPos
--

CREATE VIEW npos.seznam_sortimentu WITH (security_barrier='false') AS
 SELECT sortiment.registr,
    sortiment.skupina,
    skupiny_sortimentu.nazev AS nazev_skupiny,
    sortiment.nazev,
    sortiment.jednotkova_cena,
    carove_kody.kod,
    sortiment.prodej_povolen
   FROM ((npos.sortiment
     JOIN npos.skupiny_sortimentu ON (((skupiny_sortimentu.id)::text = (sortiment.skupina)::text)))
     LEFT JOIN npos.carove_kody ON (((carove_kody.registr)::text = (sortiment.registr)::text)))
  ORDER BY sortiment.registr;


ALTER TABLE npos.seznam_sortimentu OWNER TO "nPos";

--
-- TOC entry 220 (class 1259 OID 16631)
-- Name: skupiny_slev; Type: TABLE; Schema: npos; Owner: nPos
--

CREATE TABLE npos.skupiny_slev (
    cislo integer NOT NULL,
    nazev character varying(255) NOT NULL,
    typ_skupiny_slev character varying(45) NOT NULL,
    typ_slevy character varying(45) NOT NULL,
    hodnota_slevy numeric(10,5) NOT NULL,
    id_cile_slevy character varying(45),
    rozsah_pouziti character varying(45) NOT NULL,
    okamzik_uplatneni character varying(45) NOT NULL,
    platne boolean NOT NULL
);


ALTER TABLE npos.skupiny_slev OWNER TO "nPos";

--
-- TOC entry 221 (class 1259 OID 16634)
-- Name: staty; Type: TABLE; Schema: npos; Owner: nPos
--

CREATE TABLE npos.staty (
    iso_kod character varying(3) NOT NULL,
    bezny_nazev character varying(45) NOT NULL,
    uplny_nazev character varying(150) NOT NULL,
    hlavni boolean NOT NULL,
    mena character varying NOT NULL
);


ALTER TABLE npos.staty OWNER TO "nPos";

--
-- TOC entry 222 (class 1259 OID 16640)
-- Name: uzivatele; Type: TABLE; Schema: npos; Owner: nPos
--

CREATE TABLE npos.uzivatele (
    id_uzivatele integer NOT NULL,
    skupina integer NOT NULL,
    jmeno character varying(50),
    prijmeni character varying(50),
    blokovany boolean NOT NULL,
    heslo character varying(50) NOT NULL,
    pocet_prihlasovacich_pokusu integer NOT NULL
);


ALTER TABLE npos.uzivatele OWNER TO "nPos";

--
-- TOC entry 223 (class 1259 OID 16643)
-- Name: uzivatelske_skupiny; Type: TABLE; Schema: npos; Owner: nPos
--

CREATE TABLE npos.uzivatelske_skupiny (
    id_uzivatelske_skupiny integer NOT NULL,
    oznaceni_skupiny character varying(45) NOT NULL,
    poznamka text
);


ALTER TABLE npos.uzivatelske_skupiny OWNER TO "nPos";

--
-- TOC entry 224 (class 1259 OID 16649)
-- Name: zakaznici; Type: TABLE; Schema: npos; Owner: nPos
--

CREATE TABLE npos.zakaznici (
    id character varying(45) NOT NULL,
    nazev character varying(254),
    doplnek_nazvu character varying(254),
    ic character varying(15),
    dic character varying(20),
    blokovan boolean NOT NULL,
    duvod_blokace text,
    odebira_na_dl boolean NOT NULL,
    manualni_sleva_povolena boolean NOT NULL,
    skupina_slev integer,
    jmeno character varying(100),
    prijmeni character varying(100)
);


ALTER TABLE npos.zakaznici OWNER TO "nPos";

--
-- TOC entry 225 (class 1259 OID 16655)
-- Name: vyhledani_zakaznika; Type: VIEW; Schema: npos; Owner: nPos
--

CREATE VIEW npos.vyhledani_zakaznika WITH (security_barrier='false') AS
 SELECT zakaznici.id,
    zakaznici.nazev,
    zakaznici.doplnek_nazvu,
    zakaznici.jmeno,
    zakaznici.prijmeni,
    zakaznici.ic,
    zakaznici.dic,
    zakaznici.blokovan,
    zakaznici.duvod_blokace,
    zakaznici.odebira_na_dl,
    zakaznici.manualni_sleva_povolena,
    zakaznici.skupina_slev,
    adresy.druhy_radek,
    adresy.cp,
    adresy.cor,
    adresy.ulice,
    adresy.mesto,
    adresy.stat,
    adresy.telefon,
    adresy.mobil,
    adresy.fax,
    adresy.email,
    adresy.web,
    adresy.psc,
    adresy_zakazniku.dodaci,
    adresy.id AS id_adresy
   FROM ((npos.zakaznici
     JOIN npos.adresy_zakazniku ON (((adresy_zakazniku.zakaznik)::text = (zakaznici.id)::text)))
     JOIN npos.adresy ON ((adresy.id = adresy_zakazniku.adresa)))
  WHERE (adresy_zakazniku.hlavni = true)
  ORDER BY zakaznici.id;


ALTER TABLE npos.vyhledani_zakaznika OWNER TO "nPos";

--
-- TOC entry 3005 (class 2604 OID 16660)
-- Name: adresy id; Type: DEFAULT; Schema: npos; Owner: nPos
--

ALTER TABLE ONLY npos.adresy ALTER COLUMN id SET DEFAULT nextval('npos.adresy_id_seq'::regclass);


--
-- TOC entry 3036 (class 2604 OID 16958)
-- Name: denominace cislo; Type: DEFAULT; Schema: npos; Owner: nPos
--

ALTER TABLE ONLY npos.denominace ALTER COLUMN cislo SET DEFAULT nextval('npos.denominace_cislo_seq'::regclass);


--
-- TOC entry 3010 (class 2604 OID 16661)
-- Name: dodavatele id; Type: DEFAULT; Schema: npos; Owner: nPos
--

ALTER TABLE ONLY npos.dodavatele ALTER COLUMN id SET DEFAULT nextval('npos.dodavatele_id_seq'::regclass);


--
-- TOC entry 3011 (class 2604 OID 16662)
-- Name: dph id; Type: DEFAULT; Schema: npos; Owner: nPos
--

ALTER TABLE ONLY npos.dph ALTER COLUMN id SET DEFAULT nextval('npos.dph_id_seq'::regclass);


--
-- TOC entry 3042 (class 2604 OID 17013)
-- Name: obrazky_platidel_na_tlacitka cislo_zaznamu; Type: DEFAULT; Schema: npos; Owner: nPos
--

ALTER TABLE ONLY npos.obrazky_platidel_na_tlacitka ALTER COLUMN cislo_zaznamu SET DEFAULT nextval('npos.obrazky_platidel_na_tlacitka_cislo_zaznamu_seq'::regclass);


--
-- TOC entry 3040 (class 2604 OID 16995)
-- Name: pravidla_zaokrouhlovani cislo_pravidla; Type: DEFAULT; Schema: npos; Owner: nPos
--

ALTER TABLE ONLY npos.pravidla_zaokrouhlovani ALTER COLUMN cislo_pravidla SET DEFAULT nextval('npos.pravidla_zaokrouhlovani_cislo_pravidla_seq'::regclass);


--
-- TOC entry 3046 (class 2606 OID 16664)
-- Name: adresy_dodavatelu adresy_dodavatelu_pkey; Type: CONSTRAINT; Schema: npos; Owner: nPos
--

ALTER TABLE ONLY npos.adresy_dodavatelu
    ADD CONSTRAINT adresy_dodavatelu_pkey PRIMARY KEY (dodavatel);


--
-- TOC entry 3098 (class 2606 OID 16964)
-- Name: denominace denominace_pk; Type: CONSTRAINT; Schema: npos; Owner: nPos
--

ALTER TABLE ONLY npos.denominace
    ADD CONSTRAINT denominace_pk PRIMARY KEY (cislo);


--
-- TOC entry 3054 (class 2606 OID 16666)
-- Name: dph dph_pkey; Type: CONSTRAINT; Schema: npos; Owner: nPos
--

ALTER TABLE ONLY npos.dph
    ADD CONSTRAINT dph_pkey PRIMARY KEY (id);


--
-- TOC entry 3060 (class 2606 OID 16668)
-- Name: mena mena_pkey; Type: CONSTRAINT; Schema: npos; Owner: nPos
--

ALTER TABLE ONLY npos.mena
    ADD CONSTRAINT mena_pkey PRIMARY KEY (iso_kod);


--
-- TOC entry 3068 (class 2606 OID 16670)
-- Name: parkoviste parkoviste_pkey; Type: CONSTRAINT; Schema: npos; Owner: nPos
--

ALTER TABLE ONLY npos.parkoviste
    ADD CONSTRAINT parkoviste_pkey PRIMARY KEY (uid);


--
-- TOC entry 3050 (class 2606 OID 16672)
-- Name: dodavatele pk_dodavatele; Type: CONSTRAINT; Schema: npos; Owner: nPos
--

ALTER TABLE ONLY npos.dodavatele
    ADD CONSTRAINT pk_dodavatele PRIMARY KEY (id);


--
-- TOC entry 3071 (class 2606 OID 16674)
-- Name: platebni_prostredky platebni_prostredky_pk; Type: CONSTRAINT; Schema: npos; Owner: nPos
--

ALTER TABLE ONLY npos.platebni_prostredky
    ADD CONSTRAINT platebni_prostredky_pk PRIMARY KEY (id);


--
-- TOC entry 3100 (class 2606 OID 16997)
-- Name: pravidla_zaokrouhlovani pravidla_zaokrouhlovani_pk; Type: CONSTRAINT; Schema: npos; Owner: nPos
--

ALTER TABLE ONLY npos.pravidla_zaokrouhlovani
    ADD CONSTRAINT pravidla_zaokrouhlovani_pk PRIMARY KEY (cislo_pravidla);


--
-- TOC entry 3080 (class 2606 OID 16676)
-- Name: skupiny_slev skupiny_slev_pkey; Type: CONSTRAINT; Schema: npos; Owner: nPos
--

ALTER TABLE ONLY npos.skupiny_slev
    ADD CONSTRAINT skupiny_slev_pkey PRIMARY KEY (cislo);


--
-- TOC entry 3082 (class 2606 OID 16678)
-- Name: staty staty_pkey; Type: CONSTRAINT; Schema: npos; Owner: nPos
--

ALTER TABLE ONLY npos.staty
    ADD CONSTRAINT staty_pkey PRIMARY KEY (iso_kod);


--
-- TOC entry 3044 (class 2606 OID 16680)
-- Name: adresy uq_adresy; Type: CONSTRAINT; Schema: npos; Owner: nPos
--

ALTER TABLE ONLY npos.adresy
    ADD CONSTRAINT uq_adresy PRIMARY KEY (id);


--
-- TOC entry 3048 (class 2606 OID 16682)
-- Name: carove_kody uq_carove_kody; Type: CONSTRAINT; Schema: npos; Owner: nPos
--

ALTER TABLE ONLY npos.carove_kody
    ADD CONSTRAINT uq_carove_kody PRIMARY KEY (kod);


--
-- TOC entry 3074 (class 2606 OID 16684)
-- Name: pravidla_skupin_slev uq_cislo_pravidla; Type: CONSTRAINT; Schema: npos; Owner: nPos
--

ALTER TABLE ONLY npos.pravidla_skupin_slev
    ADD CONSTRAINT uq_cislo_pravidla PRIMARY KEY (cislo_pravidla);


--
-- TOC entry 3052 (class 2606 OID 16686)
-- Name: dodavatele uq_dodavatele; Type: CONSTRAINT; Schema: npos; Owner: nPos
--

ALTER TABLE ONLY npos.dodavatele
    ADD CONSTRAINT uq_dodavatele UNIQUE (id, ic);


--
-- TOC entry 3056 (class 2606 OID 16688)
-- Name: dph uq_dph; Type: CONSTRAINT; Schema: npos; Owner: nPos
--

ALTER TABLE ONLY npos.dph
    ADD CONSTRAINT uq_dph UNIQUE (id, typ, oznaceni, sazba);


--
-- TOC entry 3058 (class 2606 OID 16690)
-- Name: filialka uq_filialka; Type: CONSTRAINT; Schema: npos; Owner: nPos
--

ALTER TABLE ONLY npos.filialka
    ADD CONSTRAINT uq_filialka PRIMARY KEY (oznaceni);


--
-- TOC entry 3086 (class 2606 OID 16692)
-- Name: uzivatele uq_id_uzivatele; Type: CONSTRAINT; Schema: npos; Owner: nPos
--

ALTER TABLE ONLY npos.uzivatele
    ADD CONSTRAINT uq_id_uzivatele PRIMARY KEY (id_uzivatele);


--
-- TOC entry 3088 (class 2606 OID 16694)
-- Name: uzivatelske_skupiny uq_id_uzivatelske_skupiny; Type: CONSTRAINT; Schema: npos; Owner: nPos
--

ALTER TABLE ONLY npos.uzivatelske_skupiny
    ADD CONSTRAINT uq_id_uzivatelske_skupiny UNIQUE (id_uzivatelske_skupiny);


--
-- TOC entry 3094 (class 2606 OID 16696)
-- Name: zakaznici uq_id_zakaznika; Type: CONSTRAINT; Schema: npos; Owner: nPos
--

ALTER TABLE ONLY npos.zakaznici
    ADD CONSTRAINT uq_id_zakaznika UNIQUE (id);


--
-- TOC entry 3062 (class 2606 OID 16698)
-- Name: mena uq_meny; Type: CONSTRAINT; Schema: npos; Owner: nPos
--

ALTER TABLE ONLY npos.mena
    ADD CONSTRAINT uq_meny UNIQUE (iso_kod, oznaceni, narodni_symbol);


--
-- TOC entry 3064 (class 2606 OID 16700)
-- Name: merne_jednotky uq_mj; Type: CONSTRAINT; Schema: npos; Owner: nPos
--

ALTER TABLE ONLY npos.merne_jednotky
    ADD CONSTRAINT uq_mj PRIMARY KEY (mj);


--
-- TOC entry 3066 (class 2606 OID 16702)
-- Name: merne_jednotky uq_mj_oznaceni; Type: CONSTRAINT; Schema: npos; Owner: nPos
--

ALTER TABLE ONLY npos.merne_jednotky
    ADD CONSTRAINT uq_mj_oznaceni UNIQUE (oznaceni);


--
-- TOC entry 3090 (class 2606 OID 16704)
-- Name: uzivatelske_skupiny uq_oznaceni_uzivatelske_skupiny; Type: CONSTRAINT; Schema: npos; Owner: nPos
--

ALTER TABLE ONLY npos.uzivatelske_skupiny
    ADD CONSTRAINT uq_oznaceni_uzivatelske_skupiny UNIQUE (oznaceni_skupiny);


--
-- TOC entry 3076 (class 2606 OID 16706)
-- Name: skupiny_sortimentu uq_skupiny_sortimentu; Type: CONSTRAINT; Schema: npos; Owner: nPos
--

ALTER TABLE ONLY npos.skupiny_sortimentu
    ADD CONSTRAINT uq_skupiny_sortimentu PRIMARY KEY (id);


--
-- TOC entry 3078 (class 2606 OID 16708)
-- Name: sortiment uq_sortiment; Type: CONSTRAINT; Schema: npos; Owner: nPos
--

ALTER TABLE ONLY npos.sortiment
    ADD CONSTRAINT uq_sortiment PRIMARY KEY (registr);


--
-- TOC entry 3084 (class 2606 OID 16710)
-- Name: staty uq_staty; Type: CONSTRAINT; Schema: npos; Owner: nPos
--

ALTER TABLE ONLY npos.staty
    ADD CONSTRAINT uq_staty UNIQUE (iso_kod, bezny_nazev, uplny_nazev);


--
-- TOC entry 3092 (class 2606 OID 16712)
-- Name: uzivatelske_skupiny uzivatelske_skupiny_pkey; Type: CONSTRAINT; Schema: npos; Owner: nPos
--

ALTER TABLE ONLY npos.uzivatelske_skupiny
    ADD CONSTRAINT uzivatelske_skupiny_pkey PRIMARY KEY (id_uzivatelske_skupiny);


--
-- TOC entry 3096 (class 2606 OID 16714)
-- Name: zakaznici zakaznici_pkey; Type: CONSTRAINT; Schema: npos; Owner: nPos
--

ALTER TABLE ONLY npos.zakaznici
    ADD CONSTRAINT zakaznici_pkey PRIMARY KEY (id);


--
-- TOC entry 3069 (class 1259 OID 16715)
-- Name: platebni_prostredky_id_uindex; Type: INDEX; Schema: npos; Owner: nPos
--

CREATE UNIQUE INDEX platebni_prostredky_id_uindex ON npos.platebni_prostredky USING btree (id);


--
-- TOC entry 3072 (class 1259 OID 16716)
-- Name: platebni_prostredky_typ_uindex; Type: INDEX; Schema: npos; Owner: nPos
--

CREATE UNIQUE INDEX platebni_prostredky_typ_uindex ON npos.platebni_prostredky USING btree (typ);


--
-- TOC entry 3104 (class 2606 OID 16717)
-- Name: adresy_zakazniku fk_adresy_adresy_zakazniku; Type: FK CONSTRAINT; Schema: npos; Owner: nPos
--

ALTER TABLE ONLY npos.adresy_zakazniku
    ADD CONSTRAINT fk_adresy_adresy_zakazniku FOREIGN KEY (adresa) REFERENCES npos.adresy(id);


--
-- TOC entry 3102 (class 2606 OID 16722)
-- Name: adresy_dodavatelu fk_adresy_dodavatelu_adresy; Type: FK CONSTRAINT; Schema: npos; Owner: nPos
--

ALTER TABLE ONLY npos.adresy_dodavatelu
    ADD CONSTRAINT fk_adresy_dodavatelu_adresy FOREIGN KEY (adresa) REFERENCES npos.adresy(id);


--
-- TOC entry 3103 (class 2606 OID 16727)
-- Name: adresy_dodavatelu fk_adresy_dodavatelu_dodavatele; Type: FK CONSTRAINT; Schema: npos; Owner: nPos
--

ALTER TABLE ONLY npos.adresy_dodavatelu
    ADD CONSTRAINT fk_adresy_dodavatelu_dodavatele FOREIGN KEY (dodavatel) REFERENCES npos.dodavatele(id);


--
-- TOC entry 3101 (class 2606 OID 16732)
-- Name: adresy fk_adresy_staty; Type: FK CONSTRAINT; Schema: npos; Owner: nPos
--

ALTER TABLE ONLY npos.adresy
    ADD CONSTRAINT fk_adresy_staty FOREIGN KEY (stat) REFERENCES npos.staty(iso_kod);


--
-- TOC entry 3106 (class 2606 OID 16737)
-- Name: carove_kody fk_carove_kody_sortiment; Type: FK CONSTRAINT; Schema: npos; Owner: nPos
--

ALTER TABLE ONLY npos.carove_kody
    ADD CONSTRAINT fk_carove_kody_sortiment FOREIGN KEY (registr) REFERENCES npos.sortiment(registr);


--
-- TOC entry 3121 (class 2606 OID 16973)
-- Name: denominace fk_denominace_meny; Type: FK CONSTRAINT; Schema: npos; Owner: nPos
--

ALTER TABLE ONLY npos.denominace
    ADD CONSTRAINT fk_denominace_meny FOREIGN KEY (iso_kod_meny) REFERENCES npos.mena(iso_kod);


--
-- TOC entry 3107 (class 2606 OID 16742)
-- Name: filialka fk_filialka_staty; Type: FK CONSTRAINT; Schema: npos; Owner: nPos
--

ALTER TABLE ONLY npos.filialka
    ADD CONSTRAINT fk_filialka_staty FOREIGN KEY (stat) REFERENCES npos.staty(iso_kod);


--
-- TOC entry 3120 (class 2606 OID 16948)
-- Name: kurzy_men fk_kurz_mena; Type: FK CONSTRAINT; Schema: npos; Owner: nPos
--

ALTER TABLE ONLY npos.kurzy_men
    ADD CONSTRAINT fk_kurz_mena FOREIGN KEY (iso_kod_meny) REFERENCES npos.mena(iso_kod);


--
-- TOC entry 3108 (class 2606 OID 16747)
-- Name: merne_jednotky fk_mj; Type: FK CONSTRAINT; Schema: npos; Owner: nPos
--

ALTER TABLE ONLY npos.merne_jednotky
    ADD CONSTRAINT fk_mj FOREIGN KEY (zakladni_mj) REFERENCES npos.merne_jednotky(mj);


--
-- TOC entry 3124 (class 2606 OID 17017)
-- Name: obrazky_platidel_na_tlacitka fk_obrazky_platidel_na_tlacitka_denominace; Type: FK CONSTRAINT; Schema: npos; Owner: nPos
--

ALTER TABLE ONLY npos.obrazky_platidel_na_tlacitka
    ADD CONSTRAINT fk_obrazky_platidel_na_tlacitka_denominace FOREIGN KEY (denominace) REFERENCES npos.denominace(cislo);


--
-- TOC entry 3109 (class 2606 OID 16752)
-- Name: pravidla_skupin_slev fk_pravidla_slev_skupiny_slev; Type: FK CONSTRAINT; Schema: npos; Owner: nPos
--

ALTER TABLE ONLY npos.pravidla_skupin_slev
    ADD CONSTRAINT fk_pravidla_slev_skupiny_slev FOREIGN KEY (cislo_skupiny_slev) REFERENCES npos.skupiny_slev(cislo);


--
-- TOC entry 3123 (class 2606 OID 17003)
-- Name: pravidla_zaokrouhlovani fk_pravidla_zaokrouhlovani_mena; Type: FK CONSTRAINT; Schema: npos; Owner: nPos
--

ALTER TABLE ONLY npos.pravidla_zaokrouhlovani
    ADD CONSTRAINT fk_pravidla_zaokrouhlovani_mena FOREIGN KEY (iso_kod_meny) REFERENCES npos.mena(iso_kod);


--
-- TOC entry 3122 (class 2606 OID 16998)
-- Name: pravidla_zaokrouhlovani fk_pravidla_zaokrouhlovani_platebni_prostredky; Type: FK CONSTRAINT; Schema: npos; Owner: nPos
--

ALTER TABLE ONLY npos.pravidla_zaokrouhlovani
    ADD CONSTRAINT fk_pravidla_zaokrouhlovani_platebni_prostredky FOREIGN KEY (id_platebniho_prostredku) REFERENCES npos.platebni_prostredky(id);


--
-- TOC entry 3110 (class 2606 OID 16757)
-- Name: skupiny_sortimentu fk_skupiny_sortimentu; Type: FK CONSTRAINT; Schema: npos; Owner: nPos
--

ALTER TABLE ONLY npos.skupiny_sortimentu
    ADD CONSTRAINT fk_skupiny_sortimentu FOREIGN KEY (id_nadrazene_skupiny) REFERENCES npos.skupiny_sortimentu(id);


--
-- TOC entry 3111 (class 2606 OID 16762)
-- Name: sortiment fk_sortiment_dodavatele; Type: FK CONSTRAINT; Schema: npos; Owner: nPos
--

ALTER TABLE ONLY npos.sortiment
    ADD CONSTRAINT fk_sortiment_dodavatele FOREIGN KEY (dodavatel) REFERENCES npos.dodavatele(id);


--
-- TOC entry 3112 (class 2606 OID 16767)
-- Name: sortiment fk_sortiment_dph; Type: FK CONSTRAINT; Schema: npos; Owner: nPos
--

ALTER TABLE ONLY npos.sortiment
    ADD CONSTRAINT fk_sortiment_dph FOREIGN KEY (dph) REFERENCES npos.dph(id);


--
-- TOC entry 3113 (class 2606 OID 16772)
-- Name: sortiment fk_sortiment_mj; Type: FK CONSTRAINT; Schema: npos; Owner: nPos
--

ALTER TABLE ONLY npos.sortiment
    ADD CONSTRAINT fk_sortiment_mj FOREIGN KEY (mj) REFERENCES npos.merne_jednotky(mj);


--
-- TOC entry 3114 (class 2606 OID 16777)
-- Name: sortiment fk_sortiment_obsahova_mj; Type: FK CONSTRAINT; Schema: npos; Owner: nPos
--

ALTER TABLE ONLY npos.sortiment
    ADD CONSTRAINT fk_sortiment_obsahova_mj FOREIGN KEY (obsahova_mj) REFERENCES npos.merne_jednotky(mj);


--
-- TOC entry 3115 (class 2606 OID 16782)
-- Name: sortiment fk_sortiment_skupiny_sortimentu; Type: FK CONSTRAINT; Schema: npos; Owner: nPos
--

ALTER TABLE ONLY npos.sortiment
    ADD CONSTRAINT fk_sortiment_skupiny_sortimentu FOREIGN KEY (skupina) REFERENCES npos.skupiny_sortimentu(id);


--
-- TOC entry 3116 (class 2606 OID 16787)
-- Name: sortiment fk_sortiment_zakladni_obsahova_mj; Type: FK CONSTRAINT; Schema: npos; Owner: nPos
--

ALTER TABLE ONLY npos.sortiment
    ADD CONSTRAINT fk_sortiment_zakladni_obsahova_mj FOREIGN KEY (zakladni_obsahova_mj) REFERENCES npos.merne_jednotky(mj);


--
-- TOC entry 3117 (class 2606 OID 16792)
-- Name: staty fk_staty_meny; Type: FK CONSTRAINT; Schema: npos; Owner: nPos
--

ALTER TABLE ONLY npos.staty
    ADD CONSTRAINT fk_staty_meny FOREIGN KEY (mena) REFERENCES npos.mena(iso_kod);


--
-- TOC entry 3118 (class 2606 OID 16797)
-- Name: uzivatele fk_uzivatele_skupiny; Type: FK CONSTRAINT; Schema: npos; Owner: nPos
--

ALTER TABLE ONLY npos.uzivatele
    ADD CONSTRAINT fk_uzivatele_skupiny FOREIGN KEY (skupina) REFERENCES npos.uzivatelske_skupiny(id_uzivatelske_skupiny);


--
-- TOC entry 3105 (class 2606 OID 16802)
-- Name: adresy_zakazniku fk_zakaznici_adresy_zakazniu; Type: FK CONSTRAINT; Schema: npos; Owner: nPos
--

ALTER TABLE ONLY npos.adresy_zakazniku
    ADD CONSTRAINT fk_zakaznici_adresy_zakazniu FOREIGN KEY (zakaznik) REFERENCES npos.zakaznici(id);


--
-- TOC entry 3119 (class 2606 OID 16807)
-- Name: zakaznici fk_zakaznici_skupiny_slev; Type: FK CONSTRAINT; Schema: npos; Owner: nPos
--

ALTER TABLE ONLY npos.zakaznici
    ADD CONSTRAINT fk_zakaznici_skupiny_slev FOREIGN KEY (skupina_slev) REFERENCES npos.skupiny_slev(cislo);


-- Completed on 2021-01-25 22:48:41

--
-- PostgreSQL database dump complete
--

