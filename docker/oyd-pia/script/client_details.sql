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
-- Data for Name: oauth_client_details; Type: TABLE DATA; Schema: public; Owner: postgres
-- pg_dump --column-inserts --data-only --table=oauth_client_details pia > /client_details.sql
-- cat /client_details.sql

INSERT INTO oauth_client_details (client_id, resource_ids, client_secret, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove) VALUES ('eu.ownyourdata.bank', '', 'fZTpuPtu6XEAVlJCcI5y', 'eu.ownyourdata.info:read,eu.ownyourdata.info:write,eu.ownyourdata.info:update,eu.ownyourdata.bank:read,eu.ownyourdata.bank:write,eu.ownyourdata.bank:update,eu.ownyourdata.bank:delete,eu.ownyourdata.bank.*:read,eu.ownyourdata.bank.*:write,eu.ownyourdata.bank.*:update,eu.ownyourdata.bank.*:delete,eu.ownyourdata.scheduler:read,eu.ownyourdata.scheduler:write,eu.ownyourdata.scheduler:update,eu.ownyourdata.scheduler:delete,eu.ownyourdata.scheduler.email_config:read,eu.ownyourdata.scheduler.email_config:write,eu.ownyourdata.scheduler.email_config:update,eu.ownyourdata.scheduler.email_config:delete', 'client_credentials', NULL, '', 3600, 3600, '{}', '');
INSERT INTO oauth_client_details (client_id, resource_ids, client_secret, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove) VALUES ('eu.ownyourdata.allergy', '', 'nJrYORf8d6qkLGohjbGe', 'eu.ownyourdata.info:read,eu.ownyourdata.info:write,eu.ownyourdata.info:update,eu.ownyourdata.scheduler:read,eu.ownyourdata.scheduler:write,eu.ownyourdata.scheduler:update,eu.ownyourdata.scheduler:delete,eu.ownyourdata.scheduler.email_config:read,eu.ownyourdata.scheduler.email_config:write,eu.ownyourdata.scheduler.email_config:update,eu.ownyourdata.scheduler.email_config:delete,eu.ownyourdata.allergy.*:read,eu.ownyourdata.allergy.*:write,eu.ownyourdata.allergy.*:update,eu.ownyourdata.allergy.*:delete', 'client_credentials', NULL, '', 3600, 3600, '{}', '');
INSERT INTO oauth_client_details (client_id, resource_ids, client_secret, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove) VALUES ('eu.ownyourdata.room', '', '6XUmKUm4jH4myUm1fMi1', 'eu.ownyourdata.info:read,eu.ownyourdata.info:write,eu.ownyourdata.info:update,eu.ownyourdata.scheduler:read,eu.ownyourdata.scheduler:write,eu.ownyourdata.scheduler:update,eu.ownyourdata.scheduler:delete,eu.ownyourdata.room.*:read,eu.ownyourdata.room.*:write,eu.ownyourdata.room.*:update,eu.ownyourdata.room.*:delete', 'client_credentials', NULL, '', 3600, 3600, '{}', '');
INSERT INTO oauth_client_details (client_id, resource_ids, client_secret, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove) VALUES ('eu.ownyourdata.scheduler', '', 'KINu7bt2uyx38LR0yPix', 'eu.ownyourdata.info:read,eu.ownyourdata.info:write,eu.ownyourdata.info:update,eu.ownyourdata.scheduler*:read,eu.ownyourdata.scheduler*:write,eu.ownyourdata.scheduler*:update,eu.ownyourdata.scheduler*:delete', 'client_credentials', NULL, '', 3600, 3600, '{}', '');
INSERT INTO oauth_client_details (client_id, resource_ids, client_secret, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove) VALUES ('eu.ownyourdata.webhistory', '', 'ipOdTR85xIL0CIJJm2mw', 'eu.ownyourdata.webhistory:read,eu.ownyourdata.webhistory:write,eu.ownyourdata.webhistory:update,eu.ownyourdata.webhistory:delete,eu.ownyourdata.info:read,eu.ownyourdata.webhistory.log:read,eu.ownyourdata.webhistory.log:write,eu.ownyourdata.webhistory.log:update,eu.ownyourdata.webhistory.log:delete', 'client_credentials', NULL, '', 3600, 3600, '{}', '');
INSERT INTO oauth_client_details (client_id, resource_ids, client_secret, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove) VALUES ('eu.ownyourdata.collect', '', 'oinBfHVNdcr8zQ9PDlSN', 'eu.ownyourdata.info:read,eu.ownyourdata.info:write,eu.ownyourdata.info:update,eu.ownyourdata.collect:read,eu.ownyourdata.collect.*:read,eu.ownyourdata.collect*:write,eu.ownyourdata.collect*:update,eu.ownyourdata.collect*:delete,eu.ownyourdata.scheduler:read,eu.ownyourdata.scheduler.*:read,eu.ownyourdata.scheduler*:write,eu.ownyourdata.scheduler*:update,eu.ownyourdata.scheduler*:delete', 'client_credentials', NULL, '', 3600, 3600, '{}', '');

--
-- PostgreSQL database dump complete
--

