/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package RoughSet;

/**
 *
 * @author danels
 */
public abstract class IndiscernibilityRelation {

  private DecisionSystem ds;
  private IntVector attrs;     // Set of attribute indexes
  protected Partition partition;

  public IndiscernibilityRelation(DecisionSystem ds, IntVector attrs) {
    this.ds = ds;
    this.attrs = attrs;
  }

  public IndiscernibilityRelation(DecisionSystem ds) {
    this.ds = ds;
    attrs = new IntVector();
    int ci = (ds.classIndex() == -1)? ds.numAttributes() - 1: ds.classIndex();
    for (int i = 0; i < ds.numAttributes(); i++)
      if (i != ci) attrs.add(i);
  }

  protected abstract Partition BuildPartition();

  public DecisionSystem getDSystem() {
    return ds;
  }

  public IntVector getAttrs() {
    return attrs;
  }

  public Partition getPartition() {
    return partition;
  }
  
  public IndexSet getClassOf(int Item) {
    if (partition == null) return null;
    return partition.getSetOf(Item);     
  }

  public boolean areIndiscernible(int a, int b) {
    return getClassOf(a) == getClassOf(b);
  }

}
