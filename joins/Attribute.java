/**
* This class represents an attribute for a database
*
* Author: Lia Chin-Purcell
* Date: 11/9/19
*/

public class Attribute {
  private String attributeName;

  /**
  *   Makes a new Attribute with given name
  *
  *   @param attributeName the name of the attribute
  */
  public Attribute(String attributeName){
    this.attributeName = attributeName;
  }

  /**
  *   equals method
  *
  *   @return true if the two attributes are equal
  *   @param other the other attribute
  */
  public boolean equals(Attribute other){
    return attributeName.equals(other.attributeName);
  }
  
  /**
  *   to String method
  *
  *   @return the to string
  */
  @Override
  public String toString(){
    return attributeName;
  }
}
