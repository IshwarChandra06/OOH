package com.eikona.tech.repository;

import java.util.List;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.stereotype.Repository;

import com.eikona.tech.domain.CampaignGroup;

@Repository
public interface CampaignGroupRepository extends DataTablesRepository<CampaignGroup, Long>{
	 List<CampaignGroup> findAllByIsDeletedFalse();
}
