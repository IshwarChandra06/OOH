package com.eikona.tech.api.controller;



import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.eikona.tech.domain.ConstraintRange;
import com.eikona.tech.dto.ConstraintRangeDto;
import com.eikona.tech.dto.PaginatedDto;
import com.eikona.tech.dto.SearchRequestDto;
import com.eikona.tech.service.ConstraintRangeService;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;


//@Hidden
@RestController
@RequestMapping("/api/constraint-range")
public class ConstraintRangeController {
	
	@Autowired
	ConstraintRangeService constraintRangeService;

	@Hidden
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public ResponseEntity<Object> list(Model model) {
		List<ConstraintRange> orgList = constraintRangeService.findAll();
		return new ResponseEntity<>(orgList, HttpStatus.OK);
	}


	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@Operation(summary = "Privileges Required: constraintrange_add")
	@PreAuthorize("hasAuthority('constraintrange_add')")
	public ResponseEntity<Object> create(@RequestBody ConstraintRange constraintRange) {
		try {
			if(null==constraintRange.getValue()||constraintRange.getValue().isEmpty()) {
				String message="Value should not be empty.";
				return new ResponseEntity<>(message,HttpStatus.BAD_REQUEST);
			}
		ConstraintRange createdConstraintSingle = constraintRangeService.save(constraintRange);
		return new ResponseEntity<>(createdConstraintSingle, HttpStatus.OK);
	}catch (Exception e) {
		String message="Contact Admin!!";
		return new ResponseEntity<>(message,HttpStatus.BAD_REQUEST);
	}
	}
	
	@RequestMapping(value="/id/{id}",method = RequestMethod.GET)
	@Operation(summary = "Privileges Required: constraintrange_view")
	@PreAuthorize("hasAuthority('constraintrange_view')")
	public ConstraintRange get(@PathVariable long id){
		return constraintRangeService.find(id);
	}

	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
	@Operation(summary = "Privileges Required: constraintrange_update")
	@PreAuthorize("hasAuthority('constraintrange_update')")
	public ResponseEntity<Object> update(@PathVariable long id, @RequestBody ConstraintRange constraintRange) {
		try{
			if(null==constraintRange.getId()){
			return new ResponseEntity<>("Id should not be empty",HttpStatus.BAD_REQUEST);
		}
		ConstraintRange updatedConstraintSingle = constraintRangeService.save(constraintRange);
		return new ResponseEntity<>(updatedConstraintSingle, HttpStatus.OK);
		}catch (Exception e) {
			String message="Contact Admin!!";
			return new ResponseEntity<>(message,HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	@Operation(summary = "Privileges Required: constraintrange_delete")
	@PreAuthorize("hasAuthority('constraintrange_delete')")
	public ResponseEntity<Object> delete(@PathVariable(value = "id") long id) {
		try {
		constraintRangeService.delete(id);
		return ResponseEntity.noContent().build();
		}catch (Exception e) {
			String message="Contact Admin!!";
			return new ResponseEntity<>(message,HttpStatus.BAD_REQUEST);
		}
	}
	@RequestMapping(value = "/stats", method = RequestMethod.POST)
	@Operation(summary = "Privileges Required: constraintrange_view")
	@PreAuthorize("hasAuthority('constraintrange_view')")
	public PaginatedDto<ConstraintRangeDto> findPaginated(@RequestBody SearchRequestDto paginatedDto) {
		PaginatedDto<ConstraintRangeDto> constraintlistDto=null;
		List<ConstraintRangeDto> constraintListDto = new ArrayList<>();
		try {
			if( 0==paginatedDto.getPageNo() || 0== paginatedDto.getPageSize() ) {
				return new PaginatedDto<ConstraintRangeDto>(constraintListDto,
						0, 0, 0, 0, 0,"pageNo or pageSize should be greater than 0","I");
			}
			
		Page<ConstraintRange> page = constraintRangeService.findPaginated(paginatedDto.getPageNo(), paginatedDto.getPageSize(), paginatedDto.getSortField(), paginatedDto.getSortOrder());
		
		 constraintlistDto = new PaginatedDto<ConstraintRangeDto>(constraintListDto,
				page.getTotalPages(), page.getNumber()+1, page.getSize(),0, page.getTotalElements(),"Success","S");
		}
		catch (Exception e) {
			return new PaginatedDto<ConstraintRangeDto>(constraintListDto,
					0, 0, 0, 0, 0,"Failed","E");

		}
		return constraintlistDto; 
	}
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	@Operation(summary = "Privileges Required: constraintrange_view")
	@PreAuthorize("hasAuthority('constraintrange_view')")
	public @ResponseBody PaginatedDto<ConstraintRangeDto> search(@RequestBody SearchRequestDto searchRequestDto){
	String message="";
	String messageType="";
	PaginatedDto<ConstraintRangeDto> constraintListDto = null;
	List<ConstraintRangeDto> constraintRangeList = new ArrayList<>();
	try {
		
	Page<ConstraintRange> page=   constraintRangeService.searchByField(searchRequestDto.getPageNo(), 
			searchRequestDto.getPageSize(), searchRequestDto.getSortField(),searchRequestDto.getSortOrder(),
			searchRequestDto);
	List<ConstraintRange> listconstraintSingleList=  page.getContent();
		
		for (ConstraintRange constraintSingle : listconstraintSingleList) {
			ConstraintRangeDto constraintRangeDto=new ConstraintRangeDto();
			constraintRangeDto.setType(constraintSingle.getType());
			constraintRangeDto.setValue(constraintSingle.getValue());
			constraintRangeDto.setMax_value(constraintSingle.getMax_value());
			constraintRangeDto.setMin_value(constraintSingle.getMin_value());
			constraintRangeList.add(constraintRangeDto);
		}
		 message = "Success";
			messageType = "S";
		List<ConstraintRange> totalPageList=  (List<ConstraintRange>) constraintRangeService.findAll();
		Page<ConstraintRange> totalPage=new PageImpl<>(totalPageList);
		constraintListDto = new PaginatedDto<ConstraintRangeDto>(constraintRangeList, page.getTotalPages(),
				page.getNumber()+1, page.getSize(),page.getTotalElements(),totalPage.getTotalElements(),message,messageType);
		 page.getContent();
		
	}
	catch(Exception e) {
		return new PaginatedDto<ConstraintRangeDto>(constraintRangeList,
				0, 0, 0, 0, 0,"Failed","E");
	}
	
		return constraintListDto;
	}
}
