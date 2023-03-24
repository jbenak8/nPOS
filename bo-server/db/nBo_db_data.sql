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

--
-- Data for Name: countries; Type: TABLE DATA; Schema: nbo; Owner: nBo
--

INSERT INTO nbo.countries VALUES ('CZ', 'Česko', 'Česká republika', 'CZK', true);
INSERT INTO nbo.countries VALUES ('SK', 'Slovensko', 'Slovenská republika', 'EUR', false);
INSERT INTO nbo.countries VALUES ('D', 'Německo', 'Spolková republika Německo', 'EUR', false);
INSERT INTO nbo.countries VALUES ('USA', 'Spojené státy', 'Spojené státy americké', 'USD', false);
INSERT INTO nbo.countries VALUES ('JP', 'Japonsko', 'Japonské císařství', 'JPY', false);


--
-- Data for Name: currencies; Type: TABLE DATA; Schema: nbo; Owner: nBo
--

INSERT INTO nbo.currencies VALUES ('CZK', 'Česká koruna', 'Kč', true, true);
INSERT INTO nbo.currencies VALUES ('EUR', 'Euro', '€', true, false);
INSERT INTO nbo.currencies VALUES ('USD', 'Americký dolar', '$', true, false);
INSERT INTO nbo.currencies VALUES ('JPY', 'Japonský jen', '¥', false, false);


--
-- Data for Name: functions; Type: TABLE DATA; Schema: nbo; Owner: nBo
--

INSERT INTO nbo.functions VALUES ('2', 'BO', 'cz.jbenak.npos.boClient.gui.dialogs.login', 'BOC');
INSERT INTO nbo.functions VALUES ('1', 'POS', 'cz.jbenak.npos.pos.gui.prihlaseni', 'POS');


--
-- Data for Name: functions_mapping; Type: TABLE DATA; Schema: nbo; Owner: nBo
--

INSERT INTO nbo.functions_mapping VALUES (1, 1, '1');
INSERT INTO nbo.functions_mapping VALUES (2, 1, '2');


--
-- Data for Name: measure_units; Type: TABLE DATA; Schema: nbo; Owner: nBo
--

INSERT INTO nbo.measure_units VALUES ('ks', 'Kus', NULL, NULL);
INSERT INTO nbo.measure_units VALUES ('l', 'Litr', NULL, NULL);
INSERT INTO nbo.measure_units VALUES ('ml', 'Mililitr se strašně dlouhým názvem', 'l', 0.001);
INSERT INTO nbo.measure_units VALUES ('m', 'metr', NULL, NULL);
INSERT INTO nbo.measure_units VALUES ('kg', 'Kilogram', NULL, NULL);


--
-- Data for Name: numbering_series; Type: TABLE DATA; Schema: nbo; Owner: nBo
--

INSERT INTO nbo.numbering_series VALUES (1, 'FV/yyyy/$', 'INVOICE', 5, '2023-03-24', 1, 'Faktura');


--
-- Data for Name: user_groups; Type: TABLE DATA; Schema: nbo; Owner: nBo
--

INSERT INTO nbo.user_groups VALUES (1, 'globalAdmin', 'Skupina s nejvyšším oprávněním pro správu celého systému.');


--
-- Data for Name: users; Type: TABLE DATA; Schema: nbo; Owner: nBo
--

INSERT INTO nbo.users VALUES (1, 'Admin', 'Administrátor', '$2a$06$r49YuB8b5h/EjjNCkS78xuuWnvlQWM0S3ZSJJyHw0fKM0QSVWgIjW', 1, 'admin@npos.com', '+420 123 456 789', 3, false, '2023-03-24 00:33:09.720696', 'Globální administrátor.');


--
-- Data for Name: users_mapping; Type: TABLE DATA; Schema: nbo; Owner: nBo
--

INSERT INTO nbo.users_mapping VALUES (1, 1, 1);


--
-- Data for Name: vat; Type: TABLE DATA; Schema: nbo; Owner: nBo
--

INSERT INTO nbo.vat VALUES (1, 'BASE', 21.00, 'Základní', '2023-03-17');
INSERT INTO nbo.vat VALUES (2, 'BASE', 20.00, 'Základní', '2023-03-17');
INSERT INTO nbo.vat VALUES (3, 'LOWERED_1', 15.00, 'Snížená 1', '2023-03-17');
INSERT INTO nbo.vat VALUES (4, 'ZERO', 0.00, 'Nulová', '2023-03-17');
INSERT INTO nbo.vat VALUES (5, 'LOWERED_1', 10.00, 'Snížená 1', '2023-03-17');
INSERT INTO nbo.vat VALUES (6, 'LOWERED_2', 8.00, 'Snížená 2', '2023-03-17');
INSERT INTO nbo.vat VALUES (7, 'LOWERED_1', 8.00, 'Snížená 1', '2023-03-17');
INSERT INTO nbo.vat VALUES (8, 'BASE', 21.00, 'Základní', '2023-03-18');
INSERT INTO nbo.vat VALUES (9, 'BASE', 28.00, 'Základní', '2023-03-05');


--
-- Name: functions_mapping_id_seq; Type: SEQUENCE SET; Schema: nbo; Owner: nBo
--

SELECT pg_catalog.setval('nbo.functions_mapping_id_seq', 1, false);


--
-- Name: numbering_series_number_seq; Type: SEQUENCE SET; Schema: nbo; Owner: nBo
--

SELECT pg_catalog.setval('nbo.numbering_series_number_seq', 1, true);


--
-- Name: user_groups_id_seq; Type: SEQUENCE SET; Schema: nbo; Owner: nBo
--

SELECT pg_catalog.setval('nbo.user_groups_id_seq', 1, false);


--
-- Name: users_mapping_id_seq; Type: SEQUENCE SET; Schema: nbo; Owner: nBo
--

SELECT pg_catalog.setval('nbo.users_mapping_id_seq', 1, false);


--
-- Name: vat_id_seq; Type: SEQUENCE SET; Schema: nbo; Owner: nBo
--

SELECT pg_catalog.setval('nbo.vat_id_seq', 8, true);


--
-- PostgreSQL database dump complete
--

