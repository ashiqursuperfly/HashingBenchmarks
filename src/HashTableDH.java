import java.util.ArrayList;

public class HashTableDH<K,V> extends MyHashTable<K>{
    private static final int PRIME = 7;
    private boolean isHash1;
    private boolean DEBUG ;
    private boolean[] isDeleted;

    private int numberOfCollisions;

    private ArrayList<HashNode<K, V>> table; //Data container

    // current size of hashtable
    private int size; // current # of elements


    public int size() {
        return size;
    }
    public HashTableDH(boolean DEBUG, boolean isHash1, int initSize) {
        super(initSize);
        this.isHash1 = isHash1;
        isDeleted = new boolean[initSize];
        numberOfCollisions = 0;
        this.DEBUG = DEBUG;
        table = new ArrayList<>(N);
        for (int i = 0; i < N; i++) {
            table.add(null);
        }
    }


    private int doubleHash(K key, int i)
    {
        int idx =  isHash1 ? ((hash1(key) + i * auxHash(key) + i*i*hash2(key) ) % N) : ((hash2(key) + i * auxHash(key) + i*i*hash2(key)) % N);
        if(idx < 0) {
            return -1;// secondary chaining & no valid position
        }return  idx;
    }

    public boolean put(K key, V value) {

        boolean isCollision = false;
        int idx = isHash1 ? hash1(key) : hash2(key); //returns a valid index of the hashtable

        HashNode<K, V> node = table.get(idx);

        int i = 1;

        while (node != null) {
            if (node.key.equals(key)) {
                return false; //if key is already present, discard the key
            }
            isCollision = true; //collision detected
            idx = doubleHash(key, i);
            try {
                node = table.get(idx);
            }catch (IndexOutOfBoundsException e){
                if(DEBUG)System.out.println("DOUBLE HASHING: No Valid Positions Due to Secondary Clustering");
                return false;
            }
            i++;
        }
        // insert
        size++;

        HashNode<K, V> newNode = new HashNode<>(key, value);

        table.set(idx, newNode); // set the new node at the top of the chain
        isDeleted[idx] = false;
        // If load factor goes beyond LOAD_FACTOR_THRESHOLD, then double hash1 table size

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

        return "HashTableDH{" +
                "numberOfCollisions=" + numberOfCollisions +
                ", size=" + size +
                '}' +"\n"+s.toString() ;
    }
    public int indexOf(K key)
    {
        int idx = isHash1 ? hash1(key) : hash2(key);

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
