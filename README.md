# Order Management POC

Clean install with maven (mvn clean install) on the root directory.

    mvn clean install

Once clean install complete you may run the application with:

    java -jar ./om-app/target/om-app-0.0.1-SNAPSHOT.jar

Users with preloaded data:
* username: admin, password: admin, role: admin
* username: user, password: admin, role: customer
  * assets: [{TRY, 1000}, {WV, 1000}]
* username: investor, password: admin, role: customer 
  * assets: [{APPLE, 1000}]
* username: trader, password: admin, role: customer
  * assets: [{TRY, 1000}]

You may find/test the api-doc on:
    http://localhost:8080/swagger-ui/index.html

om-api-test contains end 2 end api test scripts that coulbe executed with once the application is runnig in localhost:8080:

    java -jar ./om-api-test/target/om-api-test-0.0.1-SNAPSHOT.jar
