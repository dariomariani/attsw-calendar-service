package data;

import java.util.Objects;
import java.util.UUID;

public class User {
	private UUID Id;
	private String UserName;
	
	public User(String userName) {
		Id = UUID.randomUUID();
		UserName = userName;
	}

	public UUID getId() {
		return Id;
	}

	public String getUserName() {
		return UserName;
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
		return Objects.equals(UserName, other.UserName);
	}
	
	
}