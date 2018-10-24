/**
 * Copyright © 2018 Open Text.  All Rights Reserved.
 */
package com.appworks.eim.connector.example;

import com.opentext.otag.sdk.client.v3.AuthClient;
import com.opentext.otag.sdk.handlers.AbstractAuthRequestHandler;
import com.opentext.otag.sdk.handlers.AuthRequestHandler;
import com.opentext.otag.sdk.handlers.AuthResponseDecorator;
import com.opentext.otag.sdk.types.v3.api.SDKResponse;
import com.opentext.otag.sdk.types.v3.auth.AuthHandler;
import com.opentext.otag.sdk.types.v3.auth.AuthHandlerResult;
import com.opentext.otag.sdk.types.v3.auth.RegisterAuthHandlersRequest;
import com.opentext.otag.sdk.types.v3.client.ClientRepresentation;
import com.opentext.otag.sdk.types.v3.message.AuthRequestMessage;
import com.opentext.otag.sdk.util.Cookie;
import com.opentext.otag.sdk.util.ForwardHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * This is basic auth handler that can also be used for auth decoration. It allows any users
 * through simply returning a successful result.
 */
@AuthResponseDecorator
public class ExampleAuthRequestHandler extends AbstractAuthRequestHandler implements AuthRequestHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ExampleAuthRequestHandler.class);

    private static final String EIM_COOKIE = "EIM_COOKIE";
    private static final String EIM_COOKIE_VALUE = "EIM_COOKIE_VALUE";

    public ExampleAuthRequestHandler() {

        try {
            LOG.info("Attempting to register ExampleAuthRequestHandler with OTAG as an AuthRequestHandler");
            RegisterAuthHandlersRequest request = new RegisterAuthHandlersRequest();
            AuthHandler handler = buildHandler();
            request.addHandler(handler);

            LOG.info("Added self to handler request - {}, issuing SDK request", request);

            SDKResponse sdkResponse = new AuthClient().registerAuthHandlers(request);
            LOG.info("Received response {}", sdkResponse);
        } catch (Exception e) {
            LOG.error("Failed to register with Gateway", e);
        }

    }

    @Override
    public AuthHandlerResult auth(String username, String password,
                                  ForwardHeaders headers, ClientRepresentation clientData) {
        return getAuthHandlerResult();
    }

    @Override
    public AuthHandlerResult auth(String authToken,
                                  ForwardHeaders headers,
                                  ClientRepresentation clientData) {
        return getAuthHandlerResult();
    }

    private AuthHandlerResult getAuthHandlerResult() {
        AuthHandlerResult authHandlerResult = new AuthHandlerResult(true);

        authHandlerResult.addRootCookie(EIM_COOKIE, EIM_COOKIE_VALUE);

        return authHandlerResult;
    }

    @Override
    public boolean resolveUsernamesViaOtdsResource() {
        return false;
    }

    @Override
    public String getOtdsResourceId() {
        // this simple implementation does not engage with OTDS
        return null;
    }

    @Override
    public Set<Cookie> getKnownCookies() {
        HashSet<Cookie> cookies = new HashSet<>();

        Cookie eimCookie = new Cookie();
        eimCookie.setName(EIM_COOKIE);
        eimCookie.setValue(EIM_COOKIE_VALUE);
        cookies.add(eimCookie);

        return cookies;
    }

    @Override
    public void handle(AuthRequestMessage message) {
        LOG.info("Handling auth request - {}", message);
    }
}
