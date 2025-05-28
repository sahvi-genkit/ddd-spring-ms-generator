package <%= base_package_name %>.application.interfaces;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private String errorText;
    private String errorDetails;
    private LocalDateTime timestamp;
}
