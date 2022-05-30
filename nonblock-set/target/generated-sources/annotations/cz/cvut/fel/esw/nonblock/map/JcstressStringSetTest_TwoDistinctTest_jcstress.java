package cz.cvut.fel.esw.nonblock.map;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import org.openjdk.jcstress.infra.runners.ForkedTestConfig;
import org.openjdk.jcstress.infra.collectors.TestResult;
import org.openjdk.jcstress.infra.runners.Runner;
import org.openjdk.jcstress.infra.runners.WorkerSync;
import org.openjdk.jcstress.util.Counter;
import org.openjdk.jcstress.os.AffinitySupport;
import org.openjdk.jcstress.vm.AllocProfileSupport;
import org.openjdk.jcstress.infra.runners.FootprintEstimator;
import org.openjdk.jcstress.infra.runners.VoidThread;
import org.openjdk.jcstress.infra.runners.LongThread;
import org.openjdk.jcstress.infra.runners.CounterThread;
import cz.cvut.fel.esw.nonblock.map.JcstressStringSetTest.TwoDistinctTest;
import org.openjdk.jcstress.infra.results.I_Result;

public final class JcstressStringSetTest_TwoDistinctTest_jcstress extends Runner<I_Result> {

    volatile WorkerSync workerSync;
    TwoDistinctTest[] gs;
    I_Result[] gr;

    public JcstressStringSetTest_TwoDistinctTest_jcstress(ForkedTestConfig config) {
        super(config);
    }

    @Override
    public void sanityCheck(Counter<I_Result> counter) throws Throwable {
        sanityCheck_API(counter);
        sanityCheck_Footprints(counter);
    }

    private void sanityCheck_API(Counter<I_Result> counter) throws Throwable {
        final TwoDistinctTest s = new TwoDistinctTest();
        final I_Result r = new I_Result();
        VoidThread a0 = new VoidThread() { protected void internalRun() {
            s.actor1();
        }};
        VoidThread a1 = new VoidThread() { protected void internalRun() {
            s.actor2();
        }};
        a0.start();
        a1.start();
        a0.join();
        if (a0.throwable() != null) {
            throw a0.throwable();
        }
        a1.join();
        if (a1.throwable() != null) {
            throw a1.throwable();
        }
            s.size(r);
        counter.record(r);
    }

    private void sanityCheck_Footprints(Counter<I_Result> counter) throws Throwable {
        config.adjustStrideCount(new FootprintEstimator() {
          public void runWith(int size, long[] cnts) {
            long time1 = System.nanoTime();
            long alloc1 = AllocProfileSupport.getAllocatedBytes();
            TwoDistinctTest[] ls = new TwoDistinctTest[size];
            I_Result[] lr = new I_Result[size];
            for (int c = 0; c < size; c++) {
                TwoDistinctTest s = new TwoDistinctTest();
                I_Result r = new I_Result();
                lr[c] = r;
                ls[c] = s;
            }
            LongThread a0 = new LongThread() { public long internalRun() {
                long a1 = AllocProfileSupport.getAllocatedBytes();
                for (int c = 0; c < size; c++) {
                    ls[c].actor1();
                }
                long a2 = AllocProfileSupport.getAllocatedBytes();
                return a2 - a1;
            }};
            LongThread a1 = new LongThread() { public long internalRun() {
                long a1 = AllocProfileSupport.getAllocatedBytes();
                for (int c = 0; c < size; c++) {
                    ls[c].actor2();
                }
                long a2 = AllocProfileSupport.getAllocatedBytes();
                return a2 - a1;
            }};
            a0.start();
            a1.start();
            try {
                a0.join();
                cnts[0] += a0.result();
            } catch (InterruptedException e) {
            }
            try {
                a1.join();
                cnts[0] += a1.result();
            } catch (InterruptedException e) {
            }
            for (int c = 0; c < size; c++) {
                ls[c].size(lr[c]);
            }
            for (int c = 0; c < size; c++) {
                counter.record(lr[c]);
            }
            long time2 = System.nanoTime();
            long alloc2 = AllocProfileSupport.getAllocatedBytes();
            cnts[0] += alloc2 - alloc1;
            cnts[1] += time2 - time1;
        }});
    }

    @Override
    public ArrayList<CounterThread<I_Result>> internalRun() {
        int len = config.strideSize * config.strideCount;
        gs = new TwoDistinctTest[len];
        gr = new I_Result[len];
        for (int c = 0; c < len; c++) {
            gs[c] = new TwoDistinctTest();
            gr[c] = new I_Result();
        }
        workerSync = new WorkerSync(false, 2, config.spinLoopStyle);

        control.isStopped = false;

        if (config.localAffinity) {
            try {
                AffinitySupport.tryBind();
            } catch (Exception e) {
                // Do not care
            }
        }

        ArrayList<CounterThread<I_Result>> threads = new ArrayList<>(2);
        threads.add(new CounterThread<I_Result>() { public Counter<I_Result> internalRun() {
            return task_actor1();
        }});
        threads.add(new CounterThread<I_Result>() { public Counter<I_Result> internalRun() {
            return task_actor2();
        }});

        for (CounterThread<I_Result> t : threads) {
            t.start();
        }

        if (config.time > 0) {
            try {
                TimeUnit.MILLISECONDS.sleep(config.time);
            } catch (InterruptedException e) {
            }
        }

        control.isStopped = true;

        return threads;
    }

    private void jcstress_consume(Counter<I_Result> cnt, int a) {
        TwoDistinctTest[] ls = gs;
        I_Result[] lr = gr;
        int len = config.strideSize * config.strideCount;
        int left = a * len / 2;
        int right = (a + 1) * len / 2;
        for (int c = left; c < right; c++) {
            I_Result r = lr[c];
            TwoDistinctTest s = ls[c];
            s.size(r);
            ls[c] = new TwoDistinctTest();
            cnt.record(r);
            r.r1 = 0;
        }
    }

    private void jcstress_sink(int v) {};
    private void jcstress_sink(short v) {};
    private void jcstress_sink(byte v) {};
    private void jcstress_sink(char v) {};
    private void jcstress_sink(long v) {};
    private void jcstress_sink(float v) {};
    private void jcstress_sink(double v) {};
    private void jcstress_sink(Object v) {};

    private Counter<I_Result> task_actor1() {
        int len = config.strideSize * config.strideCount;
        int stride = config.strideSize;
        Counter<I_Result> counter = new Counter<>();
        if (config.localAffinity) AffinitySupport.bind(config.localAffinityMap[0]);
        while (true) {
            WorkerSync sync = workerSync;
            if (sync.stopped) {
                return counter;
            }
            int check = 0;
            for (int start = 0; start < len; start += stride) {
                run_actor1(gs, gr, start, start + stride);
                check += 2;
                sync.awaitCheckpoint(check);
            }
            jcstress_consume(counter, 0);
            if (sync.tryStartUpdate()) {
                workerSync = new WorkerSync(control.isStopped, 2, config.spinLoopStyle);
            }
            sync.postUpdate();
        }
    }

    private void run_actor1(TwoDistinctTest[] gs, I_Result[] gr, int start, int end) {
        TwoDistinctTest[] ls = gs;
        I_Result[] lr = gr;
        for (int c = start; c < end; c++) {
            TwoDistinctTest s = ls[c];
            s.actor1();
        }
    }

    private Counter<I_Result> task_actor2() {
        int len = config.strideSize * config.strideCount;
        int stride = config.strideSize;
        Counter<I_Result> counter = new Counter<>();
        if (config.localAffinity) AffinitySupport.bind(config.localAffinityMap[1]);
        while (true) {
            WorkerSync sync = workerSync;
            if (sync.stopped) {
                return counter;
            }
            int check = 0;
            for (int start = 0; start < len; start += stride) {
                run_actor2(gs, gr, start, start + stride);
                check += 2;
                sync.awaitCheckpoint(check);
            }
            jcstress_consume(counter, 1);
            if (sync.tryStartUpdate()) {
                workerSync = new WorkerSync(control.isStopped, 2, config.spinLoopStyle);
            }
            sync.postUpdate();
        }
    }

    private void run_actor2(TwoDistinctTest[] gs, I_Result[] gr, int start, int end) {
        TwoDistinctTest[] ls = gs;
        I_Result[] lr = gr;
        for (int c = start; c < end; c++) {
            TwoDistinctTest s = ls[c];
            s.actor2();
        }
    }

}
