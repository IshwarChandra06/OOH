package com.eikona.tech.repository;

import java.util.List;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.Query;

import com.eikona.tech.domain.MediaSite;


public interface MediaSiteRepository extends DataTablesRepository<MediaSite, Long>{
	
	List<MediaSite> findAllByIsDeletedFalse();

	@Query("SELECT ms from com.eikona.tech.domain.MediaSite as ms Where ms.isDeleted = false and "
			+ "(ms.assetCode LIKE %:searchValue%      	 or ms.vendorAssetCode LIKE %:searchValue% 			or ms.ownedByOrgId.name LIKE %:searchValue% or "
			+ "ms.length LIKE %:searchValue%        	 or ms.width LIKE %:searchValue% 					or ms.height LIKE %:searchValue% or "
			+ "ms.road LIKE %:searchValue%   			 or ms.city LIKE %:searchValue%               		or ms.latitude LIKE %:searchValue% or "
			+ "ms.locality LIKE %:searchValue% 			 or ms.longitude LIKE %:searchValue% 				or ms.district LIKE %:searchValue% or "
			+ "ms.state LIKE %:searchValue% 			 or ms.pincode LIKE %:searchValue% 			 		or ms.viewingTime LIKE %:searchValue% or "
			+ "ms.orientation.value LIKE %:searchValue%  or ms.captureFrequency.value LIKE %:searchValue%   or ms.placementType.value LIKE %:searchValue% or "
			+ "ms.mediaClass.value LIKE %:searchValue%    or ms.locationType.value LIKE %:searchValue%   	or ms.illumination.value LIKE %:searchValue% or "
			+ "ms.structureType.value LIKE %:searchValue% or ms.trafficType.value LIKE %:searchValue% or "
			+ "ms.mediaType.value LIKE %:searchValue% 	  or ms.trafficDensity.value LIKE %:searchValue% or "
			+ "ms.placeType.value LIKE %:searchValue% 	  or ms.ageGroup.value LIKE %:searchValue% or "
			+ "ms.material.value LIKE %:searchValue% 	  or ms.viewingDistance LIKE %:searchValue% or "
			+ "ms.cityTier.value LIKE %:searchValue% 	  or ms.catchmentStrata.value LIKE %:searchValue% 	or ms.quality.value LIKE %:searchValue% or "
			+ "ms.status.value LIKE %:searchValue%        "
			+ ")")
	List<MediaSite> search(String searchValue);

	MediaSite findByAssetCodeAndIsDeletedFalse(String name);

	MediaSite findByAssetCode(String assetCode);
}
