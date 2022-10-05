package com.alexvar.springboot_rest.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name="shopping_items")
@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class ShoppingItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="name")
    private String name;

    @Column(name="price")
    private int price;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Override
    public boolean equals(Object o) {
        return this.id == ((ShoppingItem)o).id &&
                this.price == ((ShoppingItem)o).price &&
                this.name.equals(((ShoppingItem)o).name) &&
                this.createdAt.equals(((ShoppingItem)o).createdAt);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
