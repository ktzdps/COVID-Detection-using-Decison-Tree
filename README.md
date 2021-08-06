# COVID-Detection-using-Decison-Tree

##ABSTRACT##

Decision tree learning is the most popular and powerful approach in knowledge discovery as well as in data mining. This is used for exploring large and complex bodies of data in order to discover useful patterns. Decision tree learning uses a decision tree as a predictive model which maps observations about an item to conclusions about the item's target value. Classification algorithm processes a training set containing a set of attributes. ID3 algorithm is the most widely used decision tree based algorithms.

##INTRODUCTION##

COVID-19 spread are the non-pharmaceutical measures like social distancing and personal hygiene. The great pandemic affecting billions of lives economically and socially has motivated the scientific community to come up with solutions based on computer-aided digital technologies for diagnosis, prevention, and estimation of COVID-19. Some of these efforts focus on statistical and Artificial Intelligence-based analysis of the available data concerning COVID-19. Artificial Intelligence (AI) and Machine Learning (ML) techniques have been prominently used to efficiently solve various computer science problems ranging from bio-informatics to image processing. 
Decision Tree is a Supervised learning technique that can be used for both classification and Regression problems, but mostly it is preferred for solving Classification problems. It is a tree-structured classifier, where internal nodes represent the features of a dataset, branches represent the decision rules and each leaf node represents the outcome. 



##OBJECTIVE##

The goal of using a Decision Tree is to create a training model that can use to predict the class or value of the target variable by learning simple decision rules inferred from prior data(training data). In Decision Trees, for predicting a class label for a record we start from the root of the tree.


##METHODOLOGY##

Decision Tree is a Supervised learning technique that can be used for both classification and Regression problems, but mostly it is preferred for solving Classification problems. It is a tree-structured classifier, where internal nodes represent the features of a dataset, branches represent the decision rules and each leaf node represents the outcome. In a Decision tree, there are two nodes, which are the Decision Node and Leaf Node. Decision nodes are used to make any decision and have multiple branches, whereas Leaf nodes are the output of those decisions and do not contain any further branches. The decisions or the test are performed on the basis of features of the given dataset. 


##ALGORITHM##

    1. With the use of Java Programming language the decision tree would be implemented.
    2. Check for the above base cases.
    3. For each attribute a, find the normalized information gain ratio from splitting on a. 
    4. Let a_best be the attribute with the highest normalized information gain. 
    5. Create a decision node that splits on a_best.
    6. Recur on the sublists obtained by splitting on a_best, and add those nodes as children of node. 
    7. Concluding the processes above we are finding whether the person is infected or not on the basis of the COVID symptoms.
