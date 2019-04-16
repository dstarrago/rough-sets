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
public class eIndiscernibility extends IndiscernibilityRelation {

  double[] Alfa = {0.25, 0.0, 0.0};
  double[] Beta = {0.0, 10.0, 0.0};

  public eIndiscernibility(DecisionSystem ds, IntVector attrs) {
    super(ds, attrs);
    partition = BuildPartition();
  }
        
  public eIndiscernibility(DecisionSystem ds) {
    super(ds);
    partition = BuildPartition();
  }

  public Partition BuildPartition() {
    Partition result = new Partition();
    for (int i = 0; i < getDSystem().numInstances(); i++) {
        IndexSet classSet = new IndexSet();
        for (int j = 0; j < getDSystem().numInstances(); j++)
          if (Similarity(getDSystem().instance(j), getDSystem().instance(i)) == 1)
            classSet.add(j);
        result.add(classSet);
      }
    return result;
  } 
  
  private double Similarity(Instance subject, Instance referent) {
    double Concordance = 0;
    for (int i = 0; i < getAttrs().size(); i++) {
      double Ei = Alfa[i] * referent.value(i) + Beta[i];
      if (Math.abs(subject.value(i) - referent.value(i)) <= Ei)
        Concordance = 1;
      else {
        Concordance = 0;
        break;
      }
    }
    return Concordance;  
  }
  
}
