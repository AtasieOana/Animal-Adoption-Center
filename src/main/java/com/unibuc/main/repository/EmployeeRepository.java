package com.unibuc.main.repository;

import com.unibuc.main.config.Log;
import com.unibuc.main.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Log
    List<Employee> findAllByResponsibilityNotNull();

    @Log
    List<Employee> findAllByExperienceNotNull();

    @Log
    @Query("SELECT emp FROM Employee emp WHERE emp.personDetails.firstName = :firstName " +
            "AND emp.personDetails.lastName = :lastName AND emp.responsibility is not null")
    Optional<Employee> findCaretakerByName(String firstName, String lastName);

    @Log
    @Query("SELECT emp FROM Employee emp WHERE emp.personDetails.firstName = :firstName " +
            "AND emp.personDetails.lastName = :lastName AND emp.experience is not null")
    Optional<Employee> findVetByName(String firstName, String lastName);

}
