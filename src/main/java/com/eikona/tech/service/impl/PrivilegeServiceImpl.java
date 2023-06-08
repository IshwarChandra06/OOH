package com.eikona.tech.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.eikona.tech.domain.Privilege;
import com.eikona.tech.dto.SearchRequestDto;
import com.eikona.tech.repository.PrivilegeRepository;
import com.eikona.tech.service.PrivilegeService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class PrivilegeServiceImpl implements PrivilegeService {

	@Autowired
	protected PrivilegeRepository privilegeRepository;

	@SuppressWarnings("rawtypes")
	@Autowired
	protected PaginatedServiceImpl paginatedServiceImpl;

	@Override
	public Privilege find(Long id) {
		Privilege result = null;

		Optional<Privilege> catalog = privilegeRepository.findById(id);

		if (catalog.isPresent()) {
			result = catalog.get();
		}

		return result;
	}

	@Override
	public List<Privilege> findAll() {
		return privilegeRepository.findAllByIsDeletedFalse();
	}

	@Override
	public Privilege save(Privilege entity) {
		entity.setDeleted(false);
		return privilegeRepository.save(entity);
	}

	@Override
	public void delete(Long id) {
		Privilege result = null;

		Optional<Privilege> catalog = privilegeRepository.findById(id);

		if (catalog.isPresent()) {
			result = catalog.get();
			result.setDeleted(true);
		}
		this.privilegeRepository.save(result);
	}

	@Override
	public Privilege update(Privilege entity) {
		return privilegeRepository.save(entity);
	}

	@Override
	public Page<Privilege> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
		if (null == sortDirection || sortDirection.isEmpty()) {
			sortDirection = "asc";
		}
		if (null == sortField || sortField.isEmpty()) {
			sortField = "id";
		}
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
				: Sort.by(sortField).descending();

		Specification<Privilege> isDeleted = (root, query, cb) -> {
			return cb.equal(root.get("isDeleted"), false);
		};

		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
		return this.privilegeRepository.findAll(isDeleted, pageable);
	}

	@SuppressWarnings("unchecked")
	public Page<Privilege> searchByField(int pageNo, int pageSize, String sortField, String sortDirection,
			SearchRequestDto paginatedDto) {

		ObjectMapper oMapper = new ObjectMapper();
		Map<String, String> map = oMapper.convertValue(paginatedDto.getSearchData(), Map.class);
		if (null == sortDirection || sortDirection.isEmpty()) {
			sortDirection = "asc";
		}
		if (null == sortField || sortField.isEmpty()) {
			sortField = "id";
		}
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
				: Sort.by(sortField).descending();

		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
		Specification<Privilege> fieldSpc = paginatedServiceImpl.fieldSpecification(map);
		Page<Privilege> allPrivilege = privilegeRepository.findAll(fieldSpc, pageable);
		return allPrivilege;
	}

	@Override
	public List<Privilege> search(String searchValue) {
		List<Privilege> privilegeList = privilegeRepository.search(searchValue);
		return privilegeList;
	}

}
