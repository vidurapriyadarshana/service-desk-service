package lk.ijse.cloudops.servicedesk.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attachment {
    private String fileName;
    private String fileType;
    private long fileSize;
    private String gcsUrl;
}
