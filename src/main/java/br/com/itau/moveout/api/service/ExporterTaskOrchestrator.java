package br.com.itau.moveout.api.service;

import br.com.itau.moveout.api.entity.ExporterTask;
import br.com.itau.moveout.api.repository.ExporterTaskRepository;
import br.com.itau.moveout.domain.ExporterTaskMessage;
import br.com.itau.moveout.framework.ExporterTaskMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

@Service
public class ExporterTaskOrchestrator {
    // Manages the tasks queue
    private static final int MAX_TASKS = 90;

    private final ThreadPoolExecutor executorService;
    private final ExporterTaskRepository taskRepository;
    private final LinkedBlockingQueue<ExporterTaskMessage> messageQueue;
    private final ExporterTaskMessageHandler messagingThread;


    @Autowired
    private ExporterTaskOrchestrator(ExporterTaskRepository taskRepository) {
        this.executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(MAX_TASKS);
        this.taskRepository = taskRepository;
        this.messageQueue = new LinkedBlockingQueue<>();
        this.messagingThread = new ExporterTaskMessageHandler(messageQueue);
        this.messagingThread.start();
    }

    public void submitTask(ExporterTask task) {
        task = taskRepository.save(task);
        ExporterTaskExecutor taskExecutor = new ExporterTaskExecutor(messageQueue, task);
        executorService.submit(taskExecutor);
    }

}
