import java.util.ArrayList;
import java.util.Scanner;

public class SelfOrganizingST {
    private Node first;
    private int n;
    private boolean organize;
    private long comparisons;

    private class Node {
        private String key;
        private Integer value;
        private Node next;
    }

    public SelfOrganizingST() {
        organize = true;
    }

    public SelfOrganizingST(boolean organize) {
        this.organize = organize;
    }

    public int size() {
        return n;
    }

    public long comparisons() {
        return comparisons;
    }

    public Integer get(String key) {
        Node current = first;
        Node previous = null;
        while (current != null) {
            comparisons++;
            if (current.key.equals(key)) {
                if (organize && previous != null) {
                    previous.next = current.next;
                    current.next = first;
                    first = current;
                }
                return current.value;
            }
            previous = current;
            current = current.next;
        }
        return null;
    }

    public void put(String key, Integer value) {
        Node current = first;
        Node previous = null;
        while (current != null) {
            comparisons++;
            if (current.key.equals(key)) {
                current.value = value;
                if (organize && previous != null) {
                    previous.next = current.next;
                    current.next = first;
                    first = current;
                }
                return;
            }
            previous = current;
            current = current.next;
        }
        Node newNode = new Node();
        newNode.key = key;
        newNode.value = value;
        newNode.next = first;
        first = newNode;
        n++;
    }

    public ArrayList<String> keys() {
        ArrayList<String> keys = new ArrayList<String>();
        Node current = first;
        while (current != null) {
            keys.add(current.key);
            current = current.next;
        }
        return keys;
    }

    public void show() {
        Node current = first;
        while (current != null) {
            System.out.print(current.key + " -> ");
            current = current.next;
        }
        System.out.println();
    }

    public static boolean test() {
        SelfOrganizingST st = new SelfOrganizingST();
        assert st.size() == 0;
        assert st.get("missing") == null;
        st.put("A", 1);
        st.put("B", 2);
        st.put("C", 3);
        assert st.get("A") == 1;
        assert st.first.key.equals("A");
        assert st.get("C") == 3;
        assert st.first.key.equals("C");
        st.put("B", 4);
        assert st.first.key.equals("B");
        assert st.get("B") == 4;
        assert st.size() == 3;
        assert st.get("missing") == null;
        assert st.first.key.equals("B");
        SelfOrganizingST sequential = new SelfOrganizingST(false);
        sequential.put("A", 1);
        sequential.put("B", 2);
        assert sequential.get("A") == 1;
        sequential.put("A", 3);
        assert sequential.first.key.equals("B");
        assert sequential.get("A") == 3;
        return true;
    }

    public static void main(String[] args) {
        assert test();
        Scanner input = new Scanner(System.in);
        SelfOrganizingST st = new SelfOrganizingST();
        int i = 0;
        while (input.hasNext()) {
            st.put(input.next(), i);
            st.show();
            i++;
        }
        Node current = st.first;
        while (current != null) {
            System.out.println(current.key + " " + current.value);
            current = current.next;
        }
        input.close();
    }
}