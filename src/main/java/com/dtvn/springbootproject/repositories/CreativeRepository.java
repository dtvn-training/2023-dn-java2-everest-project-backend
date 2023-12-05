package com.dtvn.springbootproject.repositories;

import com.dtvn.springbootproject.entities.Creatives;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreativeRepository extends JpaRepository<Creatives, Long> {
}
