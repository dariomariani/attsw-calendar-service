package models;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import org.hibernate.annotations.UuidGenerator;
import org.hibernate.annotations.UuidGenerator.Style;

import jakarta.persistence.ManyToOne;

@Entity
@Table(name="event")
public class Event {
	
	@Id
    @GeneratedValue
    @UuidGenerator(style = Style.AUTO)
	private UUID id;
	
	@Column(name="name", nullable=false, length = 64)
	private String name;
	
	@Column(name="starts_at", nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime startsAt;
	
	@Column(name="ends_at", nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime endsAt;
	
	@ManyToOne
	@JoinColumn(name = "owner_id", referencedColumnName = "id")
	private User owner;

	public Event(String name, User owner, LocalDateTime startsAt, LocalDateTime endsAt) {
		this.name = name;
		this.startsAt = startsAt;
		this.owner = owner;
		this.endsAt = endsAt;
        this.id = UUID.randomUUID();
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

	public UUID getId() {
		return id;
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

}
