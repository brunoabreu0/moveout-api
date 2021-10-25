package br.com.itau.moveout.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class ExporterRequisition {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ExporterRequisitionStatus status;

    @Column(nullable = false)
    private String dbName;
    @Column(nullable = false)
    private String tableName;
    private String filterClause;
    private String selectedColumns;

    @OneToMany(mappedBy = "requisition")
    @JsonIgnore
    @ToString.Exclude
    private Set<ExporterTask> tasks;

    @Transient
    @JsonSerialize
    @JsonDeserialize
    private Integer getTasksQuantity() {
        return this.tasks.size();
    }
}
