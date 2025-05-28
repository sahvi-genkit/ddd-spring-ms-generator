package <%= base_package_name %>.application.service;

import <%= base_package_name %>.domain.model.BaseEntity;
import <%= base_package_name %>.domain.repository.Repository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

/**
 * Base service class for application services.
 *
 * @param <T> the type of entity this service manages
 * @param <ID> the type of the entity's identifier
 * @param <R> the type of repository this service uses
 */
@RequiredArgsConstructor
public abstract class BaseService<T extends BaseEntity, ID, R extends Repository<T, ID>> {
    
    protected final R repository;

    public T save(T entity) {
        return repository.save(entity);
    }

    public Optional<T> findById(ID id) {
        return repository.findById(id);
    }

    public List<T> findAll() {
        return repository.findAll();
    }

    public void delete(T entity) {
        repository.delete(entity);
    }

    public void deleteById(ID id) {
        repository.deleteById(id);
    }

    public boolean existsById(ID id) {
        return repository.existsById(id);
    }
} 