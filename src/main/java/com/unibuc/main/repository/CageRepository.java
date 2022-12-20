package com.unibuc.main.repository;

import com.unibuc.main.entity.Cage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CageRepository extends JpaRepository<Cage, Long> {

    List<Cage> findAllByCaretakerNull();
}
