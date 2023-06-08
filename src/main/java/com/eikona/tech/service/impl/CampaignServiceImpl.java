package com.eikona.tech.service.impl;

import java.security.Principal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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

import com.eikona.tech.domain.Campaign;
import com.eikona.tech.dto.SearchRequestDto;
import com.eikona.tech.repository.CampaignRepository;
import com.eikona.tech.service.CampaignService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class CampaignServiceImpl implements CampaignService {

	@Autowired
	protected CampaignRepository campaignRepository;

	@SuppressWarnings("rawtypes")
	@Autowired
	protected PaginatedServiceImpl paginatedServiceImpl;

	@Override
	public Campaign find(Long id) {
		Campaign result = null;

		Optional<Campaign> catalog = campaignRepository.findById(id);

		if (catalog.isPresent()) {
			result = catalog.get();
		}

		return result;
	}

	@Override
	public List<Campaign> findAll() {
		return (List<Campaign>) campaignRepository.findAllByIsDeletedFalse();
	}

	@Override
	public Campaign save(Campaign entity) {
		entity.setDeleted(false);
		return campaignRepository.save(entity);
	}

	@Override
	public void delete(Long id) {
		Campaign result = null;

		Optional<Campaign> catalog = campaignRepository.findById(id);

		if (catalog.isPresent()) {
			result = catalog.get();
			result.setDeleted(true);
		}
		this.campaignRepository.save(result);
	}

	@Override
	public Campaign update(Campaign entity) {
		return campaignRepository.save(entity);
	}

	public DataTablesOutput<Campaign> getAll(@Valid DataTablesInput input) {
		Specification<Campaign> isDeletedFalse = (Specification<Campaign>) (root, query, builder) -> {
			return builder.equal(root.get("isDeleted"), false);
		};
		return (DataTablesOutput<Campaign>) campaignRepository.findAll(input, isDeletedFalse);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page<Campaign> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection,
			SearchRequestDto paginatedDto, Principal principal) {

		if (null == sortDirection || sortDirection.isEmpty()) {
			sortDirection = "asc";
		}
		if (null == sortField || sortField.isEmpty()) {
			sortField = "id";
		}
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
				: Sort.by(sortField).descending();

		Specification<Campaign> isDeleted = (root, query, cb) -> {
			return cb.equal(root.get("isDeleted"), false);
		};

		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
		Specification<Campaign> orgSpcAgency = paginatedServiceImpl
				.campaignAgencySpecification(paginatedDto.getOrgId());
		Specification<Campaign> orgSpcBrand = paginatedServiceImpl.campaignBrandSpecification(paginatedDto.getOrgId());
		Specification<Campaign> orgSpc = paginatedServiceImpl
				.campaignOrganizationSpecification(paginatedDto.getOrgId());

		Page<Campaign> allCampaign = campaignRepository.findAll(isDeleted.and(orgSpc.or(orgSpcAgency).or(orgSpcBrand)),
				pageable);

		return allCampaign;
	}

	@SuppressWarnings("unchecked")
	public Page<Campaign> searchByField(int pageNo, int pageSize, String sortField, String sortDirection,
			SearchRequestDto paginatedDto, Principal principal) {

		String[] organization = { "brand", "agency" };

		List<String> organizationList = Arrays.asList(organization);

		ObjectMapper oMapper = new ObjectMapper();
		Map<String, Object> map = oMapper.convertValue(paginatedDto.getSearchData(), Map.class);

		Map<String, String> mapString = new HashMap<String, String>();
		Map<String, String> mapOrg = new HashMap<String, String>();
		Set<String> searchSet = map.keySet();
		for (String searchKey : searchSet) {
			if (organizationList.contains(searchKey)) {
				mapOrg.put(searchKey, (String) map.get(searchKey));
			} else {
				mapString.put(searchKey, (String) map.get(searchKey));
			}
		}

		if (null == sortDirection || sortDirection.isEmpty()) {
			sortDirection = "asc";
		}
		if (null == sortField || sortField.isEmpty()) {
			sortField = "id";
		}
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
				: Sort.by(sortField).descending();

		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
		Specification<Campaign> fieldSpc = paginatedServiceImpl.fieldSpecification(mapString);
		Specification<Campaign> fieldSpcOrg = paginatedServiceImpl.fieldSpecificationOrg(mapOrg);
		Specification<Campaign> orgSpcAgency = paginatedServiceImpl
				.campaignAgencySpecification(paginatedDto.getOrgId());
		Specification<Campaign> orgSpcBrand = paginatedServiceImpl.campaignBrandSpecification(paginatedDto.getOrgId());
		Specification<Campaign> orgSpc = paginatedServiceImpl
				.campaignOrganizationSpecification(paginatedDto.getOrgId());

		Page<Campaign> allCampaign = campaignRepository
				.findAll(fieldSpc.and(orgSpc.or(orgSpcAgency).or(orgSpcBrand)).and(fieldSpcOrg), pageable);

		return allCampaign;
	}

	@Override
	public List<Campaign> search(String searchValue) {

		List<Campaign> campaignList = campaignRepository.search(searchValue);
		return campaignList;
	}
}
