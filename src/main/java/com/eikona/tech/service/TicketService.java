package com.eikona.tech.service;

import org.springframework.data.domain.Page;

import com.eikona.tech.domain.Ticket;
import com.eikona.tech.dto.SearchRequestDto;
import com.eikona.tech.service.common.AbstractService;

public interface TicketService extends AbstractService<Ticket, Long> {

	Page<Ticket> findPaginated(int pageNo, int pageSize, String sortField, String sortOrder);

	Page<Ticket> searchByField(int pageNo, int pageSize, String sortField, String sortOrder,
			SearchRequestDto paginatedDto);

}
