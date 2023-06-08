package com.eikona.tech.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.eikona.tech.domain.Privilege;
import com.eikona.tech.dto.SearchRequestDto;
import com.eikona.tech.service.common.AbstractService;

public interface PrivilegeService extends AbstractService<Privilege, Long>{
	Page<Privilege> findPaginated(int pageNo, int pageSize, String sortField, String sortOrder);

	Page<Privilege> searchByField(int pageNo, int pageSize, String sortField, String sortOrder,
			SearchRequestDto paginatedDto);

	List<Privilege> search(String searchValue);
}
