package models;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name="event")
public class Event extends BaseEntity {
	
	@Column(name="name", nullable=false, length = 64)
	private String name;
	
	@Column(name="starts_at", nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime startsAt;
	
	@Column(name="ends_at", nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime endsAt;
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "owner_id", referencedColumnName = "id")
	private User owner;

	public Event(String name, User owner, LocalDateTime startsAt, LocalDateTime endsAt) {
		this.name = name;
		this.startsAt = startsAt;
		this.owner = owner;
		this.endsAt = endsAt;
	}
	
	public Event(UUID id) {
		this.id = id;
	}
	
	public Event() {
		
	}

	public LocalDateTime getStartsAt() {
		return startsAt;
	}

	public void setStartsAt(LocalDateTime startsAt) {
		this.startsAt = startsAt;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public LocalDateTime getEndsAt() {
		return endsAt;
	}
	
	public void setEndsAt(LocalDateTime endsAt) {
		this.endsAt = endsAt;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Event other = (Event) obj;
		return Objects.equals(id, other.id); 
	}

	@Override
	public String toString() {
		return "Event [id = " + id + ", name = " + name + "]";
	}

}
