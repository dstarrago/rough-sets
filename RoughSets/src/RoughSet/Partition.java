/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package RoughSet;

import weka.core.FastVector;

/**
 *
 * @author danels
 */
public class Partition extends FastVector {

  public void add(IndexSet element) {
    addElement(element);
  }

  public IndexSet at(int index) {
    if (elementAt(index) == null) {
      IndexSet result = new IndexSet();
      insertElementAt(result, index);
      return result;
    }
    else return (IndexSet)elementAt(index);
  }

  public void pack() {
    IndexSet is;
    for (int i = 0; i < size();) {
      is = at(i);
      if (is.isEmpty()) removeElementAt(i);
        else i++;
    }
  }

  @Override
  public Partition clone() {
    Partition nv = new Partition();
    nv.appendElements(this);
    return nv;
  }

  public boolean isEmpty() {
    return size() == 0;
  }

  public Partition crossProduct(Partition aVect) {
    Partition cp = new Partition();
    IndexSet intersect;
    for (int i = 0; i < size(); i++) {
      for (int j = 0; j < aVect.size(); j++) {
        intersect = at(i).intersection(aVect.at(j));
        if (!intersect.isEmpty()) cp.add(intersect);
      }
    }
    return cp;
  }

  public IndexSet getSetOf(int Item) {
    IndexSet is = null;
    boolean Found = false;
    for (int i = 0; (i < size()) && (!Found); i++) {
      is = at(i);
      Found = is.contains(Item);
    }
    if (Found) return is; 
    else return null;
  }

  public @Override String toString() {
    int z = super.size();
    StringBuffer sb = new StringBuffer();
    sb.append("[");
    for (int i = 0; i < z - 1; i++)
      sb.append(at(i).toString() + ", ");
    if (z > 0)
      sb.append(at(z - 1).toString());
    sb.append("]");
    return sb.toString();
  }

}
