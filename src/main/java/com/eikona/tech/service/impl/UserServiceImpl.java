package com.eikona.tech.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.eikona.tech.domain.User;
import com.eikona.tech.dto.PaginatedDto;
import com.eikona.tech.dto.SearchRequestDto;
import com.eikona.tech.dto.UserDto;
import com.eikona.tech.repository.UserRepository;
import com.eikona.tech.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class UserServiceImpl implements UserService {

	@Autowired
	protected UserRepository userRepository;

	@SuppressWarnings("rawtypes")
	@Autowired
	protected PaginatedServiceImpl paginatedServiceImpl;

	@Override
	public User find(Long id) {
		User result = null;

		Optional<User> catalog = userRepository.findById(id);

		if (catalog.isPresent()) {
			result = catalog.get();
		}

		return result;
	}

	@Override
	public List<User> findAll() {
		return userRepository.findAllByIsDeletedFalse();
	}

	@Override
	public User save(User entity) {
		entity.setDeleted(false);
		return userRepository.save(entity);
	}

	@Override
	public void delete(Long id) {
		User result = null;

		Optional<User> catalog = userRepository.findById(id);

		if (catalog.isPresent()) {
			result = catalog.get();
			result.setDeleted(true);
		}
		this.userRepository.save(result);
	}

	@Override
	public User update(User entity) {
		return userRepository.save(entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page<User> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection,
			SearchRequestDto paginatedDto) {

		if (null == sortDirection || sortDirection.isEmpty()) {
			sortDirection = "asc";
		}
		if (null == sortField || sortField.isEmpty()) {
			sortField = "id";
		}
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
				: Sort.by(sortField).descending();

		Specification<User> isDeleted = (root, query, cb) -> {
			return cb.equal(root.get("isDeleted"), false);
		};

		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
		Specification<User> orgSpc = paginatedServiceImpl.userOrganizationSpecification(paginatedDto.getOrgId());
		Specification<User> orgSpcByCreated = paginatedServiceImpl.usercreatedBySpecification(paginatedDto.getOrgId());

		Page<User> allUser = userRepository.findAll(isDeleted.and(orgSpcByCreated.or(orgSpc)), pageable);

		return allUser;
	}

	@SuppressWarnings("unchecked")
	public Page<User> searchByField(int pageNo, int pageSize, String sortField, String sortDirection,
			SearchRequestDto paginatedDto) {

		String[] role = { "role" };
		String[] organization = { "organization", "createByOrganization" };

		List<String> roleList = Arrays.asList(role);
		List<String> organizationList = Arrays.asList(organization);

		ObjectMapper oMapper = new ObjectMapper();
		Map<String, Object> map = oMapper.convertValue(paginatedDto.getSearchData(), Map.class);

		Map<String, String> mapString = new HashMap<String, String>();
		Map<String, String> mapRole = new HashMap<String, String>();
		Map<String, String> mapOrg = new HashMap<String, String>();
		Set<String> searchSet = map.keySet();
		for (String searchKey : searchSet) {
			if (roleList.contains(searchKey)) {
				mapRole.put(searchKey, (String) map.get(searchKey));
			} else if (organizationList.contains(searchKey)) {
				mapOrg.put(searchKey, (String) map.get(searchKey));
			} else {
				mapString.put(searchKey, (String) map.get(searchKey));
			}
		}

		if (null == sortDirection || sortDirection.isEmpty()) {
			sortDirection = "asc";
		}
		if (null == sortField || sortField.isEmpty()) {
			sortField = "id";
		}
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).descending()
				: Sort.by(sortField).ascending();

		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
		Specification<User> fieldSpc = paginatedServiceImpl.fieldSpecification(mapString);

		Specification<User> fieldSpcRole = paginatedServiceImpl.fieldSpecificationOrg(mapRole);
		Specification<User> fieldSpcOrg = paginatedServiceImpl.fieldSpecificationOrg(mapOrg);

		Specification<User> orgSpc = paginatedServiceImpl.userOrganizationSpecification(paginatedDto.getOrgId());
		Specification<User> orgSpcByCreated = paginatedServiceImpl.usercreatedBySpecification(paginatedDto.getOrgId());

		Page<User> allUser = userRepository
				.findAll(fieldSpc.and(orgSpcByCreated.or(orgSpc)).and(fieldSpcRole).and(fieldSpcOrg), pageable);//

		return allUser;
	}
	
	public PaginatedDto<UserDto> searchByField(SearchRequestDto paginatedDto){
		String message="";
		String messageType="";
		PaginatedDto<UserDto> paginatedDtoList = null;
		List<UserDto> userListDto = new ArrayList<>();
		try {
			if(null==paginatedDto.getOrgId()) {
				return new PaginatedDto<UserDto>(userListDto,
						0, 0, 0, 0, 0,"OrgId is compulsory for searching","I");
			}
			
			Page<User> page = searchByField(paginatedDto.getPageNo(), paginatedDto.getPageSize(), 
					paginatedDto.getSortField(), paginatedDto.getSortOrder(),paginatedDto);
			
			List<User> userList = page.getContent();
			
			userListDto = userList.stream().map(user ->{
				return new UserDto(user.getId(), user.getUserName(), user.isActive(), user.getRole(), user.getOrganization(), user.getPhone());
			}).collect(Collectors.toList());
			

			List<User> totaluserList = findAll();
			Page<User> totalPage = new PageImpl<User>(totaluserList);
			message = "Success";
			messageType = "S";
			paginatedDtoList = new PaginatedDto<UserDto>(userListDto,
					page.getTotalPages(), page.getNumber()+1, page.getSize(), page.getTotalElements(),totalPage.getTotalElements(),message,messageType);
			
		}catch(Exception e) {
			return new PaginatedDto<UserDto>(userListDto,
					0, 0, 0, 0, 0,"Contact Admin!!","E");
		}
		return paginatedDtoList;
	}

	@Override
	public List<User> search(String searchValue) {

		List<User> userList = userRepository.search(searchValue);
		return userList;
	}
}
