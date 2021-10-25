package br.com.itau.moveout.api.repository;

import br.com.itau.moveout.api.entity.ExporterRequisition;
import br.com.itau.moveout.api.entity.ExporterRequisitionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExporterRequisitionRepository extends JpaRepository<ExporterRequisition, Long> {
    List<ExporterRequisition> findAllByStatus(ExporterRequisitionStatus status);
}
