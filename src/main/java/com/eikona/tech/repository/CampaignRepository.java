package com.eikona.tech.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.eikona.tech.domain.Campaign;
import com.eikona.tech.domain.Organization;

@Repository
public interface CampaignRepository extends DataTablesRepository<Campaign, Long>{
	 List<Campaign> findAllByIsDeletedFalse();

	@Query("select cm from com.eikona.tech.domain.Campaign cm join cm.mediasite MediaSite where MediaSite.id = :assetId")
	List<Campaign> findCampaign(Long assetId);

	@Query("select cm from com.eikona.tech.domain.Campaign cm where cm.isDeleted=false and ( cm.name LIKE %:searchValue% or "
			+ "cm.organization.name LIKE %:searchValue% or "
			+ "cm.agency.name LIKE %:searchValue% or cm.brand.name LIKE %:searchValue% )")
	List<Campaign> search(String searchValue);

	@Query("select cm from com.eikona.tech.domain.Campaign cm where cm.id = :campaginId and (cm.organization =:organization or cm.agency =:organization or cm.brand =:organization)")
	Optional<Campaign> findByIdAndOrganization(Long campaginId, Organization organization);
}
