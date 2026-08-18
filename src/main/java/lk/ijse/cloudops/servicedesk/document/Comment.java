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
@Document(collection = "comments")
public class Comment {

    @Id
    private String id;

    @Indexed
    private String ticketId;

    private String senderType; // CLIENT, AGENT, SYSTEM
    private String senderName;
    private String message;

    @Builder.Default
    private List<Attachment> attachments = new ArrayList<>();

    @Builder.Default
    private Instant createdAt = Instant.now();
}
