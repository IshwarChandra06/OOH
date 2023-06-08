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

import com.eikona.tech.domain.Feedback;
import com.eikona.tech.dto.SearchRequestDto;
import com.eikona.tech.repository.FeedbackRepository;
import com.eikona.tech.service.FeedbackService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class FeedbackServiceImpl implements FeedbackService {

	@Autowired
	protected FeedbackRepository feedbackRepository;
	
	@SuppressWarnings("rawtypes")
   	@Autowired
    protected PaginatedServiceImpl paginatedServiceImpl;

	// @Override
	public Feedback find(Long id) {
		Feedback result = null;

		Optional<Feedback> catalog = feedbackRepository.findById(id);

		if (catalog.isPresent()) {
			result = catalog.get();
		}

		return result;
	}

	// @Override
	public List<Feedback> findAll() {
		return feedbackRepository.findAllByIsDeletedFalse();
	}

	// @Override
	public Feedback save(Feedback entity) {
		entity.setDeleted(false);
		return feedbackRepository.save(entity);
	}

	// @Override
	public void delete(Long id) {
		Feedback result = null;

		Optional<Feedback> catalog = feedbackRepository.findById(id);

		if (catalog.isPresent()) {
			result = catalog.get();
			result.setDeleted(true);
		}
		this.feedbackRepository.save(result);
	}

	// @Override
	public Feedback update(Feedback entity) {
		return feedbackRepository.save(entity);
	}

	public DataTablesOutput<Feedback> getAll(@Valid DataTablesInput input) {
		Specification<Feedback> isDeletedFalse = (Specification<Feedback>) (root, query, builder) -> {
			return builder.equal(root.get("isDeleted"), false);
		};
		return (DataTablesOutput<Feedback>) feedbackRepository.findAll(input, isDeletedFalse);
	}

	@Override
	public Page<Feedback> findPaginated(int pageNo, int pageSize, String sortField, String sortOrder) {

		Sort sort = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
				: Sort.by(sortField).descending();

		Specification<Feedback> idDeleted = (root, query, cb) -> {
			return cb.equal(root.get("isDeleted"), false);
		};

		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
		return this.feedbackRepository.findAll(idDeleted, pageable);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page<Feedback> searchByField(int pageNo, int pageSize, String sortField, String sortOrder,
			SearchRequestDto paginatedDto) {

		ObjectMapper oMapper = new ObjectMapper();
		Map<String, String> map = oMapper.convertValue(paginatedDto.getSearchData(), Map.class);

		Sort sort = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
				: Sort.by(sortField).descending();

		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
		Specification<Feedback> fieldSpc = paginatedServiceImpl.fieldSpecification(map);
		Page<Feedback> pageableList = feedbackRepository.findAll(fieldSpc, pageable);

		return pageableList;
	}


}
