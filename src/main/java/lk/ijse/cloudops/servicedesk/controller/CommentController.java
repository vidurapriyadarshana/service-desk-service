package lk.ijse.cloudops.servicedesk.controller;

import jakarta.validation.Valid;
import lk.ijse.cloudops.servicedesk.dto.request.CommentRequestDTO;
import lk.ijse.cloudops.servicedesk.dto.response.CommentResponseDTO;
import lk.ijse.cloudops.servicedesk.dto.response.StandardResponse;
import lk.ijse.cloudops.servicedesk.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tickets/{ticketId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<StandardResponse<CommentResponseDTO>> addComment(
            @PathVariable String ticketId,
            @Valid @RequestPart("comment") CommentRequestDTO requestDTO,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        CommentResponseDTO comment = commentService.addComment(ticketId, requestDTO, files);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(StandardResponse.success(HttpStatus.CREATED.value(), "Comment added successfully", comment));
    }

    @GetMapping
    public ResponseEntity<StandardResponse<List<CommentResponseDTO>>> getCommentsByTicket(@PathVariable String ticketId) {
        List<CommentResponseDTO> comments = commentService.getCommentsByTicket(ticketId);
        return ResponseEntity.ok(StandardResponse.success(HttpStatus.OK.value(), "Comments retrieved successfully", comments));
    }
}
