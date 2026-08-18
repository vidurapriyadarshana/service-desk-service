package lk.ijse.cloudops.servicedesk.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketUpdateDTO {
    private String status;         // OPEN, IN_PROGRESS, PENDING_CLIENT, RESOLVED, CLOSED
    private String priority;       // LOW, MEDIUM, HIGH, CRITICAL
    private String assignedAgent;
    private String performedBy;
}
