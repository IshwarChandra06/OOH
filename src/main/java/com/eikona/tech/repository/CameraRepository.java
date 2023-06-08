package com.eikona.tech.repository;

import java.util.List;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.stereotype.Repository;

import com.eikona.tech.domain.Camera;
@Repository
public interface CameraRepository extends DataTablesRepository<Camera, Long>{
	List<Camera> findAllByIsDeletedFalse();
}
