package data;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Event {

	private UUID Id;
	private String Name;
	private LocalDateTime StartsAt;
	private User Owner;
	private LocalDateTime EndsAt;

	public Event(String name, User owner, LocalDateTime startsAt, LocalDateTime endsAt) {
		this.Name = name;
		this.StartsAt = startsAt;
		this.Owner = owner;
		this.EndsAt = endsAt;
		this.Id = UUID.randomUUID();
	}

	public LocalDateTime getStartsAt() {
		return StartsAt;
	}

	public void setStartsAt(LocalDateTime startsAt) {
		StartsAt = startsAt;
	}

	public User getOwner() {
		return Owner;
	}

	public LocalDateTime getEndsAt() {
		return EndsAt;
	}
	
	public void setEndsAt(LocalDateTime endsAt) {
		EndsAt = endsAt;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getName() {
		return Name;
	}

	public UUID getId() {
		return Id;
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
		return Objects.equals(Id, other.Id); 
	}

}
