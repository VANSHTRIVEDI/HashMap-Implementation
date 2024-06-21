package Properties_Of_Oops.HashMapImplementation;

import java.util.ArrayList;
import java.util.List;

public class OurMap<K, V> {
    private List<Node<K, V>> bucket;
    private int capacity; // current capacity of
    private int initialCapacity = 5; // intial length of bucket array
    private int size; // number of element present in the array

    OurMap() {
        bucket = new ArrayList<>();
        capacity = initialCapacity;
        for (int i = 0; i < capacity; i++) {
            bucket.add(null);
            /*
             * this is a important step because we are ussing list and without adding any
             * element the list will be of size 0 so if a element what to be placed at
             * index
             * 2 it cannot be placed because list is 0
             */

        }

    }

    public V get(K key) {
        int bucketIndex = getBucketIndex(key);
        Node<K, V> headNode = bucket.get(bucketIndex);
        while (headNode != null) {
            if (headNode.key.equals(key))
                /*
                 * Why use equals?
                 * We have to use equals here because == compares the reference of object not
                 * the value and all the wrapper class such as Integer,String has orveridden
                 * equals method which compares its value
                 * 
                 * why is it important to overrise hashcode and equals method when using custom?
                 * It's important to override the hashCode and equals methods when using custom
                 * classes because the functioning of hashing depends on these methods. This is
                 * necessary to ensure the custom class works correctly in hash-based
                 * collections such as HashMap and HashSet. Wrapper classes for primitive types
                 * already have these methods overridden, which is why they work seamlessly with
                 * hash-based collections.
                 */

                return headNode.value;
            headNode = headNode.next;
        }
        return null;
    }

    private int getBucketIndex(K key) {
        int hashcode = key.hashCode();

        return hashcode % capacity; // to get index inside bucket capacity
    }

    public void put(K key, V value) {
        int bucketIndex = getBucketIndex(key);
        Node<K, V> headNode = bucket.get(bucketIndex);

        // if headNode is present will loop will work
        while (headNode != null) {
            if (headNode.key.equals(key)) {
                headNode.value = value;
                return;
            }
            headNode = headNode.next;
        }
        // if key not found and we have to create new node
        size++;
        Node<K, V> newhead = new Node<>(key, value);
        headNode = bucket.get(bucketIndex);
        newhead.next = headNode;
        bucket.set(bucketIndex, newhead);

        double loadFactor = (1.0 * size) / capacity;
        if (loadFactor > 0.7) {
            rehash();
        }

    }

    private void rehash() {
        System.out.println("Rehashing buckets");
        List<Node<K, V>> temp = bucket; // storing all the bucket value
        bucket = new ArrayList<>();/// creating a new bucket means there is not element in bucket now
        capacity *= 2;
        for (int i = 0; i < capacity; i++)
            bucket.add(null);
        size = 0;
        for (int i = 0; i < temp.size(); i++) {
            Node<K, V> head = temp.get(i);
            while (head != null) {
                put(head.key, head.value);
                head = head.next;
            }

        }

    }

    public void remove(K key) {
        int bucketIndex = getBucketIndex(key);
        Node<K, V> headNode = bucket.get(bucketIndex);
        Node<K, V> prevNode = null;
        while (headNode != null) {

            if (headNode.key.equals(key)) {
                if (prevNode == null) {
                    bucket.set(bucketIndex, headNode.next);
                } else {
                    prevNode.next = headNode.next;

                }
                headNode.next = null;
                size--;
            }
            prevNode = headNode;
            headNode = headNode.next;

        }

    }
}

class Node<K, V> {
    K key;
    V value;
    Node<K, V> next;

    public Node(K key, V value) {
        this.key = key;
        this.value = value;
    }
}
