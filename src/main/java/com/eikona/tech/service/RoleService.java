package com.eikona.tech.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.eikona.tech.domain.Role;
import com.eikona.tech.dto.SearchRequestDto;
import com.eikona.tech.service.common.AbstractService;

public interface RoleService extends AbstractService<Role, Long> {
	Page<Role> findPaginated(int pageNo, int pageSize, String sortField, String sortOrder);

	Page<Role> searchByField(int pageNo, int pageSize, String sortField, String sortOrder,
			SearchRequestDto paginatedDto);

	List<Role> search(String searchValue);

	List<Role> getOrgRoles();
}
