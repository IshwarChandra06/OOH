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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.eikona.tech.domain.ConstraintRange;
import com.eikona.tech.dto.SearchRequestDto;
import com.eikona.tech.repository.ConstraintRangeRepository;
import com.eikona.tech.service.ConstraintRangeService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class ConstraintRangeServiceImpl implements ConstraintRangeService {

    @Autowired
    protected ConstraintRangeRepository constraintRangeRepository;

    //@Override
    public ConstraintRange find(Long id) {
        ConstraintRange result = null;

        Optional<ConstraintRange> catalog = constraintRangeRepository.findById(id);

        if (catalog.isPresent()) {
            result = catalog.get();
        }

        return result;
    }

    //@Override
    public List<ConstraintRange> findAll() {
        return (List<ConstraintRange>) constraintRangeRepository.findAllByIsDeletedFalse();
    }

    //@Override
    public ConstraintRange save(ConstraintRange entity) {
    	entity.setDeleted(false);
        return constraintRangeRepository.save(entity);
    }

    //@Override
    public void delete(Long id) {
    	ConstraintRange result = null;

        Optional<ConstraintRange> catalog = constraintRangeRepository.findById(id);

        if (catalog.isPresent()) {
            result = catalog.get();
            result.setDeleted(true);
        }
    	
    	 this.constraintRangeRepository.save(result);
    }

    //@Override
    public ConstraintRange update(ConstraintRange entity) {
        return constraintRangeRepository.save(entity);
    }
    
    public DataTablesOutput<ConstraintRange> getAll(@Valid DataTablesInput input){
   	 Specification<ConstraintRange> isDeletedFalse = (Specification<ConstraintRange>)(root, query, builder)->{
       		return builder.equal(root.get("isDeleted"), false);
       	};
       	return (DataTablesOutput<ConstraintRange>) constraintRangeRepository.findAll(input,isDeletedFalse);
       }
    @Override
   	public Page<ConstraintRange> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
    	if(null==sortDirection || sortDirection.isEmpty()) {
    		sortDirection = "asc";
    	}
    	if(null==sortField || sortField.isEmpty()) {
    		sortField = "id";
    	}
    	Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
   			Sort.by(sortField).descending();
    	
    	Specification<ConstraintRange> isDeleted = (root, query, cb) -> {
            return cb.equal(root.get("isDeleted"),false);
        };
   		
   		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
   		return this.constraintRangeRepository.findAll(isDeleted,pageable);
   	}
	@SuppressWarnings("unchecked")
	@Override
	public Page<ConstraintRange> searchByField(int pageNumber, int pageSize, String sortField, String sortOrder,
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
		Specification<ConstraintRange> fieldSpc = fieldSpecification(map);
		Page<ConstraintRange> allConstraintRange = constraintRangeRepository.findAll(fieldSpc,pageable);
		return allConstraintRange;
	}
	
	private Specification<ConstraintRange> fieldSpecification(Map<String, String> searchMap) {
		Specification<ConstraintRange> isDeleted = (root, query, cb) -> {
            return cb.equal(root.get("isDeleted"),false);
        };
        
        Set<String> searchSet= searchMap.keySet();
        
        for (String searchKey : searchSet) {
        	isDeleted = isDeleted.and(genericSpecification(searchKey,searchMap.get(searchKey)));
		}

		return Specification.where(isDeleted);
	}

	
	private Specification<ConstraintRange> genericSpecification(String searchField,String searchValue) {
		return (root, query, cb) -> {
			if (searchField == null || searchValue.isEmpty()) {
				return cb.conjunction();
			}
			return cb.like(cb.lower(root.<String>get(searchField)), "%" + searchValue + "%");
		};
	}
   

}
