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
@Table(name = "caretakers")
public class Caretaker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "responsibility")
    private String responsibility;


    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinTable(name="caretakers_employees_details",
            joinColumns = { @JoinColumn(name="caretakers", referencedColumnName = "id")
            }, inverseJoinColumns = {
            @JoinColumn(name = "employees_details", referencedColumnName = "id")
    })
    private EmployeeDetails employeeDetails;
}
