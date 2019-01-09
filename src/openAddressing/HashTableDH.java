package openAddressing;
import java.util.ArrayList;

public class HashTableDH<K,V> {
    private static final double LOAD_FACTOR_THRESHOLD = 0.45;
    private static final int PRIME = 7;
    private boolean DEBUG ;
    private boolean[] isDeleted;

    private int numberOfCollisions;
    private int numberOfHits;

    private static final int DEFAULT_CAPACITY = 13;
    private ArrayList<HashNode<K, V>> table; //Data container

    // current size of hashtable
    private int size; // current # of elements
    private int N; // size of hashtable

    public int size() {
        return size;
    }
    public HashTableDH(boolean DEBUG) {
        N = DEFAULT_CAPACITY;
        isDeleted = new boolean[DEFAULT_CAPACITY];
        numberOfCollisions = 0;
        this.DEBUG = DEBUG;
        table = new ArrayList<>(N);
        for (int i = 0; i < N; i++) {
            table.add(null);
        }
    }
    private int hash(K key)
    {
        numberOfHits++;
        return (Math.abs(key.hashCode()) % N);
    }
    private int auxHash(K key)
    {
        return PRIME - (Math.abs(key.hashCode()) % PRIME);
    }
    private int doubleHash(K key, int i)
    {
        int idx = ((hash(key) + i * auxHash(key)) % N);
        if(idx < 0) {
            System.out.println("Hashed Two Negative index : H1: " + hash(key) + "H2: " +
                    auxHash(key) + "H1|H2: " + idx +"at i :"+ i + "Table Size: " + N
            );
            System.out.println(this);
            return -1;
        }return  idx;
    }
    public boolean put(K key, V value) {

        boolean isCollision = false;
        int idx = hash(key); //returns a valid index of the hashtable

        HashNode<K, V> node = table.get(idx);

        int i = 1;

        while (node != null) {
            if (node.key.equals(key)) {
                return false; //if key is already present, discard the key
            }
            isCollision = true; //collision detected
            idx = doubleHash(key, i);
            node = table.get(idx);
            i++;
        }
        // insert
        size++;

        HashNode<K, V> newNode = new HashNode<>(key, value);

        table.set(idx, newNode); // set the new node at the top of the chain
        isDeleted[idx] = false;
        // If load factor goes beyond LOAD_FACTOR_THRESHOLD, then double hash table size

        if (loadFactor() >= LOAD_FACTOR_THRESHOLD) {
            if(DEBUG)System.out.println("Doubling Hashtable Size :" + loadFactor());
            ArrayList<HashNode<K, V>> prevHashTable = table;
            N = 2 * N;
            size = 0;
            table = new ArrayList<>(N);
            isDeleted = new boolean[N];
            for (int j = 0; j < N; j++) {
                table.add(null);
            }
            if(DEBUG)System.out.println("Before Copying :\n"+this.numberOfCollisions);

            for (HashNode<K, V> n : prevHashTable) {
                    if(n != null)put(n.key, n.value);
            }

        }
        if(isCollision)numberOfCollisions++;

        return true;
    }

    private double loadFactor()
    {
        return size * 1.0 / N;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();

        int i = 0;
        for (HashNode<K, V> n :
        table) {
            s.append(i).append(":").append(n).append("\n");
            i++;
        }

        return "openAddressing.HashTableDH{" +
                "numberOfCollisions=" + numberOfCollisions +
                ", size=" + size +
                '}' +"\n"+s.toString() ;
    }
    public int indexOf(K key)
    {
        int idx = hash(key);

        HashNode<K,V> node = table.get(idx);

        int i = 0;
        while (node != null || isDeleted[idx])
        {
            if(node != null && key.equals(node.key))
                return idx;

            idx = doubleHash(key, i++);
            node = table.get(idx);
        }
        return -1;
    }
    public V get(K key)
    {
        int idx = indexOf(key);

        if(idx == -1)return null;

        else return table.get(idx).value;
    }
    public int getNumberOfHits() {
        return numberOfHits;
    }
    public int getNumberOfCollisions() {
        return numberOfCollisions;
    }
    public boolean remove(K key)
    {
        int idx = indexOf(key);

        if(idx == -1) return false;

        size--;
        table.set(idx, null);
        isDeleted[idx] = true;

        return true;

    }
}
