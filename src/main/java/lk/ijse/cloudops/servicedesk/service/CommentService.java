package lk.ijse.cloudops.servicedesk.service;

import lk.ijse.cloudops.servicedesk.document.Attachment;
import lk.ijse.cloudops.servicedesk.document.Comment;
import lk.ijse.cloudops.servicedesk.document.Ticket;
import lk.ijse.cloudops.servicedesk.document.TimelineEntry;
import lk.ijse.cloudops.servicedesk.dto.request.CommentRequestDTO;
import lk.ijse.cloudops.servicedesk.dto.response.CommentResponseDTO;
import lk.ijse.cloudops.servicedesk.exception.NotFoundException;
import lk.ijse.cloudops.servicedesk.repository.CommentRepository;
import lk.ijse.cloudops.servicedesk.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final TicketRepository ticketRepository;
    private final GcsStorageService gcsStorageService;
    private final ModelMapper modelMapper;

    public CommentResponseDTO addComment(String ticketId, CommentRequestDTO requestDTO, List<MultipartFile> files) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new NotFoundException("Ticket not found with id: " + ticketId));

        List<Attachment> attachments = new ArrayList<>();
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    String objectName = String.format("comments/%s/%s_%s", ticketId, UUID.randomUUID(), file.getOriginalFilename());
                    String gcsUrl = gcsStorageService.uploadFile(objectName, file);
                    attachments.add(Attachment.builder()
                            .fileName(file.getOriginalFilename())
                            .fileType(file.getContentType())
                            .fileSize(file.getSize())
                            .gcsUrl(gcsUrl)
                            .build());
                }
            }
        }

        Comment comment = Comment.builder()
                .ticketId(ticketId)
                .senderType(requestDTO.getSenderType())
                .senderName(requestDTO.getSenderName())
                .message(requestDTO.getMessage())
                .attachments(attachments)
                .createdAt(Instant.now())
                .build();

        Comment savedComment = commentRepository.save(comment);

        // Add timeline entry to Ticket
        ticket.getTimeline().add(TimelineEntry.builder()
                .action("COMMENT_ADDED")
                .performedBy(requestDTO.getSenderName())
                .details("Comment added by " + requestDTO.getSenderType())
                .timestamp(Instant.now())
                .build());
        ticket.setUpdatedAt(Instant.now());
        ticketRepository.save(ticket);

        return modelMapper.map(savedComment, CommentResponseDTO.class);
    }

    public List<CommentResponseDTO> getCommentsByTicket(String ticketId) {
        return commentRepository.findByTicketId(ticketId).stream()
                .map(c -> modelMapper.map(c, CommentResponseDTO.class))
                .collect(Collectors.toList());
    }
}
