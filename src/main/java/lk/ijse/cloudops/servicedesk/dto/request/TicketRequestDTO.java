package lk.ijse.cloudops.servicedesk.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketRequestDTO {

    @NotBlank(message = "Client ID is required")
    private String clientId;

    private String productId;

    @NotBlank(message = "Subject is required")
    private String subject;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Priority is required")
    private String priority; // LOW, MEDIUM, HIGH, CRITICAL
}
