package com.eikona.tech.api.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.eikona.tech.domain.Privilege;
import com.eikona.tech.domain.User;
import com.eikona.tech.dto.AuthenticationRequest;
import com.eikona.tech.dto.AuthenticationResponse;
import com.eikona.tech.dto.GrantAuthorityDto;
import com.eikona.tech.repository.UserRepository;
import com.eikona.tech.security.JwtUtil;
import com.eikona.tech.service.MyUserDetailsService;


@RestController
public class JwtAuthenticationController {
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtUtil jwtTokenUtil;
	
	@Autowired
	private MyUserDetailsService userDetailsService;
	
	@Autowired
	 private UserRepository userRepository;
	
	@RequestMapping(value="/authenticate",method=RequestMethod.POST)
	public  ResponseEntity<?> createAuthenticateToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception{
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
		}
		catch (BadCredentialsException e) {
			throw new Exception("Incorrect username or password",e);
		}
		final UserDetails userDetails=userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
	    final String jwt=jwtTokenUtil.generateToken(userDetails);
	    return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}
	
	
	@RequestMapping(value="/getprincipal",method=RequestMethod.POST)
	public  ResponseEntity<Object> getPrincipal(HttpServletRequest request){
		try {
			final String authorizationHeader=request.getHeader("Authorization");
			String username=null;
			String jwt=null;
			if(authorizationHeader!=null && authorizationHeader.startsWith("Bearer ")) {
				jwt=authorizationHeader.substring(7);
				username=jwtTokenUtil.extractUserName(jwt);
			}
			
			Optional<User> user =userRepository.findByUserName(username);
			List<Privilege> privilegegetList=user.get().getRole().getPrivileges();
			List<String> privilegesList=new ArrayList<>();
			GrantAuthorityDto grantAuthorityDto=new GrantAuthorityDto();
			for(Privilege privilege:privilegegetList) {
				privilegesList.add(privilege.getName());
			}
			grantAuthorityDto.setOrgId(user.get().getOrganization().getId());
			grantAuthorityDto.setOrganizationName(user.get().getOrganization().getName());
			grantAuthorityDto.setUsername(user.get().getUserName());
			grantAuthorityDto.setRole(user.get().getRole().getName());
			grantAuthorityDto.setPrivileges(privilegesList);
			
			return new ResponseEntity<>(grantAuthorityDto, HttpStatus.OK);
		
		}catch (Exception e) {
			String message="Contact Admin!!";
			return new ResponseEntity<>(message,HttpStatus.BAD_REQUEST);
		}
	}
}
