package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Organisation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String address;
    private String fiscalNumber;

    @OneToMany(mappedBy = "organisation", cascade = CascadeType.ALL)
    private List<Action> actions;
}