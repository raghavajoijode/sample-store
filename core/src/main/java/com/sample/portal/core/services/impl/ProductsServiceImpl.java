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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component(service = ProductsService.class, name = "Sample Portal Products Service")
@Designate(ocd = ProductsServiceImpl.ProductsServiceConfig.class)
public class ProductsServiceImpl implements ProductsService {

    @Reference
    private HttpClientService httpClientService;

    List<Product> products = new ArrayList<>();
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

        Product product1 = createProduct(1, "Product 1", "Description for Product 1");
        Product product2 = createProduct(2, "Product 2", "Description for Product 2");
        Product product3 = createProduct(3, "Product 3", "Description for Product 3");
        Product product4 = createProduct(4, "Product 4", "Description for Product 4");
        Product product5 = createProduct(5, "Product 5", "Description for Product 5");
        Product product6 = createProduct(6, "Product 6", "Description for Product 6");
        Product product7 = createProduct(7, "Product 7", "Description for Product 7");
        products.add(product1);
        products.add(product2);
        products.add(product3);
        products.add(product4);
        products.add(product5);
        products.add(product6);
        products.add(product7);
    }

    private static Product createProduct(int id, String title, String description) {
        Product product1 = new Product();
        product1.setId(id);
        product1.setTitle(title);
        product1.setDescription(description);
        product1.setThumbnail("https://cdn.dummyjson.com/product-images/beauty/essence-mascara-lash-princess/thumbnail.webp");
        return product1;
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
            System.out.println(e);
        }
        return new ArrayList<>();
    }

    @Override
    public Product getProductById(int id) {
        return getProducts().stream().filter(product -> product.getId() == id).findFirst().orElse(null);
    }
}
