package <%= base_package_name %>.infrastructure.persistence;

import <%= base_package_name %>.domain.model.BaseEntity;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import <%= base_package_name %>.TestConfig;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Base test class for JPA repositories.
 * Extend this class to test specific repository implementations.
 */
// @DataJpaTest
// @ContextConfiguration(classes = TestConfig.class)
public abstract class BaseJpaRepositoryTest<T extends BaseEntity, ID, R extends JpaRepository<T, ID>> {

//     @Autowired
//     protected TestEntityManager entityManager;
//
//     @Autowired
//     protected R repository;
//
//     protected abstract T createTestEntity();
//     protected abstract void assertEntityEquals(T expected, T actual);
//     protected abstract void modifyTestEntity(T entity);
//
//     @BeforeEach
//     void setUp() {
//         entityManager.clear();
//     }
//
//     @Test
//     void testSave() {
//         T entity = createTestEntity();
//
//         T saved = repository.save(entity);
//
//         assertNotNull(saved);
//         assertNotNull(saved.getId());
//         assertNotNull(saved.getCreatedAt());
//         assertNotNull(saved.getUpdatedAt());
//     }
//
//     @Test
//     void testFindById() {
//         T entity = createTestEntity();
//         entity = entityManager.persist(entity);
//         entityManager.flush();
//
//         Optional<T> found = repository.findById((ID) entity.getId());
//
//         assertTrue(found.isPresent());
//         assertEntityEquals(entity, found.get());
//     }
//
//     @Test
//     void testFindAll() {
//         T entity1 = createTestEntity();
//         T entity2 = createTestEntity();
//         entityManager.persist(entity1);
//         entityManager.persist(entity2);
//         entityManager.flush();
//
//         List<T> found = repository.findAll();
//
//         assertNotNull(found);
//         assertEquals(2, found.size());
//     }
//
//     @Test
//     void testUpdate() {
//         T entity = createTestEntity();
//         entity = entityManager.persist(entity);
//         entityManager.flush();
//
//         modifyTestEntity(entity);
//         T updated = repository.save(entity);
//         entityManager.flush();
//
//         T found = entityManager.find(entity.getClass(), entity.getId());
//         assertEntityEquals(updated, found);
//     }
//
//     @Test
//     void testDelete() {
//         T entity = createTestEntity();
//         entity = entityManager.persist(entity);
//         entityManager.flush();
//
//         repository.delete(entity);
//         entityManager.flush();
//
//         T found = entityManager.find(entity.getClass(), entity.getId());
//         assertNull(found);
//     }
//
//     @Test
//     void testDeleteById() {
//         T entity = createTestEntity();
//         entity = entityManager.persist(entity);
//         entityManager.flush();
//
//         repository.deleteById((ID) entity.getId());
//         entityManager.flush();
//
//         T found = entityManager.find(entity.getClass(), entity.getId());
//         assertNull(found);
//     }
//
//     @Test
//     void testExistsById() {
//         T entity = createTestEntity();
//         entity = entityManager.persist(entity);
//         entityManager.flush();
//
//         boolean exists = repository.existsById((ID) entity.getId());
//
//         assertTrue(exists);
//     }
}
