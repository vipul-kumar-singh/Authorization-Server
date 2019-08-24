# Spring Security 1 of 2 : Authorization-Server
## For Spring Security 2 of 2 : Resource-Server - https://github.com/vipul-kumar-singh/Resource-Server

#### _Steps To Run_

**Step 1** Clone this git repository

**Step 2** Open this project in any IntelliJ or Eclipse

**Step 3** Goto `Authorization-Server/src/main/resources/application.properties` and uncomment `spring.datasource.initialization-mode=always`.

**Step 4** Build and run the project.
Because of step 3, it will run the `schema.sql` and `data.sql` which will create the OAuth tables, client-details and some user-data.
Now you can access all the controllers and OAuth endpoints.

**Step 5** Goto `Authorization-Server/src/main/resources/application.properties` and comment `spring.datasource.initialization-mode=always`,
because we don't want our application to recreate tables/data.

**Step 6** Re-run the project and explore.
