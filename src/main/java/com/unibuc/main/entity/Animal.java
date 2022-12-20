package com.unibuc.main.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "animals")
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "animal_type")
    private String animalType;

    @Column(name = "birth_year")
    private Integer birthYear;

    @Column(name = "found_date")
    private Date foundDate;

    @ManyToOne
    @JoinTable(name="animals_cages",
            joinColumns = { @JoinColumn(name="animals", referencedColumnName = "id")
            }, inverseJoinColumns = {
            @JoinColumn(name = "cages", referencedColumnName = "id")
    })
    private Cage cage;
    /* un animal poate sta intr-o singura cusca, iar intr-o cusca pot sta mai multe animale */

    @ManyToOne
    @JoinTable(name="animals_clients",
            joinColumns = { @JoinColumn(name="animals", referencedColumnName = "id")
            }, inverseJoinColumns = {
            @JoinColumn(name = "clients", referencedColumnName = "id")
    })
    private Client client;

    @ManyToOne
    @JoinTable(name="animals_diets",
            joinColumns = { @JoinColumn(name="animals", referencedColumnName = "id")
            }, inverseJoinColumns = {
            @JoinColumn(name = "diets", referencedColumnName = "id")
    })
    private Diet diet;

}
