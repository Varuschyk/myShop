package com.alexvar.springboot_rest.model;

import com.alexvar.springboot_rest.SpringbootRestApplication;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name="users")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="name", nullable = false)
    @NotBlank(message = "Field cannot be empty!")
    @Pattern(regexp = "[A-Z][a-z]+", message = "Must starts with uppercase letter and continue sequence with lowercase")
    private String name;

    @Column(name="surname", nullable = false)
    @NotBlank(message = "Field cannot be empty!")
    @Pattern(regexp = "[A-Z][a-z]+", message = "Must starts with uppercase letter and continue sequence with lowercase")
    private String surname;

    @Column(name="password", nullable = false)
    @NotBlank(message = "Field cannot be empty!")
    private String password;

    @Column(name="email", nullable = false)
    @Pattern(regexp = "[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}", message = "Must be a valid e-mail address")
    private String email;

    @Column(name="role")
    @Enumerated(value = EnumType.STRING)
    private Role role;

    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinTable(name="stuff_list",
    joinColumns = @JoinColumn(name="user_id"),
    inverseJoinColumns = @JoinColumn(name="item_id"))
    private Set<ShoppingItem> stuffList;

    public User(long id, String name, String surname, String password, String email, Role role, Set<ShoppingItem> stuffList) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.password = SpringbootRestApplication.passwordEncoder.encode(password);
        this.email = email;
        this.role = role;
        this.stuffList = stuffList;
    }

    public String getFullName() {return name + ' ' + surname;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(name, user.name) && Objects.equals(surname, user.surname) && Objects.equals(password, user.password) && Objects.equals(email, user.email) && Objects.equals(role, user.role) && Objects.equals(stuffList, user.stuffList);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
