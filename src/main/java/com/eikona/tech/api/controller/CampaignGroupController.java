package com.eikona.tech.api.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.eikona.tech.domain.CampaignGroup;
import com.eikona.tech.dto.CampaignGroupDto;
import com.eikona.tech.dto.PaginatedDto;
import com.eikona.tech.dto.SearchRequestDto;
import com.eikona.tech.service.CampaignGroupService;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@RestController
@RequestMapping("/api/campaign-group")
public class CampaignGroupController {
	
	@Autowired
	private CampaignGroupService campaignGroupService;

	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public ResponseEntity<Object> list(Model model) {

		List<CampaignGroup> campaignList = campaignGroupService.findAll();
		return new ResponseEntity<>(campaignList, HttpStatus.OK);
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<CampaignGroup> create(@RequestBody CampaignGroup campaign) {
		CampaignGroup createdCampaign = campaignGroupService.save(campaign);

		return new ResponseEntity<CampaignGroup>(createdCampaign, HttpStatus.OK);
	}

	@RequestMapping(value = "/id/{id}", method = RequestMethod.GET)
	public CampaignGroup get(@PathVariable long id) {
		return campaignGroupService.find(id);
	}

	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
	public ResponseEntity<CampaignGroup> update(@PathVariable long id, @RequestBody CampaignGroup campaign) {
		CampaignGroup updatedCampaign = campaignGroupService.save(campaign);

		return new ResponseEntity<CampaignGroup>(updatedCampaign, HttpStatus.OK);
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@PathVariable(value = "id") long id) {
		campaignGroupService.delete(id);
		return ResponseEntity.noContent().build();
	}

	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public PaginatedDto<CampaignGroupDto> findPaginated(@RequestBody SearchRequestDto paginatedDto) {

		Page<CampaignGroup> page = campaignGroupService.findPaginated(paginatedDto.getPageNo(), paginatedDto.getPageSize(), paginatedDto.getSortField(), paginatedDto.getSortOrder());

		List<CampaignGroup> campaignList = page.getContent();
		List<CampaignGroupDto> campaignListDto = new ArrayList<>();
		for (CampaignGroup campaign : campaignList) {
			CampaignGroupDto campaignDto = new CampaignGroupDto();
			campaignDto.setId(campaign.getId());
			campaignDto.setName(campaign.getName());
			campaignDto.setCampaign(campaign.getCampaign());
			campaignListDto.add(campaignDto);
		}

		List<CampaignGroup> totalcampaignList = campaignGroupService.findAll();
		Page<CampaignGroup> totalPage = new PageImpl<CampaignGroup>(totalcampaignList);
		
		PaginatedDto<CampaignGroupDto> campaignlistDto = new PaginatedDto<CampaignGroupDto>(campaignListDto,
				page.getTotalPages(), page.getNumber(), page.getSize(), page.getTotalElements(),totalPage.getTotalElements(),"Success","");
		page.getContent();

		System.out.println();
		return campaignlistDto;
	}

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public @ResponseBody PaginatedDto<CampaignGroupDto> search(@RequestBody SearchRequestDto paginatedDto) {

		String message="";
		String messageType="";
		PaginatedDto<CampaignGroupDto> paginatedDtoList = null;
		try {
			
			Page<CampaignGroup> page = campaignGroupService.searchByField(paginatedDto.getPageNo(), paginatedDto.getPageSize(), 
					paginatedDto.getSortField(), paginatedDto.getSortOrder(),paginatedDto);
			List<CampaignGroup> campaignList = page.getContent();
			List<CampaignGroupDto> campaignListDto = new ArrayList<>();
			for (CampaignGroup campaign : campaignList) {
				CampaignGroupDto campaignDto = new CampaignGroupDto();
				campaignDto.setId(campaign.getId());
				campaignDto.setName(campaign.getName());
				campaignDto.setCampaign(campaign.getCampaign());
				campaignListDto.add(campaignDto);
			}

			List<CampaignGroup> totalcampaignList = campaignGroupService.findAll();
			Page<CampaignGroup> totalPage = new PageImpl<CampaignGroup>(totalcampaignList);
			message = "Success";
			messageType = "";
			paginatedDtoList = new PaginatedDto<CampaignGroupDto>(campaignListDto,
					page.getTotalPages(), page.getNumber(), page.getSize(), page.getTotalElements(),totalPage.getTotalElements(),"Success","");
			
		}catch(Exception e) {
			System.out.println(e);
			message = "Failed";
			messageType = "";
			paginatedDtoList = new PaginatedDto<CampaignGroupDto>(message,messageType);
		}
		return paginatedDtoList;
	}
}
