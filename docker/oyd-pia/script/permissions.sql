--
-- PostgreSQL database dump
--

-- Dumped from database version 9.5.4
-- Dumped by pg_dump version 9.5.4

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

SET search_path = public, pg_catalog;

--
-- Data for Name: permissions; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO permissions (plugin_id, permission) VALUES (1, 'eu.ownyourdata.info:read');
INSERT INTO permissions (plugin_id, permission) VALUES (1, 'eu.ownyourdata.info:write');
INSERT INTO permissions (plugin_id, permission) VALUES (1, 'eu.ownyourdata.info:update');
INSERT INTO permissions (plugin_id, permission) VALUES (1, 'eu.ownyourdata.bank:read');
INSERT INTO permissions (plugin_id, permission) VALUES (1, 'eu.ownyourdata.bank:write');
INSERT INTO permissions (plugin_id, permission) VALUES (1, 'eu.ownyourdata.bank:update');
INSERT INTO permissions (plugin_id, permission) VALUES (1, 'eu.ownyourdata.bank:delete');
INSERT INTO permissions (plugin_id, permission) VALUES (1, 'eu.ownyourdata.bank.*:read');
INSERT INTO permissions (plugin_id, permission) VALUES (1, 'eu.ownyourdata.bank.*:write');
INSERT INTO permissions (plugin_id, permission) VALUES (1, 'eu.ownyourdata.bank.*:update');
INSERT INTO permissions (plugin_id, permission) VALUES (1, 'eu.ownyourdata.bank.*:delete');
INSERT INTO permissions (plugin_id, permission) VALUES (1, 'eu.ownyourdata.scheduler:read');
INSERT INTO permissions (plugin_id, permission) VALUES (1, 'eu.ownyourdata.scheduler:write');
INSERT INTO permissions (plugin_id, permission) VALUES (1, 'eu.ownyourdata.scheduler:update');
INSERT INTO permissions (plugin_id, permission) VALUES (1, 'eu.ownyourdata.scheduler:delete');
INSERT INTO permissions (plugin_id, permission) VALUES (1, 'eu.ownyourdata.scheduler.email_config:read');
INSERT INTO permissions (plugin_id, permission) VALUES (1, 'eu.ownyourdata.scheduler.email_config:write');
INSERT INTO permissions (plugin_id, permission) VALUES (1, 'eu.ownyourdata.scheduler.email_config:update');
INSERT INTO permissions (plugin_id, permission) VALUES (1, 'eu.ownyourdata.scheduler.email_config:delete');
INSERT INTO permissions (plugin_id, permission) VALUES (2, 'eu.ownyourdata.info:read');
INSERT INTO permissions (plugin_id, permission) VALUES (2, 'eu.ownyourdata.info:write');
INSERT INTO permissions (plugin_id, permission) VALUES (2, 'eu.ownyourdata.info:update');
INSERT INTO permissions (plugin_id, permission) VALUES (2, 'eu.ownyourdata.scheduler:read');
INSERT INTO permissions (plugin_id, permission) VALUES (2, 'eu.ownyourdata.scheduler:write');
INSERT INTO permissions (plugin_id, permission) VALUES (2, 'eu.ownyourdata.scheduler:update');
INSERT INTO permissions (plugin_id, permission) VALUES (2, 'eu.ownyourdata.scheduler:delete');
INSERT INTO permissions (plugin_id, permission) VALUES (2, 'eu.ownyourdata.scheduler.email_config:read');
INSERT INTO permissions (plugin_id, permission) VALUES (2, 'eu.ownyourdata.scheduler.email_config:write');
INSERT INTO permissions (plugin_id, permission) VALUES (2, 'eu.ownyourdata.scheduler.email_config:update');
INSERT INTO permissions (plugin_id, permission) VALUES (2, 'eu.ownyourdata.scheduler.email_config:delete');
INSERT INTO permissions (plugin_id, permission) VALUES (2, 'eu.ownyourdata.allergy.*:read');
INSERT INTO permissions (plugin_id, permission) VALUES (2, 'eu.ownyourdata.allergy.*:write');
INSERT INTO permissions (plugin_id, permission) VALUES (2, 'eu.ownyourdata.allergy.*:update');
INSERT INTO permissions (plugin_id, permission) VALUES (2, 'eu.ownyourdata.allergy.*:delete');
INSERT INTO permissions (plugin_id, permission) VALUES (3, 'eu.ownyourdata.info:read');
INSERT INTO permissions (plugin_id, permission) VALUES (3, 'eu.ownyourdata.info:write');
INSERT INTO permissions (plugin_id, permission) VALUES (3, 'eu.ownyourdata.info:update');
INSERT INTO permissions (plugin_id, permission) VALUES (3, 'eu.ownyourdata.scheduler:read');
INSERT INTO permissions (plugin_id, permission) VALUES (3, 'eu.ownyourdata.scheduler:write');
INSERT INTO permissions (plugin_id, permission) VALUES (3, 'eu.ownyourdata.scheduler:update');
INSERT INTO permissions (plugin_id, permission) VALUES (3, 'eu.ownyourdata.scheduler:delete');
INSERT INTO permissions (plugin_id, permission) VALUES (3, 'eu.ownyourdata.room.*:read');
INSERT INTO permissions (plugin_id, permission) VALUES (3, 'eu.ownyourdata.room.*:write');
INSERT INTO permissions (plugin_id, permission) VALUES (3, 'eu.ownyourdata.room.*:update');
INSERT INTO permissions (plugin_id, permission) VALUES (3, 'eu.ownyourdata.room.*:delete');
INSERT INTO permissions (plugin_id, permission) VALUES (4, 'eu.ownyourdata.scheduler.*:read');
INSERT INTO permissions (plugin_id, permission) VALUES (4, 'eu.ownyourdata.scheduler.*:write');
INSERT INTO permissions (plugin_id, permission) VALUES (4, 'eu.ownyourdata.scheduler.*:update');
INSERT INTO permissions (plugin_id, permission) VALUES (4, 'eu.ownyourdata.scheduler.*:delete');
INSERT INTO permissions (plugin_id, permission) VALUES (5, 'eu.ownyourdata.webhistory:read');
INSERT INTO permissions (plugin_id, permission) VALUES (5, 'eu.ownyourdata.webhistory:write');
INSERT INTO permissions (plugin_id, permission) VALUES (5, 'eu.ownyourdata.webhistory:update');
INSERT INTO permissions (plugin_id, permission) VALUES (5, 'eu.ownyourdata.webhistory:delete');
INSERT INTO permissions (plugin_id, permission) VALUES (5, 'eu.ownyourdata.info:read');
INSERT INTO permissions (plugin_id, permission) VALUES (5, 'eu.ownyourdata.info:write');
INSERT INTO permissions (plugin_id, permission) VALUES (5, 'eu.ownyourdata.info:update');
INSERT INTO permissions (plugin_id, permission) VALUES (5, 'eu.ownyourdata.webhistory.log:read');
INSERT INTO permissions (plugin_id, permission) VALUES (5, 'eu.ownyourdata.webhistory.log:write');
INSERT INTO permissions (plugin_id, permission) VALUES (5, 'eu.ownyourdata.webhistory.log:update');
INSERT INTO permissions (plugin_id, permission) VALUES (5, 'eu.ownyourdata.webhistory.log:delete');


--
-- PostgreSQL database dump complete
--

