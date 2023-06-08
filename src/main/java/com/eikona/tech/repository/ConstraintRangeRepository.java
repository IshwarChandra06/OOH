package com.eikona.tech.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.eikona.tech.domain.ConstraintRange;

@Repository
public interface ConstraintRangeRepository extends DataTablesRepository<ConstraintRange, Long>{
	 List<ConstraintRange> findAllByIsDeletedFalse();

	@Query("select cs from com.eikona.tech.domain.ConstraintRange as cs where cs.type in :mediaSiteAttribute and cs.isDeleted= false")
	List<ConstraintRange> findByType(ArrayList<String> mediaSiteAttribute);

	@Query("select cs from com.eikona.tech.domain.ConstraintRange as cs where cs.type in :rangeList and cs.isDeleted= false")
	List<ConstraintRange> findByType(String[] rangeList);
}
