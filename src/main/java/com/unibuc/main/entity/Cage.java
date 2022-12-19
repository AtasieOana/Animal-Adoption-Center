package com.unibuc.main.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cages")
public class Cage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "number_places")
    private Integer numberPlaces;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name="cages_caretakers",
            joinColumns = { @JoinColumn(name="cages", referencedColumnName = "id")
            }, inverseJoinColumns = {
            @JoinColumn(name = "caretakers", referencedColumnName = "id")
    })
    private Caretaker caretaker;
}