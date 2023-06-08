package com.eikona.tech.service.impl;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.eikona.tech.domain.Camera;
import com.eikona.tech.repository.CameraRepository;
import com.eikona.tech.service.CameraService;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class CameraServiceImpl implements CameraService {

	@Autowired
	protected CameraRepository campaignRepository;

	@Override
	public Camera find(Long id) {
		Camera result = null;

		Optional<Camera> catalog = campaignRepository.findById(id);

		if (catalog.isPresent()) {
			result = catalog.get();
		}

		return result;
	}

	@Override
	public List<Camera> findAll() {
		return (List<Camera>) campaignRepository.findAllByIsDeletedFalse();
	}

	@Override
	public Camera save(Camera entity) {
		entity.setDeleted(false);
		return campaignRepository.save(entity);
	}

	@Override
	public void delete(Long id) {
		Camera result = null;

		Optional<Camera> catalog = campaignRepository.findById(id);

		if (catalog.isPresent()) {
			result = catalog.get();
			result.setDeleted(true);
		}
		this.campaignRepository.save(result);
	}

	@Override
	public Camera update(Camera entity) {
		return campaignRepository.save(entity);
	}

	public DataTablesOutput<Camera> getAll(@Valid DataTablesInput input) {
		Specification<Camera> isDeletedFalse = (Specification<Camera>) (root, query, builder) -> {
			return builder.equal(root.get("isDeleted"), false);
		};
		return (DataTablesOutput<Camera>) campaignRepository.findAll(input, isDeletedFalse);
	}
	
}
