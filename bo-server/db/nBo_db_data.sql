--
-- PostgreSQL database dump
--

-- Dumped from database version 14.2
-- Dumped by pg_dump version 14.2

-- Started on 2022-11-07 02:18:01

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
-- TOC entry 3415 (class 1262 OID 40961)
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
-- TOC entry 3409 (class 0 OID 49200)
-- Dependencies: 220
-- Data for Name: currency; Type: TABLE DATA; Schema: nbo; Owner: nBo
--

INSERT INTO nbo.currency VALUES ('CZK', 'Česká koruna', 'Kč', true, true);
INSERT INTO nbo.currency VALUES ('EUR', 'Euro', '€', true, false);


--
-- TOC entry 3402 (class 0 OID 40980)
-- Dependencies: 213
-- Data for Name: functions; Type: TABLE DATA; Schema: nbo; Owner: nBo
--

INSERT INTO nbo.functions VALUES ('2', 'BO', 'cz.jbenak.npos.boClient.gui.dialogs.login', 'BOC');
INSERT INTO nbo.functions VALUES ('1', 'POS', 'cz.jbenak.npos.pos.gui.prihlaseni', 'POS');


--
-- TOC entry 3403 (class 0 OID 40989)
-- Dependencies: 214
-- Data for Name: functions_mapping; Type: TABLE DATA; Schema: nbo; Owner: nBo
--

INSERT INTO nbo.functions_mapping VALUES (1, 1, '1');
INSERT INTO nbo.functions_mapping VALUES (2, 1, '2');


--
-- TOC entry 3408 (class 0 OID 49190)
-- Dependencies: 219
-- Data for Name: measure_units; Type: TABLE DATA; Schema: nbo; Owner: nBo
--

INSERT INTO nbo.measure_units VALUES ('ks', 'Kus', NULL, NULL);
INSERT INTO nbo.measure_units VALUES ('l', 'Litr', NULL, NULL);
INSERT INTO nbo.measure_units VALUES ('ml', 'Mililitr se strašně dlouhým názvem', 'l', 0.001);
INSERT INTO nbo.measure_units VALUES ('m', 'metr', NULL, NULL);


--
-- TOC entry 3401 (class 0 OID 40973)
-- Dependencies: 212
-- Data for Name: user_groups; Type: TABLE DATA; Schema: nbo; Owner: nBo
--

INSERT INTO nbo.user_groups VALUES (1, 'globalAdmin', 'Skupina s nejvyšším oprávněním pro správu celého systému.');


--
-- TOC entry 3400 (class 0 OID 40963)
-- Dependencies: 211
-- Data for Name: users; Type: TABLE DATA; Schema: nbo; Owner: nBo
--

INSERT INTO nbo.users VALUES (1, 'Admin', 'Administrátor', '$2a$06$r49YuB8b5h/EjjNCkS78xuuWnvlQWM0S3ZSJJyHw0fKM0QSVWgIjW', 1, 'admin@npos.com', '+420 123 456 789', 3, false, '2022-11-06 02:43:05.604097', 'Globální administrátor.');


--
-- TOC entry 3404 (class 0 OID 41004)
-- Dependencies: 215
-- Data for Name: users_mapping; Type: TABLE DATA; Schema: nbo; Owner: nBo
--

INSERT INTO nbo.users_mapping VALUES (1, 1, 1);


--
-- TOC entry 3424 (class 0 OID 0)
-- Dependencies: 216
-- Name: functions_mapping_id_seq; Type: SEQUENCE SET; Schema: nbo; Owner: nBo
--

SELECT pg_catalog.setval('nbo.functions_mapping_id_seq', 1, false);


--
-- TOC entry 3425 (class 0 OID 0)
-- Dependencies: 217
-- Name: user_groups_id_seq; Type: SEQUENCE SET; Schema: nbo; Owner: nBo
--

SELECT pg_catalog.setval('nbo.user_groups_id_seq', 1, false);


--
-- TOC entry 3426 (class 0 OID 0)
-- Dependencies: 218
-- Name: users_mapping_id_seq; Type: SEQUENCE SET; Schema: nbo; Owner: nBo
--

SELECT pg_catalog.setval('nbo.users_mapping_id_seq', 1, false);


-- Completed on 2022-11-07 02:18:01

--
-- PostgreSQL database dump complete
--

