package lk.ijse.cloudops.servicedesk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ServiceDeskServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceDeskServiceApplication.class, args);
    }
}
