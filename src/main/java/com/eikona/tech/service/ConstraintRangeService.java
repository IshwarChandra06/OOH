package com.eikona.tech.service;

import org.springframework.data.domain.Page;

import com.eikona.tech.domain.ConstraintRange;
import com.eikona.tech.dto.SearchRequestDto;
import com.eikona.tech.service.common.AbstractService;

public interface ConstraintRangeService extends AbstractService<ConstraintRange, Long> {

	Page<ConstraintRange> searchByField(int pageNumber, int pageSize, String sortField, String sortOrder,
			SearchRequestDto searchRequestDto);

	Page<ConstraintRange> findPaginated(int pageNo, int pageSize, String sortField, String sortOrder);

}
