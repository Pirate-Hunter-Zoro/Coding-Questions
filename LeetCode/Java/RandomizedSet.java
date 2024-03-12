package Java;
import java.util.HashMap;
/**
 * Implement the RandomizedSet class:
 * 
 * - RandomizedSet() Initializes the RandomizedSet object.
 * - bool insert(int val) Inserts an item val into the set if not present.
 * Returns
 * true if the item was not present, false otherwise.
 * - bool remove(int val) Removes an item val from the set if present. Returns
 * true if the item was present, false otherwise.
 * - int getRandom() Returns a random element from the current set of elements
 * (it's guaranteed that at least one element exists when this method is
 * called). Each element must have the same probability of being returned.
 * 
 * You must implement the functions of the class such that each function works
 * in average O(1) time complexity.
 * 
 * Link:
 * https://leetcode.com/problems/insert-delete-getrandom-o1/description/?envType=daily-question&envId=2024-01-16
 */
public class RandomizedSet {

    int count;
    private HashMap<Integer, Integer> indexToValues;
    private HashMap<Integer, Integer> valuesToIndex;
    
    public RandomizedSet() {
        count = 0;
        indexToValues = new HashMap<>();
        valuesToIndex = new HashMap<>();
    }

    public boolean insert(int val) {
        if (valuesToIndex.keySet().contains(val))
            return false;

        int index = count;
        indexToValues.put(index, val);
        valuesToIndex.put(val, index);
        count++;
        return true;
    }

    public boolean remove(int val) {
        if (!valuesToIndex.keySet().contains(val))
            return false;

        int index = valuesToIndex.get(val);
        valuesToIndex.remove(val);
        indexToValues.remove(index);
        count--;
        if (index < count) {
            int valueToMove = indexToValues.get(count);
            valuesToIndex.remove(valueToMove);
            indexToValues.remove(count);
            indexToValues.put(index, valueToMove);
            valuesToIndex.put(valueToMove, index);
        } // otherwise, we just removed the "last" value and we're fine as far as shifting the underlying map goes
        return true;
    }

    public int getRandom() {
        int idx = (int)(Math.random() * count);
        return indexToValues.get(idx);
    }

}
