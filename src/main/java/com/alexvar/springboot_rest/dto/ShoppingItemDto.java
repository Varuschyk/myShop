package com.alexvar.springboot_rest.dto;

import com.alexvar.springboot_rest.model.ShoppingItem;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonNaming
public class ShoppingItemDto {
    private long id;
    private String name;
    private double price;
    private LocalDateTime createdAt;

    public ShoppingItemDto() {}
    public ShoppingItemDto(ShoppingItem item) {
        this.id = item.getId();
        this.name = item.getName();
        this.price = item.getPrice();
        this.createdAt = item.getCreatedAt();
    }

}
