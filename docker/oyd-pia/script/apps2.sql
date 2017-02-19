--
-- PostgreSQL database dump
--

-- Dumped from database version 9.6.1
-- Dumped by pg_dump version 9.6.1

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

SET search_path = public, pg_catalog;

--
-- Data for Name: external_plugin; Type: TABLE DATA; Schema: public; Owner: postgres
-- pg_dump --column-inserts --data-only --table=external_plugin pia > /apps2.sql
-- cat /apps2.sql

INSERT INTO external_plugin (id, url, mobileurl) VALUES (1, 'https://kontoentwicklung.datentresor.org', 'https://kontoentwicklung-mobil.datentresor.org');
INSERT INTO external_plugin (id, url, mobileurl) VALUES (2, 'https://allergie-tagebuch.datentresor.org', 'https://allergie-tagebuch-mobil.datentresor.org');
INSERT INTO external_plugin (id, url, mobileurl) VALUES (3, 'https://raumklima.datentresor.org', 'https://raumklima-mobil.datentresor.org');
INSERT INTO external_plugin (id, url, mobileurl) VALUES (4, 'https://scheduler.datentresor.org', 'https://scheduler.datentresor.org');
INSERT INTO external_plugin (id, url, mobileurl) VALUES (5, 'https://webhistory.datentresor.org', 'https://webhistory.datentresor.org');
INSERT INTO external_plugin (id, url, mobileurl) VALUES (6, 'https://online-daten.datentresor.org', 'https://online-daten.datentresor.org');

--
-- PostgreSQL database dump complete
--

