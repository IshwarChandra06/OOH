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

import com.eikona.tech.domain.PriceBreak;
import com.eikona.tech.repository.PriceBreakRepository;
import com.eikona.tech.service.PriceBreakService;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class PriceBreakServiceImpl implements PriceBreakService {

    @Autowired
    protected PriceBreakRepository priceBreakRepository;

    //@Override
    public PriceBreak find(Long id) {
        PriceBreak result = null;

        Optional<PriceBreak> catalog = priceBreakRepository.findById(id);

        if (catalog.isPresent()) {
            result = catalog.get();
        }

        return result;
    }

    //@Override
    public List<PriceBreak> findAll() {
        return (List<PriceBreak>) priceBreakRepository.findAll();
    }

    //@Override
    public PriceBreak save(PriceBreak entity) {
    	entity.setDeleted(false);
        return priceBreakRepository.save(entity);
    }

    //@Override
    public void delete(Long id) {
    	PriceBreak result = null;

        Optional<PriceBreak> catalog = priceBreakRepository.findById(id);

        if (catalog.isPresent()) {
            result = catalog.get();
            result.setDeleted(true);
        }
        this.priceBreakRepository.save(result);
    }

    //@Override
    public PriceBreak update(PriceBreak entity) {
        return priceBreakRepository.save(entity);
    }
    
    public DataTablesOutput<PriceBreak> getAll(@Valid DataTablesInput input){
      	 Specification<PriceBreak> isDeletedFalse = (Specification<PriceBreak>)(root, query, builder)->{
          		return builder.equal(root.get("isDeleted"), false);
          	};
          	return (DataTablesOutput<PriceBreak>) priceBreakRepository.findAll(input,isDeletedFalse);
          }

}
