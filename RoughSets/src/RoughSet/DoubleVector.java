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
public class DoubleVector extends FastVector {
      
  public void add(VDouble element) {
    addElement(element);
  }

  public VDouble getAt(int index) {
    return (VDouble)elementAt(index);
  }
}
