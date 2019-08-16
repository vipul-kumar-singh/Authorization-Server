INSERT INTO oauth_client_details (client_id, client_secret, web_server_redirect_uri, scope, access_token_validity, refresh_token_validity, resource_ids, authorized_grant_types, additional_information) VALUES ('mobile', '$2a$10$gPhlXZfms0EpNHX0.HHptOhoFD1AoxSr/yUIdTqA8vtjeP4zi0DDu', 'http://localhost:8081/code', 'READ,WRITE', '3600', '10000', 'inventory,payment', 'authorization_code,password,refresh_token,implicit', '{}');

INSERT INTO PERMISSION (NAME) VALUES
('create_profile'),
('read_profile'),
('update_profile'),
('delete_profile');

INSERT INTO role (NAME) VALUES
('ROLE_admin'),('ROLE_user');

INSERT INTO PERMISSION_ROLE (PERMISSION_ID, ROLE_ID) VALUES
(1,1), /*create-> admin */
(2,1), /* read admin */
(3,1), /* update admin */
(4,1), /* delete admin */
(2,2),  /* read user */
(3,2);  /* update user */
insert into user (id, username,password, email, enabled, account_non_expired, credentials_non_expired, account_non_locked) VALUES ('1', 'vipul','$2a$10$MMYHx3wNssfpnV9YSKDgh.CPXEwnB1Th2txelazReVpsLFgA5YPPO', 'vipul@yopmail.com', '1', '1', '1', '1'); /* username - vipul, pass - Test@123 */
insert into  user (id, username,password, email, enabled, account_non_expired, credentials_non_expired, account_non_locked) VALUES ('2', 'xender', '$2a$10$MMYHx3wNssfpnV9YSKDgh.CPXEwnB1Th2txelazReVpsLFgA5YPPO','xender@yopmail.com', '1', '1', '1', '1'); /* username - xender, pass - Test@123 */

INSERT INTO ROLE_USER (ROLE_ID, USER_ID)
VALUES
(1, 1) /* vipul-admin */,
(2, 2) /* xender-operatorr */ ;