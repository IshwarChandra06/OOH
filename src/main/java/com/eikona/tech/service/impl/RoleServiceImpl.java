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

import com.eikona.tech.domain.Role;
import com.eikona.tech.dto.SearchRequestDto;
import com.eikona.tech.repository.RoleRepository;
import com.eikona.tech.service.RoleService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class RoleServiceImpl implements RoleService {

	@Autowired
	protected RoleRepository roleRepository;

	@SuppressWarnings("rawtypes")
	@Autowired
	protected PaginatedServiceImpl paginatedServiceImpl;

	@Override
	public Role find(Long id) {
		Role result = null;

		Optional<Role> catalog = roleRepository.findById(id);

		if (catalog.isPresent()) {
			result = catalog.get();
		}

		return result;
	}

	@Override
	public List<Role> findAll() {
		return roleRepository.findAllByIsDeletedFalse();
	}

	@Override
	public Role save(Role entity) {
		entity.setDeleted(false);
		return roleRepository.save(entity);
	}

	@Override
	public void delete(Long id) {
		Role result = null;

		Optional<Role> catalog = roleRepository.findById(id);

		if (catalog.isPresent()) {
			result = catalog.get();
			result.setDeleted(true);
		}
		this.roleRepository.save(result);
	}

	@Override
	public Role update(Role entity) {
		return roleRepository.save(entity);
	}

	@Override
	public Page<Role> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
		if (null == sortDirection || sortDirection.isEmpty()) {
			sortDirection = "asc";
		}
		if (null == sortField || sortField.isEmpty()) {
			sortField = "id";
		}
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
				: Sort.by(sortField).descending();

		Specification<Role> isDeleted = (root, query, cb) -> {
			return cb.equal(root.get("isDeleted"), false);
		};

		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
		return this.roleRepository.findAll(isDeleted, pageable);
	}

	@SuppressWarnings("unchecked")
	public Page<Role> searchByField(int pageNo, int pageSize, String sortField, String sortDirection,
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
		Specification<Role> fieldSpc = paginatedServiceImpl.fieldSpecification(map);
		Page<Role> allRole = roleRepository.findAll(fieldSpc, pageable);
		return allRole;
	}

	@Override
	public List<Role> search(String searchValue) {
		List<Role> roleList = roleRepository.search(searchValue);
		return roleList;
	}

	@Override
	public List<Role> getOrgRoles() {
		List<Role> roleList = roleRepository.findAllByIsOrgRoleTrue();
		return roleList;
	}
}
