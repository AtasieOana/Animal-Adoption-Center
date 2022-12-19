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
@Table(name = "vets")
public class Vet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "experience")
    private Integer experience;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinTable(name="vets_employees_details",
            joinColumns = { @JoinColumn(name="vets", referencedColumnName = "id")
            }, inverseJoinColumns = {
            @JoinColumn(name = "employees_details", referencedColumnName = "id")
    })
    private EmployeeDetails employeeDetails;
}