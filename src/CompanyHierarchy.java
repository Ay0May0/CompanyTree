///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Main Class File:  CompanyHierarchyMain.java
// File:             CompanyHierarchy.java
// Semester:         CS367 Summer 2017
//
// Author:           Manish Dhungana mdhungana@wisc.edu
// CS Login:         dhungana
// Lecturer's Name:  Meena Syamkumar
// Lab Section:      N/A
//
//////////////////// PAIR PROGRAMMERS COMPLETE THIS SECTION ///////////////////
//
// Pair Partner:     Jack Cerhan
// Email:            jcerhan@wisc.edu
// CS Login:         cerhan
// Lecturer's Name:  Meena Syamkumar
// Lab Section:      N/A
//
//////////////////// STUDENTS WHO GET HELP FROM OTHER THAN THEIR PARTNER //////
//
// Online sources:   StackOverflow
//
//////////////////////////// 80 columns wide //////////////////////////////////

/** The CompanyHierarchy class represent a company tree
 */

import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Represents a hierarchy of company positions.
 *
 * Bugs: none known
 *
 * @author       Manish Dhungana & Jack Cerhan Copyright (2017)
 * @version      1.0
 * @see also     TreeNode, Employee, CompanyHierarchyMain
 */

public class CompanyHierarchy {
	private TreeNode root;
	//Used for recursive search of node
	private TreeNode nodeForSearching;
	//Used for recursive search of lists of nodes
	private List<Employee> nodeList;
	private int numOfPeople;

	/** Constructs a CompanyHierarchy tree  */
	public CompanyHierarchy() {
		root = null;
		nodeForSearching = root;
		numOfPeople = 0;
		nodeList = new ArrayList<Employee>();
	}

	/** Get the name of the CEO in this company tree
	 * 
	 * @returns null if root is null, name of CEO otherwise
	 */
	public String getCEO() {
		if (root == null) {
			return null;
		}
		else {
			return root.getEmployee().getName();
		}
	}
	/** Return the number of employees in this company tree
	 * 
	 * @returns 0 if root is null, the number of employees in company 
	 * otherwise
	 */
	public int getNumEmployees() {
		if (root == null) {
			return 0;
		}
		else {
			return numOfPeople;
		}
	}

	/** Return the number of levels in the tree : 0+ values
	 * 
	 * @return 0 if root is null
	 * @returns private getMaxLevels
	 */
	public int getMaxLevels() {
		if (root == null) {
			return 0;
		}
		else {  
			return getMaxLevels(root, 0);
		}
	}

	/** Return the number of levels in the tree : 0+ values
	 * 
	 * @param node individual node of hierarchy
	 * @param count counts number of loops run
	 */
	private int getMaxLevels(TreeNode node, int count) {
		// holds value of levels other than root
		int tempHold; 
		int currMax = 0;
		for (int i = 0; i < node.getWorkers().size(); i++) {
			tempHold = getMaxLevels(node.getWorkers().get(i), count);
			if (currMax < tempHold) {
				currMax = tempHold;
			}           
		}
		count++;
		// returns number of loops run + root level
		return count + currMax;
	}

	/** Return the employee details of given employee id and name; 
	 * return null 
	 * if no such employee was found
	 * 
	 *  @param id id of employee to retrieve
	 *  @param name name of employee to retrieve
	 *  @returns private getEmployee
	 */
	public Employee getEmployee(int id, String name) {
		checkIntParam(id);
		return getEmployee(id, name, 0);
	}

	/** Return the employee details of given employee id and name; return null 
	 * if no such employee was found
	 * 
	 *  @param id id of employee to retrieve
	 *  @param name name of employee to retrieve
	 *  @param count counts number of employees searched
	 *  @returns null if employee is not found; returns employee if otherwise
	 *  @throws CompanyHierarchyException if name does not match id
	 */
	private Employee getEmployee(int id, String name, int count) {
		// returns null when count exceeds list size
		if (count >= nodeList.size()) {
			return null;
		}
		if (nodeList.get(count).getId() == id) {
			if (nodeList.get(count).getName().equals(name)) {
				return nodeList.get(count);
			}
			// id matches but name does not
			else {
				throw new CompanyHierarchyException(""
						+ "Incorrect employee name for id!");
			}
		}
		count++;
		return getEmployee(id, name, count);
	}
	/** Adds employee as a child to the given supervisor node if supervisor 
	 * exists on tree; adds employee as root node if root node is null
	 * 
	 *  @param employee contains information of employee to be added
	 *  @param supervisorId id of supervisor employee is to be added to
	 *  @param supervisorName name of supervisor employee is to be added to
	 *  @returns true if employee is added to root or to valid supervisor; 
	 *  returns 
	 *   false if otherwise
	 */
	public boolean addEmployee(Employee employee, int supervisorId, 
			String supervisorName) {
		checkIntParam(supervisorId);
		if(supervisorName == null) {
			if(root == null) {
				// this will be root
				root = new TreeNode(employee, null);
				nodeForSearching = root;
				nodeList.add(employee);
				numOfPeople++;
				return true;
			}
			else {
				return false;
			}
		}

		try {
			// tests for invalid id/name combination for supervisor
			contains(employee.getId(), employee.toString(), "Id already used!");
			if (!contains(supervisorId, supervisorName,
					"Incorrect supervisor name for id!")) {
				System.out.println("Cannot add employee as "
						+ "supervisor was not found!");
				return false;
			}
		} catch (CompanyHierarchyException e){
			System.out.println(e.getMessage());
			return false;
		}

		// supervisor valid and employee valid
		TreeNode supervisorTreeNode = getEmployeeNode(supervisorId, 
				supervisorName);
		TreeNode empTreeNode = new TreeNode(employee, supervisorTreeNode);
		// updates added worker to supervisor
		supervisorTreeNode.addWorker(empTreeNode);
		// updates supervisor to added employee
		empTreeNode.updateSupervisor(supervisorTreeNode);
		nodeList.add(empTreeNode.getEmployee());
		numOfPeople++;
		return true;
	}

	/** Returns true/false based on whether the given employee exists on the 
	 * tree
	 * 
	 * @param id id of employee to be searched
	 * @param name name of employee to be searched
	 * @param exceptionMessage message sent if employee is not found
	 * @returns private contains method
	 */
	public boolean contains(int id, String name, String exceptionMessage) {
		checkIntParam(id);
		return contains(id, name, exceptionMessage, root);
	}

	/** Returns true/false based on whether the given employee exists on the 
	 * tree
	 * 
	 * @param id id of employee to be searched
	 * @param name name of employee to be searched
	 * @param exceptionMessage message sent if employee is not found
	 * @param node individual node of searched employee
	 * @returns true if employee is found; returns false if otherwise
	 */
	private boolean contains(int id, String name, String exceptionMessage, 
			TreeNode node) {
		checkIntParam(id);
		Employee emp = node.getEmployee();

		// contains id
		if (emp.getId() == id) {
			if(!emp.getName().equals(name)) {
				throw new CompanyHierarchyException(exceptionMessage);
			}
			return true;
		}

		// if not look through worker
		List<TreeNode> workers = node.getWorkers();

		boolean wcont = false;

		for(int i = 0; i < workers.size(); i++) {
			if(!wcont) {
				wcont = contains(id, name, exceptionMessage, workers.get(i));
			}
		}
		return wcont;
	}

	/** Removes the given employee(if found on the tree) and updates all the 
	 * workers to report to the given employee's supervisor; Returns true or 
	 * false accordingly
	 * 
	 * @param id id of employee to be removed
	 * @param name name of employee to be removed
	 * @returns false if employee does not exist; returns true if employee 
	 * is removed
	 * @throws CompanyHierarchyExcception if trying to remove CEO
	 */
	public boolean removeEmployee(int id, String name) {
		checkIntParam(id);
		TreeNode node = getEmployeeNode(id, name);

		if (node == null) {
			return false;
		}
		// unable to remove CEO
		if (root.getEmployee().getId() == id && 
				root.getEmployee().getName().equals(name)) {
			throw new CompanyHierarchyException(
					"Cannot remove CEO of the company!");
		}
		// retrieves searched employee
		if (node.getEmployee().getName().equals(name) && 
				node.getEmployee().getId() == id) {
			for (int i = 0; i < node.getWorkers().size(); i++) {
				node.getWorkers().get(i).updateSupervisor(node.getSupervisor());
				node.getSupervisor().addWorker(node.getWorkers().get(i));
			}
			// removes worker from supervisor
			node.getSupervisor().getWorkers().remove(node);
			//removes employee node from nodeList
			nodeList.remove(node.getEmployee());
			numOfPeople--;
			return true;
		}
		return false;
	}
	/** Replaces the given employee(if found on the tree) and if title of old
	 *  and new employee match; Returns true or false accordingly
	 *  @param id id of employee to be replaced
	 *  @param name name of employee to be replaced
	 *  @param newEmployee contains information of employee that will be 
	 *  replacing old employee
	 *  @returns false if employee to be replaced is not found; returns 
	 *  true if old employee is replaced
	 *  @throws CompanyHierarchyException if replacing employee already 
	 *  exists or replacement employee's title does not match existing 
	 *  employee's title
	 */
	public boolean replaceEmployee(int id, String name, Employee newEmployee) {
		checkIntParam(id);
		// holds node of employee being replaced
		TreeNode tempNode;
		tempNode = getEmployeeNode(id, name);
		if (tempNode == null) {
			return false;
		}
		if (this.contains(newEmployee.getId(), newEmployee.getName(), 
				"Id already used")) {
			throw new CompanyHierarchyException("Replacing employee " + 
					"already exists on the Company Tree");
		}
		if (!newEmployee.getTitle().equals(tempNode.getEmployee().getTitle())) {
			throw new CompanyHierarchyException("Replacement title does not match "
					+ "existing title!");
		}
		tempNode.updateEmployee(newEmployee);
		return true;
	}


	/** Search and return the list of employees with the provided title; if none 
	 *  found return null
	 *  @param title title to be searched
	 *  @returns private getEmployeeWithTitle
	 */
	public List<Employee> getEmployeeWithTitle(String title) {
		return getEmployeeWithTitle(title, root);
	}

	/** Search and return the list of employees with the provided title; if none 
	 *  found return null
	 *  @param title title to be searched
	 *  @param node individual node of employee with matching title
	 *  @return list of employees with titles that match searched title
	 */
	private List<Employee> getEmployeeWithTitle(String title, TreeNode node) {
		// list containing matching title employees
		List<Employee> temp = new ArrayList<Employee>();
		Employee emp = node.getEmployee();
		if (emp.getTitle().equals(title)) {
			temp.add(emp);
		}
		// iterates through emp's workers to find more matching titles
		Iterator<TreeNode> itr = node.getWorkers().iterator();
		while (itr.hasNext()) {
			List<Employee> matchingTitleList = getEmployeeWithTitle(title, 
					itr.next());
			temp.addAll(matchingTitleList);
		}
		return temp;
	}

	/** Search and return the list of employees with date of joining within the 
	 *  provided range; if none found return null
	 *  
	 *  @param startDate starting date of employees to be searched
	 *  @param endDate ending date of employees to be searched
	 *  @throws ParseException when date cannot be parsed
	 *  @returns private getEmployeeInJoiningDateRange
	 */
	public List<Employee> getEmployeeInJoiningDateRange(String startDate,
			String endDate) 
					throws ParseException {
		return getEmployeeInJoiningDateRange(startDate, endDate,
				nodeForSearching);
	}
	/** Search and return the list of employees with date of joining within the 
	 *  provided range; if none found return null
	 *  
	 *  @param startDate the lower date bound of the search
	 *  @param endDate the upper date bound of the search 
	 *  @param node of employee being checked
	 *  @throws ParseException if date format cannot be parsed
	 *  @return List of employees whose date of joining falls
	 *   within the search range
	 */
	private List<Employee> getEmployeeInJoiningDateRange(String startDate, 
			String endDate, TreeNode node) throws ParseException {
		// list containing employees within date range
		List<Employee> demp = new ArrayList<Employee>();
		// the TreeNode of employee being checked
		Employee emp = node.getEmployee();
		// sets month/day/year format
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		// Date the employee joined being checked
		Date joinDate = df.parse(emp.getDateOfJoining());
		// start date bound of search
		Date inputStartDate = df.parse(startDate);
		// end date bound of search
		Date inputEndDate = df.parse(endDate);

		if (joinDate.compareTo(inputStartDate) >= 0 && 
				joinDate.compareTo(inputEndDate) <= 0) {
			demp.add(emp);
		}
		// iterates through demp's workers to find more matching date ranges
		Iterator<TreeNode> itr = node.getWorkers().iterator();
		while (itr.hasNext()) {
			List<Employee> validDateList = getEmployeeInJoiningDateRange(startDate, 
					endDate, itr.next());
			demp.addAll(validDateList);
		}
		return demp;
	}

	/** Return the list of employees who are in the same level as the given 
	 *  employee sharing the same supervisor
	 *  
	 *  @param id the id num of worker whose coworkers are being found
	 *  @param name the name of the employee
	 *  @throws CompanyHierarchyException if the employees not in the tree
	 *  @return List of employee's coworkers
	 */
	public List<Employee> getCoWorkers(int id, String name) {
		checkIntParam(id);
		// TreeNode of employee whose coworkers are being found
		TreeNode empNode = getEmployeeNode(id, name);
		// List of employee's coworkers
		List<Employee> cowor = new ArrayList<Employee>();
		// List of employee's coworkers' TreeNodes
		List<TreeNode> coworkNodes;
		if (!contains(id, name, "Incorrect employee name for Id")) { 
			throw new CompanyHierarchyException("Employee not found"); 
		}
		if (empNode.getSupervisor() == null) {
			return cowor;
		}
		coworkNodes = empNode.getSupervisor().getWorkers();


		for (int i = 0; i < coworkNodes.size(); i++) {
			cowor.add(coworkNodes.get(i).getEmployee());
		}
		cowor.remove(empNode.getEmployee());
		return cowor;
	}

	/** Returns the supervisor list(till CEO) for a given employee
	 * 
	 * @param id the id num of employee whose supervisor chain
	 * is being found
	 * @param name the name of the employee
	 * @returns an empty list the employee is not in the company
	 * tree otherwise returns list of supervisors
	 * @throws CompanyHierarchyExcpetion if employee is CEO 
	 */
	public List<Employee> getSupervisorChain(int id, String name) {
		checkIntParam(id);
		// node of employee whose supervisor chain is being found
		TreeNode empNode = getEmployeeNode(id, name);
		// List of employees supervisors
		List<Employee> superList = new ArrayList<Employee>();
		if (!contains(id, name, "Incorrect employee name for id!")) {
			return superList;
		}
		if (empNode.getSupervisor() == null) {
			throw new CompanyHierarchyException("No Supervisor Chain found for "
					+ "that employee!");
		}
		return getSupervisorChain(empNode.getSupervisor(), superList);
	}

	/** Returns the supervisor of an employee and that supervisors supervisor
	 * all the way up to the CEO
	 * 
	 * @param node node of employee whose supervisor is being added to superList
	 * @param superList holds super visor chain
	 * @returns superList when the node is null otherwise return  
	 * getSupervisorChain for the current node's supervisor
	 */
	private List<Employee> getSupervisorChain(TreeNode node, 
			List<Employee> superList) {
		if (node == null) {
			return superList;
		}
		superList.add(node.getEmployee());
		return getSupervisorChain(node.getSupervisor(), superList);
	}

	/////////////////////////////////////////////
	// Private helper methods below

	/**Returns the matching TreeNode of a given employee object based 
	 * on the employees name and id
	 *
	 * @param parameter the int param of another method to check if neg
	 * @throws IllegalArguementException if parameter is neg
	 */
	private void checkIntParam(int parameter) {
		if (parameter < 0) {
			throw new IllegalArgumentException();
		}
	}

	/**Returns the matching TreeNode of a given employee object based 
	 * on the employees name and id
	 *
	 * @param id the id of employee whose node is being searched for
	 * @param private getEmployeeNodem name name of employee
	 * @returns private getEmployeeNode which returns the employees
	 * TreeNode or null
	 */
	private TreeNode getEmployeeNode(int id, String name) {
		// Node first sent to recursively search the tree for 
		// the employee's node
		TreeNode searchNode = nodeForSearching;
		return getEmployeeNode(id, name, nodeForSearching);
	}

	/**Returns the matching TreeNode of a given employee object based 
	 * on the employees name and id
	 *
	 * @param id the id of employee whose node is being searched for
	 * @param name name of employee 
	 * @param node tree node currently being checked
	 * @returns treeNode of employee, returns null when the current node
	 * being checked is null
	 */
	private TreeNode getEmployeeNode(int id, String name, TreeNode node) {
		// Holds node that stores possible employee nodes
		TreeNode tempNode;
		if (node == null) {
			return null;
		}
		if (node.getEmployee().getId() == id &&
				node.getEmployee().getName().equals(name)) {
			return node;
		}
		// Recursively search all of the the nodes workers list and 
		// all of their worker lists and so on
		for (int i = 0; i < node.getWorkers().size(); i++) {
			tempNode = getEmployeeNode(id, name, node.getWorkers().get(i));
			if (tempNode != null) { 
				return tempNode;
			}
		}
		return null;
	}
}
