package br.com.itau.moveout.api.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints={
        @UniqueConstraint(columnNames = {"rowGroup", "requisition_id"})
})
public class ExporterTask {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ExporterTaskStatus status;

    @Column(nullable = false)
    private Integer rowGroup;

    @ManyToOne
    private ExporterRequisition requisition;
}
