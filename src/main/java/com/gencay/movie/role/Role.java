package com.gencay.movie.role;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.AUTO;

@NoArgsConstructor
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    private String name;

    public Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}

