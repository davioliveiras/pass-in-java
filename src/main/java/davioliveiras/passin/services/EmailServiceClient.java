package davioliveiras.passin.services;

import davioliveiras.passin.dto.email.EmailRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(url = "http://localhost:8080/api", name = "email-service")
public interface EmailServiceClient {

    @PostMapping("/email")
    void sendEmail(@RequestBody EmailRequest request);
}
