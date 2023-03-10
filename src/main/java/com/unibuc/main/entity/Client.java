package com.unibuc.main.entity;

import javax.persistence.*;
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
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_becoming_client")
    private Date dateBecomingClient;

    @Column(name = "birth_date")
    private Date birthDate;

    @Column(name = "gender")
    private String gender;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinTable(name="clients_persons_details",
            joinColumns = { @JoinColumn(name="clients", referencedColumnName = "id")
            }, inverseJoinColumns = {
            @JoinColumn(name = "persons_details", referencedColumnName = "id")
    })
    private PersonDetails personDetails;
}