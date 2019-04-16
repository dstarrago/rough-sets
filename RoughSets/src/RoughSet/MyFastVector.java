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
public abstract class MyFastVector extends FastVector {

  protected abstract Object Get(int index); 

  public @Override String toString() {
    int z = super.size();
    StringBuffer sb = new StringBuffer();
    sb.append("[");
    for (int i = 0; i < z - 1; i++) 
      sb.append(Get(i).toString() + ", ");
    if (z > 0) 
      sb.append(Get(z - 1).toString());
    sb.append("]");
    return sb.toString();
  }
  
}
