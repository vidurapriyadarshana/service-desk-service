package lk.ijse.cloudops.servicedesk.service;

import lk.ijse.cloudops.servicedesk.document.Attachment;
import lk.ijse.cloudops.servicedesk.document.Ticket;
import lk.ijse.cloudops.servicedesk.document.TimelineEntry;
import lk.ijse.cloudops.servicedesk.dto.request.TicketRequestDTO;
import lk.ijse.cloudops.servicedesk.dto.request.TicketUpdateDTO;
import lk.ijse.cloudops.servicedesk.dto.response.TicketResponseDTO;
import lk.ijse.cloudops.servicedesk.dto.response.TicketSummaryDTO;
import lk.ijse.cloudops.servicedesk.exception.NotFoundException;
import lk.ijse.cloudops.servicedesk.repository.TicketRepository;
import lk.ijse.cloudops.servicedesk.util.TicketNumberFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final TicketNumberFactory ticketNumberFactory;
    private final GcsStorageService gcsStorageService;
    private final ModelMapper modelMapper;

    public TicketResponseDTO createTicket(TicketRequestDTO requestDTO, List<MultipartFile> files) {
        String ticketNumber = ticketNumberFactory.generateTicketNumber();
        int slaHours = calculateSlaHours(requestDTO.getPriority());

        List<Attachment> attachments = new ArrayList<>();
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    String objectName = String.format("tickets/%s/%s_%s", ticketNumber, UUID.randomUUID(), file.getOriginalFilename());
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

        List<TimelineEntry> timeline = new ArrayList<>();
        timeline.add(TimelineEntry.builder()
                .action("CREATED")
                .performedBy(requestDTO.getClientId())
                .details("Ticket created with priority: " + requestDTO.getPriority())
                .timestamp(Instant.now())
                .build());

        Ticket ticket = Ticket.builder()
                .ticketNumber(ticketNumber)
                .clientId(requestDTO.getClientId())
                .productId(requestDTO.getProductId())
                .subject(requestDTO.getSubject())
                .description(requestDTO.getDescription())
                .priority(requestDTO.getPriority())
                .status("OPEN")
                .slaTargetHours(slaHours)
                .attachments(attachments)
                .timeline(timeline)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        Ticket savedTicket = ticketRepository.save(ticket);
        return modelMapper.map(savedTicket, TicketResponseDTO.class);
    }

    public TicketResponseDTO getTicketById(String id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ticket not found with id: " + id));
        return modelMapper.map(ticket, TicketResponseDTO.class);
    }

    public TicketResponseDTO getTicketByNumber(String ticketNumber) {
        Ticket ticket = ticketRepository.findByTicketNumber(ticketNumber)
                .orElseThrow(() -> new NotFoundException("Ticket not found with number: " + ticketNumber));
        return modelMapper.map(ticket, TicketResponseDTO.class);
    }

    public List<TicketSummaryDTO> getAllTickets(String clientId, String status, String priority) {
        List<Ticket> tickets;
        if (clientId != null && !clientId.isBlank()) {
            tickets = ticketRepository.findByClientId(clientId);
        } else if (status != null && !status.isBlank()) {
            tickets = ticketRepository.findByStatus(status);
        } else if (priority != null && !priority.isBlank()) {
            tickets = ticketRepository.findByPriority(priority);
        } else {
            tickets = ticketRepository.findAll();
        }

        return tickets.stream()
                .map(t -> modelMapper.map(t, TicketSummaryDTO.class))
                .collect(Collectors.toList());
    }

    public TicketResponseDTO updateTicket(String id, TicketUpdateDTO updateDTO) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ticket not found with id: " + id));

        String actor = updateDTO.getPerformedBy() != null ? updateDTO.getPerformedBy() : "AGENT";

        if (updateDTO.getStatus() != null && !updateDTO.getStatus().equals(ticket.getStatus())) {
            ticket.setStatus(updateDTO.getStatus());
            ticket.getTimeline().add(TimelineEntry.builder()
                    .action("STATUS_CHANGED")
                    .performedBy(actor)
                    .details("Status changed to " + updateDTO.getStatus())
                    .timestamp(Instant.now())
                    .build());
        }

        if (updateDTO.getPriority() != null && !updateDTO.getPriority().equals(ticket.getPriority())) {
            ticket.setPriority(updateDTO.getPriority());
            ticket.setSlaTargetHours(calculateSlaHours(updateDTO.getPriority()));
            ticket.getTimeline().add(TimelineEntry.builder()
                    .action("PRIORITY_CHANGED")
                    .performedBy(actor)
                    .details("Priority changed to " + updateDTO.getPriority())
                    .timestamp(Instant.now())
                    .build());
        }

        if (updateDTO.getAssignedAgent() != null) {
            ticket.setAssignedAgent(updateDTO.getAssignedAgent());
            ticket.getTimeline().add(TimelineEntry.builder()
                    .action("ASSIGNED")
                    .performedBy(actor)
                    .details("Assigned to " + updateDTO.getAssignedAgent())
                    .timestamp(Instant.now())
                    .build());
        }

        ticket.setUpdatedAt(Instant.now());
        Ticket updatedTicket = ticketRepository.save(ticket);
        return modelMapper.map(updatedTicket, TicketResponseDTO.class);
    }

    private int calculateSlaHours(String priority) {
        if (priority == null) return 24;
        return switch (priority.toUpperCase()) {
            case "CRITICAL" -> 1;
            case "HIGH" -> 4;
            case "MEDIUM" -> 8;
            default -> 24;
        };
    }
}
