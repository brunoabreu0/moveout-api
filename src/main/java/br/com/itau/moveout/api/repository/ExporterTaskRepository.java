package br.com.itau.moveout.api.repository;

import br.com.itau.moveout.api.entity.ExporterTask;
import br.com.itau.moveout.api.entity.ExporterTaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExporterTaskRepository extends JpaRepository<ExporterTask, Long> {
    List<ExporterTask> findAllByRequisition_IdAndStatus(Long requisitionId, ExporterTaskStatus status);
}
