package com.eikona.tech.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.eikona.tech.domain.ConstraintSingle;
import com.eikona.tech.dto.SearchRequestDto;
import com.eikona.tech.repository.ConstraintSingleRepository;
import com.eikona.tech.service.ConstraintSingleService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ConstraintSingleServiceImpl implements ConstraintSingleService {

    @Autowired
    protected ConstraintSingleRepository constraintSingleRepository;

    //@Override
    public ConstraintSingle find(Long id) {
        ConstraintSingle result = null;

        Optional<ConstraintSingle> catalog = constraintSingleRepository.findById(id);

        if (catalog.isPresent()) {
            result = catalog.get();
        }

        return result;
    }

    //@Override
    public List<ConstraintSingle> findAll() {
        return  constraintSingleRepository.findAllByIsDeletedFalse();
    }

    //@Override
    public ConstraintSingle save(ConstraintSingle entity) {
    	entity.setDeleted(false);
        return constraintSingleRepository.save(entity);
    }

    //@Override
    public void delete(Long id) {
    	
    	ConstraintSingle result = null;

        Optional<ConstraintSingle> catalog = constraintSingleRepository.findById(id);

        if (catalog.isPresent()) {
            result = catalog.get();
            result.setDeleted(true);
        }
        this.constraintSingleRepository.save(result);
    }

    //@Override
    public ConstraintSingle update(ConstraintSingle entity) {
        return constraintSingleRepository.save(entity);
    }
    
    public DataTablesOutput<ConstraintSingle> getAll(@Valid DataTablesInput input){
      	 Specification<ConstraintSingle> isDeletedFalse = (Specification<ConstraintSingle>)(root, query, builder)->{
          		return builder.equal(root.get("isDeleted"), false);
          	};
          	return (DataTablesOutput<ConstraintSingle>) constraintSingleRepository.findAll(input,isDeletedFalse);
          }
    @Override
   	public Page<ConstraintSingle> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
   		
    	if(null==sortDirection || sortDirection.isEmpty()) {
    		sortDirection = "asc";
    	}
    	if(null==sortField || sortField.isEmpty()) {
    		sortField = "id";
    	}
    	
    	Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
   			Sort.by(sortField).descending();
    	
    	Specification<ConstraintSingle> isDeleted = (root, query, cb) -> {
            return cb.equal(root.get("isDeleted"),false);
        };
   		
   		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
   		return this.constraintSingleRepository.findAll(isDeleted,pageable);
   	}
    @SuppressWarnings("unchecked")
	@Override
	public Page<ConstraintSingle> searchByField(int pageNumber, int pageSize, String sortField, String sortOrder,
			SearchRequestDto searchRequestDto) {
		ObjectMapper oMapper = new ObjectMapper();
		Map<String, String> map = oMapper.convertValue(searchRequestDto.getSearchData(), Map.class);
		if(null==sortOrder || sortOrder.isEmpty()) {
			sortOrder = "asc";
    	}
    	if(null==sortField || sortField.isEmpty()) {
    		sortField = "id";
    	}
		Sort sort = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
			Sort.by(sortField).descending();
		
		Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
		Specification<ConstraintSingle> fieldSpc = fieldSpecification(map);
		Page<ConstraintSingle> allConstraintSingle = constraintSingleRepository.findAll(fieldSpc,pageable);
		return allConstraintSingle;
	}
	
	private Specification<ConstraintSingle> fieldSpecification(Map<String, String> searchMap) {
		Specification<ConstraintSingle> isDeleted = (root, query, cb) -> {
            return cb.equal(root.get("isDeleted"),false);
        };
        
        Set<String> searchSet= searchMap.keySet();
        
        for (String searchKey : searchSet) {
        	isDeleted = isDeleted.and(genericSpecification(searchKey,searchMap.get(searchKey)));
		}

		return Specification.where(isDeleted);
	}

	
	private Specification<ConstraintSingle> genericSpecification(String searchField,String searchValue) {
		return (root, query, cb) -> {
			if (searchField == null || searchValue.isEmpty()) {
				return cb.conjunction();
			}
			return cb.like(cb.lower(root.<String>get(searchField)), "%" + searchValue + "%");
		};
	}
}
