package com.eikona.tech.service;

import java.security.Principal;
import java.util.List;

import org.springframework.data.domain.Page;

import com.eikona.tech.domain.Organization;
import com.eikona.tech.domain.Privilege;
import com.eikona.tech.dto.OrganizationImportDto;
import com.eikona.tech.dto.SearchRequestDto;
import com.eikona.tech.service.common.AbstractService;

public interface OrganizationService extends AbstractService<Organization, Long> {

	Page<Organization> searchByField(int pageNo, int pageSize, String sortField, String sortOrder,
			SearchRequestDto paginatedDto, Principal principal);

	List<Organization> search(String searchValue);

	List<Privilege> getPrivilegesByOrganization(Long orgId);


	List<Organization> getOrganizationByRole(Long roleId, Principal principal);

	Page<Organization> findPaginated(int pageNo, int pageSize, String sortField, String sortOrder,
			SearchRequestDto paginatedDto, Principal principal);

	String importOrg(List<OrganizationImportDto> organization, Principal principal);
}
