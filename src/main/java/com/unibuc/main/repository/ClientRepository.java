package com.unibuc.main.repository;

import com.unibuc.main.config.Log;
import com.unibuc.main.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    @Log
    @Query("SELECT c FROM Client c WHERE c.personDetails.firstName = :firstName " +
            "AND c.personDetails.lastName = :lastName")
    Optional<Client> findClientByName(String firstName, String lastName);

    @Log
    @Query("SELECT AVG(YEAR(CURRENT_DATE) - YEAR(c.birthDate)) FROM Client c")
    Double avgAge();

}
