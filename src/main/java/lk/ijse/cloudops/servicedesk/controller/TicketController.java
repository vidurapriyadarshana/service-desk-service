package lk.ijse.cloudops.servicedesk.controller;

import jakarta.validation.Valid;
import lk.ijse.cloudops.servicedesk.dto.request.TicketRequestDTO;
import lk.ijse.cloudops.servicedesk.dto.request.TicketUpdateDTO;
import lk.ijse.cloudops.servicedesk.dto.response.StandardResponse;
import lk.ijse.cloudops.servicedesk.dto.response.TicketResponseDTO;
import lk.ijse.cloudops.servicedesk.dto.response.TicketSummaryDTO;
import lk.ijse.cloudops.servicedesk.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<StandardResponse<TicketResponseDTO>> createTicket(
            @Valid @RequestPart("ticket") TicketRequestDTO requestDTO,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        TicketResponseDTO created = ticketService.createTicket(requestDTO, files);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(StandardResponse.success(HttpStatus.CREATED.value(), "Ticket created successfully", created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StandardResponse<TicketResponseDTO>> getTicketById(@PathVariable String id) {
        TicketResponseDTO ticket = ticketService.getTicketById(id);
        return ResponseEntity.ok(StandardResponse.success(HttpStatus.OK.value(), "Ticket retrieved successfully", ticket));
    }

    @GetMapping("/number/{ticketNumber}")
    public ResponseEntity<StandardResponse<TicketResponseDTO>> getTicketByNumber(@PathVariable String ticketNumber) {
        TicketResponseDTO ticket = ticketService.getTicketByNumber(ticketNumber);
        return ResponseEntity.ok(StandardResponse.success(HttpStatus.OK.value(), "Ticket retrieved successfully", ticket));
    }

    @GetMapping
    public ResponseEntity<StandardResponse<List<TicketSummaryDTO>>> getAllTickets(
            @RequestParam(required = false) String clientId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String priority) {
        List<TicketSummaryDTO> tickets = ticketService.getAllTickets(clientId, status, priority);
        return ResponseEntity.ok(StandardResponse.success(HttpStatus.OK.value(), "Tickets retrieved successfully", tickets));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StandardResponse<TicketResponseDTO>> updateTicket(
            @PathVariable String id,
            @RequestBody TicketUpdateDTO updateDTO) {
        TicketResponseDTO updated = ticketService.updateTicket(id, updateDTO);
        return ResponseEntity.ok(StandardResponse.success(HttpStatus.OK.value(), "Ticket updated successfully", updated));
    }
}
