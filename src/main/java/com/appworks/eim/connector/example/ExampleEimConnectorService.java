package com.appworks.eim.connector.example;

import com.opentext.otag.sdk.client.v3.ServiceClient;
import com.opentext.otag.sdk.connector.EIMConnectorClient;
import com.opentext.otag.sdk.connector.EIMConnectorClient.ConnectionResult;
import com.opentext.otag.sdk.connector.EIMConnectorClientImpl;
import com.opentext.otag.sdk.connector.EIMConnectorService;
import com.opentext.otag.sdk.handlers.AuthRequestHandler;
import com.opentext.otag.sdk.types.v3.api.SDKResponse;
import com.opentext.otag.sdk.types.v3.sdk.EIMConnector;
import com.opentext.otag.service.context.components.AWComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unused")
public class ExampleEimConnectorService implements EIMConnectorService {

    private static final Logger LOG = LoggerFactory.getLogger(ExampleEimConnectorService.class);

    public static final String EIM_CONNECTOR_NAME = "ExampleEimConnectorService";
    public static final String EIM_CONNECTOR_VERSION = "1.0.0";

    private final AuthRequestHandler authRequestHandler;

    public ExampleEimConnectorService() {
        registerConnector();
        isVisibleToEimClients();
        authRequestHandler = AWComponentContext.getComponent(ExampleAuthRequestHandler.class);
    }

    public boolean registerConnector() {
        ServiceClient serviceClient = new ServiceClient();

        EIMConnector eimConnector = new EIMConnector(getConnectorName(), getConnectorVersion(),
                getConnectionString(), getConnectionStringSettingKey(),
                getTrustedProviderName(), getTrustedProviderKey());

        LOG.info("Registering ourselves as an EIMConnector - {}", eimConnector);
        SDKResponse sdkResponse = serviceClient.registerConnector(eimConnector);
        LOG.info("EIMConnector registration complete - {}", sdkResponse);

        return sdkResponse.isSuccess();
    }

    public boolean isVisibleToEimClients() {
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

    @Override
    public String getConnectionString() {
        return "https://myeimsystem.com";
    }

    @Override
    public String getConnectionStringSettingKey() {
        return "myeimconnector.setting.string";
    }

    @Override
    public String getTrustedProviderName() {
        return "MyEimConnectorProvider-name";
    }

    @Override
    public String getTrustedProviderKey() {
        return "MyEimConnectorProvider-key";
    }

    @Override
    public boolean registerTrustedProviderKey(String serverName, String key) {
        // TODO FIXME
        // we don't bother doing much here, but this is where we send the API key to Content Server, so it can then
        // use that for things like sending notifications, and other parts of the Gateway API
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
