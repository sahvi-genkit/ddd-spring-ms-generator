package <%= base_package_name %>.infrastructure.persistence;

import <%= base_package_name %>.domain.model.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Base JPA repository interface that extends Spring Data JPA's JpaRepository.
 *
 * @param <T> the type of entity this repository manages
 * @param <ID> the type of the entity's identifier
 */
@NoRepositoryBean
public interface BaseJpaRepository<T extends BaseEntity, ID> extends JpaRepository<T, ID> {
    // Add custom JPA-specific methods here if needed
}
