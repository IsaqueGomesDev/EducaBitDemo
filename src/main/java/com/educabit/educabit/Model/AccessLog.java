package com.educabit.educabit.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "access_logs")
@Data
@NoArgsConstructor
public class AccessLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ipAddress;
    private String userAgent;
    private String referer;
    private String location; // From GeoIP
    private String requestUrl;
    private String method;
    private String username; // Null if anonymous

    private LocalDateTime timestamp = LocalDateTime.now();
}
