package br.com.itau.moveout.api.service;

import br.com.itau.moveout.api.entity.ExporterTask;
import br.com.itau.moveout.domain.ExporterTaskMessage;

import java.util.concurrent.LinkedBlockingQueue;

public class ExporterTaskExecutor extends Thread {
    // Deals with Fargate SDK
    private final LinkedBlockingQueue<ExporterTaskMessage> messageQueue;
    private final ExporterTask task;

    public ExporterTaskExecutor(LinkedBlockingQueue<ExporterTaskMessage> messageQueue, ExporterTask task) {
        this.messageQueue = messageQueue;
        this.task = task;
    }

    @Override
    public void run() {
        try {
            StringBuilder details = new StringBuilder();
            details.append("ID da task: ").append(task.getId().toString())
                    .append(" ID da requisição: ").append(task.getRequisition().getId().toString())
                    .append(" Grupo de Reqistros da Task: ").append(task.getRowGroup().toString())
            ;
            messageQueue.put(new ExporterTaskMessage("Iniciando Task de Move Out " + details));
            Thread.sleep(10000);
            messageQueue.put(new ExporterTaskMessage(task.toString()));
            messageQueue.put(new ExporterTaskMessage("Task de Move Out finalizada " + details));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
