package ci583.receiver;
/**
 * The abstract superclass for all Receivers. A Receiver accepts a series of Processes
 * and allows them to run to completion according to its specific strategy (e.g.
 * Round Robin, High/Low Priority or Multi-level Feedack Queue).
 *
 * @author Jim Burton
 */

import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;


public abstract class ModRegReceiver {

    /** The time quantum for which each module registration process will run before being
     * put back to sleep.
     */
    protected static long QUANTUM;

    /**
     * Creates a Module registration receiver with the given time quantum.
     * @param quantum
     */
    public ModRegReceiver(long quantum) {
        QUANTUM = quantum;
    }

    /**
     * Add a process to  the queue of precesses, for registering a module to a student .
     * @param m
     */
    public abstract void enqueue(ModuleRegister m);

    /**
     * Start registering modules.
     * @return
     */
    public abstract List<ModuleRegister> startRegistration();
}
