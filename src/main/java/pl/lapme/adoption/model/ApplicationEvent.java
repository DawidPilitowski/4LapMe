package pl.lapme.adoption.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import pl.lapme.adoption.model.types.EventType;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Lob
    @Column(name = "event", length = 1024)
    private String event;
    @Lob
    @Column(name = "msg", length = 1024)
    private String msg;

    @CreationTimestamp
    private LocalDateTime time;

    private EventType eventType;
}
