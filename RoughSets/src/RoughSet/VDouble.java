/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package RoughSet;

import java.io.Serializable;

/**
 *
 * @author danels
 */
public class VDouble implements Serializable {

  boolean Invalid;
  double Value;

  public VDouble() {
  }

  public VDouble(double aVal) {
    Value = aVal;
  }

  public void invalidate() {
    Invalid = true;
  }

  public @Override String toString () {
    if (Invalid) 
      return "Invalid";
    else return String.valueOf(Value);
  }
}
