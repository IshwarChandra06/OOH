package com.eikona.tech.api.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.eikona.tech.domain.Organization;
import com.eikona.tech.domain.User;
import com.eikona.tech.dto.PaginatedDto;
import com.eikona.tech.dto.SearchRequestDto;
import com.eikona.tech.dto.UserDto;
import com.eikona.tech.dto.UserResetDto;
import com.eikona.tech.repository.UserRepository;
import com.eikona.tech.service.UserService;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private AuthenticationManager authenticationManager;
    
	@Hidden
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public ResponseEntity<Object> list(Model model) {
		List<User> orgList = userService.findAll();
		return new ResponseEntity<>(orgList, HttpStatus.OK);
	}
 
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@Operation(summary = "Privileges Required: user_add")
	@PreAuthorize("hasAuthority('user_add')")
	public ResponseEntity<Object> create(@RequestBody User user, Principal principal) {
		
		try {
			
			Optional<User> userObj =userRepository.findByUserName(principal.getName());
			Organization org=userObj.get().getOrganization();
			user.setCreateByOrganization(org);
			
			if (null == user.getUserName() || user.getUserName().isEmpty() || user.getPassword().isEmpty()
					|| null == user.getPassword()) {
				String message = "Username or password should not be empty.";
				return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
			}
			
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			User createdUser = userService.save(user);
			
			return new ResponseEntity<>(createdUser, HttpStatus.OK);
		} catch (Exception e) {
			String message = "Contact Admin!!";
			return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/id/{id}", method = RequestMethod.GET)
	@Operation(summary = "Privileges Required: user_view")
	@PreAuthorize("hasAuthority('user_view')")
	public User get(@PathVariable long id) {
		return userService.find(id);
	}

	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
	@Operation(summary = "Privileges Required: user_update")
	@PreAuthorize("hasAuthority('user_update')")
	public ResponseEntity<Object> update(@PathVariable long id, @RequestBody User user) {
		try {
			
			if (null == user.getId()) {
				return new ResponseEntity<>("Id should not be empty", HttpStatus.BAD_REQUEST);
			}
			User userAudit = userService.find(id);
			user.setCreateByOrganization(userAudit.getCreateByOrganization());
			user.setCreatedBy(userAudit.getCreatedBy());
			user.setCreatedDate(userAudit.getCreatedDate());
			user.setPassword(userAudit.getPassword());
			User updatedUser = userService.save(user);
			return new ResponseEntity<>(updatedUser, HttpStatus.OK);
		} catch (Exception e) {
			String message = "Contact Admin!!";
			return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	@Operation(summary = "Privileges Required: user_delete")
	@PreAuthorize("hasAuthority('user_delete')")
	public ResponseEntity<Object> delete(@PathVariable(value = "id") long id) {
		try {
			userService.delete(id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			String message = "Contact Admin!!";
			return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/stats", method = RequestMethod.POST)
	@Operation(summary = "Privileges Required: user_view")
	@PreAuthorize("hasAuthority('user_view')")
	public PaginatedDto<UserDto> findPaginated(@RequestBody SearchRequestDto paginatedDto) {
		PaginatedDto<UserDto> userlistDto=null;
		List<UserDto> userListDto = new ArrayList<>();
		try {
			if( 0==paginatedDto.getPageNo() || 0== paginatedDto.getPageSize() ) {
				return new PaginatedDto<UserDto>(userListDto,
						0, 0, 0, 0, 0,"pageNo or pageSize should be greater than 0","I");
			}

		userlistDto = userService.searchByField(paginatedDto);
		
		}catch (Exception e) {
			return new PaginatedDto<UserDto>(userListDto,
					0, 0, 0, 0, 0,"Contact Admin!!","E");
		}
		return userlistDto;
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	@Operation(summary = "Privileges Required: user_view")
	@PreAuthorize("hasAuthority('user_view')")
	public @ResponseBody PaginatedDto<UserDto> search(@RequestBody SearchRequestDto paginatedDto ) {
		
		return userService.searchByField(paginatedDto);
	}
	
	@RequestMapping(value = "/searchbyvalue", method = RequestMethod.GET)
	@Operation(summary = "Privileges Required: user_view")
	@PreAuthorize("hasAuthority('user_view')")
	public @ResponseBody List<User> searchByValue(@RequestParam String searchValue) {
		
		return userService.search(searchValue);
		
	}
	
	@RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
	//@Operation(summary = "Privileges Required: user_update")
	//@PreAuthorize("hasAuthority('user_update')")
	public ResponseEntity<Object> resetPassword(@RequestBody UserResetDto userResetDto, Principal principal) {
		try {
			if(!(userResetDto.getNewPassword()).equals((userResetDto.getConfirmNewPassword()))) {
				String message = "New password didn't match with Confirm password";
				return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
			}
			User user=userRepository.findByUserName(principal.getName()).get();
			try {
				authenticationManager.authenticate(
						new UsernamePasswordAuthenticationToken(user.getUserName(),userResetDto.getOldPassword() ));
			}
			catch (BadCredentialsException e) {
				String message = "Input password or UserName did not match with respective username.";
				return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
			}
			
			user.setPassword(passwordEncoder.encode(userResetDto.getNewPassword()));
			userRepository.save(user);
			
			return new ResponseEntity<>(user, HttpStatus.OK);
		} catch (Exception e) {
			String message = "Contact Admin!!";
			return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
		}
	}
}
