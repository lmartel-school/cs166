public class ListNode<T> {
    private T value;
    private ListNode<T> prev;
    private ListNode<T> next;

    public ListNode<T> getPrev() {
        return prev;
    }

    public ListNode<T> getNext() {
        return next;
    }

    public void setNext(ListNode<T> node) {
        next = node;
    }

    public void setPrev(ListNode<T> node) {
        prev = node;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public ListNode(T value) {
        this.value = value;
    }

}