package com.eikona.tech.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.eikona.tech.domain.Ticket;
import com.eikona.tech.dto.SearchRequestDto;
import com.eikona.tech.repository.TicketRepository;
import com.eikona.tech.service.TicketService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class TicketServiceImpl implements TicketService {

	@Autowired
	protected TicketRepository ticketRepository;

	@SuppressWarnings("rawtypes")
	@Autowired
	protected PaginatedServiceImpl paginatedServiceImpl;

	// @Override
	public Ticket find(Long id) {
		Ticket result = null;

		Optional<Ticket> catalog = ticketRepository.findById(id);

		if (catalog.isPresent()) {
			result = catalog.get();
		}

		return result;
	}

	// @Override
	public List<Ticket> findAll() {
		return ticketRepository.findAllByIsDeletedFalse();
	}

	// @Override
	public Ticket save(Ticket entity) {
		entity.setDeleted(false);
		return ticketRepository.save(entity);
	}

	// @Override
	public void delete(Long id) {
		Ticket result = null;

		Optional<Ticket> catalog = ticketRepository.findById(id);

		if (catalog.isPresent()) {
			result = catalog.get();
			result.setDeleted(true);
		}
		this.ticketRepository.save(result);
	}

	// @Override
	public Ticket update(Ticket entity) {
		return ticketRepository.save(entity);
	}

	public DataTablesOutput<Ticket> getAll(@Valid DataTablesInput input) {
		Specification<Ticket> isDeletedFalse = (Specification<Ticket>) (root, query, builder) -> {
			return builder.equal(root.get("isDeleted"), false);
		};
		return (DataTablesOutput<Ticket>) ticketRepository.findAll(input, isDeletedFalse);
	}

	@Override
	public Page<Ticket> findPaginated(int pageNo, int pageSize, String sortField, String sortOrder) {

		Sort sort = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
				: Sort.by(sortField).descending();

		Specification<Ticket> idDeleted = (root, query, cb) -> {
			return cb.equal(root.get("isDeleted"), false);
		};

		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
		return this.ticketRepository.findAll(idDeleted, pageable);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page<Ticket> searchByField(int pageNo, int pageSize, String sortField, String sortOrder,
			SearchRequestDto paginatedDto) {

		ObjectMapper oMapper = new ObjectMapper();
		Map<String, String> map = oMapper.convertValue(paginatedDto.getSearchData(), Map.class);

		Sort sort = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
				: Sort.by(sortField).descending();

		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
		Specification<Ticket> fieldSpc = paginatedServiceImpl.fieldSpecification(map);
		Page<Ticket> pageableList = ticketRepository.findAll(fieldSpc, pageable);

		return pageableList;
	}

}
