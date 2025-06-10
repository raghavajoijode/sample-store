package com.sample.portal.core.services.impl;

import com.sample.portal.core.pojo.HttpConnectionResponse;
import com.sample.portal.core.services.HttpClientService;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.osgi.service.component.annotations.*;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;


@Designate(ocd = HttpClientServiceImpl.Config.class, factory = true)
@Component(service = HttpClientService.class, immediate = true)
public class HttpClientServiceImpl implements HttpClientService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientServiceImpl.class);

    private CloseableHttpClient httpClient;
    private PoolingHttpClientConnectionManager poolingConnectionManager;

    private int connectionTimeout;
    private int connectionRequestTimeout;
    private int socketTimeout;
    private int maxTotalConnections;
    private int maxConnectionsPerRoute;


    @Activate
    @Modified
    protected void activate(Config config) {
        this.maxConnectionsPerRoute = config.maxConnectionsPerRoute();
        this.maxTotalConnections = config.maxTotalConnections();

        this.connectionRequestTimeout = config.connectionRequestTimeout();
        this.connectionTimeout = config.connectionTimeout();
        this.socketTimeout = config.socketTimeout();
        // reset of client, so it will be recalculated on next first get
        httpClient = null;
    }


    @Deactivate
    protected void deactivate() {
        if (this.poolingConnectionManager != null) {
            this.poolingConnectionManager.shutdown();
        }

        if (this.httpClient != null) {
            try {
                this.httpClient.close();
                this.httpClient = null;
            } catch (IOException e) {
                LOGGER.error("Error closing HTTP client", e);
            }
        }

    }

    @Override
    public CloseableHttpClient getConfiguredHttpClient() {
        if (this.httpClient == null) {
            TrustStrategy acceptingTrustStrategy = (cert, authType) -> true;
            RegistryBuilder<ConnectionSocketFactory> connectionSocketFactoryRegistryBuilder = RegistryBuilder.create();
            try {
                SSLContext sslContext = SSLContexts.custom()
                        .loadTrustMaterial(null, acceptingTrustStrategy)
                        .build();
                SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
                connectionSocketFactoryRegistryBuilder.register("https", sslConnectionSocketFactory);
            } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
                LOGGER.error("Error setting SSL context", e);
            }

            Registry<ConnectionSocketFactory> socketFactoryRegistry = connectionSocketFactoryRegistryBuilder
                    .register(HTTP_SCHEME, new PlainConnectionSocketFactory())
                    .build();

            this.poolingConnectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            poolingConnectionManager.setMaxTotal(this.maxTotalConnections);
            poolingConnectionManager.setDefaultMaxPerRoute(this.maxConnectionsPerRoute);

            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(this.connectionRequestTimeout)
                    .setConnectTimeout(this.connectionTimeout)
                    .setSocketTimeout(this.socketTimeout)
                    .build();
            HttpClientBuilder httpClientBuilder = HttpClients.custom()
                    .setConnectionManager(poolingConnectionManager)
                    .setDefaultRequestConfig(requestConfig);

            this.httpClient = httpClientBuilder.build();
        }

        return this.httpClient;
    }

    @Override
    public PoolingHttpClientConnectionManager getPoolingConnectionManager() {
        return poolingConnectionManager;
    }

    @Override
    public HttpConnectionResponse execute(HttpRequestBase httpRequest) throws IOException {
        int status;
        String response;
        LOGGER.debug("Starting HTTP Request ['{}']", httpRequest.getURI());
        try (CloseableHttpResponse httpResponse = getConfiguredHttpClient().execute(httpRequest)){
            status = httpResponse.getStatusLine().getStatusCode();
            if(status != 200) {
                LOGGER.error("Failed : HTTP error code: {}", status);
            }
            response = EntityUtils.toString(httpResponse.getEntity(), StandardCharsets.UTF_8);
            LOGGER.trace("HTTP Response: {}", response);
            LOGGER.debug("Finished HTTP Request ['{}'], status: [{}]", httpRequest.getURI(), status);
        }
        return new HttpConnectionResponse(status, response);
    }


    @ObjectClassDefinition(name = "Sample Store - HTTP Client")
    public @interface Config {

        @AttributeDefinition(name = "Configuration ID") String configurationId();

        @AttributeDefinition(name = "HTTP Connection Timeout",
                description = "Value in milliseconds") int connectionTimeout() default 60000;

        @AttributeDefinition(name = "HTTP Connection Request Timeout",
                description = "Value in milliseconds") int connectionRequestTimeout() default 60000;

        @AttributeDefinition(name = "HTTP Socket Timeout",
                description = "Value in milliseconds") int socketTimeout() default 60000;

        @AttributeDefinition(name = "HTTP Max Total Connections") int maxTotalConnections() default 20;

        @AttributeDefinition(name = "HTTP Max Connections per Route") int maxConnectionsPerRoute() default 20;
    }
}
