/**
 * Copyright © 2018 Open Text.  All Rights Reserved.
 */
package com.appworks.eim.connector.example;

import com.opentext.otag.sdk.connector.EIMConnectorClient;
import com.opentext.otag.sdk.connector.EIMConnectorClientImpl;
import com.opentext.otag.sdk.handlers.AuthRequestHandler;
import com.opentext.otag.sdk.types.v3.auth.AuthHandlerResult;
import com.opentext.otag.sdk.types.v3.client.ClientRepresentation;
import com.opentext.otag.sdk.util.ForwardHeaders;
import com.opentext.otag.service.context.components.AWComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.appworks.eim.connector.example.ExampleEimConnectorService.EIM_CONNECTOR_NAME;
import static com.appworks.eim.connector.example.ExampleEimConnectorService.EIM_CONNECTOR_VERSION;

@Controller
public class ExampleController {

    private static final Logger LOG = LoggerFactory.getLogger(ExampleController.class);

    /**
     * Once deployed and <strong>enabled</strong> we should be able to hit this service on
     * {@code <host>/mvc-test-service/api/}.
     *
     * @return a welcome message
     */
    @RequestMapping(value = "/eim-connection", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity makeEimConnection() {

        LOG.info("Attempting to connect to our EIM connection via client instance - {}:{}",
                EIM_CONNECTOR_NAME, EIM_CONNECTOR_VERSION);

        EIMConnectorClient connectorClient = new EIMConnectorClientImpl(EIM_CONNECTOR_NAME, EIM_CONNECTOR_VERSION);

        EIMConnectorClient.ConnectionResult connectionResult;
        try {
            connectionResult = connectorClient.connect();
        } catch (Exception e) {
            return new ResponseEntity<>(String.format("EIM connection failed: %s", e),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(connectionResult, HttpStatus.OK);
    }

    // we normally would not pass credentials via query parameters :/

    @RequestMapping(value = "/eim-decorate-via-creds", method = RequestMethod.GET)
    @ResponseBody
    public AuthHandlerResult testDecorationViaCreds(@RequestParam("username") String username,
                                                    @RequestParam("password") String password) {
        AuthRequestHandler authHandler = getAuthDecorator();
        return authHandler.auth(username, password, new ForwardHeaders(), new ClientRepresentation());
    }

    @RequestMapping(value = "/eim-decorate-via-token", method = RequestMethod.GET)
    @ResponseBody
    public AuthHandlerResult testDecorationViaToken(@RequestParam("token") String token) {
        AuthRequestHandler authHandler = getAuthDecorator();
        return authHandler.auth(token, new ForwardHeaders(), new ClientRepresentation());
    }

    /**
     * Retrieves our EIM connectors {@link com.opentext.otag.sdk.handlers.AuthResponseDecorator} implementation.
     *
     * @return local auth request decorator
     */
    private AuthRequestHandler getAuthDecorator() {
        return AWComponentContext.getComponent(ExampleEimConnectorService.class).getAuthHandler();
    }

}
