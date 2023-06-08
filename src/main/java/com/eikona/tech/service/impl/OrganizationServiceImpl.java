package com.eikona.tech.service.impl;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.eikona.tech.domain.Organization;
import com.eikona.tech.domain.Privilege;
import com.eikona.tech.domain.Role;
import com.eikona.tech.domain.User;
import com.eikona.tech.dto.OrganizationImportDto;
import com.eikona.tech.dto.SearchRequestDto;
import com.eikona.tech.repository.OrganizationRepository;
import com.eikona.tech.repository.RoleRepository;
import com.eikona.tech.repository.UserRepository;
import com.eikona.tech.service.OrganizationService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class OrganizationServiceImpl implements OrganizationService {

	@Autowired
	protected OrganizationRepository organizationRepository;

	@Autowired
	protected RoleRepository roleRepository;

	@Autowired
	protected UserRepository userRepository;

	@SuppressWarnings("rawtypes")
	@Autowired
	protected PaginatedServiceImpl paginatedServiceImpl;

	@Override
	public Organization find(Long id) {
		Organization result = null;

		Optional<Organization> catalog = organizationRepository.findById(id);

		if (catalog.isPresent()) {
			result = catalog.get();
		}

		return result;
	}

	@Override
	public List<Organization> findAll() {
		return organizationRepository.findAllByIsDeletedFalse();
	}

	@Override
	public Organization save(Organization entity) {
		entity.setDeleted(false);
		return organizationRepository.save(entity);
	}

	@Override
	public void delete(Long id) {
		Organization result = null;

		Optional<Organization> catalog = organizationRepository.findById(id);

		if (catalog.isPresent()) {
			result = catalog.get();
			result.setDeleted(true);
		}
		this.organizationRepository.save(result);
	}

	@Override
	public Organization update(Organization entity) {
		return organizationRepository.save(entity);
	}

	public DataTablesOutput<Organization> getAll(@Valid DataTablesInput input) {
		Specification<Organization> isDeletedFalse = (Specification<Organization>) (root, query, builder) -> {
			return builder.equal(root.get("isDeleted"), false);
		};
		return (DataTablesOutput<Organization>) organizationRepository.findAll(input, isDeletedFalse);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page<Organization> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection,
			SearchRequestDto paginatedDto, Principal principal) {
		if (null == sortDirection || sortDirection.isEmpty()) {
			sortDirection = "asc";
		}
		if (null == sortField || sortField.isEmpty()) {
			sortField = "id";
		}
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
				: Sort.by(sortField).descending();

		Specification<Organization> isDeleted = (root, query, cb) -> {
			return cb.equal(root.get("isDeleted"), false);
		};

		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
		Specification<Organization> orgSpcByCreate = paginatedServiceImpl
				.createdByorganizationSpecification(paginatedDto.getOrgId());
		Specification<Organization> orgSpcByPrincipal = paginatedServiceImpl
				.orgIdSpecification(paginatedDto.getOrgId());

		Page<Organization> allOrganization = organizationRepository
				.findAll(isDeleted.and(orgSpcByPrincipal.or(orgSpcByCreate)), pageable);

		return allOrganization;
	}

	@SuppressWarnings("unchecked")
	public Page<Organization> searchByField(int pageNo, int pageSize, String sortField, String sortDirection,
			SearchRequestDto paginatedDto, Principal principal) {

		ObjectMapper oMapper = new ObjectMapper();
		Map<String, String> map = oMapper.convertValue(paginatedDto.getSearchData(), Map.class);
		if (null == sortDirection || sortDirection.isEmpty()) {
			sortDirection = "asc";
		}
		if (null == sortField || sortField.isEmpty()) {
			sortField = "id";
		}
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
				: Sort.by(sortField).descending();

		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
		Specification<Organization> fieldSpc = paginatedServiceImpl.fieldSpecification(map);
		Specification<Organization> orgSpcByCreate = paginatedServiceImpl
				.createdByorganizationSpecification(paginatedDto.getOrgId());
		Specification<Organization> orgSpcByPrincipal = paginatedServiceImpl
				.orgIdSpecification(paginatedDto.getOrgId());
		
		Page<Organization> allOrganization = organizationRepository
				.findAll(fieldSpc.and(orgSpcByPrincipal.or(orgSpcByCreate)), pageable);

		return allOrganization;
	}

	@Override
	public List<Organization> search(String searchValue) {

		List<Organization> OrganizationList = organizationRepository.search(searchValue);
		return OrganizationList;
	}

	@Override
	public List<Organization> getOrganizationByRole(Long roleId, Principal principal) {

		Role role = roleRepository.findById(roleId).get();
		User user = userRepository.findByUserName(principal.getName()).get();
		Organization org = user.getOrganization();
		
		List<Organization> orgList = null;
		if("Brand".equalsIgnoreCase(role.getName())|| "Agency".equalsIgnoreCase(role.getName())) {
			orgList = organizationRepository.findByRolesAndIsDeletedFalse(role);
		}else {
			orgList = organizationRepository.findByRolesAndCreateByOrganizationAndIsDeletedFalse(role,
					org.getId());
		}
		
		return orgList;
	}

	@Override
	public List<Privilege> getPrivilegesByOrganization(Long orgId) {

		Organization organization = organizationRepository.findById(orgId).get();
		List<Role> roleList = organization.getRoles();

		List<Long> roleIdList = new ArrayList<>();
		for (Role role : roleList) {
			roleIdList.add(role.getId());
		}

		List<Role> rolesList = roleRepository.findById(roleIdList);
		List<Privilege> privilegeList = new ArrayList<>();
		for (Role role : rolesList) {
			privilegeList.addAll(role.getPrivileges());
		}
		return privilegeList;
	}

	@Override
	public String importOrg(List<OrganizationImportDto> organization, Principal principal) {
		if (null == organization || organization.isEmpty()) {
			return "Name and Role should not be empty.";
		}

		String username = principal.getName();
		User user = userRepository.findByUserName(username).get();
		List<Organization> organizationList = new ArrayList<>();

		for (OrganizationImportDto org : organization) {
			Organization organizationObj = new Organization();
			if (null == org.getName() || org.getName().isEmpty()) {
				return "Name should not be empty.";
			}
			if (null == org.getRoles() || org.getRoles().isEmpty()) {
				return "Role should not be empty.";
			}

			String[] roleArray = org.getRoles().split(",");
			List<Role> roleList = roleRepository.firdByName(roleArray);
			List<Role> roleListSet = new ArrayList<Role>();
			for (Role role : roleList) {
				roleListSet.add(role);
			}

			organizationObj.setRoles(roleListSet);
			organizationObj.setCreateByOrganization(user.getOrganization().getId());

			organizationObj.setAddress(org.getAddress());
			organizationObj.setCity(org.getCity());
			organizationObj.setCountry(org.getCountry());
			organizationObj.setDeleted(false);
			organizationObj.setName(org.getName());
			organizationObj.setPinCode(org.getPinCode());
			organizationObj.setPocEmail(org.getPocEmail());
			organizationObj.setPocPhone(org.getPocPhone());
			organizationObj.setPointOfContact(org.getPointOfContact());
			organizationObj.setState(org.getState());

			organizationList.add(organizationObj);
		}
		organizationRepository.saveAll(organizationList);
		return "Import Success!!";
	}

//	private Specification<Organization> fieldSpecification(Map<String, String> searchMap) {
//		Specification<Organization> isDeleted = (root, query, cb) -> {
//            return cb.equal(root.get("isDeleted"),false);
//        };
//        
//        Set<String> searchSet= searchMap.keySet();
//        
//        for (String searchKey : searchSet) {
//        	isDeleted = isDeleted.and(genericSpecification(searchKey,searchMap.get(searchKey)));
//		}
//
//		return Specification.where(isDeleted);
//	}
//
//	
//	private Specification<Organization> genericSpecification(String searchField,String searchValue) {
//		return (root, query, cb) -> {
//			if (searchField == null || searchValue.isEmpty()) {
//				return cb.conjunction();
//			}
//			return cb.like(cb.lower(root.<String>get(searchField)), "%" + searchValue + "%");
//		};
//	}

}
