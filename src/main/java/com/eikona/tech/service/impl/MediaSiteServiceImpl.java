package com.eikona.tech.service.impl;

import java.io.IOException;
import java.security.Principal;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.eikona.tech.domain.MediaSite;
import com.eikona.tech.domain.Organization;
import com.eikona.tech.domain.User;
import com.eikona.tech.dto.MediaSiteImportDto;
import com.eikona.tech.dto.SearchRequestDto;
import com.eikona.tech.repository.MediaSiteRepository;
import com.eikona.tech.repository.UserRepository;
import com.eikona.tech.service.MediaSiteService;
import com.eikona.tech.util.MediaSiteExport;
import com.eikona.tech.util.MediaSiteImport;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class MediaSiteServiceImpl implements MediaSiteService {

	@Autowired
	protected MediaSiteRepository mediaSiteRepository;

	@Autowired
	protected MediaSiteExport mediaSiteExport;

	@Autowired
	protected MediaSiteImport mediaSiteImport;

	@Autowired
	protected UserRepository userRepository;

	@SuppressWarnings("rawtypes")
	@Autowired
	protected PaginatedServiceImpl paginatedServiceImpl;

	// @Override
	public MediaSite find(Long id) {
		MediaSite result = null;

		Optional<MediaSite> catalog = mediaSiteRepository.findById(id);

		if (catalog.isPresent()) {
			result = catalog.get();
		}

		return result;
	}

	// @Override
	public List<MediaSite> findAll() {
		return mediaSiteRepository.findAllByIsDeletedFalse();
	}

	// @Override
	public MediaSite save(MediaSite entity) {
		entity.setDeleted(false);
		return mediaSiteRepository.save(entity);
	}

	// @Override
	public void delete(Long id) {
		MediaSite result = null;

		Optional<MediaSite> catalog = mediaSiteRepository.findById(id);

		if (catalog.isPresent()) {
			result = catalog.get();
			result.setDeleted(true);
		}
		this.mediaSiteRepository.save(result);
	}

	// @Override
	public MediaSite update(MediaSite entity) {
		return mediaSiteRepository.save(entity);
	}

	public DataTablesOutput<MediaSite> getAll(@Valid DataTablesInput input) {

		Specification<MediaSite> isDeletedFalse = (Specification<MediaSite>) (root, query, builder) -> {
			return builder.equal(root.get("isDeleted"), false);
		};
		return (DataTablesOutput<MediaSite>) mediaSiteRepository.findAll(input, isDeletedFalse);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page<MediaSite> findPaginated(int pageNo, int pageSize, String sortField, String sortDir,
			SearchRequestDto paginatedDto) {
		
		if (null == sortDir || sortDir.isEmpty()) {
			sortDir = "asc";
		}
		if (null == sortField || sortField.isEmpty()) {
			sortField = "id";
		}
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
				: Sort.by(sortField).descending();

		Specification<MediaSite> isDeleted = (root, query, cb) -> {
			return cb.equal(root.get("isDeleted"), false);
		};
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
		Specification<MediaSite> orgSpc = paginatedServiceImpl.managedByIdSpecification(paginatedDto.getOrgId());
		Page<MediaSite> allMediaSite = mediaSiteRepository.findAll(isDeleted.and(orgSpc), pageable);
		
		return allMediaSite;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page<MediaSite> searchByField(int pageNo, int pageSize, String sortField, String sortOrder,
			SearchRequestDto paginatedDto, Principal principal) {
		ObjectMapper oMapper = new ObjectMapper();
		Map<String, String> map = oMapper.convertValue(paginatedDto.getSearchData(), Map.class);

		if (null == sortOrder || sortOrder.isEmpty()) {
			sortOrder = "asc";
		}
		if (null == sortField || sortField.isEmpty()) {
			sortField = "id";
		}
		Sort sort = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).descending()
				: Sort.by(sortField).ascending();

		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
		
		Specification<MediaSite> mediaSpec =  mediaSpecification(map, paginatedDto.getOrgId(),principal);
		
		Page<MediaSite> allMediaSite = mediaSiteRepository.findAll(mediaSpec, pageable);
		return allMediaSite;
	}
	
	@SuppressWarnings("unchecked")
	public Specification<MediaSite> mediaSpecification (Map<String, String> map , Long OrgId, Principal principal){
		User user = userRepository.findByUserName(principal.getName()).get();
		Organization organizationObj = user.getCreateByOrganization();
		
		String[] constraintSingle = {"status","orientation","captureFrequency","illumination","cityTier","mediaClass","structureType","mediaType",
				"placeType","material","placementType","catchmentStrata","locationType","trafficType","trafficDensity","viewingDistance",
				"quality"};
		String[] constraintRange = {"ageGroup"};
		String[] organization = {"ownedByOrgId","managedByOrgId"};
		
		List<String> constraintSingleList = Arrays.asList(constraintSingle);
		List<String> constraintRangeList = Arrays.asList(constraintRange);
		List<String> organizationList = Arrays.asList(organization);
		
		Map<String, String> mapString = new HashMap<String, String>();
		Map<String, String> mapSingle = new HashMap<String, String>();
		Map<String, String> mapRange = new HashMap<String, String>();
		Map<String, String> mapOrg = new HashMap<String, String>();
		Set<String> searchSet = map.keySet();
		for (String searchKey : searchSet) {
			if(constraintSingleList.contains(searchKey)) {
				mapSingle.put(searchKey, (String) map.get(searchKey));
			}else if(constraintRangeList.contains(searchKey)) {
				mapRange.put(searchKey, (String) map.get(searchKey));
			}else if(organizationList.contains(searchKey)) {
				mapOrg.put(searchKey, (String) map.get(searchKey));
			}else {
				mapString.put(searchKey, (String) map.get(searchKey));
			}
		}
		Specification<MediaSite> fieldSpc  = paginatedServiceImpl.fieldSpecification(mapString);
		
		Specification<MediaSite> fieldSpcSingle  = paginatedServiceImpl.fieldSpecificationSingle(mapSingle);
		Specification<MediaSite> fieldSpcRange  = paginatedServiceImpl.fieldSpecificationRange(mapRange);
		Specification<MediaSite> fieldSpcOrg  = paginatedServiceImpl.fieldSpecificationOrg(mapOrg);
		
		Specification<MediaSite> orgSpc = paginatedServiceImpl.managedByIdSpecification(OrgId);
		Specification<MediaSite> orgSpcCreate = paginatedServiceImpl.managedByIdSpecification(organizationObj.getId());
		
		Specification<MediaSite> mediaSpec = fieldSpc.and(orgSpc.or(orgSpcCreate)).and(fieldSpcSingle).and(fieldSpcRange).and(fieldSpcOrg);
		
		return mediaSpec;
	}

	@Override
	public void exportMediaSite(HttpServletResponse response) {
		try {
			List<MediaSite> mediaSiteList = (List<MediaSite>) mediaSiteRepository.findAll();
			mediaSiteExport.excelGenerator(mediaSiteList, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void importMediaSite(MultipartFile file) {
		try {
			List<MediaSite> mediaSite = mediaSiteImport.importMediaSite(file.getInputStream());

			mediaSiteRepository.saveAll(mediaSite);
		} catch (IOException e) {
			throw new RuntimeException("FAIL! -> message = " + e.getMessage());
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public void searchByFieldAndExport(SearchRequestDto paginatedDto, HttpServletResponse response) {
		ObjectMapper oMapper = new ObjectMapper();
		Map<String, String> map = oMapper.convertValue(paginatedDto.getSearchData(), Map.class);
		Specification<MediaSite> fieldSpc = paginatedServiceImpl.fieldSpecification(map);
		List<MediaSite> mediaSiteList = mediaSiteRepository.findAll(fieldSpc);

		try {
			mediaSiteExport.excelGenerator(mediaSiteList, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<MediaSite> search(String searchValue) {

		List<MediaSite> mediaSiteList = mediaSiteRepository.search(searchValue);
		return mediaSiteList;
	}

	@Override
	public String importMediaSite(List<MediaSiteImportDto> mediaSiteImportDto, Principal principal) throws ParseException {
		
		String message =  mediaSiteImport.importMediaSite(mediaSiteImportDto, principal);

		return message;
	}

//	@SuppressWarnings("unchecked")
//	private Specification<MediaSite> fieldSpecification(Map<String, String> searchMap) {
//		
//		ObjectMapper oMapper = new ObjectMapper();
//		
//		Specification<MediaSite> isDeleted = (root, query, cb) -> {
//            return cb.equal(root.get("isDeleted"),false);
//        };
//        
//        Set<String> searchSet= searchMap.keySet();
//        
//        for (String searchKey : searchSet) {
//        	if(searchMap.get(searchKey) instanceof String) {
//        		isDeleted = isDeleted.and(genericSpecification(searchKey,searchMap.get(searchKey)));
//        	}
//        	else {
//        		Object obj = searchMap.get(searchKey);
//        		Map<String, String> map = oMapper.convertValue(obj, Map.class);
//        		for (String name : map.keySet()) {
//        			System.err.println(map.get(name));
//        			isDeleted = isDeleted.and(genericSpecification(searchKey,name,map.get(name)));
//        		}
//        	}
//		}
//
//		return Specification.where(isDeleted);
//	}
//
//	private Specification<MediaSite> genericSpecification(String searchField,String searchValue) {
//		return (root, query, cb) -> {
//			if (searchField == null|| searchValue.isEmpty()) {
//				return cb.conjunction();
//			}
//			return cb.like(cb.lower(root.<String>get(searchField)), "%" + searchValue + "%");
//		};
//	}
//	
//	private Specification<MediaSite> genericSpecification(String searchObj, String searchField, String searchValue) {
//		return (root, query, cb) -> {
//			if (searchObj == null || searchField == null  || searchValue.isEmpty()) {
//				return cb.conjunction();
//			}
//			return cb.like(cb.lower(root.<String>get(searchObj).get(searchField)), "%" + searchValue + "%");
//		};
//	}
}
