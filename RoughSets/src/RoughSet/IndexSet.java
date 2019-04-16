/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package RoughSet;

import java.util.*;

/**
 * This class represents a set of indices and implements elemental operations
 * between sets: union, intersection, difference
 *
 * @author danels
 */
public class IndexSet {

  private TreeSet intset;

  public IndexSet() {
    intset = new TreeSet<Integer>();
  }

  public IndexSet(IndexSet is) {
    intset = new TreeSet<Integer>(is.intset);
  }

  public IndexSet(int[] values) {
    this();
    for (int i = 0; i < values.length; i++)
      add(values[i]);
  }

  public int cardinality() {
    return intset.size(); 
  }

  public void add(int Item) {
    intset.add(new Integer(Item));  
  }

  public IndexSet clone() {
    return new IndexSet(this);
  }

  public boolean contains(int Item) {
    return intset.contains(new Integer(Item));
  }

  public boolean isEmpty() {
    return intset.isEmpty();  
  }

  public boolean isSubSet(IndexSet aSet) {
    return aSet.intset.containsAll(intset);
  }

  public void append(IndexSet aSet) {
    intset.addAll(aSet.intset);
  }

  public IndexSet union(IndexSet aSet) {
    IndexSet is = clone();
    is.append(aSet);
    return is;  
  }

  public IndexSet intersection(IndexSet aSet) {
    IndexSet is = aSet.clone();
    is.intset.retainAll(intset);  
    return is;
  }

  public IndexSet difference(IndexSet aSet) {
    /**
    IntSet is = aSet.Clone();
    is.intset.removeAll(intset);  
    */
    IndexSet is = new IndexSet();
    Integer a;
    Iterator iter = intset.iterator();
    while(iter.hasNext()) {
      a = (Integer)iter.next();
      if (!aSet.intset.contains(a)) 
        is.intset.add(a);
    }
    return is;
  }

  public @Override String toString() {
    return intset.toString();
  }

  public Iterator getIterator() {
    return intset.iterator();
  }

}
