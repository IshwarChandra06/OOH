package com.eikona.tech.service;

import org.springframework.data.domain.Page;

import com.eikona.tech.domain.CampaignGroup;
import com.eikona.tech.dto.SearchRequestDto;
import com.eikona.tech.service.common.AbstractService;

public interface CampaignGroupService extends AbstractService<CampaignGroup, Long> {
	Page<CampaignGroup> findPaginated(int pageNo, int pageSize, String sortField, String sortOrder);

	Page<CampaignGroup> searchByField(int pageNo, int pageSize, String sortField, String sortOrder,
			SearchRequestDto paginatedDto);
}
