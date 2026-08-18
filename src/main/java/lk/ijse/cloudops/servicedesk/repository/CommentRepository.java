package lk.ijse.cloudops.servicedesk.repository;

import lk.ijse.cloudops.servicedesk.document.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {
    List<Comment> findByTicketId(String ticketId);
}
