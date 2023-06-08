package com.eikona.tech.api.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.eikona.tech.domain.MediaSite;
import com.eikona.tech.domain.MultimediaLog;
import com.eikona.tech.domain.User;
import com.eikona.tech.dto.ImageLogDto;
import com.eikona.tech.dto.PaginatedDto;
import com.eikona.tech.dto.SearchRequestDto;
import com.eikona.tech.repository.MediaSiteRepository;
import com.eikona.tech.repository.UserRepository;
import com.eikona.tech.service.MultimediaLogService;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;


@RestController
@RequestMapping("/api/assetevent")
public class SiteEventController {
	
	@Autowired
	private MultimediaLogService imageLogService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private MediaSiteRepository mediaSiteRepository;
	

	@Hidden
	@GetMapping("/all")
	public List<MultimediaLog> getAllImageLogs(){
		return imageLogService.findAll();
	}
	
	@GetMapping("/id/{id}")
	@Operation(summary = "Privileges Required: siteevent_view")
	@PreAuthorize("hasAuthority('siteevent_view')")
	public MultimediaLog getImageLog(@PathVariable long id){
		return imageLogService.find(id);
	}
	
	@PostMapping("/create")
	@PreAuthorize("hasAuthority('siteevent_add')")
	@Operation(summary = "Privileges Required: siteevent_add")
	public ResponseEntity<Object> createImageLog(@RequestParam String imageUrl, @RequestParam Long mediaSite, @RequestParam Long user,
			String timeStamp, @RequestParam("imagefile") MultipartFile file){
		try {
		MultimediaLog imageLog = new MultimediaLog();
		Optional<MediaSite> mediaSiteObj = mediaSiteRepository.findById(mediaSite);
		Optional<User> userObj = userRepository.findById(user);
		
		if(mediaSiteObj.isPresent()) {
			imageLog.setMediaSite(mediaSiteObj.get());
		}
		if(userObj.isPresent()) {
			imageLog.setUser(userObj.get());
		}
		
		imageLog.setImageUrl(imageUrl);
		
		Byte[] byteObjects = convertToBytes(file);
		byte[] bytes = ArrayUtils.toPrimitive(byteObjects);
		imageLog.setImage(Base64.getEncoder().encodeToString(bytes));
		SimpleDateFormat date=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			imageLog.setTimeStamp(date.parse(timeStamp));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MultimediaLog createdImageLog = imageLogService.save(imageLog);
		
		return new ResponseEntity<>(createdImageLog, HttpStatus.OK);
	}

	catch (Exception e) {
		System.out.println(e);
		String message="Contact Admin!!";
		return new ResponseEntity<>(message,HttpStatus.BAD_REQUEST);
	}
	}
	
	private Byte[] convertToBytes(MultipartFile file) {
        Byte[] byteObjects = null;
        try {
        	byteObjects = new Byte[file.getBytes().length];
        	int i = 0;
       
			for (byte b : file.getBytes()) {
			    byteObjects[i++] = b;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
        return byteObjects;
    }
	
	@PutMapping("/update/{id}")
	@PreAuthorize("hasAuthority('siteevent_update')")
	@Operation(summary = "Privileges Required: siteevent_update")
	public ResponseEntity<Object> updateImageLog(@PathVariable long id,
			@RequestBody MultimediaLog imageLog){
		try {
			if(null==imageLog.getId()){
				return new ResponseEntity<>("Id should not be empty",HttpStatus.BAD_REQUEST);
			}
			MultimediaLog auditImageLog=imageLogService.find(id);
			imageLog.setCreatedBy(auditImageLog.getCreatedBy());
			imageLog.setCreatedDate(auditImageLog.getCreatedDate());
		MultimediaLog updatedImageLog = imageLogService.save(imageLog);
		
		return new ResponseEntity<>(updatedImageLog, HttpStatus.OK) ;
		}
		catch (Exception e) {
			String message="Contact Admin!!";
			return new ResponseEntity<>(message,HttpStatus.BAD_REQUEST);
		}
	}
	
	@DeleteMapping("/delete/{id}")
	@Operation(summary = "Privileges Required: siteevent_delete")
	@PreAuthorize("hasAuthority('siteevent_delete')")
	public ResponseEntity<Object> deleteImageLog(@PathVariable long id){
		try {
		imageLogService.delete(id);
		return ResponseEntity.noContent().build();
		}
		catch (Exception e) {
			String message="Contact Admin!!";
			return new ResponseEntity<>(message,HttpStatus.BAD_REQUEST);
		}
	}
	
	@Hidden
	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public PaginatedDto<ImageLogDto> findPaginated(@RequestBody SearchRequestDto paginatedDto,
			Model model) {

		Page<MultimediaLog> page = imageLogService.findPaginated(paginatedDto.getPageNo(), paginatedDto.getPageSize(), paginatedDto.getSortField(), paginatedDto.getSortOrder());

		List<MultimediaLog> imageLogList = page.getContent();
		List<ImageLogDto> imageLogDtoList = new ArrayList<>();
		for (MultimediaLog imageLog : imageLogList) {
			ImageLogDto imageLogDto = new ImageLogDto();
			
			imageLogDto.setImageUrl(imageLog.getImageUrl());
			imageLogDto.setMediaSite(imageLog.getMediaSite());
			imageLogDto.setTimeStamp(imageLog.getTimeStamp());
			imageLogDto.setUser(imageLog.getUser());
			imageLogDto.setImage(imageLog.getImage());

			imageLogDtoList.add(imageLogDto);
		}

		List<MultimediaLog> totalList = imageLogService.findAll();
		Page<MultimediaLog> totalPage = new PageImpl<MultimediaLog>(totalList);
		
		PaginatedDto<ImageLogDto> paginatedDtoList = new PaginatedDto<ImageLogDto>(imageLogDtoList,
				page.getTotalPages(), page.getNumber(), page.getSize(), page.getTotalElements(),totalPage.getTotalElements(),"Success","");

		return paginatedDtoList;
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	@Operation(summary = "Privileges Required: siteevent_view")
	@PreAuthorize("hasAuthority('siteevent_view')")
	public @ResponseBody PaginatedDto<ImageLogDto> search(@RequestBody SearchRequestDto paginatedDto) {

		String message="";
		String messageType="";
		PaginatedDto<ImageLogDto> paginatedDtoList = null;
		List<ImageLogDto> imageLogDtoList = new ArrayList<>();
		try {
			
			Page<MultimediaLog> page = imageLogService.searchByField(paginatedDto.getPageNo(), paginatedDto.getPageSize(), 
					paginatedDto.getSortField(), paginatedDto.getSortOrder(),paginatedDto);
			List<MultimediaLog> imageLogList = page.getContent();
			

			for (MultimediaLog imageLog : imageLogList) {
				ImageLogDto imageLogDto = new ImageLogDto();
				
				imageLogDto.setImageUrl(imageLog.getImageUrl());
				imageLogDto.setMediaSite(imageLog.getMediaSite());
				imageLogDto.setTimeStamp(imageLog.getTimeStamp());
				imageLogDto.setUser(imageLog.getUser());
				imageLogDto.setImage(imageLog.getImage());

				imageLogDtoList.add(imageLogDto);
			}

			List<MultimediaLog> totalList = imageLogService.findAll();
			Page<MultimediaLog> totalPage = new PageImpl<MultimediaLog>(totalList);
			message = "Success";
			messageType = "S";
			paginatedDtoList = new PaginatedDto<ImageLogDto>(imageLogDtoList, page.getTotalPages(), page.getNumber()+1, page.getSize(), page.getTotalElements(),
					totalPage.getTotalElements(), message, messageType);
			
		}catch(Exception e) {
			return new PaginatedDto<ImageLogDto>(imageLogDtoList,
					0, 0, 0, 0, 0,"Contact Admin!!","E");
		}
		return paginatedDtoList;
	}
	@RequestMapping(value = "/from-ftp", method = RequestMethod.GET)
	public String list(Model model) throws IOException {
		String message=imageLogService.dataFromFtpToDataBase();
		return message;
	}
	
	
	
	
}
