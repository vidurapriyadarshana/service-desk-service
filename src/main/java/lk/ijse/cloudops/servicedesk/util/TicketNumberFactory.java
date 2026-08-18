package lk.ijse.cloudops.servicedesk.util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class TicketNumberFactory {

    private final AtomicInteger sequence = new AtomicInteger(1001);

    public synchronized String generateTicketNumber() {
        String year = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy"));
        int seq = sequence.getAndIncrement();
        return String.format("TICK-%s-%04d", year, seq);
    }
}
