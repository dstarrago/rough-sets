/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package RoughSet;

import java.util.*;

/**
 *
 * @author danels
 */
@Deprecated
public class Partition2 {

  private class IndexSetComparator implements Comparator {

    public int compare(Object a, Object b) {
      String sa, sb;
      sa = ((IndexSet)a).toString();
      sb = ((IndexSet)b).toString();
      return sa.compareTo(sb);
    }

  }

  TreeSet partition;

  public Partition2() {
    partition = new TreeSet<IndexSet>(new IndexSetComparator());
  }

  public Partition2(Partition2 iss) {
    partition = new TreeSet<IndexSet>(new IndexSetComparator());
    partition.addAll(iss.partition);
  }

  public int Cardinality() { 
    return partition.size();
  }

  public void Add(IndexSet Item) {
    partition.add(Item);
  }

  public IndexSet ItemAt(int index) {
    Iterator iter = partition.iterator();
    for (int i = 0; i < index; i++)
      iter.next();                        // REVISAR
    return (IndexSet)iter.next();
  }

  public void Pack() {
    Iterator iter = partition.iterator();
    IndexSet is;
    while(iter.hasNext()) {
      is = (IndexSet)iter.next();
      if (is.isEmpty()) iter.remove();
    }
  }

  public Partition2 Clone() {
    return new Partition2(this);
  }

  public boolean Empty() {
    return partition.isEmpty();
  }

  public Partition2 CrossProduct(Partition2 aSet) {
    Partition2 cp = new Partition2();
    IndexSet intersect, is1, is2;
    Iterator iter1 = partition.iterator();
    while(iter1.hasNext()) {
      is1 = (IndexSet)iter1.next();
      Iterator iter2 = aSet.partition.iterator();
      while(iter2.hasNext()) {
        is2 = (IndexSet)iter2.next();
        intersect = is1.intersection(is2);
        if (!intersect.isEmpty()) cp.Add(intersect);
      }
    }
    return cp;
  }

  public IndexSet SetOfItem(int Item) {
    Iterator iter = partition.iterator();
    IndexSet is = null;
    boolean Found = false;
    while(iter.hasNext() && !Found) {
      is = (IndexSet)iter.next();
      Found = is.contains(Item);
    }
    if (Found) return is; 
    else return null;
  }

  public @Override String toString() {
    return partition.toString();
  }
}
