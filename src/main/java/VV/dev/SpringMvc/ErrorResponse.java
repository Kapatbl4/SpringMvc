package VV.dev.SpringMvc;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private String message;
    private String detailedMessage;
    private LocalDateTime timestamp;

}
