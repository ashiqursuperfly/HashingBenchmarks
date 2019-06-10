import java.util.ArrayList;

public class HashTableCP<K,V> extends MyHashTable<K> {
    private static final int C2 = 3;
    private static final int C1 = 1;
    private boolean DEBUG ;
    private boolean isHASH1;

    private boolean[] isDeleted;
    private int numberOfCollisions;
    private ArrayList<HashNode<K, V>> data; //data container

    // current size of hashtable
    private int size; // current # of elements

    public int size() {
        return size;
    }

    public HashTableCP(boolean DEBUG, boolean isHASH1, int initSize) {
        super(initSize);
        this.isHASH1 = isHASH1;
        N = initSize;
        numberOfCollisions = 0;
        isDeleted = new boolean[initSize];
        this.DEBUG = DEBUG;
        data = new ArrayList<>(N);
        for (int i = 0; i < N; i++) {
            data.add(null);
        }

    }

    private int customHash(K key, int i)
    {
        int idx = isHASH1 ? (hash1(key) + C1 *i * auxHash(key) +  C2*i*i ) % N  : (hash2(key) + C1 *i * auxHash(key) +  C2*i*i ) % N;
        if(idx < 0) {
            return -1; // secondary chaining & no valid position
        }return  idx;
    }

    public boolean put(K key, V value) {

        boolean isCollision = false;

        int idx = isHASH1 ? hash1(key) : hash2(key); //returns a valid index of the hashtable i=0

        HashNode<K, V> node = data.get(idx);

        int i = 1;

        while (node != null) {
            if (node.key.equals(key)) {
                return false; //if key is already present, discard the key
            }
            isCollision = true; //collision detected
            idx = customHash(key, i);
            try {
                node = data.get(idx);
            }catch (IndexOutOfBoundsException e){
                if(DEBUG)System.out.println("CUSTOM PROBING: No Valid Positions Due to Secondary Clustering");
                return false;
            }
            i++;
        }
        // insert
        size++;

        HashNode<K, V> newNode = new HashNode<>(key, value);

        data.set(idx, newNode); // set the new node at the top of the chain
        isDeleted[idx] = false;

        // If load factor goes beyond LOAD_FACTOR_THRESHOLD, then double hash1 table size

        if (loadFactor() >= LOAD_FACTOR_THRESHOLD) {
            if(DEBUG)System.out.println("Doubling Hashtable Size :" + loadFactor());
            ArrayList<HashNode<K, V>> prevHashTable = data;
            N = 2 * N;
            size = 0;
            data = new ArrayList<>(N);
            isDeleted = new boolean[N];
            for (int j = 0; j < N; j++) {
                data.add(null);
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
                data) {
            s.append(i).append(":").append(n).append("\n");
            i++;
        }

        return "HashTableCP{" +
                "numberOfCollisions=" + numberOfCollisions +
                ", size=" + size +
                '}' +"\n"+s.toString() ;
    }
    public int indexOf(K key)
    {
        int idx = isHASH1 ? hash1(key) : hash2(key);

        HashNode<K,V> node = data.get(idx);

        int i = 0;
        while (node != null || isDeleted[idx])
        {
            if(node != null && key.equals(node.key))
                return idx;
            idx = customHash(key, i++);
            node = data.get(idx);
        }
        return -1;
    }
    public V get(K key)
    {
        int idx = indexOf(key);

        if(idx == -1)return null;

        else return data.get(idx).value;
    }
    public boolean remove(K k)
    {
        int idx = indexOf(k);

        if(idx == -1) return false;

        data.set(idx, null);
        isDeleted[idx] = true;
        size--;

        return true;

    }

    public int getNumberOfCollisions() {
        return numberOfCollisions;
    }


}
