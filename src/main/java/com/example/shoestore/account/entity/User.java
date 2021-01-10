package com.example.shoestore.account.entity;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NamedEntityGraph(
	name= "UserComplete",
	attributeNodes= { @NamedAttributeNode(value="userRoles", subgraph="role-subgraph") },
	subgraphs= { 
		@NamedSubgraph(name = "role-subgraph", attributeNodes = {  @NamedAttributeNode("role") })
	}
)

@Entity
@SuppressWarnings("serial")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id", nullable=false, updatable=false)
	private Long id;
	
	@NotNull
	private String username;
	
	private String password;
	
	private String firstName;
	
	private String lastName;
	
	@NotNull
	@Email
	private String email;
	
	@OneToOne(cascade= CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name="address_id")
	private Address address;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIgnore
	private Set<UserRole> userRoles = new HashSet<>();
	
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<GrantedAuthority> authorites = new HashSet<>();
		userRoles.forEach(userRole -> authorites.add(new Authority(userRole.getRole().getName())));
		return authorites;
	}

	@Override
	public String toString() {
	  return getClass().getSimpleName() + "[id=" + id + "]" + "[username=" + username + "]" + "[password=" + password + "]" + "[email=" + email + "]";
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	@Override
	public boolean isEnabled() {
		return true;
	}

}
