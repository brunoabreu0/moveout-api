package br.com.itau.moveout.api.controller;

import br.com.itau.moveout.api.entity.ExporterTask;
import br.com.itau.moveout.api.entity.ExporterTaskStatus;
import br.com.itau.moveout.api.repository.ExporterTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1")
public class ExporterTaskController {
    private final ExporterTaskRepository taskRepository;

    @Autowired
    public ExporterTaskController(
            ExporterTaskRepository taskRepository
    ) {
        this.taskRepository = taskRepository;
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<ExporterTask>> getAllExporterTasks(
            @RequestParam(name = "status", required = false) ExporterTaskStatus status,
            @RequestParam(name = "requisition_id", required = false) Long requisitionId
    ) {
        try {
            List<ExporterTask> tasks = new ArrayList<>();
            if (status == null && requisitionId == null) {
                tasks.addAll(taskRepository.findAll());
            } else {
                tasks.addAll(taskRepository.findAllByRequisition_IdAndStatus(requisitionId, status));
            }
            if (tasks.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(tasks, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<ExporterTask> getTaskById(
            @PathVariable("id") Long id
    ) {
        try {
            Optional<ExporterTask> requisition = taskRepository.findById(id);
            return requisition.map(exporterRequisition -> new ResponseEntity<>(exporterRequisition, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
