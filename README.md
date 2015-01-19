techdev portal
==============

This project is the central point for techdev services that need login.

It is an OAuth authorization server with a login with Google OAuth.

How it works
------------

A client wants to get an OAuth token to read resources from a techdev resource, i.e. trackr. E.g. our AngularJS app needs one such token.

It presents the user with a link to get a token with the implicit grant

    https://techdev-portal/oauth/authorize?client_id=...&redirect_uri=https://trackr/authorize

When the user clicks this link he ends up in this application. The /oauth/authorize endpoint is secured and the user is not logged in. So he is redirected to / which presents
a link to Google "Login with Google".

    https://accounts.google.com/o/oauth2/auth?response_type=code&client_id=...&redirect_uri=https://techdev-portal/login

This link will present the user with the known Google login screen and, if it is the first time an agreement screen that the user acknowledges techdev-portal to access
basic profile information. If he does it returns to https://techdev-portal and adds authorization code as a query parameter.

The techdev portal has a servlet filter registered that tries to log in with this token when https://techdev-portal/login is accessed. It configures a Spring OAuth client for
Google and accesses https://www.googleapis.com/plus/v1/people/me.

The tricky part is to configure the OAuth client. It's session scoped and gets on AccessTokenRequest which is even request scoped and automatically mapped from the request
parameters. Important is the "login" parameter in the redirect to https://techdev-portal/login?code=abcdfoobar.

The return value of the Google request contains the users email address which we now know he owns. It's our central identifier - we can do a lookup in our database if that email
is registered with techdev and log the user in if so.

Now comes the fun part: After successful authentication the user is redirected to the first URL he wanted to access.

     https://techdev-portal/oauth/authorize?client_id=...&redirect_uri=https://trackr/authorize

Here he can, if he hasn't yet authorize trackr to access his resources. Notice the redirect_uri? After obtaining approval and an token, the authorization server will send a redirect
there! And the user is finally back in trackr with a valid access token for the resource server.

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
    techdev:
        portal:
            google:
                client-id: %client_id%
                client-secret: %client_secret%

The client-id and client-secret are available from the Google developer console.

The needed tables in the datasource are here https://github.com/spring-projects/spring-security-oauth/blob/6d9de66787cb60249f0de00ffe9075366a803924/spring-security-oauth2/src/test/resources/schema.sql

When it is running you can test the login process without trackr by going to

    http://localhost:8081/showUser
