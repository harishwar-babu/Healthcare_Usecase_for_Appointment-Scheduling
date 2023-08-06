package com.appointmentservice.feign;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;

@FeignClient(name = "NOTIFICATION-SERVICE",path = "/notification-service/notification")
public interface NotificationFeign {
    @GetMapping("/email-notification/{username}/{date}")
    String mailNotification(@PathVariable String username, @PathVariable LocalDate date);
}
