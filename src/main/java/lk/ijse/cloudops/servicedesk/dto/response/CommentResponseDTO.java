package lk.ijse.cloudops.servicedesk.dto.response;

import lk.ijse.cloudops.servicedesk.document.Attachment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponseDTO {
    private String id;
    private String ticketId;
    private String senderType;
    private String senderName;
    private String message;
    private List<Attachment> attachments;
    private Instant createdAt;
}
