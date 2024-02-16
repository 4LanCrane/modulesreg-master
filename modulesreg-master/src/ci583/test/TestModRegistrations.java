package ci583.test;

/**
 * Tests for the CI583 Registration assignment. When your code passes these tests you can be
 * fairly confident it is along the right lines, but passing the tests does not imply that
 * your code is perfect.
 *
 * @author Jim Burton
 */

import ci583.receiver.*;
import org.junit.Before;
import org.junit.Test;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class TestModRegistrations {
    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testRRReceiver() {
        ModRegReceiver r = new RRReceiver(100);
        r.enqueue(new ModuleRegister("P1", 5000));
        r.enqueue(new ModuleRegister("P2", 3000));
        r.enqueue(new ModuleRegister("P3", 1000));
        r.enqueue(new ModuleRegister("P4", 4000));

        Stream<String> names = r.startRegistration().stream().map(ModuleRegister::getName);
        assertEquals("[P3, P2, P4, P1]", Arrays.toString(names.toArray()));

    }

    @Test
    public void testPReceiver() {
        ModRegReceiver r = new PReceiver(100);
        r.enqueue(new ModuleRegister("P1", 2000, ModuleRegister.PRIORITY.MED));
        r.enqueue(new ModuleRegister("P2", 3000, ModuleRegister.PRIORITY.LOW));
        r.enqueue(new ModuleRegister("P3", 4000, ModuleRegister.PRIORITY.MED));
        r.enqueue(new ModuleRegister("P4", 4000, ModuleRegister.PRIORITY.HIGH));
        r.enqueue(new ModuleRegister("P5", 4000, ModuleRegister.PRIORITY.LOW));
        r.enqueue(new ModuleRegister("P6", 4000, ModuleRegister.PRIORITY.HIGH));

        Stream<String> names = r.startRegistration().stream().map(ModuleRegister::getName);
        assertEquals("[P6, P4, P3, P1, P2, P5]", Arrays.toString(names.toArray()));
    }

    @Test
    public void doMLFQScheduler() {
        ModRegReceiver r = new MLFQReceiver(100);
        r.enqueue(new ModuleRegister("P1", 2000));
        r.enqueue(new ModuleRegister("P2", 3000));
        r.enqueue(new ModuleRegister("P3", 4000));
        r.enqueue(new ModuleRegister("P4", 4000));
        r.enqueue(new ModuleRegister("P5", 4000));
        r.enqueue(new ModuleRegister("P6", 4000));

        Stream<String> names = r.startRegistration().stream().map(ModuleRegister::getName);
        assertEquals("[P1, P2, P4, P5, P6, P3]", Arrays.toString(names.toArray()));
    }
}
