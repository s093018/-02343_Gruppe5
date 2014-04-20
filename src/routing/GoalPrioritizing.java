package routing;

import java.util.ArrayList;

import imageProcessing.Point;

/**
 * @author Julian Villadsen - s123641
 */
public class GoalPrioritizing {
	/**
	 * goalPrioritizing() takes 3 Points (a starting point and 2 "Goals"),
	 * 
	 * compares the distance to them using the stated priority [0; 100]
	 * (the closer to 0, the higher priority firstGoal will have)
	 * 
	 * and returns the Point for the goal, to which there exists
	 * the shortest distance with the priority taken into account  
	 */
	public Point goalPrioritizing(Point robotPosition, Point firstGoal, Point secondGoal, int priority){
		//TODO BFS from robotPosition to firstGoal
		//TODO BFS from robotPosition to secondGoal
		
		// compare distance using priority and return preferable goal
		ArrayList<Integer> firstPath = null, secondPath = null; //TODO replace with returned values from BFS
		if(firstPath.size() < secondPath.size()*(priority/100))
			return firstGoal;
		return secondGoal;
	}

}
