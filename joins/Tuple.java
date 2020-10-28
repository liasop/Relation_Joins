/**
* This class represents a tuple for a database.
*
* Author: Lia Chin-Purcell
* Date: 11/9/19
*/

import java.util.ArrayList;
public class Tuple implements Comparable<Tuple>{

  private ArrayList<String> values;
  private int sortedAttributeIndex;

  /**
  *   Makes a new Tuple with the values given as a string delimnated by |
  *
  *   @param stringVals the string with the values
  */
  public Tuple(String stringVals){
    values = new ArrayList<String>();
    //populate values
    String[] vals = stringVals.split("\\|");
    for(int i = 0; i < vals.length; i++){
      values.add(vals[i]);
    }
  }

  /**
  *   Makes a new Tuple with two tuples given as the arguments. Combines the tuples
  *   into a moster tuple
  *
  *   @param t1 one of the tuples
  *   @param t2 the other tuple
  */
  public Tuple(Tuple t1, Tuple t2){
    values = new ArrayList<String>();
    //add t1 tuples
    for(int i = 0; i < t1.values.size(); i++){
      values.add(t1.values.get(i));
    }
    //add t2 tuples
    for(int i = 0; i < t2.values.size(); i++){
      if(!values.contains(t2.get(i))){
        values.add(t2.values.get(i));
      }
    }
  }

  /**
  *   gets the value at the specified index in the tuple
  *
  *   @param index the index of the value
  *   @return the value at that index, as a String
  */
  public String get(int index){
    return values.get(index);
  }

  /**
  *   sets the sorted attribute index to the specified index for sorting the
  *   tuples
  *
  *   @param index the index to sort on
  */
  public void setsortedAttributeIndex(int index){
    sortedAttributeIndex = index;
  }

  /**
  *   Implementing the compareTo method for comparable class.
  *
  *   @param other the other tuple to compare
  */
  @Override
  public int compareTo(Tuple other){
    return values.get(sortedAttributeIndex).compareTo(other.values.get(sortedAttributeIndex));

  }

  /**
  *   to String method
  *
  *   @return the to string
  */
  @Override
  public String toString(){
    String str = "";
    for(int i = 0; i < values.size(); i++){
      str += values.get(i) + "|";
    }
    return str;
  }
}
