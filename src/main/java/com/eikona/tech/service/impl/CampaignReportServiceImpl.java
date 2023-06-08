package com.eikona.tech.service.impl;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.eikona.tech.domain.Campaign;
import com.eikona.tech.domain.MultimediaLog;
import com.eikona.tech.domain.MediaSite;
import com.eikona.tech.dto.AssetReportDto;
import com.eikona.tech.dto.CampaignDataDto;
import com.eikona.tech.dto.SearchRequestDto;
import com.eikona.tech.repository.CampaignRepository;
import com.eikona.tech.repository.MultimediaLogRepository;
import com.eikona.tech.repository.MediaSiteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class CampaignReportServiceImpl {
	
	@Autowired
	private CampaignRepository campaignRepository;
	
	@Autowired
	private MultimediaLogRepository imageLogRepository;
	
	@Autowired
	private MediaSiteRepository mediaSiteRepository;
	
	@Autowired
	private MediaSiteServiceImpl mediaSiteServiceImpl;
	
	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	public AssetReportDto findCampaign(Long assetId, Date startDate, Date endDate) {
		AssetReportDto reportDto = new AssetReportDto();
		List<CampaignDataDto>  campaignDataDtoList = new ArrayList<>();
		
		List<Campaign> campaignList = campaignRepository.findCampaign(assetId);
		List<MultimediaLog> imageLogList = imageLogRepository.findImageLog(assetId, startDate, endDate);// 
		int count = 0;
		for(MultimediaLog imageLog : imageLogList) {
			count++;
		}
		for(Campaign campaign : campaignList) {
			CampaignDataDto campaignDataDto = new CampaignDataDto();
			
			campaignDataDto.setName(campaign.getName());
			campaignDataDto.setStartDate(campaign.getStartDate());
			campaignDataDto.setEndDate(campaign.getEndDate());
			
			campaignDataDtoList.add(campaignDataDto);
		}
		
		reportDto.setData(campaignDataDtoList);
		reportDto.setOccupiedDays(count);
		return reportDto;
	}

	@SuppressWarnings("unchecked")
	public Page<MediaSite> findAssetByCampagin(int pageNo, int pageSize, SearchRequestDto searchData, Principal principal) {

		ObjectMapper oMapper = new ObjectMapper();
		Map<String, String> map = oMapper.convertValue(searchData.getSearchData(), Map.class);
		
		Map<String, String> mapObj = new HashMap<String, String>();
		for(String key : map.keySet()) {
			if(!("campaignId".equalsIgnoreCase(key))) {
				mapObj.put(key, map.get(key));
			}
		}
		Specification<MediaSite> mediaSiteFieldSpec = mediaSiteServiceImpl.mediaSpecification(mapObj, searchData.getOrgId(), principal);
		
		Specification<MediaSite> isDeleted = (root, query, cb) -> {
			return cb.equal(root.get("isDeleted"), false);
		};

		Long campaignId = Long.parseLong(map.get("campaignId"));
		Campaign campaign = campaignRepository.findById(campaignId).get();

		List<MediaSite> mediaSiteList = campaign.getMediasite();

		List<Long> idList = new ArrayList<Long>();
		for (MediaSite mediaSite : mediaSiteList) {
			idList.add(mediaSite.getId());
		}

		Specification<MediaSite> mediaSiteSpec = (root, query, cb) -> {
			if (idList == null || idList.isEmpty()) {
				return cb.conjunction();
			}

			return root.get("id").in(idList);
		};

		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

		Page<MediaSite> page = mediaSiteRepository.findAll(isDeleted.and(mediaSiteSpec).and(mediaSiteFieldSpec), pageable);

		return page;
	}
}
