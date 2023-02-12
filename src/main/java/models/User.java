package models;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="user")
public class User {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	private UUID id;
	
	@Column(name="username", nullable=false, unique=true, length = 64)
	private String userName;
	
	@OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Event> events;

	public User(String userName) {
		this.userName = userName;
        this.id = UUID.randomUUID();
	}
	
	public User() {
	}

	public UUID getId() {
		return id;
	}

	public String getUserName() {
		return userName;
	}

	public List<Event> getEvents() {
		return events;
	}

	public void setEvents(List<Event> events) {
		this.events = events;
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
		return Objects.equals(userName, other.userName);
	}
	
	
}