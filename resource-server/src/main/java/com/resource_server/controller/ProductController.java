package com.resource_server.controller;

import com.resource_server.dto.ProductRequest;
import com.resource_server.dto.ProductResponse;
import com.resource_server.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/api/users")
    @PreAuthorize(("hasAuthority('SCOPE_message.read')"))
    public String[] getUser(){
        return new String[]{"Kishor", "Radha", "Shyam", "Hari", "Gopal", "Krishna"};
    }

    @PostMapping("/product")
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest productRequest){
        ProductResponse productResponse = productService.createProduct(productRequest);
        return new ResponseEntity<>(productResponse, HttpStatus.CREATED);
    }

    @GetMapping("/api/products")
    @PreAuthorize(("hasAuthority('SCOPE_message.read')"))
    public List<ProductResponse> getAllProduct(){
        return productService.getAllProduct();
    }

    @GetMapping("/api/product/{id}")
    @PreAuthorize(("hasAuthority('SCOPE_message.read')"))
    public ResponseEntity<ProductResponse> getProductById(@PathVariable("id") String productId){
        ProductResponse productResponse = productService.getProductById(productId);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") String productId){
        productService.deleteProduct(productId);
        return new ResponseEntity<>("Product Delete Successfully", HttpStatus.OK);
    }

    @PutMapping("/product/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable("id") String productId,
            @Valid @RequestBody ProductRequest productRequest)
    {
        ProductResponse productResponse = productService.updateProduct(productId, productRequest);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }
}
