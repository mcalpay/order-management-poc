# Order Management POC

Clean install with maven (mvn clean install) on the root directory.

    mvn clean install

Once clean install complete you may run the application with:

    java -jar ./om-app/target/om-app-0.0.1-SNAPSHOT.jar

Application will run a h2-console which is preloaded with data at stocks-app/src/main/resources/import.sql
You may view, access the api with swagger at:

    http://localhost:8080/swagger-ui/index.html

