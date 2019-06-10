public class HashNode<K,V> {
    K key;
    V value;

    public HashNode() {
    }

    public HashNode(K key, V value)
    {
        this.key = key;
        this.value = value;
    }


    @Override
    public String toString() {
        return "(" + key +
                "," + value +
                ')';
    }
}
