package com.eikona.tech.service;

import java.security.Principal;
import java.util.List;

import org.springframework.data.domain.Page;

import com.eikona.tech.domain.Campaign;
import com.eikona.tech.dto.SearchRequestDto;
import com.eikona.tech.service.common.AbstractService;

public interface CampaignService extends AbstractService<Campaign, Long> {
	Page<Campaign> findPaginated(int pageNo, int pageSize, String sortField, String sortOrder,SearchRequestDto paginatedDto, Principal principal);

	Page<Campaign> searchByField(int pageNo, int pageSize, String sortField, String sortOrder,
			SearchRequestDto paginatedDto, Principal principal);

	List<Campaign> search(String searchValue);
}
