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
public class CommentRequestDTO {

    @NotBlank(message = "Sender type is required")
    private String senderType; // CLIENT, AGENT, SYSTEM

    @NotBlank(message = "Sender name is required")
    private String senderName;

    @NotBlank(message = "Message is required")
    private String message;
}
