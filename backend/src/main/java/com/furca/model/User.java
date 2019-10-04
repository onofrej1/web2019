package com.furca.model;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import java.util.Set;

@Entity
@Table(name = "user")
public class User implements Specifiable {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
 
    @Column(name="username", nullable = false, unique = true)
    @NotNull
    private String username;
 
    @Column(name="password", nullable = false)
    @NotNull
    private String password;
    
    @Column(name="email", nullable = false, unique = true)
    @NotNull
    @Email
    private String email;
    
    @Column(name="name", nullable = false)
    @NotNull
    private String name;
    
    private Set<Role> roles;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
    
	@ManyToMany
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}