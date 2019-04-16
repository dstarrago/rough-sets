/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package RoughSet;

import weka.core.Instances;
import weka.core.Attribute;
import weka.core.Instance;

/**
 *
 * @author Danel
 */
public class DecisionSystem {
  
  private Instances dataset;
  private IndexSet universe;
  private Partition classPartition;

  public DecisionSystem(Instances dataSet) {
    this.dataset = dataSet;
    inflateUniverse();
    CreateClassPartition();
  }

  private void inflateUniverse() {
    universe = new IndexSet();
    for (int i = 0; i < dataset.numInstances(); i++)
      universe.add(i);
  }

  /**
   * Create a class partition consisting in a set for each class containing
   * the index of instances belonging to that class
   */
  private void CreateClassPartition() {
    classPartition = new Partition();
    int ci = (dataset.classIndex() == -1)? dataset.numAttributes() - 1: dataset.classIndex();
    for (int i = 0; i < dataset.numClasses(); i++)
      classPartition.add(new IndexSet());
    for(int i = 0; i < dataset.numInstances(); i++)
      classPartition.at((int)dataset.instance(i).value(ci)).add(i);
  }

  public IndexSet getUniverse() {
    return universe;
  }

  public Partition getClassPartition() {
    return classPartition;
  }

  public IndexSet getClass(int index) {
    return classPartition.at(index);
  }

  public IndexSet getClassOf(int itemIndex) {
    return classPartition.getSetOf(itemIndex);
  }

  public int numAttributes() {
    return dataset.numAttributes();
  }

  public int numInstances() {
    return dataset.numInstances();
  }

  public int classIndex() {
    return dataset.classIndex();
  }

  public Attribute attribute(int index) {
    return dataset.attribute(index);
  }

  public Instance instance(int index) {
    return dataset.instance(index);
  }

  public Instances getDataSet() {
    return dataset;
  }
  
}
