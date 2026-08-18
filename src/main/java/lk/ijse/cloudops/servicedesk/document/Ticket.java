package lk.ijse.cloudops.servicedesk.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "tickets")
public class Ticket {

    @Id
    private String id;

    @Indexed(unique = true)
    private String ticketNumber;

    @Indexed
    private String clientId;

    private String productId;
    private String subject;
    private String description;
    private String priority; // LOW, MEDIUM, HIGH, CRITICAL
    private String status;   // OPEN, IN_PROGRESS, PENDING_CLIENT, RESOLVED, CLOSED
    private int slaTargetHours;
    private String assignedAgent;

    @Builder.Default
    private List<Attachment> attachments = new ArrayList<>();

    @Builder.Default
    private List<TimelineEntry> timeline = new ArrayList<>();

    @Builder.Default
    private Instant createdAt = Instant.now();

    @Builder.Default
    private Instant updatedAt = Instant.now();
}
