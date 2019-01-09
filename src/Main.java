import openAddressing.HashTableCP;
import openAddressing.HashTableDH;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Main {

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
    //TODO : Create a Hashtable interface, for a generic test function

    public static void testSC(int testSize, boolean DEBUG)
    {
        HashTableSC<String,Integer> hashtableSC = new HashTableSC<>(DEBUG);
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < testSize ; i++)
        {
            String s = generateRandomString(7);
            strings.add(s);
            if(!hashtableSC.put(s, i))
                i--;

            if(DEBUG) System.out.println(hashtableSC);


        }
        if(DEBUG)System.out.println(hashtableSC);

        System.out.println("REPORT :"+"Test Size: "+ testSize);
        System.out.println("Using SEPARATE CHAINING Method: # of Collisions: "
                + hashtableSC.getNumberOfCollisions()
                + " Average hits: " + hashtableSC.getNumberOfHits() * 1.0 / testSize);


    }
    public static void testDH(int testSize, boolean DEBUG){
        HashTableDH<String,Integer> hashTableDH = new HashTableDH<>(DEBUG);
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < testSize ; i++)
        {
            String s = generateRandomString(7);
            strings.add(s);
            if(!hashTableDH.put(s, i))
                i--;
            if(DEBUG) System.out.println(hashTableDH);
        }
        if(DEBUG)System.out.println(hashTableDH);

        System.out.println("REPORT: " +"Test Size: "+ testSize);
        System.out.println("Using DOUBLE HASHING Method: # of Collisions: "
                + hashTableDH.getNumberOfCollisions()
                + " Average hits: " + hashTableDH.getNumberOfHits() * 1.0 / testSize);

                System.out.println("REMOVING :"+strings.size());

        for (String s :
                strings) {

            if(!hashTableDH.remove(s.trim()))
            {
                System.out.println("Couldnot delete key: "+s);
                System.out.println("Searching the key: " + hashTableDH.get(s));
            }

        }
        System.out.println(hashTableDH.size());



    }
    public static void testCP(int testSize, boolean DEBUG)
    {
        HashTableCP<String,Integer> hashTableDH = new HashTableCP<>(DEBUG);
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < testSize ; i++)
        {
            String s = generateRandomString(7);
            strings.add(s);
            if(!hashTableDH.put(s, i))
                i--;

            if(DEBUG) System.out.println(hashTableDH);


        }
        if(DEBUG)System.out.println(hashTableDH);

        System.out.println("REPORT: " +"Test Size: "+ testSize);
        System.out.println("Using CUSTOM PROBING Method: # of Collisions: "
                + hashTableDH.getNumberOfCollisions()
                + " Average hits: " + hashTableDH.getNumberOfHits() * 1.0 / testSize);

    }

    public static void main(String[] args) {
        // write your code here
        int testSize = 2000;

        //testCP(testSize, false);
        testDH(testSize, true);
        //testSC(testSize, false);

    }


}
