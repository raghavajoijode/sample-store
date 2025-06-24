package com.sample.portal.core.services;

import com.sample.portal.core.pojo.Product;

import java.util.List;

public interface ProductsService {
    List<Product> getProducts();
    Product getProductById(String id);
}
