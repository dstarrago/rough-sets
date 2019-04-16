/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package RoughSet;

import weka.core.Instance;

/**
 *
 * @author danels
 */
public class SimilarityRelation extends IndiscernibilityRelation {
  
private DoubleVector attrRanges = new DoubleVector();
private double EPSILON = 0.87;   // Valor utilizado por Yailé 0.87


  public SimilarityRelation(DecisionSystem ds, IntVector attrs) {
    super(ds, attrs);
    partition = BuildPartition();
  }

  public SimilarityRelation(DecisionSystem ds, IntVector attrs, double SimilarityThreshold) {
    super(ds, attrs);
    EPSILON = SimilarityThreshold;
    partition = BuildPartition();
  }

  public SimilarityRelation(DecisionSystem ds) {
    super(ds);
    partition = BuildPartition();
  }

  public SimilarityRelation(DecisionSystem ds, double SimilarityThreshold) {
    super(ds);
    EPSILON = SimilarityThreshold;
    partition = BuildPartition();
  }

  protected Partition BuildPartition(){
    AttrRanges();
    Partition result = new Partition();
    for (int i = 0; i < getDSystem().numInstances(); i++) {
        IndexSet classSet = new IndexSet();
        classSet.add(i);
        for (int j = 0; j < i; j++)
          if (Similarity(getDSystem().instance(i), getDSystem().instance(j)) > EPSILON)
            classSet.add(j);
        for (int j = i + 1; j < getDSystem().numInstances(); j++)
          if (Similarity(getDSystem().instance(i), getDSystem().instance(j)) > EPSILON)
            classSet.add(j);
        result.add(classSet);
      }
    return result;
  } 

  private double Similarity(Instance A, Instance B) {
    final double W = (1.0) / (getAttrs().size());
    double Sum = 0;
    int rangeCount = 0;
    for (int i = 0; i < getAttrs().size(); i++) {
      if (getDSystem().attribute(getAttrs().getAt(i)).isNumeric()) {
        if (attrRanges.getAt(rangeCount).Value != 0) {
          double R = 1 - Math.abs(A.value(i) - B.value(i)) / attrRanges.getAt(rangeCount++).Value;
          Sum += W * R;
        } else {
          Sum += W;
          rangeCount++;
        }
      } else {
        if (A.value(i) == B.value(i))
          Sum += W;
      }
    }
    return Sum;  
  }

  /**
   * Compute numeric attributes ranges
   */
  private void AttrRanges() {
    VDouble range;
    double value;
    for (int i = 0; i < getAttrs().size(); i++)
      if (getDSystem().attribute(getAttrs().getAt(i)).isNumeric()) {
        double max = Double.NaN;
        double min = Double.NaN;
        for (int j = 0; j < getDSystem().numInstances(); j++) {
          value = getDSystem().instance(j).value(i);
          if (Double.isNaN(min)) {
            min = max = value;
          } else if (value < min) {
            min = value;
          } else if (value > max) {
            max = value;
          }
        }
        range = new VDouble();
        range.Value = max - min;
        attrRanges.add(range);
      }
  }
  
}
