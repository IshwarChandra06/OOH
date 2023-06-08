package com.eikona.tech.service.impl;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.eikona.tech.domain.Campaign;
import com.eikona.tech.domain.MediaSite;
import com.eikona.tech.domain.MultimediaLog;
import com.eikona.tech.domain.Organization;
import com.eikona.tech.domain.User;
import com.eikona.tech.dto.AssetReportDto;
import com.eikona.tech.dto.CampaignDataDto;
import com.eikona.tech.dto.ImageLogDto;
import com.eikona.tech.dto.MediaSiteImageLogDto;
import com.eikona.tech.repository.CampaignRepository;
import com.eikona.tech.repository.MultimediaLogRepository;
import com.eikona.tech.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class MultimediaLogReportServiceImpl {

	@Autowired
	private CampaignRepository campaignRepository;

	@Autowired
	private MultimediaLogRepository imageLogRepository;

	@Autowired
	private UserRepository userRepository;

//	@Autowired
//	private MediaSiteRepository mediaSiteRepository;

	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	public AssetReportDto findImageLog(Long campaginId, Date startDate, Date endDate, Principal principal) {// Long
																											// organizationId,

		User user = userRepository.findByUserName(principal.getName()).get();
		Organization organization = user.getOrganization();

		AssetReportDto reportDto = new AssetReportDto();
		List<CampaignDataDto> campaignDataDtoList = new ArrayList<>();
		
//		Organization organization = new Organization();
//		organization.setId(organizationId);
//		Optional<Campaign> campaign = campaignRepository.findByIdAndOrganization(campaginId, organization);

		Optional<Campaign> campaign = campaignRepository.findByIdAndOrganization(campaginId, organization);
		List<MediaSite> mediaSiteList = new ArrayList<MediaSite>();
		if (campaign.isPresent()) {
			mediaSiteList = campaign.get().getMediasite();
		}

		int i = 1;
		List<MediaSite> mediaSiteListObj = new ArrayList<MediaSite>();
		for (MediaSite mediaSite : mediaSiteList) {
			mediaSiteListObj.add(mediaSite);
			if (i % 3 == 0) {
				findImageLogData(mediaSiteListObj, startDate, endDate);

				mediaSiteListObj = new ArrayList<MediaSite>();
			}
			i++;

			if (mediaSiteList.size() == i - 1) {
				findImageLogData(mediaSiteListObj, startDate, endDate);
			}
		}
		List<Long> idList = new ArrayList<Long>();
		// List<ImageLog> imageLogList = new ArrayList<ImageLog>();
		for (MediaSite mediaSite : mediaSiteList) {
			// List<ImageLog> imageList = imageLogRepository.findImageLog(mediaSite.getId(),
			// startDate, endDate);
			idList.add(mediaSite.getId());
			// imageLogList.addAll(imageList);
		}

		// Long[] idArray = new Long[idList.size()];
		List<MultimediaLog> imageList = imageLogRepository.findImageLog(idList, startDate, endDate);

		reportDto.setData(imageList);
		return reportDto;
	}

	private List<MediaSiteImageLogDto> findImageLogData(List<MediaSite> mediaSiteList, Date startDate, Date endDate) {

		List<Long> idList = new ArrayList<Long>();
		for (MediaSite mediaSite : mediaSiteList) {
			idList.add(mediaSite.getId());
		}

		List<MediaSiteImageLogDto> mediaSiteImageLogDto = imageLogRepository.findMediaSiteImageLog(idList, startDate,
				endDate);

		return mediaSiteImageLogDto;
	}

	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	public AssetReportDto findImageLog(Long campaginId, Long assetId, Principal principal) {
		User user = userRepository.findByUserName(principal.getName()).get();
		Organization organization = user.getOrganization();

		AssetReportDto reportDto = new AssetReportDto();
		List<CampaignDataDto> campaignDataDtoList = new ArrayList<>();

		Optional<Campaign> campaign = campaignRepository.findByIdAndOrganization(campaginId, organization);
		Date startDate = null;
		Date endDate = null;
		if (campaign.isPresent()) {
			startDate = campaign.get().getStartDate();
			endDate = campaign.get().getEndDate();
		}

		long diff = 0;
		if (null != startDate && null != endDate) {
			diff = endDate.getTime() - startDate.getTime();
		}

		int totalDays = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

		List<MultimediaLog> imageList = imageLogRepository.findImageLog(assetId, startDate, endDate);
		reportDto.setData(imageList);
		reportDto.setTotalDays(totalDays);
		return reportDto;
	}

	@SuppressWarnings({ "rawtypes", "unused", "unchecked" })
	public AssetReportDto findImageLog(Long campaginId, Long assetId, Date startDate, Date endDate) {
		AssetReportDto reportDto = new AssetReportDto();
		List<CampaignDataDto> campaignDataDtoList = new ArrayList<>();

		Optional<Campaign> campaign = campaignRepository.findById(campaginId);

		long diff = 0;
		if (null != startDate && null != endDate) {
			diff = endDate.getTime() - startDate.getTime();
		}

		int totalDays = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

		List<MultimediaLog> imageList = imageLogRepository.findImageLog(assetId, startDate, endDate);
		int count = 0;
		JSONArray json = new JSONArray();

		SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd");
		for (MultimediaLog imageLog : imageList) {

			String date = formate.format(imageLog.getTimeStamp());
			if (!json.contains(date)) {
				json.add(date);
				count++;
			}
		}
		
		List<ImageLogDto> imageLogDtoList = new ArrayList<ImageLogDto>();
		
		for (MultimediaLog imageLog : imageList) {
			ImageLogDto imageLogDto = new ImageLogDto();
			
			try {
				
//				String base64 = imageLog.getImageUrl();
//				
//				if(null != base64 && !base64.isEmpty()) {
//					String imageUrl = imageLog.getOriginalPath();
//					InputStream inputStream = new FileInputStream(imageUrl);
//				
//					byte[] bytes = IOUtils.toByteArray(inputStream);
//					base64 =Base64.encodeBase64String(bytes);
//				}
//				imageLogDto.setImage(base64);
				imageLogDto.setMediaSite(imageLog.getMediaSite());
//				imageLogDto.setImageUrl(imageLog.getImageUrl());
				imageLogDto.setTimeStamp(imageLog.getTimeStamp());
				imageLogDto.setUser(imageLog.getUser());
				imageLogDto.setThumbnailUrl(imageLog.getThumbnailPath());
				imageLogDto.setOriginalUrl(imageLog.getOriginalPath());
				imageLogDto.setVideoFirstFramePath(imageLog.getVideoFirstFramePath());
				imageLogDto.setVideoUrl(imageLog.getVideoUrl());
				
				imageLogDtoList.add(imageLogDto);
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}

		reportDto.setData(imageLogDtoList);
		reportDto.setOccupiedDays(count);
		reportDto.setTotalDays(totalDays + 1);
		reportDto.setEmptyDays(totalDays - count + 1);
		return reportDto;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public AssetReportDto findImagelog(Long assetId, Date startDate, Date endDate, Principal principal) {

		User user = userRepository.findByUserName(principal.getName()).get();
		Organization organization = user.getOrganization();

		AssetReportDto reportDto = new AssetReportDto();
		List<MultimediaLog> imageLogList = imageLogRepository.findImageLog(assetId, startDate, endDate, organization);
		int count = 0;
		JSONArray json = new JSONArray();

		SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd");
		for (MultimediaLog imageLog : imageLogList) {

			String date = formate.format(imageLog.getTimeStamp());
			if (!json.contains(date)) {
				json.add(date);
				count++;
			}
		}
		
		List<ImageLogDto> imageLogDtoList = new ArrayList<ImageLogDto>();
		for (MultimediaLog imageLog : imageLogList) {
			ImageLogDto imageLogDto = new ImageLogDto();
			
			try {
				
//				String base64 = imageLog.getImageUrl();
				
//				if(null != base64 && !base64.isEmpty()) {
//					String imageUrl = imageLog.getOriginalPath();
//					InputStream inputStream = new FileInputStream(imageUrl);
//				
//					byte[] bytes = IOUtils.toByteArray(inputStream);
//					base64 =Base64.encodeBase64String(bytes);
//				}
//				imageLogDto.setImage(base64);
				imageLogDto.setMediaSite(imageLog.getMediaSite());
//				imageLogDto.setImageUrl(imageLog.getImageUrl());
				imageLogDto.setTimeStamp(imageLog.getTimeStamp());
				imageLogDto.setUser(imageLog.getUser());
				imageLogDto.setThumbnailUrl(imageLog.getThumbnailPath());
				imageLogDto.setOriginalUrl(imageLog.getOriginalPath());
				imageLogDto.setVideoFirstFramePath(imageLog.getVideoFirstFramePath());
				imageLogDto.setVideoUrl(imageLog.getVideoUrl());
				
				imageLogDtoList.add(imageLogDto);
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		
		reportDto.setData(imageLogDtoList);
		reportDto.setOccupiedDays(count);
		return reportDto;
	}



	@SuppressWarnings({ "unchecked" })
	public Page<MultimediaLog> findPaginatedCampaignAndDate(int pageNo, int pageSize, Object searchData) {
		Page<MultimediaLog> pages = null;

		try {
			Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

			ObjectMapper oMapper = new ObjectMapper();
			Map<String, String> map = oMapper.convertValue(searchData, Map.class);

			if (!(map.isEmpty())) {

				String dateStr = String.valueOf(map.get("date"));
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				Date date = null;
				try {
					date = format.parse(dateStr);
				} catch (ParseException e) {
					e.printStackTrace();
				}

				Calendar startCalendar = Calendar.getInstance();
				startCalendar.setTime(date);
				startCalendar.set(Calendar.HOUR, 0);
				startCalendar.set(Calendar.MINUTE, 0);
				startCalendar.set(Calendar.SECOND, 0);

				Calendar endCalendar = Calendar.getInstance();
				endCalendar.setTime(date);
				endCalendar.set(Calendar.HOUR, 23);
				endCalendar.set(Calendar.MINUTE, 59);
				endCalendar.set(Calendar.SECOND, 59);

				Date startDate = startCalendar.getTime();
				Date endDate = endCalendar.getTime();

				String campaginIdStr = String.valueOf(map.get("campaginId"));
				Long campaginId = Long.parseLong(campaginIdStr);

				Optional<Campaign> campaign = campaignRepository.findById(campaginId);

				List<MediaSite> mediaSiteList = new ArrayList<MediaSite>();
				if (campaign.isPresent()) {
					mediaSiteList = campaign.get().getMediasite();
				}

				Specification<MultimediaLog> isDeleted = (root, query, cb) -> {
					query.orderBy(cb.desc(root.get("mediaSite")));
					return cb.equal(root.get("isDeleted"), false);
				};
				
				Specification<MultimediaLog> dateSpc = (root, query, cb) -> {
					return cb.between(root.get("timeStamp"), startDate, endDate);
				};

				Specification<MultimediaLog> mediaSpc = (root, query, cb) -> {
					return cb.equal(root.get("id"), 0l);
				};
				for (MediaSite mediaSite : mediaSiteList) {
					Specification<MultimediaLog> mediaSpc1 = (Specification<MultimediaLog>) generatedSpecification(mediaSite);
					mediaSpc = mediaSpc.or(mediaSpc1);
				}
				
				pages = imageLogRepository.findAll(isDeleted.and(dateSpc).and(mediaSpc), pageable);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return pages;
	}
	
	private Specification<MultimediaLog> generatedSpecification(MediaSite mediaSite) {

		return (root, query, cb) -> {
			return cb.equal(root.get("mediaSite"), mediaSite);
		};
	}

	@SuppressWarnings({ "unchecked" })
	public Page<MultimediaLog> findPaginatedCampagianAndAsset(int pageNo, int pageSize, Object searchData) {

		ObjectMapper oMapper = new ObjectMapper();
		Map<String, Integer> map = oMapper.convertValue(searchData, Map.class);

		Long campaginId = 0l;

		Specification<MultimediaLog> assetSpc = null;
		if (!(map.isEmpty())) {

			campaginId = new Long(map.get("campaginId"));
			Long assetId = new Long(map.get("assetId"));
			assetSpc = (root, query, cb) -> {
				return cb.equal(root.get("id"), assetId);
			};
		}

		Optional<Campaign> campaign = campaignRepository.findById(campaginId);

		Specification<MultimediaLog> dateSpc = null;
		if (campaign.isPresent()) {
			Date startDate = campaign.get().getStartDate();
			Date endDate = campaign.get().getEndDate();
			dateSpc = (root, query, cb) -> {
				return cb.between(root.get("timeStamp"), startDate, endDate);
			};
		}

		Specification<MultimediaLog> isDeleted = (root, query, cb) -> {
			return cb.equal(root.get("isDeleted"), false);
		};

		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

		return this.imageLogRepository.findAll(isDeleted.and(assetSpc).and(dateSpc), pageable);
	}

	@SuppressWarnings("unchecked")
	public Page<MultimediaLog> findPaginatedAssetAndMonth(int pageNo, int pageSize, Object searchData) {

		ObjectMapper oMapper = new ObjectMapper();
		Map<String, String> map = oMapper.convertValue(searchData, Map.class);

		Specification<MultimediaLog> assetSpc = null;
		Specification<MultimediaLog> dateSpc = null;
		if (!(map.isEmpty())) {
			String assetIdStr = String.valueOf(map.get("assetId"));
			Long assetId = Long.parseLong(assetIdStr);

			assetSpc = (root, query, cb) -> {
				return cb.equal(root.get("id"), assetId);
			};

			String dateStr = String.valueOf(map.get("date"));
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
			Date date = null;
			try {
				date = format.parse(dateStr);
			} catch (ParseException e) {
				e.printStackTrace();
			}

			Calendar startCalendar = Calendar.getInstance();
			startCalendar.setTime(date);
			startCalendar.set(Calendar.HOUR, 0);
			startCalendar.set(Calendar.MINUTE, 0);
			startCalendar.set(Calendar.SECOND, 0);

			Calendar endCalendar = Calendar.getInstance();
			endCalendar.setTime(date);
			endCalendar.set(Calendar.HOUR, 23);
			endCalendar.set(Calendar.MINUTE, 59);
			endCalendar.set(Calendar.SECOND, 59);

			int daysInMonth = endCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
			endCalendar.set(Calendar.DAY_OF_MONTH, daysInMonth);

			dateSpc = (root, query, cb) -> {
				return cb.between(root.get("timeStamp"), startCalendar.getTime(), endCalendar.getTime());
			};

		}

		Specification<MultimediaLog> isDeleted = (root, query, cb) -> {
			return cb.equal(root.get("isDeleted"), false);
		};

		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

		return this.imageLogRepository.findAll(isDeleted.and(assetSpc).and(dateSpc), pageable);
	}

	@SuppressWarnings({ "unchecked", "unused" })
	public Page<MultimediaLog> findPaginatedCampagianAndAsset(int pageNo, int pageSize, Object searchData, Date sDate,
			Date eDate) {

		ObjectMapper oMapper = new ObjectMapper();
		Map<String, Integer> map = oMapper.convertValue(searchData, Map.class);

		Long campaginId = 0l;

		Specification<MultimediaLog> assetSpc = null;
		if (!(map.isEmpty())) {

			campaginId = new Long(map.get("campaginId"));
			Long assetId = new Long(map.get("assetId"));
			assetSpc = (root, query, cb) -> {
				return cb.equal(root.get("id"), assetId);
			};
		}

		Specification<MultimediaLog> isDeleted = (root, query, cb) -> {
			return cb.equal(root.get("isDeleted"), false);
		};

		Specification<MultimediaLog> dateSpc = (root, query, cb) -> {
			return cb.between(root.get("timeStamp"), sDate, eDate);
		};

		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

		return this.imageLogRepository.findAll(isDeleted.and(assetSpc).and(dateSpc), pageable);
	}

}
