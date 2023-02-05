package models;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Event {

	private UUID id;
	private String name;
	private LocalDateTime startsAt;
	private User owner;
	private LocalDateTime endsAt;

	public Event(String name, User owner, LocalDateTime startsAt, LocalDateTime endsAt) {
		this.name = name;
		this.startsAt = startsAt;
		this.owner = owner;
		this.endsAt = endsAt;
		this.id = UUID.randomUUID();
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
