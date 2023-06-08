package com.eikona.tech.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.eikona.tech.domain.Backlog;
import com.eikona.tech.repository.BacklogRepository;
import com.eikona.tech.service.BacklogService;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class BacklogServiceImpl implements BacklogService {

    @Autowired
    protected BacklogRepository backlogRepository;

    //@Override
    public Backlog find(Long id) {
        Backlog result = null;

        Optional<Backlog> catalog = backlogRepository.findById(id);

        if (catalog.isPresent()) {
            result = catalog.get();
        }

        return result;
    }

    //@Override
    public List<Backlog> findAll() {
        return  backlogRepository.findAllByIsDeletedFalse();
    }

    //@Override
    public Backlog save(Backlog entity) {
    	entity.setDeleted(false);
        return backlogRepository.save(entity);
    }

    //@Override
    public void delete(Long id) {
    	 Backlog result = null;

         Optional<Backlog> catalog = backlogRepository.findById(id);

         if (catalog.isPresent()) {
             result = catalog.get();
             result.setDeleted(true);
         }
         this.backlogRepository.save(result);
    }

    //@Override
    public Backlog update(Backlog entity) {
        return backlogRepository.save(entity);
    }
    public DataTablesOutput<Backlog> getAll(@Valid DataTablesInput input){
	 Specification<Backlog> isDeletedFalse = (Specification<Backlog>)(root, query, builder)->{
    		return builder.equal(root.get("isDeleted"), false);
    	};
    	return (DataTablesOutput<Backlog>) backlogRepository.findAll(input,isDeletedFalse);
    }

}
