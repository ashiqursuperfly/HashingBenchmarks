public abstract class MyHashTable<K> {
    private int numberOfHash1Hits;
    private int numberOfHash2Hits;
    private int PRIME = 7;
    public static final double LOAD_FACTOR_THRESHOLD = 0.75;
    public int N;
    public MyHashTable(int n) {
        N = n;
    }

    public int hash1(K key)
    {
        numberOfHash1Hits++;
        return ( Math.abs(hash6432shift(key.hashCode())) % N);
    }
    public int auxHash(K key)
    {
        return PRIME - (Math.abs(key.hashCode()) % PRIME);
    }


    public int hash2(K key) {
        numberOfHash2Hits++;
        return (int) ((( Math.abs(key.hashCode())*Long.parseLong("2654435769")) >> 32) % N);
    }


    private int hash6432shift(long key)
    {
        key = (~key) + (key << 18); // key = (key << 18) - key - 1;
        key = key ^ (key >>> 31);
        key = key * 21; // key = (key + (key << 2)) + (key << 4);
        key = key ^ (key >>> 11);
        key = key + (key << 6);
        key = key ^ (key >>> 22);
        return (int) key;
    }
    public int getNumberOfHash1Hits() {
        return numberOfHash1Hits;
    }
    public int getNumberOfHash2Hits() {
        return numberOfHash2Hits;
    }
    public void setNumberOfHash1Hits(int numberOfHash1Hits) {
        this.numberOfHash1Hits = numberOfHash1Hits;
    }
    public void setNumberOfHash2Hits(int numberOfHash2Hits) {
        this.numberOfHash2Hits = numberOfHash2Hits;
    }
}
