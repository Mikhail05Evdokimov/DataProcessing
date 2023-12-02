package ru.nsu.evdokimov;

import sun.misc.Signal;
import sun.misc.SignalHandler;

/**
 * Оюработчик стоп сигнала
 */
public class StopSignalHandler implements SignalHandler {

    // Static method to install the signal handler
    public static void install(String signalName, SignalHandler handler) {
        Signal signal = new Signal(signalName);
        StopSignalHandler diagnosticSignalHandler = new StopSignalHandler();
        Signal.handle(signal, diagnosticSignalHandler);
        diagnosticSignalHandler.setHandler(handler);
    }

    private SignalHandler handler;

    private void setHandler(SignalHandler handler) {
        this.handler = handler;
    }

    // Signal handler method
    @Override
    public void handle(Signal sig) {

        try {
            handler.handle(sig);

        } catch (Exception e) {
            System.out.println("Signal handler failed, reason " + e);
        }
    }
}
