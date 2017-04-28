insert into oauth_client_details (
	client_id, client_secret, scope, authorized_grant_types,
	web_server_redirect_uri, authorities, access_token_validity,
	refresh_token_validity, additional_information, autoapprove)
values (
	'fooClientIdPassword', 'secret', 'foo,read,write',
	'password,authorization_code,refresh_token', null, null, 36000, 36000, null, true);

insert into oauth_client_details (
	client_id, client_secret, scope, authorized_grant_types,
	web_server_redirect_uri, authorities, access_token_validity,
	refresh_token_validity, additional_information, autoapprove)
values (
	'sampleClientId', 'secret', 'read,write,foo,bar',
	'implicit', null, null, 36000, 36000, null, false);

insert into oauth_client_details (
	client_id, client_secret, scope, authorized_grant_types,
	web_server_redirect_uri, authorities, access_token_validity,
	refresh_token_validity, additional_information, autoapprove)
value (
	'barClientIdPassword', 'secret', 'bar,read,write',
	'password,authorization_code,refresh_token', null, null, 36000, 36000, null, true);

insert into users (
	username, password, enabled)
values (
	'john','123',true);

insert into authorities (
	username,authority)
values (
	'john','USER');