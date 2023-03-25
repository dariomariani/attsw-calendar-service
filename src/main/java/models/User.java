package models;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="app_user")
public class User extends BaseEntity {
	
	@Column(name="username", nullable=false, unique=true, length = 64)
	private String username;
	
	@OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Event> events;

	public User(String username) {
		this.username = username;
	}
	
	public User(UUID id, String username) {
		this.id = id;
		this.username = username;
	}
	
	public User() {
	}

	public String getUsername() {
		return username;
	}

	public void setEvents(List<Event> events) {
		this.events = events;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(username, other.username);
	}

	@Override
	public String toString() {
		return "User [id = " + id + ", userName = " + username + ", events = " + events + "]";
	}
	
	
	
}