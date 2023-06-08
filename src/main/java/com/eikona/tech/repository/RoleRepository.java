package com.eikona.tech.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.eikona.tech.domain.Role;

@Repository
public interface RoleRepository extends DataTablesRepository<Role, Long>{
	List<Role> findAllByIsDeletedFalse();
	List<Role> findAllByIsOrgRoleTrue();
	@Query("SELECT br from com.eikona.tech.domain.Role as br WHERE br.isDeleted=false")
	Page<Role> findAllData(Pageable pageable);
	@Query("select cm from com.eikona.tech.domain.Role cm where cm.isDeleted=false and (cm.name LIKE %:searchValue%)")
	List<Role> search(String searchValue);
	@Query("SELECT br from com.eikona.tech.domain.Role as br WHERE br.isDeleted=false and br.id in :roleIdList")
	List<Role> findById(List<Long> roleIdList);
	
	@Query("SELECT br from com.eikona.tech.domain.Role as br WHERE br.isDeleted=false and name in :roleArray")
	List<Role> firdByName(String[] roleArray);
	
	Role findByName(String string);
}
