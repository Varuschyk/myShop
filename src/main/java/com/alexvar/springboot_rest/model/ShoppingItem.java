package com.alexvar.springboot_rest.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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
    private double price;

    @Column(name="created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToMany
    @JoinTable(name = "item_user",
            joinColumns = @JoinColumn(name = "item_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> users;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShoppingItem that = (ShoppingItem) o;
        return price == that.price && Objects.equals(name, that.name) && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, createdAt);
    }
}
