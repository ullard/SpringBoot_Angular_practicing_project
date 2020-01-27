INSERT INTO oauth_client_details
	(client_id, client_secret, scope, authorized_grant_types,
	web_server_redirect_uri, authorities, access_token_validity,
	refresh_token_validity, additional_information, autoapprove)
VALUES
	('fooClientIdPassword', '$2a$10$1u6pajYNkrFRXFF5/2iiC.kFKGnnfu1Q6tvQVv6zxIA8Z.Bzx3F4W', 'foo,read,write',
	'password,authorization_code,refresh_token,client_credentials', null, null, 36000, 36000, null, true);
INSERT INTO oauth_client_details
	(client_id, client_secret, scope, authorized_grant_types,
	web_server_redirect_uri, authorities, access_token_validity,
	refresh_token_validity, additional_information, autoapprove)
VALUES
	('sampleClientId', '$2a$10$1u6pajYNkrFRXFF5/2iiC.kFKGnnfu1Q6tvQVv6zxIA8Z.Bzx3F4W', 'read,write,foo,bar',
	'implicit', null, null, 36000, 36000, null, false);
INSERT INTO oauth_client_details
	(client_id, client_secret, scope, authorized_grant_types,
	web_server_redirect_uri, authorities, access_token_validity,
	refresh_token_validity, additional_information, autoapprove)
VALUES
	('barClientIdPassword', '$2a$10$1u6pajYNkrFRXFF5/2iiC.kFKGnnfu1Q6tvQVv6zxIA8Z.Bzx3F4W', 'bar,read,write',
	'password,authorization_code,refresh_token', null, null, 36000, 36000, null, true);