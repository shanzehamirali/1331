import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Generic class that represents a LinkedList.
 * @author samirali3
 * @version 1.0
 * @param <T> Describes type of parameter
 */
public class LinkedList<T> implements List<T> {
    private Node<T> head;
    private int size;

    /**
     * Constructor that sets head to null and size of linked list to 0.
     */
    public LinkedList() {
        head = null;
        size = 0;
    }

    /**
     * Constructor that adds array data elements to create a linked list.
     * @param data Generic array of data to add to the linked list
     */
    public LinkedList(T[] data) {
        for (T datum : data) {
            add(datum); // keep adding to end
        }
    }

    /**
     * Getter method for head node of the linked list.
     * @return First node of the linked list
     */
    public Node<T> getHead() {
        return head;
    }

    /**
     * Iterates through the linked list to return an array with the data.
     * @return Array of generic type data
     */
    public T[] toArray() {
        T[] arr = (T[]) new Object[size];
        Iterator<T> iterator = iterator();
        int index = 0;
        while (iterator.hasNext()) {
            arr[index] = iterator.next();
            index++;
        }
        return arr;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(
                String.format("===== LINKEDLIST %d =====\nisEmpty: %s\nsize: %d\nhead: %s\ndata: [",
                        hashCode(),
                        isEmpty(),
                        size(),
                        (head == null ? "null" : head.getData())));

        T[] data = toArray();
        if (data == null) {
            sb.append("TODO: Implement toArray method...");
        } else {
            for (int i = 0; i < data.length - 1; ++i) {
                sb.append(String.format("%s, ", data[i])); // append all but last value
            }
            if (data.length > 0) {
                sb.append(String.format("%s", data[data.length - 1])); // append last value
            }
        }
        sb.append("]\n============================");
        return sb.toString();
    }

    @Override
    public void add(T element) throws IllegalArgumentException {
        if (element == null) {
            throw new IllegalArgumentException("Cannot pass null element");
        }
        Node<T> nT = new Node<T>(element);
        if (head == null) {
            this.head = nT;
        } else {
            Node<T> current = head;
            while (current.getNext() != null) {
                current = current.getNext();
            }
            current.setNext(nT);
        }
        size++;
    }

    @Override
    public void add(int index, T element) throws IndexOutOfBoundsException, IllegalArgumentException {
        if (index < 0 || index > this.size()) {
            throw new IndexOutOfBoundsException("Index invalid");
        }
        if (element == null) {
            throw new IllegalArgumentException("Cannot pass null element");
        }
        Node<T> newNode = new Node<>(element);
        if (index == 0) {
            newNode.setNext(head);
            head = newNode;
        } else {
            Node<T> current = head;
            for (int i = 1; i < index; i++) {
                current = current.getNext();
            }
            newNode.setNext(current.getNext());
            current.setNext(newNode);
        }
        size++;
    }

    @Override
    public T remove() throws NoSuchElementException {
        if (head == null) {
            throw new NoSuchElementException("List is empty");
        }
        T element = head.getData();
        head = head.getNext();
        size--;
        return element;
    }

    @Override
    public T remove(int index) throws NoSuchElementException, IndexOutOfBoundsException {
        if (index < 0 || index >= size) {
            if (head == null) {
                throw new NoSuchElementException("List is empty");
            }
            throw new IndexOutOfBoundsException("Invalid index");
        }
        Node<T> current = head;
        Node<T> prev = null;
        int count = 0;
        while (current != null && count < index) {
            prev = current;
            current = current.getNext();
            count++;
        }
        T element = current.getData();
        if (prev == null) {
            head = head.getNext();
        } else {
            prev.setNext(current.getNext());
        }
        size--;
        return element;
    }

    @Override
    public T remove(T element) throws IllegalArgumentException, NoSuchElementException {
        if (element == null) {
            throw new IllegalArgumentException("Cannot pass null element");
        }
        Node<T> current = head;
        Node<T> prev = null;
        while (current != null) {
            if (current.getData().equals(element)) {
                if (prev == null) {
                    head = head.getNext();
                } else {
                    prev.setNext(current.getNext());
                }
                size--;
                return current.getData();
            }
            prev = current;
            current = current.getNext();
        }
        throw new NoSuchElementException("List is empty");
    }

    @Override
    public T set(int index, T element) throws IndexOutOfBoundsException, IllegalArgumentException {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Invalid index");
        }
        if (element == null) {
            throw new IllegalArgumentException("Cannot pass null element");
        }
        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.getNext();
        }
        T old = current.getData();
        current.setData(element);
        return old;
    }

    @Override
    public T get(int index) throws IndexOutOfBoundsException {
        if (index >= this.size() || index < 0 || this.isEmpty()) {
            throw new IndexOutOfBoundsException("Invalid Index");
        } else if (head == null) {
            return null;
        } else {
            Iterator<T> iterator = iterator();
            int i = 0;
            while (iterator.hasNext() && i < index) {
                iterator.next();
                i++;
            }
            if (i != index) {
                throw new IndexOutOfBoundsException("Invalid index");
            }
            return iterator.next();
        }
    }

    @Override
    public boolean contains(T element) throws IllegalArgumentException {
        if (element == null) {
            throw new IllegalArgumentException("Cannot pass null element");
        }
        Iterator<T> iterator = iterator();
        while (iterator.hasNext()) {
            if (iterator.next().equals(element)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void clear() {
        head = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return (head == null);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<T> iterator() {
        return new LinkedListIterator<>(this);
    }
}