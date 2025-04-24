package com.resource_server.service;

import com.resource_server.dto.ProductRequest;
import com.resource_server.dto.ProductResponse;
import com.resource_server.entities.Product;
import com.resource_server.exception.ResourceNotFoundException;
import com.resource_server.repository.ProductRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = modelMapper.map(productRequest, Product.class);
        Product createProduct = productRepo.save(product);
        return modelMapper.map(createProduct, ProductResponse.class);
    }

    @Override
    public List<ProductResponse> getAllProduct() {
        List<Product> products = productRepo.findAll();
        return products.stream().map((element) -> modelMapper
                .map(element, ProductResponse.class)).toList();
    }

    @Override
    public ProductResponse getProductById(String id) {
        Product product = productRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Product", "id", id));
        return modelMapper.map(product, ProductResponse.class);
    }

    @Override
    public void deleteProduct(String id) {
        Product product = productRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Product", "id", id));
        productRepo.delete(product);
    }

    @Override
    public ProductResponse updateProduct(String id, ProductRequest productRequest) {
        Product product = productRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Product", "id", id));
        product.setName(productRequest.getName());
        product.setPrice(productRequest.getPrice());
        product.setBrand(productRequest.getBrand());
        product.setStockQty(productRequest.getStockQty());
        product.setCategory(productRequest.getCategory());
        Product updateProduct = productRepo.save(product);
        return modelMapper.map(updateProduct, ProductResponse.class);
    }
}
