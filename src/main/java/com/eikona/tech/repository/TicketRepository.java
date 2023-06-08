package com.eikona.tech.repository;

import java.util.List;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.stereotype.Repository;

import com.eikona.tech.domain.Ticket;

@Repository
public interface TicketRepository extends DataTablesRepository<Ticket, Long>{
	List<Ticket> findAllByIsDeletedFalse();
}
