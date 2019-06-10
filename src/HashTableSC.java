import java.util.ArrayList;

class HashNodeLL<K, V> {
    K key;
    V value;
    HashNodeLL<K, V> next;

    public HashNodeLL(K key, V value) {
        this.key = key;
        this.value = value;
        next = null;
    }

    @Override
    public String toString() {

        StringBuilder s = new StringBuilder();

        HashNodeLL<K, V> iterator = next;

        s.append("(").append(key.toString()).append(",").append(value.toString()).append(")").append(" -> ");

        while (iterator != null) {
            s.append("(").append(iterator.key.toString()).append(",").append(iterator.value.toString()).append(")").append(" -> ");
            iterator = iterator.next;

        }
        return s.toString();

    }
}

public class HashTableSC<K, V> extends MyHashTable<K> {

    private boolean DEBUG;
    private boolean isHASH1;

    private int numberOfCollisions;

    private ArrayList<HashNodeLL<K, V>> table; //Hashtable is an Array of Linked Lists
    // # of current chains / total current capacity of hashtable

    // current size of hashtable
    private int size;

    public HashTableSC(boolean DEBUG, boolean isHASH1, int initSize) {
        super(initSize);
        this.DEBUG = DEBUG;
        this.isHASH1 = isHASH1;
        numberOfCollisions = 0;
        table = new ArrayList<>();
        N = initSize; // initially capacity set to default
        size = 0;

        // Create empty chains
        for (int i = 0; i < N; i++)
            table.add(null);
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    //Hash Function that returns a valid index in the hashtable for a key


    public V remove(K key) {

        int i = isHASH1 ? hash1(key) : hash2(key); //index for the key to be removed

        // top element of the linked list having our key
        HashNodeLL<K, V> head = table.get(i);

        // Search for key in its chain
        HashNodeLL<K, V> prev = null;
        while (head != null) {
            // If Key found
            if (head.key.equals(key))
                break;

            // Else keep moving in chain
            prev = head;
            head = head.next;
        }

        if (head == null) //key not found in the list
            return null;

        size--;

        // Remove key
        if (prev != null) // if our key is not the top element
            prev.next = head.next;
        else
            table.set(i, head.next); //if our key is the top element in the Linked list

        return head.value;
    }

    // Returns value for a key
    public V get(K key) {
        // Find head of chain for a given key
        int bucketIndex = isHASH1 ? hash1(key) : hash2(key);
        HashNodeLL<K, V> head = table.get(bucketIndex);

        // look up that chain
        while (head != null) {
            if (head.key.equals(key))
                return head.value;
            head = head.next;
        }

        // If key not found
        return null;
    }

    public boolean put(K key, V value) {

        int idx = isHASH1 ? hash1(key) : hash2(key); //returns a valid index of the chain

        HashNodeLL<K, V> head = table.get(idx);// Find head of chain for given key

        while (head != null) {
            if (head.key.equals(key)) {
                return false; //if key is already present, discard the key
            }
            head = head.next;
        }

        // insert
        size++;
        head = table.get(idx);

        if (head != null) numberOfCollisions++;

        HashNodeLL<K, V> newNode = new HashNodeLL<>(key, value);
        newNode.next = head;

        table.set(idx, newNode); // set the new node at the top of the chain

        // If load factor goes beyond LOAD_FACTOR_THRESHOLD, then double hash1 table size
        if (loadFactor() >= LOAD_FACTOR_THRESHOLD) {
            if (DEBUG)
                System.out.println("Doubling Hashtable size - Collisions So Far :" + this.numberOfCollisions + "Load Factor :" + loadFactor());

            ArrayList<HashNodeLL<K, V>> prevHashTable = table;
            table = new ArrayList<>();
            N = 2 * N;
            size = 0;

            for (int i = 0; i < N; i++)
                table.add(null);

            for (HashNodeLL<K, V> top : prevHashTable) {
                while (top != null) {
                    put(top.key, top.value);
                    top = top.next;
                }
            }

        }
        return true;
    }

    private double loadFactor() {
        return size * 1.0 / N;
    }

    @Override
    public String toString() {

        StringBuilder str = new StringBuilder();

        int i = 0;
        for (HashNodeLL<K, V> ll : table) {
            if (ll != null) str.append(i).append(":").append(ll.toString()).append("\n");
            else str.append(i).append(":").append("null").append("\n");
            i++;
        }
        return "HashTableSC{" +
                "numberOfCollisions=" + numberOfCollisions +
                " numChains=" + N +
                " size=" + size +
                '}' + "\n" + str.toString();
    }

    public int getNumberOfCollisions() {
        return numberOfCollisions;
    }


}