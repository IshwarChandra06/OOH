package com.eikona.tech.repository;

import java.util.Date; 
import java.util.List;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.eikona.tech.domain.MultimediaLog;
import com.eikona.tech.domain.Organization;
import com.eikona.tech.dto.MediaSiteImageLogDto;

@Repository
public interface MultimediaLogRepository extends DataTablesRepository<MultimediaLog, Long>, JpaSpecificationExecutor<MultimediaLog>{
	 List<MultimediaLog> findAllByIsDeletedFalse();

	@Query("select ml from com.eikona.tech.domain.MultimediaLog as ml where ml.isDeleted = false and ml.timeStamp between :startDate and :endDate and ml.mediaSite.id in :idList order by timeStamp desc ")
	List<MultimediaLog> findImageLog(List<Long> idList,Date startDate, Date endDate);
	
	@Query("select ml from com.eikona.tech.domain.MultimediaLog as ml where ml.isDeleted = false and ml.timeStamp between :startDate and :endDate and ml.mediaSite.id =:assetId order by timeStamp desc ")
	List<MultimediaLog> findImageLog(Long assetId,Date startDate, Date endDate);

	@Query("select new com.eikona.tech.dto.MediaSiteImageLogDto(ml.mediaSite.assetCode, ml.timeStamp, ml.image) from com.eikona.tech.domain.MultimediaLog as ml where ml.isDeleted = false and ml.timeStamp between :startDate and :endDate and ml.mediaSite.id in :idList ")
	List<MediaSiteImageLogDto> findMediaSiteImageLog(List<Long> idList,Date startDate, Date endDate);

	@Query("select ml from com.eikona.tech.domain.MultimediaLog as ml where ml.isDeleted = false and ml.timeStamp between :startDate and :endDate and ml.mediaSite.id =:assetId "
			+ "and (ml.mediaSite.managedByOrgId = :organization or ml.mediaSite.ownedByOrgId = :organization ) order by timeStamp desc ")
	List<MultimediaLog> findImageLog(Long assetId, Date startDate, Date endDate, Organization organization);

	MultimediaLog findByImageUrl(String readUrl);
	
	@Query("select ml.id from com.eikona.tech.domain.MultimediaLog as ml where ml.imageUrl=:readUrl")
	Long findIdByImageUrl(String readUrl);
}
