 package com.prodapt.practice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.prodapt.practice.entity.Cycle;
 
  
	@Repository
	public interface CycleRepository extends JpaRepository<Cycle, Long> {

		Cycle findByName(String name);
	     
	}
 
