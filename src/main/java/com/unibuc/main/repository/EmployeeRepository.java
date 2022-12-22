package com.unibuc.main.repository;

import com.unibuc.main.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findAllByResponsibilityNotNull();

    List<Employee> findAllByExperienceNotNull();

    @Query("SELECT emp FROM Employee emp WHERE emp.personDetails.firstName = :firstName " +
            "AND emp.personDetails.lastName = :lastName AND emp.responsibility is not null")
    Optional<Employee> findCaretakerByName(String firstName, String lastName);

    @Query("SELECT emp FROM Employee emp WHERE emp.personDetails.firstName = :firstName " +
            "AND emp.personDetails.lastName = :lastName AND emp.experience is not null")
    Optional<Employee> findVetByName(String firstName, String lastName);
    /*
    @Modifying
    @Transactional
    @Query("UPDATE Employee e SET e = :newEmployee WHERE e.id = :id")
    void updateEmployee(Long id, Employee newEmployee);
     */
}
