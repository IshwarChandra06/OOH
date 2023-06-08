package com.eikona.tech.service;

import org.springframework.data.domain.Page;

import com.eikona.tech.domain.ConstraintSingle;
import com.eikona.tech.dto.SearchRequestDto;
import com.eikona.tech.service.common.AbstractService;

public interface ConstraintSingleService extends AbstractService<ConstraintSingle, Long> {

	Page<ConstraintSingle> searchByField(int pageNumber, int pageSize, String sortField, String sortOrder,
			SearchRequestDto searchRequestDto);

	Page<ConstraintSingle> findPaginated(int pageNo, int pageSize, String sortField, String sortOrder);

}
