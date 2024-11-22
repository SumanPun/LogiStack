package com.inventory.LogiStack;

import com.inventory.LogiStack.entity.Role;
import com.inventory.LogiStack.entity.User;
import com.inventory.LogiStack.enums.RoleEnum;
import com.inventory.LogiStack.repositories.RoleRepository;
import com.inventory.LogiStack.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@SpringBootApplication
public class LogiStackApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(LogiStackApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) throws Exception {

		Optional<Role> roleUser = roleRepository.findRoleByName(RoleEnum.ROLE_ADMIN.name());
		if(roleUser.isEmpty()){
			Role role = new Role();
			role.setName(RoleEnum.ROLE_NORMAL.name());
			roleRepository.save(role);
		}
		Optional<Role> roleManager = roleRepository.findRoleByName(RoleEnum.ROLE_MANAGER.name());
		if(roleManager.isEmpty()){
			Role role = new Role();
			role.setName(RoleEnum.ROLE_MANAGER.name());
			roleRepository.save(role);
		}
		Optional<Role> roleSupplier = roleRepository.findRoleByName(RoleEnum.ROLE_SUPPLIER.name());
		if(roleSupplier.isEmpty()){
			Role role = new Role();
			role.setName(RoleEnum.ROLE_SUPPLIER.name());
			roleRepository.save(role);
		}
		Optional<Role> roleAdmin = roleRepository.findRoleByName(RoleEnum.ROLE_ADMIN.name());
		if(roleAdmin.isEmpty()){
			Role adminRole = new Role();
			adminRole.setName(RoleEnum.ROLE_ADMIN.name());
			roleRepository.save(adminRole);
		}

		Optional<User> checkAdmin = userRepository.findByEmail("admin@test.com");
		if(checkAdmin.isEmpty()){
			User user = new User();
			user.setEmail("admin@test.com");
			user.setFirstName("Admin");
			user.setLastName("Admin");
			user.setPassword(passwordEncoder.encode("password"));
			Role adminRole = roleRepository.findRole(RoleEnum.ROLE_ADMIN.name());
			user.setRoles(Set.of(adminRole));
			user.setCreatedAt(LocalDateTime.now());
			user.setActive(true);
			userRepository.save(user);
		}
	}
}
