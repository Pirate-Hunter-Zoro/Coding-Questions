package Java;
import java.util.HashMap;
import java.util.HashSet;
/**
 * RandomizedCollection is a data structure that contains a collection of
 * numbers, possibly duplicates (i.e., a multiset). It should support inserting
 * and removing specific elements and also reporting a random element.
 * 
 * Implement the RandomizedCollection class:
 * - RandomizedCollection() Initializes the empty RandomizedCollection object.
 * - bool insert(int val) Inserts an item val into the multiset, even if the item
 *  is already present. Returns true if the item is not present, false otherwise.
 * - bool remove(int val) Removes an item val from the multiset if present.
 *  Returns true if the item is present, false otherwise. Note that if val has
 *  multiple occurrences in the multiset, we only remove one of them.
 * - int getRandom() Returns a random element from the current multiset of
 *  elements. The probability of each element being returned is linearly related
 *  to the number of the same values the multiset contains.
 * 
 * You must implement the functions of the class such that each function works
 * on average O(1) time complexity.
 * 
 * Note: The test cases are generated such that getRandom will only be called if
 * there is at least one item in the RandomizedCollection.
 * 
 * Link:
 * https://leetcode.com/problems/insert-delete-getrandom-o1-duplicates-allowed/description/
 */
public class RandomizedCollection {

    int count;
    private HashMap<Integer, HashSet<Integer>> valueToIndices;
    private HashMap<Integer, Integer> indexToValue;
    private HashMap<Integer, Integer> valueToCount;
    
    public RandomizedCollection() {
        count = 0;
        valueToIndices = new HashMap<>();
        indexToValue = new HashMap<>();
        valueToCount = new HashMap<>();
    }

    public boolean insert(int val) {
        if (valueToIndices.keySet().contains(val)) {
            valueToIndices.get(val).add(count);
            indexToValue.put(count, val);
            valueToCount.put(val, valueToCount.get(val)+1);
            count++;
            return false;
        } else {
            valueToIndices.put(val, new HashSet<>());
            valueToIndices.get(val).add(count);
            indexToValue.put(count, val);
            valueToCount.put(val, 1);
            count++;
            return true;
        }
    }

    public boolean remove(int val) {
        if (!valueToCount.keySet().contains(val))
            return false;

        count--;
        int index = valueToIndices.get(val).iterator().next();
        valueToIndices.get(val).remove(index);
        if (valueToIndices.get(val).isEmpty()) { // that was the last instance of this element
            valueToIndices.remove(val);
            valueToCount.remove(val);
            indexToValue.remove(index);
        } else {
            valueToCount.put(val, valueToCount.get(val)-1);
            valueToIndices.get(val).remove(index);
            indexToValue.remove(index);
        }
        // now we need to do the shifting
        if (index < count) { // then we need to shift the "last" element in terms of our indexing
            int valueToMove = indexToValue.get(count);
            indexToValue.remove(count);
            indexToValue.put(index, valueToMove);
            valueToIndices.get(valueToMove).remove(count);
            valueToIndices.get(valueToMove).add(index);
        } // otherwise we just removed the "last" element and do not need to make any other changes
        return true;
    }

    public int getRandom() {
        int idx = (int)(count * Math.random());
        return indexToValue.get(idx);
    }

}
