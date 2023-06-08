package com.eikona.tech.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.eikona.tech.domain.User;

@Repository
public interface UserRepository extends DataTablesRepository<User, Long>{
	List<User> findAllByIsDeletedFalse();
	@Query("SELECT br from com.eikona.tech.domain.User as br WHERE br.isDeleted=false")
	Page<User> findAllData(Pageable pageable);
	@Query("select cm from com.eikona.tech.domain.User cm where cm.isDeleted=false and (cm.userName LIKE %:searchValue% or "
			+ "cm.organization.name LIKE %:searchValue% or "
			+ "cm.phone LIKE %:searchValue%)")
	List<User> search(String searchValue);
	Optional<User> findByUserName(String username);
}
