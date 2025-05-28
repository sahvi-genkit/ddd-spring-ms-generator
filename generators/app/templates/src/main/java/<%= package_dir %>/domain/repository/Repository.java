package <%= base_package_name %>.domain.repository;

import <%= base_package_name %>.domain.model.BaseEntity;
import java.util.List;
import java.util.Optional;

/**
 * Base repository interface for domain entities.
 *
 * @param <T> the type of entity this repository manages
 * @param <ID> the type of the entity's identifier
 */
public interface Repository<T extends BaseEntity, ID> {
    T save(T entity);
    Optional<T> findById(ID id);
    List<T> findAll();
    void delete(T entity);
    void deleteById(ID id);
    boolean existsById(ID id);
} 