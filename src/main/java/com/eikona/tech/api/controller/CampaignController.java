package com.eikona.tech.api.controller;

import java.security.Principal;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.eikona.tech.domain.Campaign;
import com.eikona.tech.dto.CampaignDto;
import com.eikona.tech.dto.PaginatedDto;
import com.eikona.tech.dto.SearchRequestDto;
import com.eikona.tech.service.CampaignService;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;


//@Hidden
@RestController
@RequestMapping("/api/campaign")
public class CampaignController {
	
	@Autowired
	private CampaignService campaignService;
	
	@Hidden
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public ResponseEntity<Object> list(Model model) {

		List<Campaign> campaignList = campaignService.findAll();
		return new ResponseEntity<>(campaignList, HttpStatus.OK);
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@Operation(summary = "Privileges Required: campaign_add")
	@PreAuthorize("hasAuthority('campaign_add')")
	public ResponseEntity<Object> create(@RequestBody Campaign campaign, Principal principal) {
		try {
			
		if(null==campaign.getName()||campaign.getName().isEmpty()) {
			String message="Name should not be empty.";
			return new ResponseEntity<>(message,HttpStatus.BAD_REQUEST);
		}
		Campaign createdCampaign = campaignService.save(campaign); 

		return new ResponseEntity<>(createdCampaign, HttpStatus.OK);
		}catch (Exception e) {
			String message="Contact Admin!!";
			return new ResponseEntity<>(message,HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/id/{id}", method = RequestMethod.GET)
	@Operation(summary = "Privileges Required: campaign_view")
	@PreAuthorize("hasAuthority('campaign_view')")
	public Campaign get(@PathVariable long id) {
		return campaignService.find(id);
	}

	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
	@Operation(summary = "Privileges Required: campaign_update")
	@PreAuthorize("hasAuthority('campaign_update')")
	public ResponseEntity<Object> update(@PathVariable long id, @RequestBody Campaign campaign) {
		try {
			if(null==campaign.getId()){
				return new ResponseEntity<>("Id should not be empty",HttpStatus.BAD_REQUEST);
			}
		Campaign auditCampaign=campaignService.find(id);
		campaign.setCreatedBy(auditCampaign.getCreatedBy());
		campaign.setCreatedDate(auditCampaign.getCreatedDate());
		Campaign updatedCampaign = campaignService.save(campaign);
		return new ResponseEntity<>(updatedCampaign, HttpStatus.OK);
		}
		catch (Exception e) {
			String message="Contact Admin!!";
			return new ResponseEntity<>(message,HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	@Operation(summary = "Privileges Required: campaign_delete")
	@PreAuthorize("hasAuthority('campaign_delete')")
	public ResponseEntity<Object> delete(@PathVariable(value = "id") long id) {
		try {
		campaignService.delete(id);
		return ResponseEntity.noContent().build();
		}
		catch (Exception e) {
			String message="Contact Admin!!";
			return new ResponseEntity<>(message,HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/stats", method = RequestMethod.POST)
	@Operation(summary = "Privileges Required: campaign_view")
	@PreAuthorize("hasAuthority('campaign_view')")
	public PaginatedDto<CampaignDto> findPaginated(@RequestBody SearchRequestDto paginatedDto, Principal principal) {
		PaginatedDto<CampaignDto> campaignlistDto=null;
		List<CampaignDto> campaignListDto = new ArrayList<>();
		try {
			if( 0==paginatedDto.getPageNo() || 0== paginatedDto.getPageSize() ) {
				return new PaginatedDto<CampaignDto>(campaignListDto,
						0, 0, 0, 0, 0,"pageNo or pageSize should be greater than 0","I");
			}
		Page<Campaign> page = campaignService.searchByField(paginatedDto.getPageNo(), paginatedDto.getPageSize(), paginatedDto.getSortField(), paginatedDto.getSortOrder(),paginatedDto,principal);

		 campaignlistDto = new PaginatedDto<CampaignDto>(campaignListDto,
				page.getTotalPages(), page.getNumber()+1, page.getSize(),page.getTotalElements(), page.getTotalElements(),"Success","S");
		}catch (Exception e) {
			return new PaginatedDto<CampaignDto>(campaignListDto,
					0, 0, 0, 0, 0,"Contact Admin!!","E");
		}
		return campaignlistDto;
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	@Operation(summary = "Privileges Required: campaign_view")
	@PreAuthorize("hasAuthority('campaign_view')")
	public @ResponseBody PaginatedDto<CampaignDto> search(@RequestBody SearchRequestDto paginatedDto, Principal principal) {

		String message="";
		String messageType="";
		PaginatedDto<CampaignDto> paginatedDtoList = null;
		List<CampaignDto> campaignListDto = new ArrayList<>();
		try {
			if(null==paginatedDto.getOrgId()) {
				return new PaginatedDto<CampaignDto>(campaignListDto,
						0, 0, 0, 0, 0,"OrgId is compulsory for searching","I");
			}
			Page<Campaign> page = campaignService.searchByField(paginatedDto.getPageNo(), paginatedDto.getPageSize(), 
					paginatedDto.getSortField(), paginatedDto.getSortOrder(),paginatedDto, principal);
			List<Campaign> campaignList = page.getContent();
			
			for (Campaign campaign : campaignList) {
				CampaignDto campaignDto = new CampaignDto();

				campaignDto.setId(campaign.getId());
				campaignDto.setName(campaign.getName());
				campaignDto.setOrganization(campaign.getOrganization());
				campaignDto.setAgency(campaign.getAgency());
				campaignDto.setBrand(campaign.getBrand());
				campaignDto.setStartDate(campaign.getStartDate());
				campaignDto.setEndDate(campaign.getEndDate());
				campaignDto.setOwnedAsset(campaign.isOwnedAsset());
				campaignDto.setMediasite(campaign.getMediasite());
				campaignListDto.add(campaignDto);
			}

			List<Campaign> totalcampaignList = campaignService.findAll();
			Page<Campaign> totalPage = new PageImpl<Campaign>(totalcampaignList);
			message = "Success";
			messageType = "S";
			paginatedDtoList = new PaginatedDto<CampaignDto>(campaignListDto,
					page.getTotalPages(), page.getNumber()+1, page.getSize(), page.getTotalElements(),totalPage.getTotalElements(),message,messageType);
			
		}catch(Exception e) {
			return new PaginatedDto<CampaignDto>(campaignListDto,
					0, 0, 0, 0, 0,"Contact Admin!!","E");
		}
		return paginatedDtoList;
	}
	@RequestMapping(value = "/searchbyvalue", method = RequestMethod.GET)
	@Operation(summary = "Privileges Required: campaign_view")
	@PreAuthorize("hasAuthority('campaign_view')")
	public @ResponseBody List<Campaign> searchByValue(@RequestParam String searchValue) {
		
		List<Campaign> campaignList = campaignService.search(searchValue);
		return campaignList;
		
	}
	
}
