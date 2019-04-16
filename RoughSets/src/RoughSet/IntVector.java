/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package RoughSet;

import weka.core.FastVector;

/**
 *
 * @author Danel
 */
public class IntVector extends FastVector {

  public IntVector() {
  }

  public IntVector(int val) {
    add(val);
  }

  public IntVector(int[] values) {
    for (int i = 0; i < values.length; i++)
      add(values[i]);
  }

  public void add(int element) {
    addElement(new Integer(element));
  }

  public int getAt(int index) {
    return ((Integer)elementAt(index)).intValue();
  }

}
