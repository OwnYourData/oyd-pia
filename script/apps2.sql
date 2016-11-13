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
-- Data for Name: external_plugin; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO external_plugin (id, url, mobileurl) VALUES (1000, 'https://kontoentwicklung.datentresor.org', 'https://kontoentwicklung.datentresor.org');
INSERT INTO external_plugin (id, url, mobileurl) VALUES (1001, 'https://allergie.datentresor.org', 'https://allergie.datentresor.org');
INSERT INTO external_plugin (id, url, mobileurl) VALUES (1002, 'https://raumklima.datentresor.org', 'https://raumklima.datentresor.org');


--
-- PostgreSQL database dump complete
--

