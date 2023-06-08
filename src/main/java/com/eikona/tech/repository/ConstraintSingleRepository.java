package com.eikona.tech.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.eikona.tech.domain.ConstraintSingle;

@Repository
public interface ConstraintSingleRepository extends DataTablesRepository<ConstraintSingle, Long>{
	 List<ConstraintSingle> findAllByIsDeletedFalse();

	@Query("select cs from com.eikona.tech.domain.ConstraintSingle as cs where cs.type in :mediaSiteAttribute and cs.isDeleted= false")
	List<ConstraintSingle> findByType(ArrayList<String> mediaSiteAttribute);

	@Query("select cs from com.eikona.tech.domain.ConstraintSingle as cs where cs.type in :singleList and cs.isDeleted= false")
	List<ConstraintSingle> findByType(String[] singleList);
}
