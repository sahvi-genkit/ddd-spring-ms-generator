package <%= base_package_name %>.domain.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * Base class for all domain entities.
 */
@Getter
@Setter
public abstract class BaseEntity implements Serializable {
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    private boolean active = true;
} 