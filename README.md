techdev portal
==============

This project is the central point for techdev services that need login.

It is an OAuth authorization server with a login with Google OAuth.

How it works
------------

The Google login button executes all the Google authentication logic and in case of success gives us an access token. That access token
is used to verify the user in the backend and log him in.

Since the Google login button works with Javascript we can't send a classical redirect to the client. Instead we wend the redirect URL
as plain text and perform the redirect in Javascript.

How to run and build
--------------------
It's Spring Boot, so it's pretty straight forward. Run the class TechdevPortal in your IDE or use the gradle run task or build the jar
and start it with java -jar. The following configuration properties are needed in the application.yaml

    spring:
        datasource:
            driverClassName: org.postgresql.Driver %change if you use another database for the oauth details%
            url: jdbc:postgresql://127.0.0.1:5432/%oauth_database%
            username: %user%
            password: %password%

It doesn't make much sense to use a in memory database because the resource servers need to access the oauth_access_token table.


If you don't add anything form login is active. This is helpful for developing. Add --spring.profiles.active=google-login if you want to use the Google OAuth login process.
This needs additional properties:

    techdev:
        portal:
            google:
                client-id: %client_id%

The client-id is available from the Google developer console.

When it is running you can test the login process without trackr by going to

    http://localhost:8081/landing

If you have form login enabled you should get a login form (you have to add users to the user table to be able to login), if you have Google login activated a link to sign in with
Google should be present.

Database schema
---------------
The database tables are set up by flyway. You have to provide an empty schema.
See http://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#common-application-properties on how to configure properties like schema or switch flyway off completely.
