package com.resource_server.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponse {

    private String name;

    private double price;

    private String category;

    private int stockQty;

    private String brand;
}
