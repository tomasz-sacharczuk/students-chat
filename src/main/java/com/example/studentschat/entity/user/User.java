package com.example.studentschat.entity.user;

import com.example.studentschat.entity.ChangeGroupRequest;
import com.example.studentschat.entity.Group;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@Setter
@Table(name = "users")
@NoArgsConstructor
public class User implements UserDetails {

	public static final String POLE_NIE_MOZE_BYC_PUSTE = "Pole nie może być puste";
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "user_id")
	private Long id;

	@NotNull(message = POLE_NIE_MOZE_BYC_PUSTE)
	@Size(min=2, message = "Login musi mieć co najmniej 2 znaki")
	private String username;

	@NotNull(message = POLE_NIE_MOZE_BYC_PUSTE)
	@Size(min=4, message = "Hasło musi mieć co najmniej 4 znaki")
	private String password;

	@NotNull(message = POLE_NIE_MOZE_BYC_PUSTE)
	@Size(min=2, message = "Imię musi mieć co najmniej 2 znaki")
	private String name;

	@NotNull(message = POLE_NIE_MOZE_BYC_PUSTE)
	@Size(min=3, message = "Nazwisko musi mieć co najmniej 3 znaki")
	private String surname;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "users_roles",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id")
	)
	private Set<Role> roles = new HashSet<>();

	@OneToMany(mappedBy="requestedByUser")
	private Set<ChangeGroupRequest> changeGroupByRequests = new HashSet<>();

	@OneToMany(mappedBy="requestedToUser")
	private Set<ChangeGroupRequest> changeGroupToRequests = new HashSet<>();

	@Transient
	private Long groupId;

	@ManyToOne
	@JoinColumn(name = "group_id")
	private Group group;

	private LocalDateTime chatBanEndTime;

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

	public boolean anyActiveRequestExists() {
		boolean activeRequestExist = false;
		for(ChangeGroupRequest req: changeGroupByRequests){
			if(req.getStatus()==ChangeGroupRequest.STATUS_NEW ||
					req.getStatus()==ChangeGroupRequest.STATUS_ACCEPTED_BY_USER){
				activeRequestExist=true;
			}
		};

		for(ChangeGroupRequest req: changeGroupToRequests){
			if(req.getStatus()==ChangeGroupRequest.STATUS_NEW ||
					req.getStatus()==ChangeGroupRequest.STATUS_ACCEPTED_BY_USER){
				activeRequestExist=true;
			}
		};
		return activeRequestExist;
	}

	public String getHiddenSurname(){
		return surname.replaceAll(".","*").replaceFirst(".",surname.substring(0,1));
	}
}
