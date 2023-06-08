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

import com.eikona.tech.domain.ConstraintSingle;
import com.eikona.tech.dto.ConstraintSingleDto;
import com.eikona.tech.dto.PaginatedDto;
import com.eikona.tech.dto.SearchRequestDto;
import com.eikona.tech.service.ConstraintSingleService;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;

//@Hidden
@RestController
@RequestMapping("/api/constraint-single")
public class ConstraintSingleController {

	@Autowired
	ConstraintSingleService constraintSingleService;

	@Hidden
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public ResponseEntity<Object> list(Model model) {
		List<ConstraintSingle> orgList = constraintSingleService.findAll();
		return new ResponseEntity<>(orgList, HttpStatus.OK);
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@Operation(summary = "Privileges Required: constraintsingle_add")
	@PreAuthorize("hasAuthority('constraintsingle_add')")
	public ResponseEntity<Object> create(@RequestBody ConstraintSingle constraintSingle) {
		try {
			if (null == constraintSingle.getValue() || constraintSingle.getValue().isEmpty()) {
				String message = "Value should not be empty.";
				return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
			}
			ConstraintSingle createdConstraintSingle = constraintSingleService.save(constraintSingle);
			return new ResponseEntity<>(createdConstraintSingle, HttpStatus.OK);
		} catch (Exception e) {
			String message = "Contact Admin!!";
			return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/id/{id}", method = RequestMethod.GET)
	@Operation(summary = "Privileges Required: constraintsingle_view")
	@PreAuthorize("hasAuthority('constraintsingle_view')")
	public ConstraintSingle get(@PathVariable long id) {
		return constraintSingleService.find(id);
	}

	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
	@Operation(summary = "Privileges Required: constraintsingle_update")
	@PreAuthorize("hasAuthority('constraintsingle_update')")
	public ResponseEntity<Object> update(@PathVariable long id, @RequestBody ConstraintSingle constraintSingle) {
		try {
			if (null == constraintSingle.getId()) {
				return new ResponseEntity<>("Id should not be empty", HttpStatus.BAD_REQUEST);
			}
			ConstraintSingle updatedConstraintSingle = constraintSingleService.save(constraintSingle);
			return new ResponseEntity<>(updatedConstraintSingle, HttpStatus.OK);
		} catch (Exception e) {
			String message = "Contact Admin!!";
			return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	@Operation(summary = "Privileges Required: constraintsingle_delete")
	@PreAuthorize("hasAuthority('constraintsingle_delete')")
	public ResponseEntity<Object> delete(@PathVariable(value = "id") long id) {
		try {
		constraintSingleService.delete(id);
		return ResponseEntity.noContent().build();
		}
		catch (Exception e) {
			String message = "Contact Admin!!";
			return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/stats", method = RequestMethod.POST)
	@Operation(summary = "Privileges Required: constraintsingle_view")
	@PreAuthorize("hasAuthority('constraintsingle_view')")
	public PaginatedDto<ConstraintSingleDto> findPaginated(@RequestBody SearchRequestDto paginatedDto) {
		PaginatedDto<ConstraintSingleDto> constraintlistDto = null;
		List<ConstraintSingleDto> constraintListDto = new ArrayList<>();
		try {

			if (0 == paginatedDto.getPageNo() || 0 == paginatedDto.getPageSize()) {
				return new PaginatedDto<ConstraintSingleDto>(constraintListDto, 0, 0, 0, 0, 0,
						"pageNo or pageSize should be greater than 0", "I");
			}

			Page<ConstraintSingle> page = constraintSingleService.findPaginated(paginatedDto.getPageNo(),
					paginatedDto.getPageSize(), paginatedDto.getSortField(), paginatedDto.getSortOrder());

			constraintlistDto = new PaginatedDto<ConstraintSingleDto>(constraintListDto, page.getTotalPages(),
					page.getNumber() + 1, page.getSize(), 0, page.getTotalElements(), "Success", "S");

		} catch (Exception e) {
			return new PaginatedDto<ConstraintSingleDto>(constraintListDto, 0, 0, 0, 0, 0, "Failed", "E");
		}
		return constraintlistDto;
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	@Operation(summary = "Privileges Required: constraintsingle_view")
	@PreAuthorize("hasAuthority('constraintsingle_view')")
	public @ResponseBody PaginatedDto<ConstraintSingleDto> search(@RequestBody SearchRequestDto searchRequestDto) {
		String message = "";
		String messageType = "";
		PaginatedDto<ConstraintSingleDto> constraintListDto = null;
		List<ConstraintSingleDto> constraintSingleList = new ArrayList<>();
		try {
			Page<ConstraintSingle> page = constraintSingleService.searchByField(searchRequestDto.getPageNo(),
					searchRequestDto.getPageSize(), searchRequestDto.getSortField(), searchRequestDto.getSortOrder(),
					searchRequestDto);
			List<ConstraintSingle> listconstraintSingleList = page.getContent();

			for (ConstraintSingle constraintSingle : listconstraintSingleList) {
				ConstraintSingleDto constraintSingleDto = new ConstraintSingleDto();
				constraintSingleDto.setType(constraintSingle.getType());
				constraintSingleDto.setValue(constraintSingle.getValue());
				constraintSingleList.add(constraintSingleDto);
			}
			message = "Success";
			messageType = "S";
			List<ConstraintSingle> totalPageList = (List<ConstraintSingle>) constraintSingleService.findAll();
			Page<ConstraintSingle> totalPage = new PageImpl<>(totalPageList);
			constraintListDto = new PaginatedDto<ConstraintSingleDto>(constraintSingleList, page.getTotalPages(),
					page.getNumber() + 1, page.getSize(), page.getTotalElements(), totalPage.getTotalElements(),
					message, messageType);
			page.getContent();

		} catch (Exception e) {
			return new PaginatedDto<ConstraintSingleDto>(constraintSingleList, 0, 0, 0, 0, 0, "Failed", "E");
		}

		return constraintListDto;
	}

}
