/*
 * RSTMetrics.java
 * Copyright (C) 2009 Danel Sanchez Tarrago
 */

package RoughSet;

import java.io.*;
import java.util.*;

import weka.core.Instances;
import weka.core.Utils;
import weka.core.Option;

/**
 *
 * @author Danel Sanchez Tarrago (danels@uclv.edu.cu)
 * @version $Revision: 1.0 $
 */
public class RoughSet {
   
  public Partition classPartition;
  public Partition indiscernability;
  public Instances dataset;
  public IndexSet universe;
  public Partition UpperApprox;
  public Partition LowerApprox;
  public IndexSet Positive;
  public IndexSet Negative;
  public IndexSet Boundary;
  public double SimilarityThreshold = 0.87;
  //public double SimilarityThreshold = 0.90;
  //public double SimilarityThreshold = 0.95;
  //public double SimilarityThreshold = 0.99;

  /** Constructor */ 
  public RoughSet(Instances instances) throws Exception {
    dataset = instances;
    Initialize();
    ComputApproximations();
}

private void Initialize() throws Exception {
    if (dataset.numAttributes() < 2)
      throw new Exception("Análisis Imposible: faltan atributos");
    if (dataset.numInstances() == 0)
      throw new Exception("Análisis imposible: base de datos sin instancias");
    universe = new IndexSet();
    for (int i = 0; i < dataset.numInstances(); i++)
      universe.add(i);
    CreateClassPartition();
    SimilarityRelation sr = new SimilarityRelation(new DecisionSystem(dataset), SimilarityThreshold);
    indiscernability = sr.getPartition();
  }
        
  /**
   * Create a class partition consisting in a set for each class containing 
   * the index of instances belonging to that class
   */
  private void CreateClassPartition() {
    classPartition = new Partition();
    //
    //   the predictable attribute is always assumed to be in the last position
    //
    int ci = dataset.numAttributes() - 1;
    /**
    if (dataset.classIndex() == -1) 
      ci = dataset.numAttributes() - 1;
    else
      ci = dataset.classIndex();
     */
    for (int i = 0; i < dataset.numClasses(); i++)
      classPartition.add(new IndexSet());
    for(int i = 0; i < dataset.numInstances(); i++)
      classPartition.at((int)dataset.instance(i).value(ci)).add(i);
  }
  
  /**
   * Return the set containing indexes of all elements belonging to a classindex
   */
  public IndexSet InstancesOfClass(int classIndex) {
    return classPartition.at(classIndex);
  }

  /**
   * Return Upper Approximation of a set if instances from indiscernibility Relation
   */
  private IndexSet UpperApprox(IndexSet instances) {    // X
    IndexSet Rprime, result = new IndexSet();
    Iterator it = instances.getIterator();
    while (it.hasNext()) {
      int x = ((Integer)it.next()).intValue();      // x
      Rprime = indiscernability.at(x);             // R'(x)
      if (!Rprime.isEmpty())
        result.append(Rprime);                      // U R'(x)
    }
    return result;
  }
      
  /**
   * Return Lower Approximation of a set if instances from indiscernibility Relation
   */
  private IndexSet LowerApprox(IndexSet instances) {          // X
    IndexSet Rprime, result = new IndexSet();
    Iterator it = instances.getIterator();
    while (it.hasNext()) {
      int x = ((Integer)it.next()).intValue();      // x
      Rprime = indiscernability.at(x);             // R'(x)
      if (Rprime.isSubSet(instances))
        result.add(x);
    }
    return result;
  }
  
  /**
   * Compute Upper and Lower Approximations, and Positive, Negative and Boundary Regions
   */
  private void ComputApproximations() {
    IndexSet uUpperApp = new IndexSet();
    IndexSet la, ua;
    LowerApprox = new Partition();
    UpperApprox = new Partition();
    Positive = new IndexSet();
    Negative = new IndexSet();
    Boundary = new IndexSet();
    for (int i = 0; i < dataset.numClasses(); i++) {
      la = LowerApprox(InstancesOfClass(i));
      ua = UpperApprox(InstancesOfClass(i));
      LowerApprox.add(la);
      UpperApprox.add(ua);
      if (!la.isEmpty())
        Positive.append(la);
      if (!ua.isEmpty())
        uUpperApp.append(ua);
    }
    Negative.append(universe.difference(uUpperApp));
    Boundary.append(uUpperApp.difference(Positive));
  }

  /**
   * Returns a string describing this class
   * @return a description of the class suitable for
   * displaying in the explorer/experimenter gui
   */
  public String globalInfo() {
    return "Extract Rough Set Characteristics from datasets"; 
  }

   /**
   * Returns an enumeration describing the available options.
   *
   * @return an enumeration of all the available options.
   */
  public Enumeration listOptions() {
    Vector newVector = new Vector(1);
    newVector.addElement(new Option(
	              "\tSimilarityThreshold (default 0.87)",
	              "E", 1, "-E <num>"));
    return newVector.elements();
  }
  
  public void setSimilarityThreshold(double e) {
    SimilarityThreshold = e;
  }

/**
   * Parses a given list of options. Valid option is:<p>
   *   
   * -E similarity threshold <br>
   * How much similar two instances have to be for been considered the same instance. <p>
   * Values ranges from 0.5 to 1. Values closer to 1 enforce similarity. 
   * 
   * @param options the list of options as an array of strings
   * @exception Exception if an option is not supported 
   */
  public void setOptions(String[] options) throws Exception {
    String similarityString = Utils.getOption('E', options);
    if (similarityString.length() != 0) {
      setSimilarityThreshold(Double.valueOf(similarityString).doubleValue());
    } else {
      setSimilarityThreshold(0.87);
    }
  }

  /**
   * Gets the current settings of the RSTMetrics object.
   *
   * @return an array of strings suitable for passing to setOptions
   */
  public String [] getOptions() {
    String [] options = new String [8];
    int current = 0;

    options[current++] = "-E"; 
    options[current++] = "" + SimilarityThreshold;

    while (current < options.length) {
    options[current++] = "";
    }
    return options;
  }
  
  /**
   * Main method for testing this class.
   *
   * @param argv the options
   */
  public static void main(String args[]) throws Exception {
    //System.out.println("Hola mundo");
    
    BufferedReader dataReader = null;
    String dataFileName, classIndexString, objectOutputFileName;
    Instances data = null;
    int classIndex = -1;
    
    try {
      try {
      classIndexString = Utils.getOption('c', args);
      if (classIndexString.length() != 0) {
	classIndex = Integer.parseInt(classIndexString);
      }
      dataFileName = Utils.getOption('f', args); 
      objectOutputFileName = Utils.getOption('d', args);
        if (dataFileName.length() != 0) {
            dataReader = new BufferedReader(new FileReader(dataFileName));
            }
        } catch (Exception e) {
            throw new Exception("Can't open file " + e.getMessage() + '.');
        }
      if (dataFileName.length() != 0) {
        data = new Instances(dataReader);
	if (classIndex != -1) {
	  data.setClassIndex(classIndex - 1);
	} else {
	  data.setClassIndex(data.numAttributes() - 1);
	}
	if (classIndex > data.numAttributes()) {
	  throw new Exception("Index of class attribute too large.");
	}
      }
    // Do what you want to do with data
    
    } catch (Exception e) {
          throw new Exception("\nWeka exception: " + e.getMessage());
      }
    // Save the classifier if an object output file is provided
    if (objectOutputFileName.length() != 0) {
      PrintWriter pw = new PrintWriter(new FileWriter(objectOutputFileName));
      RoughSet rstm = new RoughSet(data);
      pw.print(rstm.toString());
      pw.close();
    }
    
  }
}
      