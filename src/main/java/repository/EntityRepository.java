package repository;

import java.util.List;
import java.util.UUID;

public interface EntityRepository<T> {
	
	UUID save(T entity);

	T findById(UUID id, Class<T> entityType);

	List<T> findAll(Class<T> entityType);
}
