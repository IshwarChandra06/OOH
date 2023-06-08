package com.eikona.tech.repository;

import java.util.List;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.stereotype.Repository;

import com.eikona.tech.domain.PriceBreak;

@Repository
public interface PriceBreakRepository extends DataTablesRepository<PriceBreak, Long>{
	List<PriceBreak> findAllByIsDeletedFalse();
}
