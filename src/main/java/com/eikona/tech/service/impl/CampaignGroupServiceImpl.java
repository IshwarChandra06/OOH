package com.eikona.tech.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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

import com.eikona.tech.domain.CampaignGroup;
import com.eikona.tech.dto.SearchRequestDto;
import com.eikona.tech.repository.CampaignGroupRepository;
import com.eikona.tech.service.CampaignGroupService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class CampaignGroupServiceImpl implements CampaignGroupService {

    @Autowired
    protected CampaignGroupRepository campaignGroupRepository;
    @SuppressWarnings("rawtypes")
	@Autowired
    protected PaginatedServiceImpl paginatedServiceImpl;
    @Override
    public CampaignGroup find(Long id) {
        CampaignGroup result = null;

        Optional<CampaignGroup> catalog = campaignGroupRepository.findById(id);

        if (catalog.isPresent()) {
            result = catalog.get();
        }

        return result;
    }

   @Override
    public List<CampaignGroup> findAll() {
        return  campaignGroupRepository.findAllByIsDeletedFalse();
    }

    //@Override
    public CampaignGroup save(CampaignGroup entity) {
    	entity.setDeleted(false);
        return campaignGroupRepository.save(entity);
    }

    @Override
    public void delete(Long id) {
    	CampaignGroup result = null;

         Optional<CampaignGroup> catalog = campaignGroupRepository.findById(id);

         if (catalog.isPresent()) {
             result = catalog.get();
             result.setDeleted(true);
         }
         this.campaignGroupRepository.save(result);
    }

    @Override
    public CampaignGroup update(CampaignGroup entity) {
        return campaignGroupRepository.save(entity);
    }
    
    public DataTablesOutput<CampaignGroup> getAll(@Valid DataTablesInput input){
     	 Specification<CampaignGroup> isDeletedFalse = (Specification<CampaignGroup>)(root, query, builder)->{
         		return builder.equal(root.get("isDeleted"), false);
         	};
         	return (DataTablesOutput<CampaignGroup>) campaignGroupRepository.findAll(input,isDeletedFalse);
         }
    @Override
   	public Page<CampaignGroup> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
   		
    	Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
   			Sort.by(sortField).descending();
    	
    	Specification<CampaignGroup> isDeleted = (root, query, cb) -> {
            return cb.equal(root.get("isDeleted"),false);
        };
   		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
   		return this.campaignGroupRepository.findAll(isDeleted,pageable);
   	}

	@SuppressWarnings("unchecked")
	public Page<CampaignGroup> searchByField(int pageNo, int pageSize, String sortField, String sortDirection,
			 SearchRequestDto paginatedDto) {
		
		ObjectMapper oMapper = new ObjectMapper();
		Map<String, String> map = oMapper.convertValue(paginatedDto.getSearchData(), Map.class);
		
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
			Sort.by(sortField).descending();
		
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
		Specification<CampaignGroup> fieldSpc = paginatedServiceImpl.fieldSpecification(map);
		Page<CampaignGroup> allCampaignGroup = campaignGroupRepository.findAll(fieldSpc,pageable);
		return allCampaignGroup;
	}

}
