# stop and remove any running PIA instances
docker stop datentresor
docker rm $(docker ps -q -f status=exited)
PIA_ID=$(docker run --name datentresor -d oydeu/oyd-pia_armhf

# wait until PIA is up and running
until $(curl --output /dev/null --silent --head --fail http://127.0.0.1:8080); do
  printf '.'
  sleep 5
done

# configure and run scheduler service
SCHEDULER_SECRET=$(cat /dev/urandom | LC_CTYPE=C tr -dc 'a-zA-Z0-9' | fold -w 20 | head -n 1)
docker exec $PIA_ID su postgres -c "psql -U postgres -d pia -c \"INSERT INTO oauth_client_details (client_id, resource_ids, client_secret, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove) VALUES ('eu.ownyourdata.service_scheduler', '', '$SCHEDULER_SECRET', 'eu.ownyourdata.scheduler:read,eu.ownyourdata.scheduler.status:read,eu.ownyourdata.scheduler.email_config:read', 'client_credentials', NULL, '', 3600, 3600, '{}', '');\""
docker exec $PIA_ID su postgres -c "psql -U postgres -d pia -c \"INSERT INTO repo (id, description, identifier) VALUES (1, 'Scheduler Status', 'eu.ownyourdata.scheduler.status');\""
docker exec $PIA_ID su postgres -c "psql -U postgres -d pia -c \"SELECT pg_catalog.setval('repo_id_seq', 2, false);\""
docker exec $PIA_ID su postgres -c "psql -U postgres -d pia -c \"INSERT INTO item (id, value, belongs_id) VALUES (1, '{\\\"active\\\": true}', 1);\""
docker exec $PIA_ID su postgres -c "psql -U postgres -d pia -c \"SELECT pg_catalog.setval('item_id_seq', 2, false);\""

docker exec -d $PIA_ID bash -c "cd /service-scheduler; rails runner \"ApplicationController.helpers.execute\" -s $SCHEDULER_SECRET"
