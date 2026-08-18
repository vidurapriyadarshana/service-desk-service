package lk.ijse.cloudops.servicedesk.repository;

import lk.ijse.cloudops.servicedesk.document.Ticket;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends MongoRepository<Ticket, String> {
    Optional<Ticket> findByTicketNumber(String ticketNumber);
    List<Ticket> findByClientId(String clientId);
    List<Ticket> findByStatus(String status);
    List<Ticket> findByPriority(String priority);
}
