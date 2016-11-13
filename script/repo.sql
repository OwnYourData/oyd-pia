--
-- PostgreSQL database dump
--

-- Dumped from database version 9.6.0
-- Dumped by pg_dump version 9.6.0

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

SET search_path = public, pg_catalog;

--
-- Data for Name: repo; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO repo (id, creator, description, identifier) VALUES (1003, NULL, 'n/a', 'eu.ownyourdata.bank');
INSERT INTO repo (id, creator, description, identifier) VALUES (1220, NULL, 'n/a', 'eu.ownyourdata.bank.log');
INSERT INTO repo (id, creator, description, identifier) VALUES (1222, NULL, 'n/a', 'eu.ownyourdata.bank.reference');
INSERT INTO repo (id, creator, description, identifier) VALUES (1225, NULL, NULL, 'eu.ownyourdata.info');
INSERT INTO repo (id, creator, description, identifier) VALUES (1227, NULL, 'n/a', 'eu.ownyourdata.scheduler');
INSERT INTO repo (id, creator, description, identifier) VALUES (1229, NULL, 'n/a', 'eu.ownyourdata.room.nagios');
INSERT INTO repo (id, creator, description, identifier) VALUES (1232, NULL, 'n/a', 'eu.ownyourdata.room.temp1');
INSERT INTO repo (id, creator, description, identifier) VALUES (2282, NULL, 'n/a', 'eu.ownyourdata.room.hum1');
INSERT INTO repo (id, creator, description, identifier) VALUES (3332, NULL, 'n/a', 'eu.ownyourdata.allergy.pollination');
INSERT INTO repo (id, creator, description, identifier) VALUES (3341, NULL, 'n/a', 'eu.ownyourdata.allergy.condition');
INSERT INTO repo (id, creator, description, identifier) VALUES (3352, NULL, 'n/a', 'eu.ownyourdata.allergy.medintake');


--
-- Name: repo_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('repo_id_seq', 1, false);


--
-- PostgreSQL database dump complete
--

