package com.eikona.tech.security;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.eikona.tech.domain.Privilege;
import com.eikona.tech.domain.Role;
import com.eikona.tech.domain.User;
import com.eikona.tech.repository.PrivilegeRepository;
import com.eikona.tech.repository.RoleRepository;
import com.eikona.tech.repository.UserRepository;

@Service
public class DBSeeder implements CommandLineRunner {
	
	 private UserRepository userRepository;
	 
	 private PrivilegeRepository privilegeRepository;
	 
	 private RoleRepository roleRepository;
	 
     private PasswordEncoder passwordEncoder;
     
     public DBSeeder(PrivilegeRepository privilegeRepository,RoleRepository roleRepository,UserRepository userRepository, PasswordEncoder passwordEncoder) {
    	 this.privilegeRepository=privilegeRepository;
    	 this.roleRepository=roleRepository;
    	 this.userRepository = userRepository;
         this.passwordEncoder = passwordEncoder;
     }

	@Override
	public void run(String... args) throws Exception {
	List<Privilege> privilegeList = privilegeRepository.findAllByIsDeletedFalse();
		
		if(null==privilegeList || privilegeList.isEmpty()) {
			List<Privilege> privileges = SeedPrivileges();
			Role admin = seedRole(privileges);
			seedUser(admin);
		}
	}
	
	
	private List<Privilege> SeedPrivileges() {
		
		
		Privilege organizationView = new Privilege("organization_view", false);
		Privilege organizationCreate = new Privilege("organization_add", false);
		Privilege organizationUpdate = new Privilege("organization_update", false);
		Privilege organizationDelete = new Privilege("organization_delete", false);
		
		Privilege campaignView = new Privilege("campaign_view", false);
		Privilege campaignCreate = new Privilege("campaign_add", false);
		Privilege campaignUpdate = new Privilege("campaign_update", false);
		Privilege campaignDelete = new Privilege("campaign_delete", false);
		
		Privilege mediasiteView = new Privilege("mediasite_view", false);
		Privilege mediasiteCreate = new Privilege("mediasite_add", false);
		Privilege mediasiteUpdate = new Privilege("mediasite_update", false);
		Privilege mediasiteDelete = new Privilege("mediasite_delete", false);
		
		Privilege mediaReportView = new Privilege("mediareport_view", false);
		Privilege campaignReportView = new Privilege("campaignreport_view", false);
		
		Privilege siteeventView = new Privilege("siteevent_view", false);
		Privilege siteeventCreate = new Privilege("siteevent_add", false);
		Privilege siteeventUpdate = new Privilege("siteevent_update", false);
		Privilege siteeventDelete = new Privilege("siteevent_delete", false);
		Privilege siteeventreportView = new Privilege("siteeventreport_view", false);
		
		Privilege constraintsingleView = new Privilege("constraintsingle_view", false);
		Privilege constraintsingleCreate = new Privilege("constraintsingle_add", false);
		Privilege constraintsingleUpdate = new Privilege("constraintsingle_update", false);
		Privilege constraintsingleDelete = new Privilege("constraintsingle_delete", false);
		
		Privilege constraintrangeView = new Privilege("constraintrange_view", false);
		Privilege constraintrangeCreate = new Privilege("constraintrange_add", false);
		Privilege constraintrangeUpdate = new Privilege("constraintrange_update", false);
		Privilege constraintrangeDelete = new Privilege("constraintrange_delete", false);
		
		Privilege userView = new Privilege("user_view", false);
		Privilege userCreate = new Privilege("user_add", false);
		Privilege userUpdate = new Privilege("user_update", false);
		Privilege userDelete = new Privilege("user_delete", false);
		
		Privilege roleView = new Privilege("role_view", false);
		Privilege roleCreate = new Privilege("role_add", false);
		Privilege roleUpdate = new Privilege("role_update", false);
		Privilege roleDelete = new Privilege("role_delete", false);
		
		Privilege privilegeCreate = new Privilege("privilege_add", false);
		Privilege privilegeView = new Privilege("privilege_view", false);
		Privilege privilegeUpdate = new Privilege("privilege_update", false);
		Privilege privilegeDelete = new Privilege("privilege_delete", false);
		
		List<Privilege> privileges = Arrays.asList(
				organizationView,organizationCreate,organizationUpdate,organizationDelete,
				campaignView,campaignCreate,campaignUpdate,campaignDelete,
				mediasiteView,mediasiteCreate,mediasiteUpdate,mediasiteDelete,
				
				mediaReportView,campaignReportView,
				
				siteeventView,siteeventCreate,siteeventUpdate,siteeventDelete,siteeventreportView,
				constraintsingleView,constraintsingleCreate,constraintsingleUpdate,constraintsingleDelete,
				constraintrangeView,constraintrangeCreate,constraintrangeUpdate,constraintrangeDelete,
				
				
				userView, userCreate, userUpdate, userDelete,
				roleView, roleCreate, roleUpdate, roleDelete,
				privilegeView, privilegeCreate, privilegeUpdate,privilegeDelete
		);
		
		privilegeRepository.saveAll(privileges);
		
		return privileges;
	}

	private Role seedRole(List<Privilege> privileges) {
		Role admin=roleRepository.findByName("Admin");
		if(null==admin) {
			 admin= new Role("Admin", privileges, false);
			roleRepository.save(admin);
		}
		return admin;
	}

	private void seedUser(Role admin) {
		List<User> userList=userRepository.findAllByIsDeletedFalse();
		if(null==userList || userList.isEmpty()) {
			User adminUser= new User("Superadmin", passwordEncoder.encode("Admin@123"), true, admin, false);
			
			userRepository.save(adminUser);
		}
	}
}
