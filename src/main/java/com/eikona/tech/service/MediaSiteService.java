package com.eikona.tech.service;

import java.security.Principal;
import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.eikona.tech.domain.MediaSite;
import com.eikona.tech.dto.MediaSiteImportDto;
import com.eikona.tech.dto.SearchRequestDto;
import com.eikona.tech.service.common.AbstractService;

public interface MediaSiteService extends AbstractService<MediaSite, Long> {


	Page<MediaSite> searchByField(int pageNo, int pageSize, String sortField, String sortOrder,
			SearchRequestDto paginatedDto, Principal principal);

	Page<MediaSite> findPaginated(int pageNo, int pageSize, String sortField, String sortDir,SearchRequestDto paginatedDto);

	void exportMediaSite(HttpServletResponse response);

	void searchByFieldAndExport(SearchRequestDto paginatedDto, HttpServletResponse response);

	void importMediaSite(MultipartFile file);

	List<MediaSite> search(String searchValue);

	String importMediaSite(List<MediaSiteImportDto> mediaSiteImportDto, Principal principal) throws ParseException;

}
