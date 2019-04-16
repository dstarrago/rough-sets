/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package RoughSet;

import java.util.*;

/**
 *
 * @author Danel
 */
public class RSTmeasures {

  public RoughSet roughset;

  private double TotalRoughMembership;
  private double TotalRoughInvolvement;
  private double TotalRoughAgreement;
  private DoubleVector MeanRoughMembershipForClass;
  private DoubleVector MeanRoughInvolvementForClass;
  private DoubleVector MeanRoughAgreementForClass;
  private DoubleVector AccuracyForClasses;
  private DoubleVector QualityForClasses;

  public RSTmeasures(RoughSet r) {
    roughset = r;
    ComputeMembershipFunctions();
    ComputMeasures();
  }
  
  /**
   * Compute efficiently the Mean Rough Membership and the Mean Rough Involvement
   * for all classes, and the Total Rough Membership and Total Rough Involvement
   */
  private void ComputeMembershipFunctions() {
    MeanRoughMembershipForClass = new DoubleVector();
    MeanRoughInvolvementForClass = new DoubleVector();
    MeanRoughAgreementForClass = new DoubleVector();
    TotalRoughMembership = 0;
    TotalRoughInvolvement = 0;
    TotalRoughAgreement = 0;
    for (int i = 0; i < roughset.dataset.numClasses(); i++) {
      double PartialRoughMembership = 0;
      double PartialRoughInvolvement = 0;
      double PartialRoughAgreement = 0;
      IndexSet X = roughset.InstancesOfClass(i);
      Iterator iter = X.getIterator();
      while(iter.hasNext()) {         // Recorrer las instancias de X
        IndexSet R = ClassOf((Integer)iter.next());
        IndexSet I = R.intersection(X);
        IndexSet O = R.union(X);
        double RoughMembership = (double)I.cardinality() / R.cardinality();   // Certainly |R| >= 1 because reflection
        double RoughInvolvement = (X.cardinality() == 0)? 0 : (double)I.cardinality() / X.cardinality();
        double RoughAgreement = (double)I.cardinality() / O.cardinality();
        PartialRoughMembership += RoughMembership;
        PartialRoughInvolvement += RoughInvolvement;
        PartialRoughAgreement += RoughAgreement;
      }
      MeanRoughMembershipForClass.addElement(new VDouble(PartialRoughMembership / X.cardinality()));
      MeanRoughInvolvementForClass.addElement(new VDouble(PartialRoughInvolvement / X.cardinality()));
      MeanRoughAgreementForClass.addElement(new VDouble(PartialRoughAgreement / X.cardinality()));
      TotalRoughMembership += ((VDouble)MeanRoughMembershipForClass.elementAt(i)).Value;
      TotalRoughInvolvement += ((VDouble)MeanRoughInvolvementForClass.elementAt(i)).Value;
      TotalRoughAgreement += ((VDouble)MeanRoughAgreementForClass.elementAt(i)).Value;
    }
  }


  /**
   * Compute for each class Accuracy, Quality and Dependency of Approximation
   */
  private void ComputMeasures() {
    AccuracyForClasses = new DoubleVector();
    QualityForClasses = new DoubleVector();
    //fDependency = new VectorOfDoubles();
    for (int i = 0; i < roughset.dataset.numClasses(); i++) {
      AccuracyForClasses.addElement(Accuracy(i));
      QualityForClasses.addElement(Quality(i));
      //fDependency.addElement(Dependency(i));
    }
  }

  /**
   * Compute Accuracy of Approximation for one Class
   */
  private VDouble Accuracy(int ClassIndex) { // Precisión de la Aproximación para una clase
    VDouble v = new VDouble();
    if (roughset.UpperApprox.at(ClassIndex).cardinality() == 0)
      v.invalidate();  // There is no metric in this case
      else v.Value = (double)roughset.LowerApprox.at(ClassIndex).cardinality() / roughset.UpperApprox.at(ClassIndex).cardinality();
    return v;
  }

  /**
   * Compute Mean Accuracy of Approximation
   */
  public double MeanAccuracy() {  // Precisión Media de la clasificación
    double Cum = 0;
    for (int i = 0; i < roughset.dataset.numClasses(); i++) {
      VDouble v = (VDouble)AccuracyForClasses.elementAt(i);
      if (!v.Invalid)
        Cum += v.Value;
    }
    return Cum / roughset.dataset.numClasses();
  }

  /**
   * Compute Weighted Classification Accuracy
   */
  /* Precisión ponderada de la clasificación
   */
  public double WeightedClassifAccuracy() {
    double Sum = 0;
    VDouble v;
    for (int i = 0; i < roughset.dataset.numClasses(); i++) {
      v = (VDouble)AccuracyForClasses.elementAt(i);
      if (!v.Invalid)
        Sum += v.Value * roughset.InstancesOfClass(i).cardinality();
    }
    return Sum / roughset.dataset.numInstances();
  }

  /**
   * Compute Generalized Classification Accuracy
   * weighing by MeanRoughMembershipForClass
   */
  /* Precisión generalizada de la clasificación
   */
  public double GenClassifAccuracy1() {
    double Sum = 0;
    VDouble v;
    for (int i = 0; i < roughset.dataset.numClasses(); i++) {
      v = (VDouble)AccuracyForClasses.elementAt(i);
      if (!v.Invalid)
        Sum += v.Value * ((VDouble)MeanRoughMembershipForClass.elementAt(i)).Value;
    }
    return Sum / TotalRoughMembership;
  }

  /**
   * Compute Generalized Classification Accuracy
   * weighing by MeanRoughInvolvementForClass
   */
  /* Precisión generalizada de la clasificación
   */
  public double GenClassifAccuracy2() {
    double Sum = 0;
    VDouble v;
    for (int i = 0; i < roughset.dataset.numClasses(); i++) {
      v = (VDouble)AccuracyForClasses.elementAt(i);
      if (!v.Invalid)
        Sum += v.Value * ((VDouble)MeanRoughInvolvementForClass.elementAt(i)).Value;
    }
    return Sum / TotalRoughInvolvement;
  }

  /**
   * Compute Generalized Classification Accuracy
   * weighing by MeanRoughAgreementForClass
   */
  /* Precisión generalizada de la clasificación
   */
  public double GenClassifAccuracy3() {
    double Sum = 0;
    VDouble v;
    for (int i = 0; i < roughset.dataset.numClasses(); i++) {
      v = (VDouble)AccuracyForClasses.elementAt(i);
      if (!v.Invalid)
        Sum += v.Value * ((VDouble)MeanRoughAgreementForClass.elementAt(i)).Value;
    }
    return Sum / TotalRoughAgreement;
  }

  /**
   * Compute Quality of Approximation for one Class
   */
  private VDouble Quality(int ClassIndex) {  // Calidad de la Aproximación para una clase
    VDouble v = new VDouble();
    if (roughset.InstancesOfClass(ClassIndex).cardinality() == 0)
      v.invalidate();   // There is no metric in this case
      else v.Value = (double)roughset.LowerApprox.at(ClassIndex).cardinality() / roughset.InstancesOfClass(ClassIndex).cardinality();
    return v;
  }

  /**
   * Compute Quality of Classification
   */
  public double ClassificationQuality() {  // Calidad de la clasificación
    double Cum = 0;
    for (int i = 0; i < roughset.dataset.numClasses(); i++) {
      Cum += roughset.LowerApprox.at(i).cardinality();
    }
    return Cum  / roughset.universe.cardinality();
  }

  /**
   * Compute Generalized Classification Quality
   * weighing by MeanRoughMembershipForClass
   */
  /* Calidad generalizada de la clasificación
   */
  public double GenClassifQuality1() {
    double Sum = 0;
    VDouble v;
    for (int i = 0; i < roughset.dataset.numClasses(); i++) {
      v = (VDouble)QualityForClasses.elementAt(i);
      if (!v.Invalid)
        Sum += v.Value * ((VDouble)MeanRoughMembershipForClass.elementAt(i)).Value;
    }
    return Sum / TotalRoughMembership;
  }

  /**
   * Compute Generalized Classification Quality
   * weighing by MeanRoughInvolvementForClass
   */
  /* Calidad generalizada de la clasificación
   */
  public double GenClassifQuality2() {
    double Sum = 0;
    VDouble v;
    for (int i = 0; i < roughset.dataset.numClasses(); i++) {
      v = (VDouble)QualityForClasses.elementAt(i);
      if (!v.Invalid)
        Sum += v.Value * ((VDouble)MeanRoughInvolvementForClass.elementAt(i)).Value;
    }
    return Sum / TotalRoughInvolvement;
  }
  /**
   * Compute Generalized Classification Quality
   * weighing by MeanRoughAgreementForClass
   */
  /* Calidad generalizada de la clasificación
   */
  public double GenClassifQuality3() {
    double Sum = 0;
    VDouble v;
    for (int i = 0; i < roughset.dataset.numClasses(); i++) {
      v = (VDouble)QualityForClasses.elementAt(i);
      if (!v.Invalid)
        Sum += v.Value * ((VDouble)MeanRoughAgreementForClass.elementAt(i)).Value;
    }
    return Sum / TotalRoughAgreement;
  }
  /**
   * Compute Generalized Approximation Ratio
   */
  /* Coeficiente de Aproximación General
   */
  public double GenApproxRatio(){
    double Inf = 0;
    double Sup = 0;
    for (int i = 0; i < roughset.dataset.numClasses(); i++) {
      Inf += roughset.LowerApprox.at(i).cardinality();
      Sup += roughset.UpperApprox.at(i).cardinality();
    }
  return Inf / Sup;
  }

  private VDouble Dependency(int ClassIndex) {
    VDouble v = new VDouble();
    if (roughset.universe.cardinality() == 0)
      v.invalidate();
    else
      v.Value = (double)roughset.LowerApprox.at(ClassIndex).cardinality() / roughset.universe.cardinality();
    return v;
  }

  /**
   * Compute an instance Rough Membership to AClassSet
   */
  /* ( Realmente no estoy usando este metodo
   *   en su lugar uso ComputeMembershipFunctions )
   */
  public double RoughMembership(int instance, IndexSet AClassSet) {
    IndexSet R = ClassOf(instance);   // Certainly |R| >= 1 because reflection
    IndexSet I = R.intersection(AClassSet);
    return (double)I.cardinality() / R.cardinality();
  }

  /**
   * Compute the Mean Rough Membership to AClassSet
   */
  /*   Media de la pertenencia aproximada por clases
   * ( Realmente no estoy usando este metodo
   *   en su lugar uso ComputeMembershipFunctions )
   */
  private VDouble MeanClassRoughMembership(IndexSet AClassSet) {
    double partialSum = 0;
    VDouble v = new VDouble();
    if (roughset.indiscernability.size() == 0)
      v.invalidate();   // There is no metric in this case
      else {
        for (int j = 0; j < roughset.indiscernability.size(); j++)
          partialSum += RoughMembership(j, AClassSet);
        v.Value = partialSum / roughset.indiscernability.size();
      }
    return v;
  }

  /**
   * Compute an instance Rough Involvement to AClassSet
   */
  /* ( Realmente no estoy usando este metodo
   *   en su lugar uso ComputeMembershipFunctions )
   */
  public double RoughInvolvement(int instance, IndexSet AClassSet) {  // Realmente no estoy usando este metodo
    IndexSet R = ClassOf(instance);
    IndexSet I = R.intersection(AClassSet);
    return (AClassSet.cardinality() == 0)? 0: (double)I.cardinality() / AClassSet.cardinality();
  }

  /**
   * Compute the Mean Rough Involvement to AClassSet
   */
  /*   Media del compromiso aproximado por clases
   * ( Realmente no estoy usando este metodo
   *   en su lugar uso ComputeMembershipFunctions )
   */
  private VDouble MeanClassRoughInvolvement(IndexSet AClassSet) {
    double partialSum = 0;
    VDouble v = new VDouble();
    if (roughset.indiscernability.size() == 0)
      v.invalidate();   // There is no metric in this case
      else {
        for (int j = 0; j < roughset.indiscernability.size(); j++)
          partialSum += RoughInvolvement(j, AClassSet);
        v.Value = partialSum / roughset.indiscernability.size();
      }
    return v;
  }

  /**
   * Return the Equivalence or Similarity Class of the instance by the relation given at indiscenibility
   */
  private IndexSet ClassOf(int instance) {
    return roughset.indiscernability.at(instance);
  }

  /**
   * Returns the metrics values as strings.
   *
   * @return the metrics values as strings.
   */
  public @Override String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append("RST Characteristics for " + roughset.dataset.relationName() + " dataset ");
    //sb.append("\nUniverse set:" + universe.toString());
    //sb.append("\nPositive Set:" + Positive.toString());
    //sb.append("\nNegative Set:" + Negative.toString());
    //sb.append("\nBoundary Set:" + Boundary.toString());
    //sb.append("\nEquivalence Relations: " + relation.toString());
    //sb.append("\nDiscernability: " + indiscernability.toString());
    //sb.append("\nLower Approximation: " + LowerApprox.toString());
    //sb.append("\nUpper Approximation: " + UpperApprox.toString());
    sb.append("with " + roughset.dataset.numAttributes()+ " attributes, " + roughset.dataset.numInstances() + " instances and " + roughset.dataset.numClasses() + " classes.");
    sb.append("");
    sb.append("\nMean Accuracy of Approximation: " + MeanAccuracy()); // Precisión media de la aproximación
    sb.append("\nQuality of Classification: " + ClassificationQuality());  // Calidad de la clasificación
    sb.append("\nGeneralized Classification Accuracy: " + GenClassifAccuracy1());  // Precisión generalizada de la clasificación
    sb.append("\nGeneralized Classification Quality: " + GenClassifQuality1());  // Calidad generalizada de la clasificación
    sb.append("\nGeneral Approximation Ratio: " + GenApproxRatio());  // Coeficiente de Aproximación General
    sb.append("\nWeighted Classification Accuracy: " + WeightedClassifAccuracy());  // Precisión ponderada de la clasificación
    sb.append("\nApproximation Accuracy for Classes: " + AccuracyForClasses.toString());  // Vector de Precisión de la Aproximación de Clases ( |R-| / |R+| )
    sb.append("\nClassification Quality for Classes: " + QualityForClasses.toString());   // Vector de Calidad de la Aproximación de Clases ( |R-| / |X| )
    sb.append("\nMean Rough Membership for Classes: " + MeanRoughMembershipForClass.toString());  // Media de la pertenencia aproximada por clases (pesos de la precisión generalizada)
    sb.append("\nMean Rough Involvement for Classes: " + MeanRoughInvolvementForClass.toString());  // Media del compromiso aproximado por clases (pesos de la calidad generalizada)
    //sb.append("\nVector de Grado de Dependencia de Clases ( |R-| / |U| ): " + fDependency.toString());
    return sb.toString();
  }
}
