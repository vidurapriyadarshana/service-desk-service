package lk.ijse.cloudops.servicedesk.dto.response;

import lk.ijse.cloudops.servicedesk.document.Attachment;
import lk.ijse.cloudops.servicedesk.document.TimelineEntry;
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
public class TicketResponseDTO {
    private String id;
    private String ticketNumber;
    private String clientId;
    private String productId;
    private String subject;
    private String description;
    private String priority;
    private String status;
    private int slaTargetHours;
    private String assignedAgent;
    private List<Attachment> attachments;
    private List<TimelineEntry> timeline;
    private Instant createdAt;
    private Instant updatedAt;
}
