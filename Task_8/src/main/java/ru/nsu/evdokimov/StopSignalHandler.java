package ru.nsu.evdokimov;

import sun.misc.Signal;
import sun.misc.SignalHandler;

public class StopSignalHandler implements SignalHandler {

    // Static method to install the signal handler
    public static void install(String signalName, SignalHandler handler) {
        Signal signal = new Signal(signalName);
        StopSignalHandler diagnosticSignalHandler = new StopSignalHandler();
        SignalHandler oldHandler = Signal.handle(signal, diagnosticSignalHandler);
        diagnosticSignalHandler.setHandler(handler);
        diagnosticSignalHandler.setOldHandler(oldHandler);
    }

    private SignalHandler oldHandler;
    private SignalHandler handler;

    private StopSignalHandler() {
    }

    private void setOldHandler(SignalHandler oldHandler) {
        this.oldHandler = oldHandler;
    }

    private void setHandler(SignalHandler handler) {
        this.handler = handler;
    }

    // Signal handler method
    @Override
    public void handle(Signal sig) {
        System.out.println("Stop Signal handler called for signal " + sig);
        try {
            handler.handle(sig);


        } catch (Exception e) {
            System.out.println("Signal handler failed, reason " + e);
        }
    }
}
