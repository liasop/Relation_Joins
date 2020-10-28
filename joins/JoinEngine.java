/**
* This class has the main method and reads in the database
*
* Author: Lia Chin-Purcell
* Date:  11/9/19
*/

import java.util.*;
import java.io.*;

public class JoinEngine{

  private static ArrayList<Relation> relations; //all the relations in our database

  public static void main(String[] args){
    // When the program starts, you should read every file in the data/ directory into a object representing a relation
    //System.out.println(date.getTime());
    relations = readFiles();

    //get the selection from the user
    Scanner sc = new Scanner(System.in);
    ArrayList<String> relationNames = new ArrayList<String>();

    System.out.println("Available relations:\n");
    for(int i = 0; i < relations.size(); i++){
      System.out.println(relations.get(i).getName() + " ");
      relationNames.add(relations.get(i).getName());
    }
    System.out.print(" Your selection (separated by space): ");

    String selection = sc.nextLine().toLowerCase();
    String[] relationsToJoin = selection.split(" ");

    if(!relationNames.contains(relationsToJoin[0]) || !relationNames.contains(relationsToJoin[1])){
      throw new IllegalArgumentException("one or more of those relations doesn't exist");
    }

    //get the relations from their names
    //place holders
    Relation r = new Relation("");
    Relation s = new Relation("");

    for(int i = 0; i < relations.size(); i++){
      if(relations.get(i).getName().equals(relationsToJoin[0])){
        r = relations.get(i);
      }
      else if (relations.get(i).getName().equals(relationsToJoin[1])){
        s = relations.get(i);
      }
    }

    //print the menu
    System.out.println("Choose a join algorithm:");
    System.out.println("1. Nested loop join");
    System.out.println("2. Hash Join");
    System.out.println("3. Sort-Merge Join");
    System.out.print(" Your selection: ");

    String intStr = sc.nextLine();
    int joinType = Integer.parseInt(intStr);

    Relation t;

    if(joinType == 1){
      long startMs = System.nanoTime();
      t = nestedLoopJoin(r, s);
      t.setAttributes(r,s);
      long endMs = System.nanoTime();
      System.out.println(t);
      System.out.println("Time= " + ((endMs - startMs)/ (double) 1000000 ) + " ms");
    }
    else if (joinType == 2){
      long startMs = System.nanoTime();
      t = hashJoin(r,s);
      t.setAttributes(r,s);
      long endMs = System.nanoTime();
      System.out.println(t);
      System.out.println("Time= " + ((endMs - startMs)/ (double) 1000000) + " ms");

    }
    else if(joinType == 3){
      long startMs = System.nanoTime();
      t = sortMergJoin(r,s);
      t.setAttributes(r,s);
      long endMs = System.nanoTime();
      System.out.println(t);
      System.out.println("Time= " +((endMs - startMs)/ (double) 1000000) + " ms");

    }
  }

  /**
  *   This method joins the relations given using the merg-join method
  *
  *   @return the joined relation with name "t"
  *   @param r one of the relations to be joined
  *   @param s the other relation to be joined
  */
  private static Relation sortMergJoin(Relation r, Relation s){
    //get the tuples and the attributes from the realtions
    ArrayList<Tuple> rTups = r.getTuples();
    ArrayList<Tuple> sTups = s.getTuples();
    ArrayList<Attribute> rAtts = r.getAtts();
    ArrayList<Attribute> sAtts = s.getAtts();
    //find the common attribute and index values
    Attribute common = new Attribute("");
    int commonIndexR = -1;
    int commonIndexS = -1;

    boolean found = false;
    for(commonIndexR = 0; commonIndexR < rAtts.size(); commonIndexR++){
      for(commonIndexS = 0; commonIndexS < sAtts.size(); commonIndexS++){
        if(rAtts.get(commonIndexR).equals(sAtts.get(commonIndexS))){
          common = rAtts.get(commonIndexR);
          found = true;
          break;
        }
      }
      if(found){
        break;
      }
    }
    if(!r.isSortedOnAtt(common)){
      r.sort(common);
    }
    if(!s.isSortedOnAtt(common)){
      s.sort(common);
    }

    Relation t = new Relation("t");
    int i = 0;
    int j = 0;
    while(i < rTups.size() && j < sTups.size()){
      // Match found, enter merge phase
      //if (R[i].c == S[j].c) {
      if (rTups.get(i).get(commonIndexR).equals(sTups.get(j).get(commonIndexS))){
        //while (R[i].c == S[j].c && i < R.size()) {
        while (i < rTups.size() && rTups.get(i).get(commonIndexR).equals(sTups.get(j).get(commonIndexS))) {
          int k = j;
          //while (R[i].c == S[k].c && k < S.size()) {
          while (k < sTups.size() && rTups.get(i).get(commonIndexR).equals(sTups.get(k).get(commonIndexS))){
            // create new tuple (R[i], S[k]) and add it to T
            Tuple tTup = new Tuple(rTups.get(i), sTups.get(k));
            t.addTuple(tTup);
            k++;
          }
          i++;
        }
      }
      else if (rTups.get(i).get(commonIndexR).compareTo(sTups.get(j).get(commonIndexS)) < 0){
        i++;
      }
      else{
        j++;
      }
    }
    return t;
  }

  /**
  *   This method joins the relations given using the hash-join method
  *
  *   @return the joined relation with name "t"
  *   @param r one of the relations to be joined
  *   @param s the other relation to be joined
  */
  private static Relation hashJoin(Relation r, Relation s){
    Relation t = new Relation("t");

    // Phase I: Hash every tuple of R by the value
    // of the common attribute
    HashMap<String, Tuple> map = new HashMap<String,Tuple>();

    ArrayList<Tuple> rTups = r.getTuples();
    ArrayList<Tuple> sTups = s.getTuples();

    ArrayList<Attribute> rAtts = r.getAtts();
    ArrayList<Attribute> sAtts = s.getAtts();

    //find the common attribute
    Attribute common = new Attribute("");
    int commonIndexR = -1;
    int commonIndexS = -1;

    boolean found = false;
    for(commonIndexR = 0; commonIndexR < rAtts.size(); commonIndexR++){
      for(commonIndexS = 0; commonIndexS < sAtts.size(); commonIndexS++){
        if(rAtts.get(commonIndexR).equals(sAtts.get(commonIndexS))){
          common = rAtts.get(commonIndexR);
          found = true;
          break;
        }
      }
      if(found){
        break;
      }
    }

    for(int i = 0; i < rTups.size(); i++){
      if(!map.containsKey(rTups.get(i).get(commonIndexR))){
        map.put(rTups.get(i).get(commonIndexR), rTups.get(i));
      }
      else{
        throw new IllegalArgumentException("Hash-join cannot be performed\nThe common attribute in R must be unique");
      }
    }

    // Phase II: Join up with S
    for(int j = 0; j < sTups.size(); j++){
      if(map.containsKey(sTups.get(j).get(commonIndexS))){
        Tuple tup = map.get(sTups.get(j).get(commonIndexS));
        Tuple newTup = new Tuple (tup, sTups.get(j));
        t.addTuple(newTup);
      }
    }
    return t;
  }

  /**
  *   This method joins the relations given using the nested loop-join method
  *
  *   @return the joined relation with name "t"
  *   @param r one of the relations to be joined
  *   @param s the other relation to be joined
  */
  private static Relation nestedLoopJoin(Relation r, Relation s){
    Relation t = new Relation("t");

    ArrayList<Tuple> rTups = r.getTuples();
    ArrayList<Tuple> sTups = s.getTuples();
    ArrayList<Attribute> rAtts = r.getAtts();
    ArrayList<Attribute> sAtts = s.getAtts();

    //find the common attribute
    Attribute common = new Attribute("");
    int commonIndexR = -1;
    int commonIndexS = -1;

    boolean found = false;
    for(commonIndexR = 0; commonIndexR < rAtts.size(); commonIndexR++){
      for(commonIndexS = 0; commonIndexS < sAtts.size(); commonIndexS++){
        if(rAtts.get(commonIndexR).equals(sAtts.get(commonIndexS))){
          common = rAtts.get(commonIndexR);
          found = true;
          break;
        }
      }
      if(found){
        break;
      }
    }

    for(int i = 0; i < rTups.size(); i++){
      for(int j = 0; j < sTups.size(); j++){
        if(rTups.get(i).get(commonIndexR).equals(sTups.get(j).get(commonIndexS))){
          Tuple tTup = new Tuple(rTups.get(i), sTups.get(j));
          t.addTuple(tTup);
        }
      }
    }
    return t;
  }

  /**
  *   readFiles reads the file and turns them into relations
  *
  *   @return the relations in tbe dataset
  */
  private static ArrayList<Relation> readFiles(){
    ArrayList<Relation> relations = new ArrayList<Relation>();

    try{
      File folder = new File ("data/");
      File[] files = folder.listFiles();

      for(int i = 0; i < files.length; i++){
        Scanner sc = new Scanner(files[i]);

        //You must use the file name, minus the file extension (.txt), as the name of the relation.
        String relationName = files[i].getName().substring(0, files[i].getName().length() - 4);

        //make a new relation
        Relation r = new Relation(relationName);
        //The comment on the first line has all the attribute names.
        String lineOfAttributes = sc.nextLine();
        String attributeString = lineOfAttributes.substring(1);
        //set the attributes
        r.setAttributes(attributeString);

        while(sc.hasNext()){
          String line = sc.nextLine();
          //check the second #
          if(line.charAt(0) == '#'){
            // If it's specified, then it contains the name of the attribute by which all tuples in the file is sorted
            //yes, they are sorted!
            String sortedAttribute = line.substring(1);
            r.setSorted(sortedAttribute);
          }
          else{
            //not the # line
            //make the tuple and add it
            Tuple t = new Tuple(line);
            r.addTuple(t);
          }
        }
        relations.add(r);
      }
    }
    catch (IOException e) {
      System.out.println("An error occurred.");
    }
    return relations;
  }
}
