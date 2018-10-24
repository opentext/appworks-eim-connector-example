/**
 * Copyright © 2018 Open Text.  All Rights Reserved.
 */
package com.appworks.eim.connector.example;

import com.opentext.otag.sdk.client.v3.ServiceClient;
import com.opentext.otag.sdk.connector.EIMConnectorClient;
import com.opentext.otag.sdk.connector.EIMConnectorClient.ConnectionResult;
import com.opentext.otag.sdk.connector.EIMConnectorClientImpl;
import com.opentext.otag.sdk.connector.EIMConnectorService;
import com.opentext.otag.sdk.handlers.AuthRequestHandler;
import com.opentext.otag.sdk.types.v3.TrustedProvider;
import com.opentext.otag.sdk.types.v3.api.SDKResponse;
import com.opentext.otag.sdk.types.v3.sdk.EIMConnector;
import com.opentext.otag.sdk.types.v3.settings.Setting;
import com.opentext.otag.service.context.components.AWComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

@SuppressWarnings("unused")
public class ExampleEimConnectorService implements EIMConnectorService {

    private static final Logger LOG = LoggerFactory.getLogger(ExampleEimConnectorService.class);

    static final String EIM_CONNECTOR_NAME = "ExampleEimConnectorService";
    static final String EIM_CONNECTOR_VERSION = "1.0.0";

    private final AuthRequestHandler authRequestHandler;

    private ApplicationContext applicationContext;
    public static final String MY_EIM_CONNECTOR_PROVIDER_NAME = "MyEimConnectorProvider-name";

    public ExampleEimConnectorService() {
        registerConnector();

        // verify others can see this service via the Gateways SDK, a connector wouldn't normally do this itself :)
        isVisibleToEimClients();

        authRequestHandler = AWComponentContext.getComponent(ExampleAuthRequestHandler.class);
    }

    private boolean registerConnector() {
        ServiceClient serviceClient = new ServiceClient();

        EIMConnector eimConnector = new EIMConnector(getConnectorName(), getConnectorVersion(),
                getConnectionString(), getConnectionStringSettingKey(),
                getTrustedProviderName(), getTrustedProviderKey());

        LOG.info("Registering ourselves as an EIMConnector - {}", eimConnector);
        SDKResponse sdkResponse = serviceClient.registerConnector(eimConnector);
        LOG.info("EIMConnector registration complete - {}", sdkResponse);

        return sdkResponse.isSuccess();
    }

    private boolean isVisibleToEimClients() {
        EIMConnectorClient eimConnectorClient = new EIMConnectorClientImpl(getConnectorName(), getConnectorVersion());
        LOG.info("Using EIMConnectorClient to connect");

        ConnectionResult connect = eimConnectorClient.connect();
        LOG.info("Self Connection Result - message={} connector={} ", connect.getMessage(), connect.getConnector());

        return connect.isSuccess();
    }

    @Override
    public String getConnectorName() {
        return EIM_CONNECTOR_NAME;
    }

    @Override
    public String getConnectorVersion() {
        return EIM_CONNECTOR_VERSION;
    }

    /**
     * The value of the shared connection String. Normally the {@link AuthRequestHandler} returned
     * by our {@link #getAuthHandler()} method will used this URL String as the endpoint to forward
     * the authentication request to. A local Content Servers login endpoint for example.
     *
     * @return EIM system connection URL String
     */
    @Override
    public String getConnectionString() {
        return "https://myeimsystem.com";
    }

    /**
     * The {@link Setting} key the Gateway stores the EIM connectors shared URL under
     * (see {@link ConnectionResult#connector#getConnectionString()}).
     *
     * @return connection setting key
     */
    @Override
    public String getConnectionStringSettingKey() {
        return "myeimconnector.setting.string";
    }

    /**
     * The id of the {@link TrustedProvider} associated with this EIM connector.
     *
     * @return API key provider name
     */
    @Override
    public String getTrustedProviderName() {
        return "MyEimConnectorProvider-name";
    }

    /**
     * The id of the {@link TrustedProvider} associated with this EIM connector.
     *
     * @return API key provider name
     */
    @Override
    public String getTrustedProviderKey() {
        return "MyEimConnectorProvider-key";
    }

    @Override
    public boolean registerTrustedProviderKey(String serverName, String key) {
        // we don't bother doing much here, but this is where we send a Trusted Provider API key to
        // Content Server in some of our connector use cases, so it can then use that for things like
        // sending notifications, and other parts of the Gateway API that make use of the API key access
        return true;
    }

    @Override
    public AuthRequestHandler getAuthHandler() {
        return authRequestHandler;
    }

    @Override
    public void onUpdateConnector(EIMConnector updated) {
        LOG.info("Updated the EIMConnector - new value = {}", updated);
    }

}
