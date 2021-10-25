package br.com.itau.moveout.api.service;

import br.com.itau.moveout.api.entity.ExporterRequisition;
import br.com.itau.moveout.api.entity.ExporterTask;
import br.com.itau.moveout.api.entity.ExporterTaskStatus;
import br.com.itau.moveout.api.repository.ExporterRequisitionRepository;
import br.com.itau.moveout.domain.ExporterRequisitionMessage;
import br.com.itau.moveout.framework.ExporterRequisitionMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class ExporterRequisitionOrchestrator {

    private final ExporterTaskOrchestrator taskOrchestrator;
    private final ExporterRequisitionRepository requisitionRepository;
    private final LinkedBlockingQueue<ExporterRequisitionMessage> messageQueue;
    private final ExporterRequisitionMessageHandler messenger;

    @Autowired
    public ExporterRequisitionOrchestrator(ExporterRequisitionRepository requisitionRepository,
                                           ExporterTaskOrchestrator taskOrchestrator) {
        this.requisitionRepository = requisitionRepository;
        this.taskOrchestrator = taskOrchestrator;
        this.messageQueue = new LinkedBlockingQueue<>();
        this.messenger = new ExporterRequisitionMessageHandler(this.messageQueue);
        this.messenger.start();
    }

    public ExporterRequisition submitRequisition(ExporterRequisition requisition) throws InterruptedException {
        System.out.println(requisition.toString());
        messageQueue.put(new ExporterRequisitionMessage("Criando Nova Requisição de Move Out"));
        requisition = requisitionRepository.save(requisition);
        messageQueue.put(new ExporterRequisitionMessage("Nova Requisição de Move Out criada. Preparando dados no CDP"));
        Thread.sleep(5000);
        Integer rowGroupSize = 5;
        messageQueue.put(new ExporterRequisitionMessage("Dados preparados no CDP. Criando " + rowGroupSize.toString() + " Tasks de Move Out"));
        List<ExporterTask> tasks = new ArrayList<>();
        tasks.add(new ExporterTask(null, LocalDateTime.now(), ExporterTaskStatus.READY, 0, requisition));
        tasks.add(new ExporterTask(null, LocalDateTime.now(), ExporterTaskStatus.READY, 1, requisition));
        tasks.add(new ExporterTask(null, LocalDateTime.now(), ExporterTaskStatus.READY, 2, requisition));
        tasks.add(new ExporterTask(null, LocalDateTime.now(), ExporterTaskStatus.READY, 3, requisition));
        tasks.add(new ExporterTask(null, LocalDateTime.now(), ExporterTaskStatus.READY, 4, requisition));
        messageQueue.put(new ExporterRequisitionMessage(rowGroupSize.toString() + " Tasks de Move Out criadas. Submetendo tasks criadas para a pool de execução"));
        tasks.forEach(taskOrchestrator::submitTask);
        messageQueue.put(new ExporterRequisitionMessage(rowGroupSize.toString() + " Tasks de Move Out submetidas"));
        return requisition;
    }

}
