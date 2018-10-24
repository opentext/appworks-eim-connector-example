# AppWorks EIM Connector Example 16.5.0

The project contains example code for an AppWorks 16.5.0 Service that houses an EIMConnectorService with
and AuthHandler that may decorate requests from the AppWorks Gateway. A small example API is provided to 
make use of the EIMConnector contained within as if you were another service trying to use its connection.

This service should be able to be registered as an authentication handler in the AppWorks Gateways administration
UI.

Please see the following link for general information of AppWorks Services.

https://developer.opentext.com/awd/resources/articles/15239948/developer+guide+opentext+appworks+16+service+development+kit.

## Building the service

This is a Java 8 project and uses Apache Maven to perform the build so you will both installed. Once built using this
project and will produce a zip (appworks-service-example_1.0.0.zip) in the `/target` directory that can be deployed
to an AppWorks Gateway 16.2 instance. All you need to do is run `mvn clean package`.

Please review the pom.xml for the build process, but more importantly the service's code itself for information on
how the AppWorks SDK works and how to build a minimal but functional AppWorks Spring MVC service.

## Deploying the service

This service can be deployed via the AppWorks 16.5.0  administration UI. Once installed ensure it is enabled 
via the UI, and then you should be able to make a request 
to `http://{yourhost}:8080/appworks-spring-eim-connector-service/api/eim-connection`, and this should be served by the 
`com.appworks.web.xml.less.ExampleController` Spring MVC controller.

## SDK Examples API

This service's API exposes a number of endpoints that exercise the EIM connector and its AuthHandler. They are 
listed below, and are all called via a HTTP `GET`.

### Get EIM Connection

We construct an instance of a EIMConnectorClient, that connects to our own services hosted EIMConnectorService 
implementation. The EIM connector simply shares a URL among other AppWorks services, we are acting as one of those
services here.

`http://{yourhost}:8080/appworks-spring-eim-connector-service/api/eim-connection`

### Decorate auth request via creds

This endpoint will call the EIM connectors AuthHandler as if it were the AppWorks Gateway asking us to decorate
and inbound auth request. Please included the query parameters as shown.

`http://{yourhost}:8080/appworks-spring-eim-connector-service/api/eim-decorate-via-creds?username=test&password=test`

### Decorate auth request via a token

This endpoint will call the EIM connectors AuthHandler as if it were the AppWorks Gateway asking us to decorate
and inbound auth request. Please included the query parameters as shown.

`http://{yourhost}:8080/appworks-spring-eim-connector-service/api/eim-decorate-via-token?token=test`

