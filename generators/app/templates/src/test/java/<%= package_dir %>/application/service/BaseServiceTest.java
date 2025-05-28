package <%= base_package_name %>.application.service;

import <%= base_package_name %>.domain.model.BaseEntity;
import <%= base_package_name %>.domain.repository.Repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Base test class for application services.
 * Extend this class to test specific service implementations.
 */
public abstract class BaseServiceTest<T extends BaseEntity, ID, S extends BaseService<T, ID, ? extends Repository<T, ID>>> {

    @Mock
    protected Repository<T, ID> repository;

    protected S service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = createService(repository);
    }

    protected abstract S createService(Repository<T, ID> repository);
    protected abstract T createTestEntity();
    protected abstract ID createTestId();

    @Test
    void testSave() {
        T entity = createTestEntity();
        when(repository.save(any())).thenReturn(entity);

        T saved = service.save(entity);

        assertNotNull(saved);
        verify(repository).save(entity);
    }

    @Test
    void testFindById() {
        T entity = createTestEntity();
        ID id = createTestId();
        when(repository.findById(id)).thenReturn(Optional.of(entity));

        Optional<T> found = service.findById(id);

        assertTrue(found.isPresent());
        assertEquals(entity, found.get());
        verify(repository).findById(id);
    }

    @Test
    void testFindAll() {
        List<T> entities = Arrays.asList(createTestEntity(), createTestEntity());
        when(repository.findAll()).thenReturn(entities);

        List<T> found = service.findAll();

        assertNotNull(found);
        assertEquals(2, found.size());
        verify(repository).findAll();
    }

    @Test
    void testDelete() {
        T entity = createTestEntity();
        doNothing().when(repository).delete(entity);

        service.delete(entity);

        verify(repository).delete(entity);
    }

    @Test
    void testDeleteById() {
        ID id = createTestId();
        doNothing().when(repository).deleteById(id);

        service.deleteById(id);

        verify(repository).deleteById(id);
    }

    @Test
    void testExistsById() {
        ID id = createTestId();
        when(repository.existsById(id)).thenReturn(true);

        boolean exists = service.existsById(id);

        assertTrue(exists);
        verify(repository).existsById(id);
    }
} 