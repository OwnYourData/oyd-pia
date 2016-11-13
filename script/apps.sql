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
-- Data for Name: plugin; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO plugin (plugin_type, id, identifier, name) VALUES ('EXTERNAL', 1000, 'eu.ownyourdata.bank', 'Kontoentwicklung');
INSERT INTO plugin (plugin_type, id, identifier, name) VALUES ('EXTERNAL', 1001, 'eu.ownyourdata.allergy', 'Allergie-Tagebuch');
INSERT INTO plugin (plugin_type, id, identifier, name) VALUES ('EXTERNAL', 1002, 'eu.ownyourdata.room', 'Raumklima');


--
-- Name: plugin_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('plugin_id_seq', 1, false);


--
-- PostgreSQL database dump complete
--

