package repository;

import java.util.List;
import java.util.UUID;

public interface EntityRepository<T> {
	
	UUID save(T entity);

	<T> T findById(UUID id, Class<T> entityType);

	<T> List<T> findAll(Class<T> entityType);
}
