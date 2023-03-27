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
-- Data for Name: currencies; Type: TABLE DATA; Schema: nbo; Owner: nBo
--

INSERT INTO nbo.currencies (iso_code, name, symbol, acceptable, main) VALUES ('CZK', 'Česká koruna', 'Kč', true, true);
INSERT INTO nbo.currencies (iso_code, name, symbol, acceptable, main) VALUES ('EUR', 'Euro', '€', true, false);
INSERT INTO nbo.currencies (iso_code, name, symbol, acceptable, main) VALUES ('USD', 'Americký dolar', '$', true, false);
INSERT INTO nbo.currencies (iso_code, name, symbol, acceptable, main) VALUES ('JPY', 'Japonský jen', '¥', false, false);


--
-- Data for Name: countries; Type: TABLE DATA; Schema: nbo; Owner: nBo
--

INSERT INTO nbo.countries (iso_code, common_name, full_name, currency_code, main) VALUES ('CZ', 'Česko', 'Česká republika', 'CZK', true);
INSERT INTO nbo.countries (iso_code, common_name, full_name, currency_code, main) VALUES ('SK', 'Slovensko', 'Slovenská republika', 'EUR', false);
INSERT INTO nbo.countries (iso_code, common_name, full_name, currency_code, main) VALUES ('D', 'Německo', 'Spolková republika Německo', 'EUR', false);
INSERT INTO nbo.countries (iso_code, common_name, full_name, currency_code, main) VALUES ('USA', 'Spojené státy', 'Spojené státy americké', 'USD', false);
INSERT INTO nbo.countries (iso_code, common_name, full_name, currency_code, main) VALUES ('JP', 'Japonsko', 'Japonské císařství', 'JPY', false);


--
-- Data for Name: functions; Type: TABLE DATA; Schema: nbo; Owner: nBo
--

INSERT INTO nbo.functions (id, description, component, key_component) VALUES ('2', 'BO', 'cz.jbenak.npos.boClient.gui.dialogs.login', 'BOC');
INSERT INTO nbo.functions (id, description, component, key_component) VALUES ('1', 'POS', 'cz.jbenak.npos.pos.gui.prihlaseni', 'POS');


--
-- Data for Name: user_groups; Type: TABLE DATA; Schema: nbo; Owner: nBo
--

INSERT INTO nbo.user_groups (id, name, description) VALUES (1, 'globalAdmin', 'Skupina s nejvyšším oprávněním pro správu celého systému.');


--
-- Data for Name: functions_mapping; Type: TABLE DATA; Schema: nbo; Owner: nBo
--

INSERT INTO nbo.functions_mapping (id, group_id, function_id) VALUES (1, 1, '1');
INSERT INTO nbo.functions_mapping (id, group_id, function_id) VALUES (2, 1, '2');


--
-- Data for Name: measure_units; Type: TABLE DATA; Schema: nbo; Owner: nBo
--

INSERT INTO nbo.measure_units (unit, name, base_unit, ratio) VALUES ('ks', 'Kus', NULL, NULL);
INSERT INTO nbo.measure_units (unit, name, base_unit, ratio) VALUES ('l', 'Litr', NULL, NULL);
INSERT INTO nbo.measure_units (unit, name, base_unit, ratio) VALUES ('ml', 'Mililitr se strašně dlouhým názvem', 'l', 0.001);
INSERT INTO nbo.measure_units (unit, name, base_unit, ratio) VALUES ('m', 'metr', NULL, NULL);
INSERT INTO nbo.measure_units (unit, name, base_unit, ratio) VALUES ('kg', 'Kilogram', NULL, NULL);


--
-- Data for Name: numbering_series; Type: TABLE DATA; Schema: nbo; Owner: nBo
--

INSERT INTO nbo.numbering_series (number, definition, document_type, sequence_number_length, valid_from, start_serial_from, label) VALUES (1, 'FV/yyyy-mm-dd/{S}/$', 'INVOICE', 5, '2023-03-27', 1, 'Faktura');
INSERT INTO nbo.numbering_series (number, definition, document_type, sequence_number_length, valid_from, start_serial_from, label) VALUES (2, 'DL/yyyy-mm-dd/{S}/$', 'DELIVERY_NOTE', 5, '2023-03-27', 1, 'Dodací list');
INSERT INTO nbo.numbering_series (number, definition, document_type, sequence_number_length, valid_from, start_serial_from, label) VALUES (3, 'PD/yyyy-mm-dd/{S}/$', 'RECEIPT', 5, '2023-03-27', 1, 'Paragon');
INSERT INTO nbo.numbering_series (number, definition, document_type, sequence_number_length, valid_from, start_serial_from, label) VALUES (4, 'DO/yyyy-mm-dd/{S}/$', 'CREDIT_NOTE', 5, '2023-03-27', 1, 'Dobropis');


--
-- Data for Name: users; Type: TABLE DATA; Schema: nbo; Owner: nBo
--

INSERT INTO nbo.users (id, user_name, user_surname, password, group_id, mail, phone, rest_login_attempts, locked, last_login_timestamp, note) VALUES (1, 'Admin', 'Administrátor', '$2a$06$r49YuB8b5h/EjjNCkS78xuuWnvlQWM0S3ZSJJyHw0fKM0QSVWgIjW', 1, 'admin@npos.com', '+420 123 456 789', 3, false, '2023-03-27 01:55:19.402924', 'Globální administrátor.');


--
-- Data for Name: users_mapping; Type: TABLE DATA; Schema: nbo; Owner: nBo
--

INSERT INTO nbo.users_mapping (id, user_id, group_id) VALUES (1, 1, 1);


--
-- Data for Name: vat; Type: TABLE DATA; Schema: nbo; Owner: nBo
--

INSERT INTO nbo.vat (id, vat_type, percentage, label, valid_from) VALUES (1, 'BASE', 21.00, 'Základní', '2023-03-27');
INSERT INTO nbo.vat (id, vat_type, percentage, label, valid_from) VALUES (2, 'LOWERED_1', 15.00, 'Snížená 1', '2023-03-27');
INSERT INTO nbo.vat (id, vat_type, percentage, label, valid_from) VALUES (3, 'LOWERED_2', 10.00, 'Snížená 2', '2023-03-27');
INSERT INTO nbo.vat (id, vat_type, percentage, label, valid_from) VALUES (4, 'ZERO', 0.00, 'Nulová', '2023-03-27');


--
-- Name: functions_mapping_id_seq; Type: SEQUENCE SET; Schema: nbo; Owner: nBo
--

SELECT pg_catalog.setval('nbo.functions_mapping_id_seq', 1, false);


--
-- Name: numbering_series_number_seq; Type: SEQUENCE SET; Schema: nbo; Owner: nBo
--

SELECT pg_catalog.setval('nbo.numbering_series_number_seq', 4, true);


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

SELECT pg_catalog.setval('nbo.vat_id_seq', 4, true);


--
-- PostgreSQL database dump complete
--

