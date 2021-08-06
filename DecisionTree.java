import java.io.BufferedReader;

import java.io.File;

import java.io.FileReader;

import java.io.IOException;

import java.util.*;

import com.sun.jdi.Value;

class Node

{

    /**

           * The characteristic value of reaching this node

     */

    public String lastFeatureValue;

    /**

           * Feature name or answer of this node

     */

    public String featureName;

    /**

           * Classification node of this node

     */

    public List<Node> childrenNodeList = new ArrayList<Node>();

	@Override

	public String toString() {

		return "Node [lastFeatureValue=" + lastFeatureValue + ", featureName=" + featureName + ", childrenNodeList="

				+ childrenNodeList + "]";

	}

}

public class DecisionTree {

	  // Feature list

    public static List<String> featureList = new ArrayList<String>();

         // characteristic value list

    public static List<List<String>> featureValueTableList = new ArrayList<List<String>>();

         // Get global data

    public static Map<Integer, List<String>> tableMap = new HashMap<Integer, List<String>>();

    

    /**

     * Initialization data 

* 

* @param file

*/

public static void readOriginalData(File file)

{

  int index = 0;

  try

  {

      FileReader fr = new FileReader(file);

      BufferedReader br = new BufferedReader(fr);

      String line;

      while ((line = br.readLine()) != null)

      {

                           // get the feature name

          if (line.startsWith("@feature"))

          {

              line = br.readLine();

              String[] row = line.split(",");

              for (String s : row)

              {

                  featureList.add(s.trim());

              }

          }

          else if (line.startsWith("@data"))

          {

              while ((line = br.readLine()) != null)

              {

                  if (line.equals(""))

                  {

                      continue;

                  }

                  String[] row = line.split(",");

                  if (row.length != featureList.size())

                  {

                                                   throw new Exception ("List data and feature numbers are inconsistent");

                  }

                  List<String> tempList = new ArrayList<String>();

                  for (String s : row)

                  {

                      if (s.trim().equals(""))

                      {

                                                           throw new Exception ("List data cannot be empty");

                      }

                      tempList.add(s.trim());

                  }

                  tableMap.put(index++, tempList);

              }



                                   // Traverse tableMap to get a list of attribute values

              Map<Integer, Set<String>> valueSetMap = new HashMap<Integer, Set<String>>();

              for (int i = 0; i < featureList.size(); i++)

              {

                  valueSetMap.put(i, new HashSet<String>());

              }

              for (Map.Entry<Integer, List<String>> entry : tableMap.entrySet())

              {

                  List<String> dataList = entry.getValue();

                  for (int i = 0; i < dataList.size(); i++)

                  {

                      valueSetMap.get(i).add(dataList.get(i));

                  }

              }

              for (Map.Entry<Integer, Set<String>> entry : valueSetMap.entrySet())

              {

                  List<String> valueList = new ArrayList<String>();

                  for (String s : entry.getValue())

                  {

                      valueList.add(s);

                  }

                  featureValueTableList.add(valueList);

              }

          }

          else

          {

              continue;

          }

      }

      br.close();

  }

  catch (IOException e1)

  {

      e1.printStackTrace();

  }

  catch (Exception e)

  {

      e.printStackTrace();

  }

}



/**

 * Calculate entropy

* 

* @param dataSetList

* @return

*/

public static double calculateEntropy(List<Integer> dataSetList)

{

if (dataSetList == null || dataSetList.size() <= 0)

{

  return 0;

}

       // got the answer

int resultIndex = tableMap.get(dataSetList.get(0)).size() - 1;

Map<String, Integer> valueMap = new HashMap<String, Integer>();

for (Integer id : dataSetList)

{

  String value = tableMap.get(id).get(resultIndex);

  Integer num = valueMap.get(value);

  if (num == null || num == 0)

  {

      num = 0;

  }

  valueMap.put(value, num + 1);
  System.out.println("resultindex: "+resultIndex);
}

double entropy = 0;

for (Map.Entry<String, Integer> entry : valueMap.entrySet())

{

  double prob = entry.getValue() * 1.0 / dataSetList.size();

  entropy -= prob * Math.log10(prob) / Math.log10(2);

}

return entropy;

}



/**

 * Divide a data set

* 

* @param dataSetList

 * The data set to be divided

* @param featureIndex

 * The first few features (feature subscript, starting from 0)

* @param value

 * Get a data set of a certain characteristic value

* @return

*/

public static List<Integer> splitDataSet(List<Integer> dataSetList, int featureIndex, String value)

{

List<Integer> resultList = new ArrayList<Integer>();

for (Integer id : dataSetList)

{

  if (tableMap.get(id).get(featureIndex).equals(value))

  {

      resultList.add(id);

  }

}

return resultList;

}



/**

 * Choose one of the best features (maximum information gain) among the specified features for dividing the data set

* 

* @param dataSetList

 * @return returns the subscript of the best feature

*/

public static int chooseBestFeatureToSplit(List<Integer> dataSetList, List<Integer> featureIndexList)

{

double baseEntropy = calculateEntropy(dataSetList);

double bestInformationGain = 0;

int bestFeature = -1;



       // Loop through all features

for (int temp = 0; temp < featureIndexList.size() - 1; temp++)

{

  int i = featureIndexList.get(temp);



               // Get the feature set

  List<String> featureValueList = new ArrayList<String>();

  for (Integer id : dataSetList)

  {

      String value = tableMap.get(id).get(i);

      featureValueList.add(value);

  }

  Set<String> featureValueSet = new HashSet<String>();

  featureValueSet.addAll(featureValueList);



               // Get the entropy under this category

  double newEntropy = 0;

  for (String featureValue : featureValueSet)

  {

      List<Integer> subDataSetList = splitDataSet(dataSetList, i, featureValue);

      double probability = subDataSetList.size() * 1.0 / dataSetList.size();

      newEntropy += probability * calculateEntropy(subDataSetList);

  }

               // get information gain

  double informationGain = baseEntropy - newEntropy;

               // Get the feature index with the largest information gain

  if (informationGain > bestInformationGain)

  {

      bestInformationGain = informationGain;

      bestFeature = temp;

  }

}

return bestFeature;

}



/**

 * Most votes get the value with the most occurrences

* 

* @param dataSetList

* @return

*/

public static String majorityVote(List<Integer> dataSetList)

{

       // got the answer 

int resultIndex = tableMap.get(dataSetList.get(0)).size() - 1;

Map<String, Integer> valueMap = new HashMap<String, Integer>();

for (Integer id : dataSetList)

{

  String value = tableMap.get(id).get(resultIndex);

  Integer num = valueMap.get(value);

  if (num == null || num == 0)

  {

      num = 0;

  }

  valueMap.put(value, num + 1);

}



int maxNum = 0;

String value = "";



for (Map.Entry<String, Integer> entry : valueMap.entrySet())

{

  if (entry.getValue() > maxNum)

  {

      maxNum = entry.getValue();

      value = entry.getKey();

  }

}



return value;

}



/**

 * Create decision tree

* 

* @param dataSetList

 *            data set 

* @param featureIndexList

 * List of available features

* @param lastFeatureValue

 * Reach the previous characteristic value of this node

* @return

*/

public static Node createDecisionTree(List<Integer> dataSetList, List<Integer> featureIndexList, String lastFeatureValue)

{

       // If there is only one value, then directly return the leaf node

       int valueIndex = featureIndexList.get (featureIndexList.size ()-1); // Label index

       // select the first value

       String firstValue = tableMap.get (dataSetList.get (0)). get (valueIndex); // Label value

int firstValueNum = 0;

for (Integer id : dataSetList)

{

  if (firstValue.equals(tableMap.get(id).get(valueIndex)))

  {

      firstValueNum++;

  }

}

       if (firstValueNum == dataSetList.size ()) // All data belong to the same category

{

  Node node = new Node();

  node.lastFeatureValue = lastFeatureValue;

  node.featureName = firstValue;

  node.childrenNodeList = null;

  return node;

}



       // The feature values are not completely the same after traversing all the features, and the result of the majority vote is returned

       if (featureIndexList.size () == 1) // The label is left

{

  Node node = new Node();

  node.lastFeatureValue = lastFeatureValue;

  node.featureName = majorityVote(dataSetList);

  node.childrenNodeList = null;

  return node;

}



       // Get the feature with the largest information gain

int bestFeatureIndex = chooseBestFeatureToSplit(dataSetList, featureIndexList);

       // Get the global index of this feature

//System.out.println("FeatureIndexList: "+featureIndexList);

//System.out.println("BestFeature: "+bestFeatureIndex);

if(bestFeatureIndex == -1)

{

	bestFeatureIndex = 0;

}

int realFeatureIndex = featureIndexList.get(bestFeatureIndex);

String bestFeatureName = featureList.get(realFeatureIndex);



       // Construct a decision tree

Node node = new Node();

node.lastFeatureValue = lastFeatureValue;

node.featureName = bestFeatureName;



       // Get the set of all eigenvalues

List<String> featureValueList = featureValueTableList.get(realFeatureIndex);



       // delete this feature

featureIndexList.remove(bestFeatureIndex);



       // Traverse all values of the feature, divide the data set, and then recursively get the child nodes

for (String fv : featureValueList)

{

               // Get the sub-data set

  List<Integer> subDataSetList = splitDataSet(dataSetList, realFeatureIndex, fv);

               // If the sub-data set is empty, use majority voting to give an answer.

  if (subDataSetList == null || subDataSetList.size() <= 0)

  {

      Node childNode = new Node();

      childNode.lastFeatureValue = fv;

      childNode.featureName = majorityVote(dataSetList);

      childNode.childrenNodeList = null;

      node.childrenNodeList.add(childNode);

      break;

  }

               // Add child nodes

  Node childNode = createDecisionTree(subDataSetList, featureIndexList, fv);

  node.childrenNodeList.add(childNode);

}



return node;

}



/**

 * Enter the test data to get the prediction result of the decision tree

       * @param decisionTree decision tree

       * @param featureList feature list

       * @param testDataList test data

 * @return

 */

public static String getDTAnswer(Node decisionTree, List<String> featureList, List<String> testDataList)

{

    if (featureList.size() - 1 != testDataList.size())

    {
    	System.out.println ("The input data is incomplete");
        return "ERROR";

    }



    while (decisionTree != null)

    {

                     // If the child node is empty, return the answer to this node.

        if (decisionTree.childrenNodeList == null || decisionTree.childrenNodeList.size() <= 0)

        {

            return decisionTree.featureName;

        }

                     // The child node is not empty, then determine the feature value to find the child node

        for (int i = 0; i < featureList.size() - 1; i++)

        {

                             // Find the current feature index

            if (featureList.get(i).equals(decisionTree.featureName))

            {

                                     // Get test data characteristic value

                String featureValue = testDataList.get(i);

                                     // Find the node containing this feature value in the child node

                Node childNode = null;

                for (Node cn : decisionTree.childrenNodeList)

                {

                    if (cn.lastFeatureValue.equals(featureValue))

                    {

                        childNode = cn;

                        break;

                    }

                }

                                     // If this node is not found, it means that there is no feature value to this node in the training set

                if (childNode == null)

                {

                                             System.out.println ("No data found for this characteristic value");

                    return "ERROR";

                }



                decisionTree = childNode;

                break;

            }

        }

    }

    return "ERROR";

}



	public static void main(String[] args) {

		//System.out.println("Compiled Successfully!");

		readOriginalData(new File("C:/Users/harsh/Desktop/minor test/sample1.txt"));

		//System.out.println("After Reading file: "+featureList+"\n"+featureValueTableList+"\n"+tableMap);

		List<Integer> dataSetList = new ArrayList<Integer>(tableMap.keySet());

		double entropy = calculateEntropy(dataSetList);
		

		//System.out.println("Entropy: "+entropy);

		List<Integer> featureIndexList = new ArrayList<Integer>();

		for(int i=0; i<featureList.size(); i++)

		{

			featureIndexList.add(i);

		}

		int bestFeatureIndex = chooseBestFeatureToSplit(dataSetList,featureIndexList);

		List<List<Integer>> dataSetSplit = new ArrayList<List<Integer>>();

		for(String i : featureValueTableList.get(bestFeatureIndex))

		{

			dataSetSplit.add(new ArrayList<Integer>(splitDataSet(dataSetList,bestFeatureIndex,i)));

		}

		//System.out.println("SplitDataSet: "+dataSetSplit);

		String majorityVotedValue = majorityVote(dataSetList);

		//System.out.println("Majority Vote: "+majorityVotedValue);

		Node nodeDt = createDecisionTree(dataSetList,featureIndexList,majorityVotedValue);

		//System.out.println("DT Node: "+nodeDt);
		//System.out.println("dd: "+ featureList.size());
		
		List<String> testlist=new ArrayList<String>();
        System.out.println("Enter the input for : "+featureList);
        for (int i = 0; i < 14; i++)
        {
        	Scanner input=new Scanner(System.in);

            String a = input.nextLine();
            testlist.add(a);
	
        }
        //System.out.println("d1: "+ testlist.size());
        System.out.println(testlist);          
        
		String getDT = getDTAnswer(nodeDt, featureList, testlist);
		
		System.out.println("DT answer: "+ getDT);
		
		//If(getDT == Yes)

	} 

}