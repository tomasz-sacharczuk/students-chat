package com.example.studentschat.entity.user;

import java.util.*;

import javax.persistence.*;

import com.example.studentschat.entity.ChangeGroupRequest;
import com.example.studentschat.entity.Group;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Getter
@Setter
@Table(name = "users")
@NoArgsConstructor
public class User implements UserDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "user_id")
	private Long id;
	private String username;
	private String password;
	private String name;
	private String surname;
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "users_roles",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id")
	)
	private Set<Role> roles = new HashSet<>();

	@OneToOne(mappedBy="requestedByUser")
	private ChangeGroupRequest changeGroupByRequest;

	@OneToOne(mappedBy="requestedToUser")
	private ChangeGroupRequest changeGroupToRequest;

	@ManyToOne
	@JoinColumn(name = "group_id")
	private Group group;

	public User(String username, String password, Group group, String name, String surname) {
		this.name = name;
		this.surname = surname;
		this.username = username;
		this.password = password;
		this.group = group;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<Role> roles = getRoles();
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();

		for (Role role : roles) {
			authorities.add(new SimpleGrantedAuthority(role.getName()));
		}
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
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

	@Override
	public String toString() {
		return "User{" +
				"name='" + name + '\'' +
				", surname='" + surname + '\'' +
				'}';
	}
}
