package com.sample.portal.core.services.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.portal.core.pojo.HttpConnectionResponse;
import com.sample.portal.core.pojo.Product;
import com.sample.portal.core.pojo.Products;
import com.sample.portal.core.services.HttpClientService;
import com.sample.portal.core.services.ProductsService;
import org.apache.http.client.methods.HttpGet;
import org.eclipse.jetty.http.HttpHeader;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component(service = ProductsService.class, name = "Sample Portal Products Service")
@Designate(ocd = ProductsServiceImpl.ProductsServiceConfig.class)
public class ProductsServiceImpl implements ProductsService {

    @Reference
    private HttpClientService httpClientService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductsServiceImpl.class);
    private String productsApi;
    private String productDetailBaseApi;

    @ObjectClassDefinition(name = "Sample Portal Products Service Configuration")
    public @interface ProductsServiceConfig {
        String products_api() default "https://dummyjson.com/products?limit=30";
        String product_detail_base_api() default "https://dummyjson.com/products/";
    }

    @Activate
    protected void activate(ProductsServiceConfig config) {
        productsApi = config.products_api();
        productDetailBaseApi = config.product_detail_base_api();
    }

    @Override
    public List<Product> getProducts() {
        try {
            HttpGet httpGet = new HttpGet(productsApi);
            httpGet.setHeader(HttpHeader.CONTENT_TYPE.asString(), "application/json");
            HttpConnectionResponse connectionResponse = httpClientService.execute(httpGet);
            Products products = new ObjectMapper().readValue(connectionResponse.getResponse(), Products.class);
            return products.getProducts();
        } catch (IOException e) {
            LOGGER.error("Error fetching products: ", e);
        }
        return new ArrayList<>();
    }

    @Override
    public Product getProductById(String id) {
        try {
            HttpGet httpGet = new HttpGet(productDetailBaseApi + id);
            httpGet.setHeader(HttpHeader.CONTENT_TYPE.asString(), "application/json");
            HttpConnectionResponse connectionResponse = httpClientService.execute(httpGet);
            return new ObjectMapper().readValue(connectionResponse.getResponse(), Product.class);
        } catch (IOException e) {
            LOGGER.error("Error fetching product by ID {}: ", id, e);
        }
        return null;
    }
}
