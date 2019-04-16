/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package RoughSet;

import weka.core.Instances;
import weka.core.FastVector;
import weka.filters.unsupervised.attribute.PKIDiscretize;

/**
 *
 * @author danels
 */
public class ClassicRSTRelation extends IndiscernibilityRelation {

  private Instances dataset;

  public ClassicRSTRelation(DecisionSystem ds, IntVector attrs) throws Exception {
    super(ds, attrs);
    preProcessData();
    partition = BuildPartition();
  }

  public ClassicRSTRelation(DecisionSystem ds)  throws Exception {
    super(ds);
    preProcessData();
    partition = BuildPartition();
  }

  /**
   * Data is tried to be discretized and the result is assigned to attribute dataset.
   * If discretization is impossible an exception is thrown.
   * @throws java.lang.Exception
   */
  @SuppressWarnings("static-access")
  private void preProcessData() throws Exception {
    Instances instances = getDSystem().getDataSet();
    PKIDiscretize theFilter = new PKIDiscretize();
    theFilter.setInputFormat(instances);
    try {
      dataset = theFilter.useFilter(instances, theFilter);
    } catch (Exception e) {
        throw new Exception("Discretization failed");
    }
  }

  protected Partition BuildPartition() {
    FastVector relation = new FastVector();
    int numAttrs = getAttrs().size();
    for (int i = 0; i < numAttrs; i++)
      relation.addElement(EquivalenceRelation(getAttrs().getAt(i)));
    if (numAttrs > 2) {
      Partition Result = (Partition)relation.firstElement();
      for(int i = 1; i < numAttrs - 1; i++) {
        Partition temp = Result;
        Result = temp.crossProduct((Partition)relation.elementAt(i));
      }
      return Result;
    }
    else    // numAttribute should be 2
    {
      Partition Result = ((Partition)relation.firstElement()).clone();
      Result.pack();
      return Result;
    }
  }
  
  private Partition EquivalenceRelation(int attrIndex) {
    Partition er = new Partition();
    for (int i = 0; i < dataset.attribute(attrIndex).numValues(); i++)
      er.add(InstancesXAttrVal(attrIndex, dataset.attribute(attrIndex).value(i)));
    return er;
  }
      
  private IndexSet InstancesXAttrVal(int attrIndex, String classVal) {
    IndexSet is = new IndexSet();
    for (int i = 0; i < dataset.numInstances(); i++)
      if (dataset.instance(i).stringValue(attrIndex).equals(classVal))
        is.add(i);
    return is;
  }
}
