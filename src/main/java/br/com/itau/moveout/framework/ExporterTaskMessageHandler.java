package br.com.itau.moveout.framework;

import br.com.itau.moveout.domain.ExporterTaskMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;

public class ExporterTaskMessageHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(ExporterTaskMessageHandler.class);
    private final BlockingQueue<ExporterTaskMessage> messageQueue;

    public ExporterTaskMessageHandler(BlockingQueue<ExporterTaskMessage> messageQueue) {
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

    private void processMessage(ExporterTaskMessage message) {
        log.info("Task Message: " + message.getMessage());
    }
}
