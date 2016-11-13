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
-- Data for Name: oauth_client_details; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO oauth_client_details (client_id, resource_ids, client_secret, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove) VALUES ('piaapp', '', 'jaNBqNphGFjrkQm5ijSQG', 'read,write', 'password,refresh_token,authorization_code,implicit', NULL, 'ROLE_ADMIN,ROLE_USER', 1800, NULL, '{}', '');
INSERT INTO oauth_client_details (client_id, resource_ids, client_secret, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove) VALUES ('eu.ownyourdata.bank', '', 'qHT5LfqaF8ZMOtd2sY1I', 'eu.ownyourdata.info:read,eu.ownyourdata.info:write,eu.ownyourdata.info:update,eu.ownyourdata.bank:read,eu.ownyourdata.bank:write,eu.ownyourdata.bank:update,eu.ownyourdata.bank:delete,eu.ownyourdata.bank.*:read,eu.ownyourdata.bank.*:write,eu.ownyourdata.bank.*:update,eu.ownyourdata.bank.*:delete,eu.ownyourdata.scheduler:read,eu.ownyourdata.scheduler:write,eu.ownyourdata.scheduler:update,eu.ownyourdata.scheduler:delete,eu.ownyourdata.scheduler.email_config:read,eu.ownyourdata.scheduler.email_config:write,eu.ownyourdata.scheduler.email_config:update,eu.ownyourdata.scheduler.email_config:delete', 'client_credentials', NULL, '', 3600, 3600, '{}', '');
INSERT INTO oauth_client_details (client_id, resource_ids, client_secret, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove) VALUES ('eu.ownyourdata.allergy', '', 'U1LxqRwC3YVFY6GkdTV5', 'eu.ownyourdata.info:read,eu.ownyourdata.info:write,eu.ownyourdata.info:update,eu.ownyourdata.scheduler:read,eu.ownyourdata.scheduler:write,eu.ownyourdata.scheduler:update,eu.ownyourdata.scheduler:delete,eu.ownyourdata.scheduler.email_config:read,eu.ownyourdata.scheduler.email_config:write,eu.ownyourdata.scheduler.email_config:update,eu.ownyourdata.scheduler.email_config:delete,eu.ownyourdata.allergy.*:read,eu.ownyourdata.allergy.*:write,eu.ownyourdata.allergy.*:update,eu.ownyourdata.allergy.*:delete', 'client_credentials', NULL, '', 3600, 3600, '{}', '');
INSERT INTO oauth_client_details (client_id, resource_ids, client_secret, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove) VALUES ('eu.ownyourdata.room', '', 'NTwX8BQT6NJO97jReiI2', 'eu.ownyourdata.info:read,eu.ownyourdata.info:write,eu.ownyourdata.info:update,eu.ownyourdata.scheduler:read,eu.ownyourdata.scheduler:write,eu.ownyourdata.scheduler:update,eu.ownyourdata.scheduler:delete,eu.ownyourdata.room.*:read,eu.ownyourdata.room.*:write,eu.ownyourdata.room.*:update,eu.ownyourdata.room.*:delete', 'client_credentials', NULL, '', 3600, 3600, '{}', '');


--
-- PostgreSQL database dump complete
--

