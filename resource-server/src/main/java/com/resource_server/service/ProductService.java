package com.resource_server.service;

import com.resource_server.dto.ProductRequest;
import com.resource_server.dto.ProductResponse;

import java.util.List;

public interface ProductService {

    ProductResponse createProduct(ProductRequest productRequest);

    List<ProductResponse> getAllProduct();

    ProductResponse getProductById(String id);

    void deleteProduct(String id);

    ProductResponse updateProduct(String id, ProductRequest productRequest);
}
