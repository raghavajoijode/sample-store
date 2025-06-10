package com.sample.portal.core.services;

import com.sample.portal.core.pojo.HttpConnectionResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.http.HttpScheme;

import java.io.IOException;
import java.util.Locale;

public interface HttpClientService {
    String HTTP_SCHEME = HttpScheme.HTTP.name().toLowerCase(Locale.getDefault());
    String HTTPS_SCHEME = HttpScheme.HTTPS.name().toLowerCase(Locale.getDefault());

    CloseableHttpClient getConfiguredHttpClient();
    PoolingHttpClientConnectionManager getPoolingConnectionManager();
    HttpConnectionResponse execute(HttpRequestBase httpRequest) throws IOException;
}
