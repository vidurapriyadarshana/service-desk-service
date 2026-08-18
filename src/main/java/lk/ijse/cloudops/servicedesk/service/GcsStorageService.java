package lk.ijse.cloudops.servicedesk.service;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class GcsStorageService {

    private final Storage storage;

    @Value("${gcs.bucket-name:cloudops-ticket-attachments-dev}")
    private String bucketName;

    public GcsStorageService(Storage storage) {
        this.storage = storage;
    }

    public String uploadFile(String objectName, MultipartFile file) {
        String fallbackUrl = String.format("https://storage.googleapis.com/%s/%s", bucketName, objectName);
        try {
            BlobId blobId = BlobId.of(bucketName, objectName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                    .setContentType(file.getContentType())
                    .build();
            storage.create(blobInfo, file.getBytes());
            log.info("Successfully uploaded file to GCS: {}/{}", bucketName, objectName);
            return fallbackUrl;
        } catch (Exception e) {
            log.warn("GCS file upload bypassed/failed (local environment fallback): {}", e.getMessage());
            return fallbackUrl;
        }
    }
}
