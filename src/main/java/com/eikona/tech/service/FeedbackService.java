package com.eikona.tech.service;

import org.springframework.data.domain.Page;

import com.eikona.tech.domain.Feedback;
import com.eikona.tech.dto.SearchRequestDto;
import com.eikona.tech.service.common.AbstractService;

public interface FeedbackService extends AbstractService<Feedback, Long> {

	Page<Feedback> findPaginated(int pageNo, int pageSize, String sortField, String sortOrder);

	Page<Feedback> searchByField(int pageNo, int pageSize, String sortField, String sortOrder,
			SearchRequestDto paginatedDto);

}
