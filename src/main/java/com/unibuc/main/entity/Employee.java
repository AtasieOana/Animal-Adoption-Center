package com.unibuc.main.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "caretakers")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employment_date")
    private Date employmentDate;

    @Column(name = "responsibility")
    private String responsibility;

    @Column(name = "experience")
    private Integer experience;

    @Column(name = "salary")
    private Integer salary;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinTable(name="employees_persons_details",
            joinColumns = { @JoinColumn(name="employees", referencedColumnName = "id")
            }, inverseJoinColumns = {
            @JoinColumn(name = "persons_details", referencedColumnName = "id")
    })
    private PersonDetails personDetails;
}
