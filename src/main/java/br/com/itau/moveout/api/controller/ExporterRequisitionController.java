package br.com.itau.moveout.api.controller;

import br.com.itau.moveout.api.entity.ExporterRequisition;
import br.com.itau.moveout.api.entity.ExporterRequisitionStatus;
import br.com.itau.moveout.api.repository.ExporterRequisitionRepository;
import br.com.itau.moveout.api.service.ExporterRequisitionOrchestrator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1")
public class ExporterRequisitionController {
    private final ExporterRequisitionRepository requisitionRepository;
    private final ExporterRequisitionOrchestrator requisitionOrchestrator;

    @Autowired
    public ExporterRequisitionController(ExporterRequisitionRepository requisitionRepository,
                                         ExporterRequisitionOrchestrator requisitionOrchestrator) {
        this.requisitionRepository = requisitionRepository;
        this.requisitionOrchestrator = requisitionOrchestrator;
    }

    @GetMapping("/requisitions")
    public ResponseEntity<List<ExporterRequisition>> getAllExporterRequisitions(
            @RequestParam(name = "status", required = false) ExporterRequisitionStatus status
            ) {
        try {
            List<ExporterRequisition> requisitions = new ArrayList<>();
            if (status == null)
                requisitions.addAll(requisitionRepository.findAll());
            else
                requisitions.addAll(requisitionRepository.findAllByStatus(status));
            if (requisitions.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(requisitions, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/requisitions/{id}")
    public ResponseEntity<ExporterRequisition> getRequisitionById(
            @PathVariable("id") Long id
    ) {
        try {
            Optional<ExporterRequisition> requisition = requisitionRepository.findById(id);
            return requisition.map(exporterRequisition -> new ResponseEntity<>(exporterRequisition, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/requisitions")
    public ResponseEntity<ExporterRequisitionResponse> createRequisition(
            @RequestBody ExporterRequisitionBody requisitionBody
    ) {
        try {
            ExporterRequisition requisition = new ExporterRequisition();
            requisition.setDbName(requisitionBody.getDbName());
            requisition.setTableName(requisitionBody.getTableName());
            requisition.setSelectedColumns(requisitionBody.getSelectedColumns());
            requisition.setFilterClause(requisitionBody.getFilterClause());
            requisition.setCreatedAt(LocalDateTime.now());
            requisition.setStatus(ExporterRequisitionStatus.CREATING);
            requisition = requisitionOrchestrator.submitRequisition(requisition);
            return new ResponseEntity<>(new ExporterRequisitionResponse(requisition.getId()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
