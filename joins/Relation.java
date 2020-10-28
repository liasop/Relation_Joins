/**
* This class represents a relation for a database
*
* Author: Lia Chin-Purcell
* Date: 11/9/19
*/

import java.util.*;
public class Relation{

  private String relationName; // name of the relation
  private ArrayList<Tuple> tuples; // the tuples in the relation
  private ArrayList<Attribute> attributes; // the attributes of the relation
  private Attribute sortedAttribute; //the attribute on which the relation is sorted.
  //if null, the relation is not yet sorted on an attribute


  /**
  *   Makes a new relation. Name is given.
  *
  *   @param relationName the name of the relation
  */
  public Relation(String relationName) {
    //sorted = false;
    //set the relation name
    this.relationName = relationName;
    //make a new ArrayList of the tuples
    tuples = new ArrayList<Tuple>();
    //A relation's list of attribute names should NOT be added as a tuple
    attributes = new ArrayList<Attribute>();
  }

  /**
  *   This method sets the attribute on which the relation is sorted.
  *
  *   @param attribute the sorted attribute
  */
  public void setSorted(String attribute){
    //set the sortedAttribute
    sortedAttribute = new Attribute(attribute);
  }

  /**
  *   This method returns true if the relation is sorted on the given attribute and false
  *   otherwise
  *
  *   @param attribute the sorted attribute
  *   @return true if the relation is sorted on the given attribute
  */
  public boolean isSortedOnAtt(Attribute att){
    if (sortedAttribute != null && sortedAttribute.equals(att)){
      return true;
    }
    return false;
  }

  /**
  *   This method adds a tuple to the relation
  *
  *   @param t the tupple to add
  */
  public void addTuple(Tuple t){
    tuples.add(t);
  }

  /**
  *   This method sets the attributes based on a string input
  *   delimated by |
  *
  *   @param attrStr the attribute string
  */
  public void setAttributes(String attrStr){
    //split the string
    String[] as = attrStr.split("\\|");
    //add the attributes
    for (int i = 0; i < as.length; i++)  {
      attributes.add(new Attribute(as[i]));
    }
  }

  /**
  *   This method sets the attributes based on two relations
  *
  *   @param r one of the relations
  *   @param s the other relation
  */
  public void setAttributes(Relation s, Relation r){
    //add s attributes
    for(int i = 0; i < s.attributes.size(); i++){
      attributes.add(s.attributes.get(i));
    }
    //add r attributes
    for(int i = 0; i < r.attributes.size(); i++){
      if(!attributes.contains(r.attributes.get(i))){
        attributes.add(r.attributes.get(i));
      }
    }
  }

  /**
  *   This method returns the name of the relation
  *
  *   @return the relation's name
  */
  public String getName(){
    return relationName;
  }

  /**
  *   This method sorts the tuples on the given attribute
  *
  *   @param attributeToSort the attribute on which to sort
  */
  public void sort(Attribute attributeToSort){
    //find the attribute index
    int sortedAttributeIndex= -1;
    for(sortedAttributeIndex = 0; sortedAttributeIndex < attributes.size(); sortedAttributeIndex++){
      if (attributes.get(sortedAttributeIndex).equals(attributeToSort)){
        break;
      }
    }

    //set the attributeIndex for all tuples
    for(int i = 0; i< tuples.size(); i++){
      tuples.get(i).setsortedAttributeIndex(sortedAttributeIndex);
    }
    //sort the tuples
    Collections.sort(tuples);
  }

  /**
  *   This method gets the tuples from the relation
  *
  *   @return the tuples from the relation
  */
  public ArrayList<Tuple> getTuples(){
    return tuples;
  }

  /**
  *   This method gets the attributes from the relation
  *
  *   @return the attributes from the relation
  */
  public ArrayList<Attribute> getAtts(){
    return attributes;
  }

  /**
  *   This method returns the toString
  *
  *   @return the toString
  */
  @Override
  public String toString(){
    String str = "";
    for(int i = 0; i< attributes.size(); i++){
      str += attributes.get(i) + "|";
    }
    str += "\n";
    for(int i = 0; i< tuples.size(); i++){
      str += tuples.get(i) + "\n";
    }
    return str;
  }
}
