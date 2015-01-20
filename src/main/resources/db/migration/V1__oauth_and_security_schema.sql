CREATE TABLE users (
  username VARCHAR(100) NOT NULL PRIMARY KEY,
  password VARCHAR(500) NOT NULL,
  enabled  BOOLEAN      NOT NULL
);

CREATE TABLE authorities (
  username  VARCHAR(100) NOT NULL,
  authority VARCHAR(50)  NOT NULL,
  CONSTRAINT fk_authorities_users FOREIGN KEY (username) REFERENCES users (username)
);

CREATE UNIQUE INDEX ix_auth_username ON authorities (username, authority);

CREATE TABLE oauth_access_token (
  token_id          CHARACTER VARYING(256),
  token             BYTEA,
  authentication_id CHARACTER VARYING(256),
  user_name         CHARACTER VARYING(256),
  client_id         CHARACTER VARYING(256),
  authentication    BYTEA,
  refresh_token     CHARACTER VARYING(256)
);

CREATE TABLE oauth_approvals (
  userid         CHARACTER VARYING(256),
  clientid       CHARACTER VARYING(256),
  scope          CHARACTER VARYING(256),
  status         CHARACTER VARYING(10),
  expiresat      TIMESTAMP WITHOUT TIME ZONE,
  lastmodifiedat TIMESTAMP WITHOUT TIME ZONE
);