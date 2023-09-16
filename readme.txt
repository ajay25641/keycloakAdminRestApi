Prerequisite

Before testing this api please make sure some of the following things-

1. Jdk-17 or later
2. Mention keycloak details in application.properties file with the following key-value pairs
   a. keycloak.auth-server-url=<Your keycloak server url>
   b. keycloak.realm=<Your realm name>
   c. keycloak.resource=<client id name>
   d. keycloak.credentials.secret=<client secret>

   please make sure to provide appropriate privilage to client to make changes in the realm.


For documentation purpose i have used openApi.
Please refer to the below url for accessing documentation ui
http://localhost:8080/swagger-ui/index.html#/
(I am assuming spring boot application is running on localHost with port 8080)

github url-

