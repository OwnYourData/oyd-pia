ALTER TABLE oauth_client_token DROP CONSTRAINT "fk_oauth_client_tokn_user_name", ADD CONSTRAINT "fk_oauth_client_tokn_user_name" FOREIGN KEY (user_name) REFERENCES jhi_user(login) ON UPDATE CASCADE;
ALTER TABLE oauth_access_token DROP CONSTRAINT "fk_oauth_access_tokn_user_name", ADD CONSTRAINT "fk_oauth_access_tokn_user_name" FOREIGN KEY (user_name) REFERENCES jhi_user(login) ON UPDATE CASCADE;
