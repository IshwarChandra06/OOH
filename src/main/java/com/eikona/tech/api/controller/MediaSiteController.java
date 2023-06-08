package com.eikona.tech.api.controller;

import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.eikona.tech.domain.ConstraintRange;
import com.eikona.tech.domain.ConstraintSingle;
import com.eikona.tech.domain.MediaSite;
import com.eikona.tech.domain.Organization;
import com.eikona.tech.domain.User;
import com.eikona.tech.dto.MediaSiteDto;
import com.eikona.tech.dto.MediaSiteImportDto;
import com.eikona.tech.dto.PaginatedDto;
import com.eikona.tech.dto.SearchRequestDto;
import com.eikona.tech.repository.MediaSiteRepository;
import com.eikona.tech.repository.UserRepository;
import com.eikona.tech.service.ConstraintRangeService;
import com.eikona.tech.service.ConstraintSingleService;
import com.eikona.tech.service.MediaSiteService;
import com.eikona.tech.service.MultimediaLogService;
import com.eikona.tech.service.OrganizationService;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/mediasite")
public class MediaSiteController {

	@Autowired
	private MediaSiteService mediaSiteService;

	@Autowired
	private OrganizationService organizationService;

	@Autowired
	private ConstraintRangeService constraintRangeService;

	@Autowired
	private ConstraintSingleService constraintSingleService;

	@Autowired
	private MediaSiteRepository mediaSiteRepository;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private MultimediaLogService multimediaLogService;

	@Hidden
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public ResponseEntity<Object> list(Model model) {
		List<MediaSite> mediaSiteList = mediaSiteService.findAll();
		return new ResponseEntity<>(mediaSiteList, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public ResponseEntity<Object> test(Model model) {
		multimediaLogService.multimediaProcessingFromDirectory();
		List<MediaSite> mediaSiteList = new ArrayList<>();
		return new ResponseEntity<>(mediaSiteList, HttpStatus.OK);
	}

	@Hidden
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/get-options", method = RequestMethod.GET)
	public <T> ResponseEntity<Object> newPage() {
		List<T> list = new ArrayList<>();
		List<Organization> organizationList = organizationService.findAll();
		List<ConstraintSingle> constraintSingleList = constraintSingleService.findAll();
		List<ConstraintRange> constraintRangeList = constraintRangeService.findAll();

		list.addAll((Collection<? extends T>) organizationList);
		list.addAll((Collection<? extends T>) constraintSingleList);
		list.addAll((Collection<? extends T>) constraintRangeList);

		return new ResponseEntity<>(list, HttpStatus.OK);
	}

	@Operation(summary = "Privileges Required: mediasite_add")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@PreAuthorize("hasAuthority('mediasite_add')")
	public ResponseEntity<Object> create(@RequestBody MediaSite mediaSite, Principal principal) {
		try {
			Optional<User> userObj = userRepository.findByUserName(principal.getName());
			Organization org = userObj.get().getOrganization();
			mediaSite.setCreateByOrganization(org);

			if (null == mediaSite.getAssetCode() || mediaSite.getAssetCode().isEmpty()) {
				String message = "Asset Code should not be empty.";
				return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
			}
			if (null == mediaSite.getLocality() || mediaSite.getLocality().isEmpty()) {
				String message = "Locality should not be empty.";
				return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
			}
			if (null == mediaSite.getCity() || mediaSite.getCity().isEmpty()) {
				String message = "City should not be empty.";
				return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
			}
			if (null == mediaSite.getState() || mediaSite.getState().isEmpty()) {
				String message = "State should not be empty.";
				return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
			}
			if (null == mediaSite.getPincode() || mediaSite.getPincode().isEmpty()) {
				String message = "Pincode should not be empty.";
				return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
			}
//			if (null == mediaSite.getSiteName() || mediaSite.getSiteName().isEmpty()) {
//				String message = "Site Name should not be empty.";
//				return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
//			}
//			if ((null == mediaSite.getLatitude() || mediaSite.getLatitude().isEmpty())&&(null == mediaSite.getLongitude() || mediaSite.getLongitude().isEmpty())) {
//				String message = "Longitude and Latitude should not be empty.";
//				return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
//			}
			
			MediaSite createdMediaSite = mediaSiteService.save(mediaSite);
			return new ResponseEntity<>(createdMediaSite, HttpStatus.OK);
		} catch (Exception e) {
			String message = "Contact Admin!!";
			return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
		}
	}

	@Operation(summary = "Privileges Required: mediasite_view")
	@RequestMapping(value = "/id/{id}", method = RequestMethod.GET)
	@PreAuthorize("hasAuthority('mediasite_view')")
	public MediaSite get(@PathVariable long id) {
		return mediaSiteService.find(id);
	}

	@Operation(summary = "Privileges Required: mediasite_update")
	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
	@PreAuthorize("hasAuthority('mediasite_update')")
	public ResponseEntity<Object> update(@PathVariable long id, @RequestBody MediaSite mediaSite) {
		try {
			if (null == mediaSite.getId()) {
				return new ResponseEntity<>("Id should not be empty", HttpStatus.BAD_REQUEST);
			}
			MediaSite auditMediaSite = mediaSiteService.find(id);
			mediaSite.setCreateByOrganization(auditMediaSite.getCreateByOrganization());
			mediaSite.setCreatedBy(auditMediaSite.getCreatedBy());
			mediaSite.setCreatedDate(auditMediaSite.getCreatedDate());
			MediaSite updatedMediaSite = mediaSiteService.save(mediaSite);
			return new ResponseEntity<>(updatedMediaSite, HttpStatus.OK);
		} catch (Exception e) {
			String message = "Contact Admin!!";
			return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	@Operation(summary = "Privileges Required: mediasite_delete")
	@PreAuthorize("hasAuthority('mediasite_delete')")
	public ResponseEntity<Object> delete(@PathVariable(value = "id") long id) {
		try {
			mediaSiteService.delete(id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			String message = "Contact Admin!!";
			return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/createten", method = RequestMethod.POST)
	@Operation(summary = "Privileges Required: mediasite_add")
	@PreAuthorize("hasAuthority('mediasite_add')")
	public ResponseEntity<Object> add(@RequestBody List<MediaSite> mediasite) {
		try {
			if (null == mediasite || mediasite.isEmpty()) {
				String message = "Asset Code should not be empty.";
				return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
			}
			List<MediaSite> createdMediaSite = (List<MediaSite>) mediaSiteRepository.saveAll(mediasite);
			return new ResponseEntity<>(createdMediaSite, HttpStatus.OK);
		} catch (Exception e) {
			String message = "Contact Admin!!";
			return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/stats", method = RequestMethod.POST)
	@Operation(summary = "Privileges Required: mediasite_view")
	@PreAuthorize("hasAuthority('mediasite_view')")
	public PaginatedDto<MediaSiteDto> findPaginated(@RequestBody SearchRequestDto paginatedDto, Principal principal) {
		PaginatedDto<MediaSiteDto> mediaSiteDtoPageList = null;
		List<MediaSiteDto> mediaSiteDtoList = new ArrayList<>();
		try {
			if (0 == paginatedDto.getPageNo() || 0 == paginatedDto.getPageSize()) {
				return new PaginatedDto<MediaSiteDto>(mediaSiteDtoList, 0, 0, 0, 0, 0,
						"pageNo or pageSize should be greater than 0", "I");
			}
			Page<MediaSite> page = mediaSiteService.searchByField(paginatedDto.getPageNo(), paginatedDto.getPageSize(),
					paginatedDto.getSortField(), paginatedDto.getSortOrder(), paginatedDto, principal);
					
			mediaSiteDtoPageList = new PaginatedDto<MediaSiteDto>(mediaSiteDtoList, page.getTotalPages(),
					page.getNumber() + 1, page.getSize(), page.getTotalElements(), page.getTotalElements(), "Success", "S");
		} catch (Exception e) {
			return new PaginatedDto<MediaSiteDto>(mediaSiteDtoList, 0, 0, 0, 0, 0, "Contact Admin!!", "E");
		}
		return mediaSiteDtoPageList;
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	@PreAuthorize("hasAuthority('mediasite_view')")
	@Operation(summary = "Privileges Required: mediasite_view")
	public @ResponseBody PaginatedDto<MediaSiteDto> search(@RequestBody SearchRequestDto paginatedDto, Principal principal) {

		String message = "";
		String messageType = "";
		PaginatedDto<MediaSiteDto> paginatedDtoList = null;
		List<MediaSiteDto> mediaSiteDtoList = new ArrayList<>();
		try {
			if (null == paginatedDto.getOrgId()) {
				return new PaginatedDto<MediaSiteDto>(mediaSiteDtoList, 0, 0, 0, 0, 0,
						"OrgId is compulsory for searching", "I");
			}
			Page<MediaSite> page = mediaSiteService.searchByField(paginatedDto.getPageNo(), paginatedDto.getPageSize(),
					paginatedDto.getSortField(), paginatedDto.getSortOrder(), paginatedDto, principal);
			List<MediaSite> mediaSiteList = page.getContent();

			for (MediaSite mediaSite : mediaSiteList) {
				MediaSiteDto mediaSiteDto = new MediaSiteDto();

				mediaSiteDto.setId(mediaSite.getId());
				mediaSiteDto.setSiteName(mediaSite.getSiteName());
				mediaSiteDto.setAgeGroup(mediaSite.getAgeGroup());
				mediaSiteDto.setAssetCode(mediaSite.getAssetCode());
				mediaSiteDto.setCaptureFrequency(mediaSite.getCaptureFrequency());
				mediaSiteDto.setCatchmentStrata(mediaSite.getCatchmentStrata());
				mediaSiteDto.setCity(mediaSite.getCity());
				mediaSiteDto.setDistrict(mediaSite.getDistrict());
				mediaSiteDto.setHeight(mediaSite.getHeight());
				mediaSiteDto.setIllumination(mediaSite.getIllumination());
				mediaSiteDto.setLatitude(mediaSite.getLatitude());
				mediaSiteDto.setLength(mediaSite.getLength());
				mediaSiteDto.setLocality(mediaSite.getLocality());
				mediaSiteDto.setLocationType(mediaSite.getLocationType());
				mediaSiteDto.setLongitude(mediaSite.getLongitude());
				mediaSiteDto.setMaterial(mediaSite.getMaterial());
				mediaSiteDto.setMediaClass(mediaSite.getMediaClass());
				mediaSiteDto.setMediaType(mediaSite.getMediaType());
				mediaSiteDto.setOwnedByOrgId(mediaSite.getOwnedByOrgId());
				mediaSiteDto.setOrientation(mediaSite.getOrientation());
				mediaSiteDto.setPincode(mediaSite.getPincode());
				mediaSiteDto.setPlacementType(mediaSite.getPlacementType());
				mediaSiteDto.setPlaceType(mediaSite.getPlaceType());
				mediaSiteDto.setQuality(mediaSite.getQuality());
				mediaSiteDto.setRoad(mediaSite.getRoad());
				mediaSiteDto.setState(mediaSite.getState());
				mediaSiteDto.setStructureType(mediaSite.getStructureType());
				mediaSiteDto.setTrafficDensity(mediaSite.getTrafficDensity());
				mediaSiteDto.setTrafficSignal(mediaSite.isTrafficSignal());
				mediaSiteDto.setTrafficType(mediaSite.getTrafficType());
				mediaSiteDto.setVendorAssetCode(mediaSite.getVendorAssetCode());
				mediaSiteDto.setViewingDistance(mediaSite.getViewingDistance());
				mediaSiteDto.setStatus(mediaSite.getStatus());
				mediaSiteDto.setCityTier(mediaSite.getCityTier());
				mediaSiteDto.setViewingTime(mediaSite.getViewingTime());
				mediaSiteDto.setWidth(mediaSite.getWidth());
				mediaSiteDto.setManagedByStartDate(mediaSite.getManagedByStartDate());
				mediaSiteDto.setManagedByEndDate(mediaSite.getManagedByEndDate());
				mediaSiteDto.setManagedByOrgId(mediaSite.getManagedByOrgId());
				mediaSiteDtoList.add(mediaSiteDto);
			}

			List<MediaSite> totalOrgList = mediaSiteService.findAll();
			Page<MediaSite> totalPage = new PageImpl<MediaSite>(totalOrgList);
			message = "Success";
			messageType = "S";
			paginatedDtoList = new PaginatedDto<MediaSiteDto>(mediaSiteDtoList, page.getTotalPages(),
					page.getNumber() + 1, page.getSize(), page.getTotalElements(), totalPage.getTotalElements(),
					message, messageType);

		} catch (Exception e) {
			return new PaginatedDto<MediaSiteDto>(mediaSiteDtoList, 0, 0, 0, 0, 0, "Contact Admin!!", "E");
		}

		return paginatedDtoList;
	}

	@Hidden
	@RequestMapping(value = "/export", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<Object> export(HttpServletResponse response) {
		//
		try {
			response.setContentType("application/octet-stream");

			DateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
			String currentDateTime = dateFormat.format(new Date());

			String headerKey = "Content-Disposition";
			String headerValue = "attachment; filename=MediaSite_Report_" + currentDateTime + ".xlsx";
			response.setHeader(headerKey, headerValue);
			mediaSiteService.exportMediaSite(response);
			return new ResponseEntity<>("Export Success", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Contact Admin!!", HttpStatus.BAD_REQUEST);
		}
	}

	@Hidden
	@RequestMapping(value = "/searchandexport", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<Object> searchAndExport(@RequestBody SearchRequestDto paginatedDto,
			HttpServletResponse response) {
		try {
			response.setContentType("application/octet-stream");

			DateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
			String currentDateTime = dateFormat.format(new Date());

			String headerKey = "Content-Disposition";
			String headerValue = "attachment; filename=MediaSite_Report_" + currentDateTime + ".xlsx";
			response.setHeader(headerKey, headerValue);

			mediaSiteService.searchByFieldAndExport(paginatedDto, response);

			return new ResponseEntity<>("Export Success", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Contact Admin!!", HttpStatus.BAD_REQUEST);
		}

	}

	@Hidden
	@RequestMapping(value = "/import", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<Object> importData(@RequestParam MultipartFile file) {
		try {
			mediaSiteService.importMediaSite(file);
			return new ResponseEntity<>("Export Success", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Contact Admin!!", HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/searchbyvalue", method = RequestMethod.GET)
	@Operation(summary = "Privileges Required: mediasite_view")
	@PreAuthorize("hasAuthority('mediasite_view')")
	public @ResponseBody List<MediaSite> searchByValue(@RequestParam String searchValue) {

		List<MediaSite> mediaSiteList = mediaSiteService.search(searchValue);
		return mediaSiteList;

	}

	@RequestMapping(value = "/importlist", method = RequestMethod.POST)
	@Operation(summary = "Privileges Required: mediasite_add")
	@PreAuthorize("hasAuthority('mediasite_add')")
	public @ResponseBody ResponseEntity<Object> importData(@RequestBody List<MediaSiteImportDto> mediaSiteImportDto, Principal principal) {

		try {
			String message = mediaSiteService.importMediaSite(mediaSiteImportDto, principal);
			
			if("Import Success!!".equalsIgnoreCase(message)) {
				return new ResponseEntity<>(message, HttpStatus.OK);
			}else {
				return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			return new ResponseEntity<>("Contact Admin!!", HttpStatus.BAD_REQUEST);
		}
	}
}
