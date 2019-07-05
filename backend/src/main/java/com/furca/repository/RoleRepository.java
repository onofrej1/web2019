package com.furca.repository;

import com.furca.model.Role;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoleRepository extends JpaRepository<Role, Long>{
	
	@Query("SELECT t FROM Role t WHERE t.name IN :roles")
	Set<Role> findByRoleNames(@Param("roles") Set<String> roles);
}
