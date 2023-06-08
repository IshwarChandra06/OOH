package com.eikona.tech.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.eikona.tech.domain.Organization;
import com.eikona.tech.domain.Role;

@Repository
public interface OrganizationRepository extends DataTablesRepository<Organization, Long>{
	
	List<Organization> findAllByIsDeletedFalse();
	
	@Query("SELECT br from com.eikona.tech.domain.Organization as br WHERE br.isDeleted=false")
	Page<Organization> findAllData(Pageable pageable);
	
	Organization findByName(String field);

	@Query("SELECT br from com.eikona.tech.domain.Organization as br WHERE br.isDeleted=false "
			+ "and (br.name LIKE %:searchValue% or br.address like %:searchValue% or br.city like %:searchValue% or "
			+ "br.state like %:searchValue% or  br.country like %:searchValue% or  br.pinCode like %:searchValue% or "
			+ "br.pointOfContact like %:searchValue% or br.pocEmail like %:searchValue% or br.pocPhone like %:searchValue% )")
	List<Organization> search(String searchValue);
	
	//@Query("SELECT br from com.eikona.tech.domain.Organization as br JOIN br.roles as r WHERE br.isDeleted=false and br.createByOrganization=:id and br.roles=:role")
	List<Organization> findByRolesAndCreateByOrganizationAndIsDeletedFalse(Role role,Long id);
	
	List<Organization> findByRolesAndIsDeletedFalse(Role role);

}