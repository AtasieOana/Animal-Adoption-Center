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
@Table(name = "medical_records")
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "general_health_state")
    private String generalHealthState;

    @Column(name = "generation_date")
    private Date generationDate;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinTable(name="animals_medical_records",
            joinColumns = { @JoinColumn(name="medical_records", referencedColumnName = "id")
            }, inverseJoinColumns = {
            @JoinColumn(name = "animals", referencedColumnName = "id")
    })
    private Animal animal;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name="vaccines_medical_records",
            joinColumns = { @JoinColumn(name="medical_records", referencedColumnName = "id")
            }, inverseJoinColumns = {
            @JoinColumn(name = "vaccines", referencedColumnName = "id")
    })
    private Vaccine vaccine;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name="employees_medical_records",
            joinColumns = { @JoinColumn(name="medical_records", referencedColumnName = "id")
            }, inverseJoinColumns = {
            @JoinColumn(name = "employees", referencedColumnName = "id")
    })
    private Employee vet;
}
