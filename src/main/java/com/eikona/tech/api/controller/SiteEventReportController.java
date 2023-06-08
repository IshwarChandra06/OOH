package com.eikona.tech.api.controller;

import java.io.File;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.eikona.tech.domain.MultimediaLog;
import com.eikona.tech.domain.MediaSite;
import com.eikona.tech.dto.AssetReportDto;
import com.eikona.tech.dto.ImageLogDto;
import com.eikona.tech.dto.PaginatedDto;
import com.eikona.tech.dto.SearchRequestDto;
import com.eikona.tech.service.MediaSiteService;
import com.eikona.tech.service.impl.CampaignReportServiceImpl;
import com.eikona.tech.service.impl.MultimediaLogReportServiceImpl;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/eventsreport")
public class SiteEventReportController {

	@Autowired
	private MultimediaLogReportServiceImpl reportServiceImpl;
	
	@Autowired
	private CampaignReportServiceImpl campaignReportServiceImpl;
	
	@Autowired
	private MediaSiteService mediaSiteService;

	// @Hidden
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/bycampaignanddate", method = RequestMethod.GET)
	@PreAuthorize("hasAuthority('campaignreport_view')")
	@Operation(summary = "campaign daily report , Privileges Required: campaignreport_view")
	public @ResponseBody ResponseEntity<Object> campaginDate(@RequestParam Long campaginId, // @RequestParam Long
																							// organizationId,
			@RequestParam String datestr, Principal principal) {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date date = format.parse(datestr);

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

			AssetReportDto reportDto = reportServiceImpl.findImageLog(campaginId, startCalendar.getTime(),
					endCalendar.getTime(), principal);

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
			reportDto.setTotalDays(daysInMonth);// AssetReportDto
			return new ResponseEntity<>(reportDto, HttpStatus.OK);

		} catch (Exception e) {
			System.out.println(e);
			return new ResponseEntity<>("Contact Admin!!", HttpStatus.BAD_REQUEST);
		}
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/byassetanddate", method = RequestMethod.GET)
	@PreAuthorize("hasAuthority('mediareport_view')")
	@Operation(summary = "Media daily report , Privileges Required: mediareport_view")
	public @ResponseBody ResponseEntity<Object> assetDate(@RequestParam Long assetId, @RequestParam String datestr,
			Principal principal) {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date date = format.parse(datestr);

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

			AssetReportDto reportDto = reportServiceImpl.findImagelog(assetId, startCalendar.getTime(),
					endCalendar.getTime(), principal);

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
//			int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
//			reportDto.setTotalDays(daysInMonth);// AssetReportDto
			return new ResponseEntity<>(reportDto, HttpStatus.OK);

		} catch (Exception e) {
			System.out.println(e);
			return new ResponseEntity<>("Contact Admin!!", HttpStatus.BAD_REQUEST);
		}
	}

	@SuppressWarnings("rawtypes")
	@PreAuthorize("hasAuthority('mediareport_view')")
	@Operation(summary = "Media Monthly Report Split By Date , Privileges Required: mediareport_view")
	@RequestMapping(value = "/byassetanddaterange", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<Object> assetDateRange(@RequestParam Long assetId,
			@RequestParam String startDateStr, @RequestParam String endDateStr, Principal principal) {

		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

			Calendar startCalendar = Calendar.getInstance();
			startCalendar.setTime(format.parse(startDateStr));
			startCalendar.set(Calendar.HOUR, 0);
			startCalendar.set(Calendar.MINUTE, 0);
			startCalendar.set(Calendar.SECOND, 0);

			Calendar endCalendar = Calendar.getInstance();
			endCalendar.setTime(format.parse(endDateStr));
			endCalendar.set(Calendar.HOUR, 23);
			endCalendar.set(Calendar.MINUTE, 59);
			endCalendar.set(Calendar.SECOND, 59);

			AssetReportDto reportDto = reportServiceImpl.findImagelog(assetId, startCalendar.getTime(),
					endCalendar.getTime(), principal);

			long diff = endCalendar.getTime().getTime() - startCalendar.getTime().getTime();
			int totalDays = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + 1;
			reportDto.setTotalDays(totalDays);
			reportDto.setEmptyDays(totalDays - reportDto.getOccupiedDays());
			return new ResponseEntity<>(reportDto, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>("Contact Admin!!", HttpStatus.BAD_REQUEST);
		}
	}

	// @Hidden
	@SuppressWarnings("rawtypes")
	@PreAuthorize("hasAuthority('campaignreport_view')")
	@RequestMapping(value = "/bycampaginandasset", method = RequestMethod.GET)
	@Operation(summary = "campaign media report , Privileges Required: campaignreport_view")
	public @ResponseBody ResponseEntity<Object> campaginAsset(@RequestParam Long campaginId, @RequestParam Long assetId,
			Principal principal) {
		try {
			AssetReportDto reportDto = reportServiceImpl.findImageLog(campaginId, assetId, principal);

			return new ResponseEntity<>(reportDto, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>("Contact Admin!!", HttpStatus.BAD_REQUEST);
		}
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/bycampaginandassetanddaterange", method = RequestMethod.GET)
	@PreAuthorize("hasAuthority('mediareport_view')")
	@Operation(summary = "campaign media report split by date , Privileges Required: mediareport_view")
	public @ResponseBody ResponseEntity<Object> campaginAsset(@RequestParam Long campaginId, @RequestParam Long assetId,
			@RequestParam String startDateStr, @RequestParam String endDateStr) {
		try {

			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

			Calendar startCalendar = Calendar.getInstance();
			startCalendar.setTime(format.parse(startDateStr));
			startCalendar.set(Calendar.HOUR, 0);
			startCalendar.set(Calendar.MINUTE, 0);
			startCalendar.set(Calendar.SECOND, 0);

			Calendar endCalendar = Calendar.getInstance();
			endCalendar.setTime(format.parse(endDateStr));
			endCalendar.set(Calendar.HOUR, 23);
			endCalendar.set(Calendar.MINUTE, 59);
			endCalendar.set(Calendar.SECOND, 59);

			AssetReportDto reportDto = reportServiceImpl.findImageLog(campaginId, assetId, startCalendar.getTime(),
					endCalendar.getTime());
			return new ResponseEntity<>(reportDto, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>("Contact Admin!!", HttpStatus.BAD_REQUEST);
		}
	}

	@SuppressWarnings("rawtypes")
	@Operation(summary = "Media Monthly Report , Privileges Required: mediareport_view")
	@PreAuthorize("hasAuthority('mediareport_view')")
	@RequestMapping(value = "/byassetandmonth", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<Object> assetDateImageLog(@RequestParam Long assetId,
			@RequestParam String month, HttpServletResponse response, Principal principal) {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
			Date sDate = format.parse(month);

			Calendar startCalendar = Calendar.getInstance();
			startCalendar.setTime(sDate);
			startCalendar.set(Calendar.DAY_OF_MONTH, 1);
			startCalendar.set(Calendar.HOUR, 0);
			startCalendar.set(Calendar.MINUTE, 0);
			startCalendar.set(Calendar.SECOND, 0);

			Calendar endCalendar = Calendar.getInstance();
			endCalendar.setTime(sDate);
			endCalendar.set(Calendar.HOUR, 23);
			endCalendar.set(Calendar.MINUTE, 59);
			endCalendar.set(Calendar.SECOND, 59);

			int daysInMonth = endCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
			endCalendar.set(Calendar.DAY_OF_MONTH, daysInMonth);

			AssetReportDto reportDto = reportServiceImpl.findImagelog(assetId, startCalendar.getTime(),
					endCalendar.getTime(), principal);

			reportDto.setTotalDays(daysInMonth);
			reportDto.setEmptyDays(daysInMonth - reportDto.getOccupiedDays());
			return new ResponseEntity<>(reportDto, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>("Contact Admin!!", HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/bycampaignanddate", method = RequestMethod.POST)
	@PreAuthorize("hasAuthority('campaignreport_view')")
	@Operation(summary = "campaign daily report pagination ,Privileges Required: campaignreport_view")
	public PaginatedDto<Object> findPaginatedCampaignAndDate(@RequestBody SearchRequestDto paginatedDto) {
		PaginatedDto<Object> paginatedDtoList = null;
		List<Object> objectList = new ArrayList<>();
		try {

			if (0 == paginatedDto.getPageNo() || 0 == paginatedDto.getPageSize()) {
				return new PaginatedDto<Object>(objectList, 0, 0, 0, 0, 0,
						"pageNo or pageSize should be greater than 0", "I");
			}

			
			Page<MultimediaLog> page = reportServiceImpl.findPaginatedCampaignAndDate(paginatedDto.getPageNo(),
					paginatedDto.getPageSize(), paginatedDto.getSearchData());
			
			for (MultimediaLog imageLog : page.getContent()) {
				ImageLogDto imageLogDto = new ImageLogDto();
				
				try {
					imageLogDto.setMediaSite(imageLog.getMediaSite());
					imageLogDto.setImageUrl(imageLog.getImageUrl());
					imageLogDto.setTimeStamp(imageLog.getTimeStamp());
					imageLogDto.setUser(imageLog.getUser());
					imageLogDto.setThumbnailUrl(imageLog.getThumbnailPath());
					imageLogDto.setOriginalUrl(imageLog.getOriginalPath());
					imageLogDto.setVideoFirstFramePath(imageLog.getVideoFirstFramePath());
					imageLogDto.setVideoUrl(imageLog.getVideoUrl());
					
					objectList.add(imageLogDto);
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
			
			paginatedDtoList = new PaginatedDto<Object>(objectList, page.getTotalPages(), page.getNumber() + 1,
					page.getSize(), page.getTotalElements(), page.getTotalElements(), "success", "S");

		} catch (Exception e) {
			System.out.println(e);
			return new PaginatedDto<Object>(objectList, 0, 0, 0, 0, 0, "Failed", "E");
		}
		return paginatedDtoList;
	}
	@GetMapping(value = "/imagesrc", produces = "image/jpg")
	@ResponseBody
	public FileSystemResource imageSource(@RequestParam(value="id") String path) {
		
		String name = path+".jpg";
		System.out.println();
	    return new FileSystemResource(new File(name));
	}
	
	@RequestMapping(value = "/bycampaignanddate/stats", method = RequestMethod.POST)
	@PreAuthorize("hasAuthority('campaignreport_view')")
	@Operation(summary = "campaign daily report pagination ,Privileges Required: campaignreport_view")
	public PaginatedDto<Object> findPaginatedCampaignAndDateStats(@RequestBody SearchRequestDto paginatedDto) {
		PaginatedDto<Object> paginatedDtoList = null;
		List<Object> objectList = new ArrayList<>();
		try {

			if (0 == paginatedDto.getPageNo() || 0 == paginatedDto.getPageSize()) {
				return new PaginatedDto<Object>(objectList, 0, 0, 0, 0, 0,
						"pageNo or pageSize should be greater than 0", "I");
			}

			Page<MultimediaLog> page = reportServiceImpl.findPaginatedCampaignAndDate(paginatedDto.getPageNo(),
					paginatedDto.getPageSize(), paginatedDto.getSearchData());
			
			paginatedDtoList = new PaginatedDto<Object>(objectList, page.getTotalPages(), page.getNumber() + 1,
					page.getSize(), 0, page.getTotalElements(), "success", "S");

		} catch (Exception e) {
			System.out.println(e);
			return new PaginatedDto<Object>(objectList, 0, 0, 0, 0, 0, "Failed", "E");
		}
		return paginatedDtoList;
	}

	// ready
	@RequestMapping(value = "/bycampaginandasset/stats", method = RequestMethod.POST)
	@Hidden
	public PaginatedDto<Object> findPaginatedCampagianAndAsset(@RequestBody SearchRequestDto paginatedDto) {
		PaginatedDto<Object> paginatedDtoList = null;
		List<Object> objectList = new ArrayList<>();
		try {

			if (0 == paginatedDto.getPageNo() || 0 == paginatedDto.getPageSize()) {
				return new PaginatedDto<Object>(objectList, 0, 0, 0, 0, 0,
						"pageNo or pageSize should be greater than 0", "I");
			}

			Page<MultimediaLog> page = reportServiceImpl.findPaginatedCampagianAndAsset(paginatedDto.getPageNo(),
					paginatedDto.getPageSize(), paginatedDto.getSearchData());

			paginatedDtoList = new PaginatedDto<Object>(objectList, page.getTotalPages(), page.getNumber() + 1,
					page.getSize(), 0, page.getTotalElements(), "success", "S");

		} catch (Exception e) {

			System.out.println(e);
			return new PaginatedDto<Object>(objectList, 0, 0, 0, 0, 0, "Failed", "E");
		}
		return paginatedDtoList;
	}

	@RequestMapping(value = "/bycampaginandassetanddate/stats", method = RequestMethod.POST)
	@Hidden
	public PaginatedDto<Object> findPaginatedCampagianAndAssetAndDate(@RequestBody SearchRequestDto paginatedDto) {
		PaginatedDto<Object> paginatedDtoList = null;
		List<Object> objectList = new ArrayList<>();
		try {

			if (0 == paginatedDto.getPageNo() || 0 == paginatedDto.getPageSize()) {
				return new PaginatedDto<Object>(objectList, 0, 0, 0, 0, 0,
						"pageNo or pageSize should be greater than 0", "I");
			}

			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date sDate = format.parse(paginatedDto.getsDate());
			Date eDate = format.parse(paginatedDto.geteDate());

			Calendar startCalendar = Calendar.getInstance();
			startCalendar.setTime(sDate);
			startCalendar.set(Calendar.DAY_OF_MONTH, 1);
			startCalendar.set(Calendar.HOUR, 0);
			startCalendar.set(Calendar.MINUTE, 0);
			startCalendar.set(Calendar.SECOND, 0);

			Calendar endCalendar = Calendar.getInstance();
			endCalendar.setTime(eDate);
			endCalendar.set(Calendar.HOUR, 23);
			endCalendar.set(Calendar.MINUTE, 59);
			endCalendar.set(Calendar.SECOND, 59);
			Page<MultimediaLog> page = reportServiceImpl.findPaginatedCampagianAndAsset(paginatedDto.getPageNo(),
					paginatedDto.getPageSize(), paginatedDto.getSearchData(), startCalendar.getTime(),
					endCalendar.getTime());

			paginatedDtoList = new PaginatedDto<Object>(objectList, page.getTotalPages(), page.getNumber() + 1,
					page.getSize(), 0, page.getTotalElements(), "success", "S");

		} catch (Exception e) {

			System.out.println(e);
			return new PaginatedDto<Object>(objectList, 0, 0, 0, 0, 0, "Failed", "E");
		}
		return paginatedDtoList;
	}

	@RequestMapping(value = "/byassetandmonth/stats", method = RequestMethod.POST)
	@Hidden
	public PaginatedDto<Object> findPaginatedAssetAndMonth(@RequestBody SearchRequestDto paginatedDto) {
		PaginatedDto<Object> paginatedDtoList = null;
		List<Object> objectList = new ArrayList<>();
		try {

			if (0 == paginatedDto.getPageNo() || 0 == paginatedDto.getPageSize()) {
				return new PaginatedDto<Object>(objectList, 0, 0, 0, 0, 0,
						"pageNo or pageSize should be greater than 0", "I");
			}

			Page<MultimediaLog> page = reportServiceImpl.findPaginatedAssetAndMonth(paginatedDto.getPageNo(),
					paginatedDto.getPageSize(), paginatedDto.getSearchData());

			paginatedDtoList = new PaginatedDto<Object>(objectList, page.getTotalPages(), page.getNumber() + 1,
					page.getSize(), 0, page.getTotalElements(), "success", "S");

		} catch (Exception e) {
			System.out.println(e);
			return new PaginatedDto<Object>(objectList, 0, 0, 0, 0, 0, "Failed", "E");
		}
		return paginatedDtoList;
	}
	
	@RequestMapping(value = "/assetsbycampaginid", method = RequestMethod.POST)
	public PaginatedDto<Object> findAssetByCampagin(@RequestBody SearchRequestDto paginatedDto,  Principal principal) {
		PaginatedDto<Object> paginatedDtoList = null;
		List<Object> objectList = new ArrayList<>();
		try {

			if (0 == paginatedDto.getPageNo() || 0 == paginatedDto.getPageSize()) {
				return new PaginatedDto<Object>(objectList, 0, 0, 0, 0, 0,
						"pageNo or pageSize should be greater than 0", "I");
			}

			Page<MediaSite> page = campaignReportServiceImpl.findAssetByCampagin(paginatedDto.getPageNo(),
					paginatedDto.getPageSize(), paginatedDto, principal);

			for(MediaSite mediaSite : page.getContent()) {
				objectList.add(mediaSite);
			}
			
			List<MediaSite> totalList = mediaSiteService.findAll();
			
			paginatedDtoList = new PaginatedDto<Object>(objectList, page.getTotalPages(), page.getNumber() + 1,
					page.getSize(),  page.getTotalElements(),totalList.size(), "success", "S");

		} catch (Exception e) {
			System.out.println(e);
			return new PaginatedDto<Object>(objectList, 0, 0, 0, 0, 0, "Failed", "E");
		}
		return paginatedDtoList;
	}
}
