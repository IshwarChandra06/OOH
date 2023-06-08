package com.eikona.tech.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.eikona.tech.domain.User;
import com.eikona.tech.dto.PaginatedDto;
import com.eikona.tech.dto.SearchRequestDto;
import com.eikona.tech.dto.UserDto;
import com.eikona.tech.service.common.AbstractService;

public interface UserService extends AbstractService<User, Long> {
	PaginatedDto<UserDto> searchByField(SearchRequestDto paginatedDto);

	List<User> search(String searchValue);

	Page<User> findPaginated(int pageNo, int pageSize, String sortField, String sortOrder,
			SearchRequestDto paginatedDto);
}
