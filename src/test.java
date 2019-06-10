import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
@SuppressWarnings("Duplicates")
public class test {

    public static String generateRandomString(int length)
    {
        StringBuilder str = new StringBuilder();

        char randomChar;
        for (int i = 0; i < length; i++) {
            randomChar = (char) ThreadLocalRandom.current().nextInt((int)'a', (int)'z' + 1);
            str.append(randomChar);
        }
        return str.toString();
    }

    public static void testSC(int testSize, boolean DEBUG, boolean isHash1,int initSize) {

        HashTableSC<String,Integer> hashtableSC = new HashTableSC<>(DEBUG, isHash1, initSize);
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < testSize ; i++)
        {
            String s = generateRandomString(7);
            if(strings.contains(s))continue;
            if(hashtableSC.put(s, i)){
                strings.add(s);
            }

            if(DEBUG) System.out.println(hashtableSC);
        }
        if(DEBUG)System.out.println(hashtableSC);

        if(DEBUG)System.out.println("Searching :"+strings.size() + "Elements");
        hashtableSC.setNumberOfHash1Hits(0);
        hashtableSC.setNumberOfHash2Hits(0);
        for (String s :
                strings) {
            hashtableSC.get(s.trim());
        }

        String method = isHash1 ? "HASH1" : "HASH2";
        double avgHits = isHash1 ? hashtableSC.getNumberOfHash1Hits() * 1.0 / testSize : hashtableSC.getNumberOfHash2Hits() * 1.0 / testSize;

        System.out.println("SEPARATECHAINING----"+testSize+"---"+strings.size()+"---"+method +"---"
                + hashtableSC.getNumberOfCollisions()
                + "---" + avgHits);

        if(DEBUG)System.out.println("REMOVING :"+strings.size());
        for (String s :
                strings) {
            hashtableSC.remove(s.trim());
        }
        if(DEBUG)System.out.println(hashtableSC.size());
    }
    public static void testDH(int testSize, boolean DEBUG,boolean isHash1, int initSize) {
        HashTableDH<String,Integer> hashTableDH = new HashTableDH<>(DEBUG,isHash1,initSize);
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < testSize ; i++)
        {
            String s = generateRandomString(7);
            if(strings.contains(s))continue;
            if(hashTableDH.put(s, i)){
                strings.add(s);
            }
            if(DEBUG) System.out.println(hashTableDH);
        }
        if(DEBUG)System.out.println(hashTableDH);

        hashTableDH.setNumberOfHash1Hits(0);
        hashTableDH.setNumberOfHash2Hits(0);
        for (String s :
                strings) {
            hashTableDH.get(s.trim());
        }

        String method = isHash1 ? "HASH1" : "HASH2";
        double avgHits = isHash1 ? hashTableDH.getNumberOfHash1Hits() * 1.0 / testSize : hashTableDH.getNumberOfHash2Hits() * 1.0 / testSize;
        System.out.println("DOUBLE HASHING------"+testSize+"---"+strings.size()+"---"+method +"---"
                + hashTableDH.getNumberOfCollisions()
                + "---" + avgHits);

        if(DEBUG)System.out.println("REMOVING :"+strings.size());
        for (String s :
                strings) {

            if(!hashTableDH.remove(s.trim()))
            {
                System.out.println("Couldnot delete key: "+s);
                System.out.println("Searching the key: " + hashTableDH.get(s));
            }

        }
        if(DEBUG)System.out.println(hashTableDH.size());



    }
    public static void testCP(int testSize, boolean DEBUG, boolean isHash1, int initSize) {
        HashTableCP<String,Integer> hashTableCP = new HashTableCP<>(DEBUG,isHash1,initSize);
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < testSize ; i++)
        {
            String s = generateRandomString(7);
            if(strings.contains(s))continue;
            if(hashTableCP.put(s, i)){
                strings.add(s);
            }
            if(DEBUG) System.out.println(hashTableCP);


        }
        if(DEBUG)System.out.println(hashTableCP);
        hashTableCP.setNumberOfHash1Hits(0);
        hashTableCP.setNumberOfHash2Hits(0);
        for (String s :
                strings) {
            hashTableCP.get(s.trim());
        }

        String method = isHash1 ? "HASH1" : "HASH2";
        double avgHits = isHash1 ? hashTableCP.getNumberOfHash1Hits() * 1.0 / testSize : hashTableCP.getNumberOfHash2Hits() * 1.0 / testSize;
        System.out.println("QUADRATIC PROBING---"+testSize+"---"+strings.size()+"---"+method +"---"
                + hashTableCP.getNumberOfCollisions()
                + "---" + avgHits);
        if(DEBUG)System.out.println("REMOVING All "+ strings.size() + "Elements");
        for (String s : strings) {
            if(!hashTableCP.remove(s.trim()))
            {
                System.out.println("Couldnot delete key: "+s);
                System.out.println("Searching the key: " + hashTableCP.get(s));
            }
        }
        if(DEBUG)System.out.println(hashTableCP.size());

        for (int i = 0; i < testSize ; i++)
        {
            String s = generateRandomString(7);
            strings.add(s);
            if(!hashTableCP.put(s, i))
                i--;

            if(DEBUG) System.out.println(hashTableCP);


        }
        if(DEBUG)System.out.println(hashTableCP);

    }

    public static void main(String[] args) {
        // write your code here
        int testSize = 1500;
        int numberOfTests = 10;
        for (int i = 0; i < numberOfTests; i++) {
            System.out.println("TEST#"+i);
            testSize+=1000;
            testCP(testSize, false, true, 7);
            testCP(testSize, false, false, 7);
            testDH(testSize, false, true, 7);
            testDH(testSize, false, false, 7);
            testSC(testSize, false, true, 7);
            testSC(testSize, false, false, 7);
        }

    }

}
        /*REPORT: Test Size: 10000
        Using Custom Probing Method: HASH1# of Collisions: 3780 Average hits: 1.2826
        REPORT: Test Size: 10000
        Using DOUBLE HASHING Method: HASH1# of Collisions: 3772 Average hits: 1.2846
        REPORT :Test Size: 10000
        Using SEPARATE CHAINING Method:HASH1# of Collisions: 3343 Average hits: 1.0
        REPORT: Test Size: 10000
        Using Custom Probing Method: HASH2# of Collisions: 3612 Average hits: 1.2599
        REPORT: Test Size: 10000
        Using DOUBLE HASHING Method: HASH2# of Collisions: 3597 Average hits: 1.2668
        REPORT :Test Size: 10000
        Using SEPARATE CHAINING Method: HASH2# of Collisions: 3364 Average hits: 1.0 */