package com.eikona.tech.service;

import org.springframework.data.domain.Page;

import com.eikona.tech.domain.MultimediaLog;
import com.eikona.tech.dto.SearchRequestDto;
import com.eikona.tech.service.common.AbstractService;

public interface MultimediaLogService extends AbstractService<MultimediaLog, Long> {

	/**
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @param sortField
	 * @param sortOrder
	 * @return
	 */
	Page<MultimediaLog> findPaginated(int pageNo, int pageSize, String sortField, String sortOrder);

	/**
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @param sortField
	 * @param sortOrder
	 * @param paginatedDto
	 * @return
	 */
	Page<MultimediaLog> searchByField(int pageNo, int pageSize, String sortField, String sortOrder,
			SearchRequestDto paginatedDto);

	/**
	 * 
	 */
	void multimediaProcessingFromDirectory();

	/**
	 * 
	 * @return
	 */
	@Deprecated
	String dataFromFtpToDataBase();

}
