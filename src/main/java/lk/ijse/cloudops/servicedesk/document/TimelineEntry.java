package lk.ijse.cloudops.servicedesk.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimelineEntry {
    private String action; // CREATED, ASSIGNED, STATUS_CHANGED, COMMENT_ADDED, RESOLVED, CLOSED
    private String performedBy;
    private String details;
    @Builder.Default
    private Instant timestamp = Instant.now();
}
