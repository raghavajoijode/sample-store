package com.sample.portal.core.models;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.sample.portal.core.pojo.Product;
import com.sample.portal.core.services.ProductsService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.settings.SlingSettingsService;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.apache.sling.api.resource.ResourceResolver.PROPERTY_RESOURCE_TYPE;

@Model(adaptables = SlingHttpServletRequest.class)
public class ProductsModel {

    @OSGiService
    private ProductsService productsService;

    @SlingObject
    private SlingHttpServletRequest request;

    @PostConstruct
    protected void init() {
        request.getRequestPathInfo().getSelectors();
        // do nothing here for now
    }



    // list of products
    public List<Product> getProducts() {
        return productsService.getProducts();
    }

}
