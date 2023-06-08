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

import com.eikona.tech.domain.Schedule;
import com.eikona.tech.repository.ScheduleRepository;
import com.eikona.tech.service.ScheduleService;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    protected ScheduleRepository scheduleRepository;

    //@Override
    public Schedule find(Long id) {
        Schedule result = null;

        Optional<Schedule> catalog = scheduleRepository.findById(id);

        if (catalog.isPresent()) {
            result = catalog.get();
        }

        return result;
    }

    //@Override
    public List<Schedule> findAll() {
        return  scheduleRepository.findAllByIsDeletedFalse();
    }

    //@Override
    public Schedule save(Schedule entity) {
    	entity.setDeleted(false);
        return scheduleRepository.save(entity);
    }

    //@Override
    public void delete(Long id) {
    	 Schedule result = null;

         Optional<Schedule> catalog = scheduleRepository.findById(id);

         if (catalog.isPresent()) {
             result = catalog.get();
             result.setDeleted(true);
         }
         this.scheduleRepository.save(result);
    }

    //@Override
    public Schedule update(Schedule entity) {
        return scheduleRepository.save(entity);
    }
    public DataTablesOutput<Schedule> getAll(@Valid DataTablesInput input){
    	 Specification<Schedule> isDeletedFalse = (Specification<Schedule>)(root, query, builder)->{
        		return builder.equal(root.get("isDeleted"), false);
        	};
        	return (DataTablesOutput<Schedule>) scheduleRepository.findAll(input,isDeletedFalse);
        }
    

}
