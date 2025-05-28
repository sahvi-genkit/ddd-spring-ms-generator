package <%= base_package_name %>.domain.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Base test class for domain entities.
 * Extend this class to test specific entity implementations.
 */
public abstract class BaseEntityTest<T extends BaseEntity> {

    protected abstract T createEntity();
    protected abstract void assertEntityEquals(T expected, T actual);

    @Test
    void testEntityCreation() {
        T entity = createEntity();
        assertNotNull(entity);
        assertTrue(entity.isActive());
        assertNull(entity.getId());
        assertNull(entity.getCreatedAt());
        assertNull(entity.getUpdatedAt());
        assertNull(entity.getCreatedBy());
        assertNull(entity.getUpdatedBy());
    }

    @Test
    void testEntityAuditFields() {
        T entity = createEntity();
        LocalDateTime now = LocalDateTime.now();
        String user = "testUser";

        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);
        entity.setCreatedBy(user);
        entity.setUpdatedBy(user);

        assertEquals(now, entity.getCreatedAt());
        assertEquals(now, entity.getUpdatedAt());
        assertEquals(user, entity.getCreatedBy());
        assertEquals(user, entity.getUpdatedBy());
    }

    @Test
    void testEntityActiveStatus() {
        T entity = createEntity();
        assertTrue(entity.isActive());

        entity.setActive(false);
        assertFalse(entity.isActive());
    }

    @Test
    void testEntityEquality() {
        T entity1 = createEntity();
        T entity2 = createEntity();
        
        // Set same ID to test equality
        Long id = 1L;
        entity1.setId(id);
        entity2.setId(id);

        assertEntityEquals(entity1, entity2);
    }
} 