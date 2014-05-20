import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class BinomialTreeTest {

    @Test
    public void BasicTest() {
        LazyBinomialHeap heap = new LazyBinomialHeap();
        heap.enqueue(3);
        assertEquals(heap.min(), 3);
    }

    @Test
    public void testMinBasic() throws Exception {
        LazyBinomialHeap heap = new LazyBinomialHeap();
        heap.enqueue(3);
        heap.enqueue(4);
        heap.enqueue(2);
        heap.enqueue(3);
        heap.enqueue(5);
        assertEquals(2, heap.min());
        assertEquals(2, heap.min());
    }

    @Test
    public void testExtractMinBasic() throws Exception {
        LazyBinomialHeap heap = new LazyBinomialHeap();
        heap.enqueue(3);
        heap.enqueue(4);
        heap.enqueue(2);
        heap.enqueue(3);
        heap.enqueue(5);
        assertEquals(2, heap.extractMin());
        assertEquals(3, heap.extractMin());
    }



    @Test
    public void testRandomStuff() throws Exception {
        for (int times = 0; times < 100; times++) {
            List<LazyBinomialHeap> heaps = new ArrayList<LazyBinomialHeap>();
            int n = (int) (Math.random() * 20) + 2;
            int min = -1;
            for (int i = 0; i < n; i++) {
                LazyBinomialHeap h = new LazyBinomialHeap();
                for (int j = 0; j < Math.random() * 10000; j++) {
                    int v = (int) (Math.random() * 1000000);
                    h.enqueue(v);
                    if (v < min || min == -1) min = v;
                }
                heaps.add(h);
            }

            for (int _ = 0; _ < n - 1; _++) {
                int i = (int) (Math.random() * heaps.size());
                int j = (int) (Math.random() * (heaps.size() - 1));
                LazyBinomialHeap h1 = heaps.remove(i);
                LazyBinomialHeap h2 = heaps.remove(j);
                LazyBinomialHeap h3 = LazyBinomialHeap.meld(h1, h2);
                heaps.add(h3);
                for (int k = 0; k < Math.random() * 1000; k++) {
                    int v = (int) (Math.random() * 1000000);
                    h3.enqueue(v);
                    if (v < min || min == -1) min = v;
                }
            }

            assertEquals(1, heaps.size());
            assertEquals(min, heaps.get(0).extractMin());
        }
    }

    @Test
    public void ExtractMinTest() {
        LazyBinomialHeap heap = new LazyBinomialHeap();
        for (int i = 0; i < 1000; i++) {
            heap.enqueue(i);
        }

        for (int i = 0; i < 1000; i++) {
            System.out.println(i);
            assertEquals(i, heap.extractMin());
        }
    }
}
