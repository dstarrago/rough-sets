/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package RoughSet;

/**
 *
 * @author danels
 */
public class Relation extends MyFastVector {

  public Relation () {
  }

  public void Add(Partition element) {
    super.addElement(element);
  }

  public Partition Get(int index) {
    return (Partition)elementAt(index);
  }

}
