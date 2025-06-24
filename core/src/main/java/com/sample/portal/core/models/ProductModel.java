package com.sample.portal.core.models;

import com.sample.portal.core.pojo.Product;
import com.sample.portal.core.services.ProductsService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import javax.annotation.PostConstruct;
import java.util.List;

@Model(adaptables = SlingHttpServletRequest.class)
public class ProductModel {

    @OSGiService
    private ProductsService productsService;

    @SlingObject
    private SlingHttpServletRequest request;


    @PostConstruct
    protected void init() {
        // do nothing here for now
    }


    // product
    public Product getProduct() {
        String productId = request.getRequestPathInfo().getSelectorString();
        return productsService.getProductById(productId);
    }

}
