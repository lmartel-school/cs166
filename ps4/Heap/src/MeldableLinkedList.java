import java.util.Iterator;

/**
 * A doubly-linked list that supports O(1) concatenation. You are not required
 * to implement this class, but we strongly recommend doing so as a stepping
 * stone toward building the LazyBinomialHeap.
 */
public class MeldableLinkedList<T> implements Iterable<ListNode<T>> {
    private ListNode<T> head;
    private ListNode<T> tail;
    private int size;


    /**
     * Must meld tail to head.
     *
     * @param list
     */
    public void concat(MeldableLinkedList<T> list) {
        assert(list.getHead() != null);
        tail.setNext(list.getHead());
        list.getHead().setPrev(tail);
        tail = list.getTail();
        list.setHead(head);
        size += list.getSize();
    }


    public ListNode<T> append(T value) {
        ListNode<T> node = new ListNode<T>(value);

        if (this.head == null) {
            head = node;
        }

        if (this.tail == null) {
            tail = node;
        } else {
            tail.setNext(node);
            node.setPrev(tail);
            tail = node;
        }

        this.size++;

        return node;
    }

    @Override
    public Iterator<ListNode<T>> iterator() {
        return new LinkedListIterator(head);
    }

    public void setHead(ListNode<T> head) {
        this.head = head;
    }

    public ListNode<T> getHead() {
        return head;
    }

    public ListNode<T> getTail() {
        return tail;
    }

    public int getSize() {
        return size;
    }

    public void remove(ListNode<T> node) {
        if (head == node) {
            head = node.getNext();
        }

        if (tail == node) {
            tail = node.getPrev();
        }

        if (node.getNext() != null) {
            node.getNext().setPrev(node.getPrev());
        }

        if (node.getPrev() != null) {
            node.getPrev().setNext(node.getNext());
        }
        size--;
    }


    private class LinkedListIterator implements Iterator<ListNode<T>> {
        private ListNode<T> current;

        private LinkedListIterator(ListNode<T> head) {
            current = head;
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public ListNode<T> next() {
            ListNode<T> cursor = current;
            current = current.getNext();
            return cursor;
        }

        @Override
        public void remove() {
            return;
        }
    }
}
