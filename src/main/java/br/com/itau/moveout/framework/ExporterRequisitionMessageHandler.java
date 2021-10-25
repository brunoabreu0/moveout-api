package br.com.itau.moveout.framework;

import br.com.itau.moveout.domain.ExporterRequisitionMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;

public class ExporterRequisitionMessageHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(ExporterRequisitionMessageHandler.class);
    private final BlockingQueue<ExporterRequisitionMessage> messageQueue;

    public ExporterRequisitionMessageHandler(BlockingQueue<ExporterRequisitionMessage> messageQueue) {
        this.messageQueue = messageQueue;
    }

    @Override
    public void run() {
        // TODO: we may want a way to stop the thread
        while (true) {
            try {
                processMessage(messageQueue.take());
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
    }

    private void processMessage(ExporterRequisitionMessage message) {
        log.info("Requisition Message: " + message.getMessage());
    }
}
