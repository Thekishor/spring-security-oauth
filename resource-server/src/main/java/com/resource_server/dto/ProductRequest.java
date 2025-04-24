package com.resource_server.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequest {

    @NotBlank(message = "Product Name cannot be blank")
    private String name;

    @NotNull(message = "Product Price cannot be null")
    @Positive(message = "Product Price must be greater than 0")
    private double price;

    @NotBlank(message = "Product Category Cannot be blank")
    private String category;

    @NotNull(message = "Product Stock cannot be null")
    @Positive(message = "Product Stock must be greater than 0")
    private int stockQty;

    @NotBlank(message = "Product Brand Cannot be blank")
    private String brand;
}
