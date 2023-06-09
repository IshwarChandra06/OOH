package com.eikona.tech.repository;

import java.util.List;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.stereotype.Repository;

import com.eikona.tech.domain.Feedback;

@Repository
public interface FeedbackRepository extends DataTablesRepository<Feedback, Long>{
	 List<Feedback> findAllByIsDeletedFalse();
}
