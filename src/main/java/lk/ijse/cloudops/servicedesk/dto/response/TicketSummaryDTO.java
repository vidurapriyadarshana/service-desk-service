package lk.ijse.cloudops.servicedesk.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketSummaryDTO {
    private String id;
    private String ticketNumber;
    private String clientId;
    private String productId;
    private String subject;
    private String priority;
    private String status;
    private int slaTargetHours;
    private String assignedAgent;
    private Instant createdAt;
}
