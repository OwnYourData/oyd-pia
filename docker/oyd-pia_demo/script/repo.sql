--
-- PostgreSQL database dump
--

-- Dumped from database version 9.6.1
-- Dumped by pg_dump version 9.6.1

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

INSERT INTO repo (id, creator, description, identifier) VALUES (1007, NULL, 'Scheduler', 'eu.ownyourdata.scheduler');
INSERT INTO repo (id, creator, description, identifier) VALUES (1009, NULL, 'Raumklima-Skript', 'eu.ownyourdata.room.script');
INSERT INTO repo (id, creator, description, identifier) VALUES (1011, NULL, 'NAGIOS Import', 'eu.ownyourdata.room.nagios');
INSERT INTO repo (id, creator, description, identifier) VALUES (1014, NULL, 'Feuchtigkeit', 'eu.ownyourdata.room.hum');
INSERT INTO repo (id, creator, description, identifier) VALUES (2343, NULL, 'Temperatur', 'eu.ownyourdata.room.temp');
INSERT INTO repo (id, creator, description, identifier) VALUES (3086, NULL, 'Pollenbelastung', 'eu.ownyourdata.allergy.pollination');
INSERT INTO repo (id, creator, description, identifier) VALUES (3690, NULL, 'Befinden', 'eu.ownyourdata.allergy.condition');
INSERT INTO repo (id, creator, description, identifier) VALUES (3693, NULL, 'Medikamenteneinnahme', 'eu.ownyourdata.allergy.medintake');
INSERT INTO repo (id, creator, description, identifier) VALUES (3714, NULL, 'Kontodaten', 'eu.ownyourdata.bank');
INSERT INTO repo (id, creator, description, identifier) VALUES (3931, NULL, 'Protokoll', 'eu.ownyourdata.bank.log');
INSERT INTO repo (id, creator, description, identifier) VALUES (3933, NULL, 'Referenzwert', 'eu.ownyourdata.bank.reference');
INSERT INTO repo (id, creator, description, identifier) VALUES (3937, NULL, 'Info', 'eu.ownyourdata.info');


--
-- Name: repo_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('repo_id_seq', 1, false);


--
-- PostgreSQL database dump complete
--

