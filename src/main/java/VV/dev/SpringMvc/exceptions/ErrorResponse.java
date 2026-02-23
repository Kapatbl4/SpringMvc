package VV.dev.SpringMvc.exceptions;

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
