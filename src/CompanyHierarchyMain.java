///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Title:            Program 2
// Files:            CompanyHierarchyMain.java, TreeNode.java Employee.java
//					 CompanyHierarchy.java CompanyHierarchyException.java
// Semester:         CS367 Summer 2017
//
// Author:           Manish Dhungana
// Email:            mdhungana@wisc.edu
// CS Login:         dhungana
// Lecturer's Name:  Meena Syamkumar
//
//////////////////// PAIR PROGRAMMERS COMPLETE THIS SECTION ////////////////////
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

/** CompanyHierarchyMain implementation
 */

import java.util.*;
import java.io.*;
import java.text.ParseException;

public class CompanyHierarchyMain {

	private static CompanyHierarchy checkInputAndReturnTree (String [] args) {
		// *** Step 1: Check whether exactly one command-line argument is given *** 
		if (args.length > 1) {
			System.out.print("Usage: java -cp . CompanyHierarchyMain FileName");
		}
		// *** Step 2: Check whether the input file exists and is readable ***
		File inputFile = new File(args[0]);
		if (!inputFile.exists() || !inputFile.canRead()){
			System.out.print("Error: Cannot access input file");
		}
		/* Step 3: Load the data from the input file and use it to 
		 *  construct a company tree. Note: people are to be added to the 
		 *  company tree in the order in which they appear in the text file. 
		 */

		// CompanyHierarchy object used to store entire company tree
		CompanyHierarchy compHier = new CompanyHierarchy(); 
		// Used to store the individual commands from the input file
		// after the input line is split
		String[] inputArray;

		try {
			// Use to read text from input file
			Scanner inFileRead = new Scanner(inputFile);
			inputArray = inFileRead.nextLine().split(",");
			for (int i = 0; i < inputArray.length; i++) {
				inputArray[i] = inputArray[i].trim();
			}
			// Use to store employee object of CEO
			Employee CEO = new Employee(inputArray[0], 
					Integer.parseInt(inputArray[1]),inputArray[2], 
					inputArray[3]);
			compHier.addEmployee(CEO, 0, null);

			// Creates rest of employees and adds them to the company tree
			while (inFileRead.hasNextLine()) {
				inputArray = inFileRead.nextLine().split(",");
				for (int i = 0; i < inputArray.length; i++) {
					inputArray[i] = inputArray[i].trim();
				}
				// Holds the employee created from the info held on the line of
				// the file that was just read
				Employee newEmp = new Employee(inputArray[0], 
						Integer.parseInt(inputArray[1]),
						inputArray[2], inputArray[3]);
				compHier.addEmployee(newEmp, 
						Integer.parseInt(inputArray[5].trim()), 
						inputArray[4]);
			}
			inFileRead.close();
		} catch (FileNotFoundException e) {

		} 
		return compHier;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CompanyHierarchy tree = checkInputAndReturnTree(args);

		/* Step 4: Prompt the user to enter command options and 
		 *  process them until the user types x for exit. 
		 */
		boolean stop = false;

		Scanner stdin = new Scanner(System.in);
		while (!stop) {
			System.out.println("\nEnter Command: ");
			String input = stdin.nextLine();
			String remainder = null;
			//if the user enters more than one letter, the extra command 
			//is set as remainder
			if (input.length() > 0) {
				char option = input.charAt(0);
				if (input.length() > 1) {
					remainder = input.substring(1).trim();
				}
				String[] inputArray;
				//switch statement for the letter/command entered from the user
				switch (option) {

				/** a newid,newname,DOJ,title,supervisorId,supervisorName		
				 * Add a new employee with given details to the company tree. 
				 * Display "Employee added" if the addition was successful. 
				 * If there is no such supervisor in the company tree, 
				 * display "Cannot add employee as supervisor was not found!"
				 */
				case 'a': {
					// Used to hold employee object created based on details 
					//from user input
					Employee newEmp;
					try {
						inputArray = remainder.split(",");
						newEmp = new Employee(inputArray[1], 
								Integer.parseInt(inputArray[0]), 
								(inputArray[2]), inputArray[3]);
						if(tree.addEmployee(newEmp, 
								Integer.parseInt(inputArray[4]),
								inputArray[5])) {
							System.out.println("Employee added");
						}
					} catch (CompanyHierarchyException e) {
						System.out.println(e.getMessage());
					}
					break;
				}

				/** s id name		Print the name(s) of all the 
				 * supervisors in the supervisor chain of the given 
				 * employee. Print the names on separate lines. 
				 * If no such employee is found, display 
				 * "Employee not found!"*/	
				case 's':{
					inputArray  = remainder.split(",");
					for (int i = 0; i < inputArray.length; i++) {
						inputArray[i] = inputArray[i].trim();
					}
					// Holds list of emplooyee's supervisors
					List<Employee> superVisors;
					try {
						superVisors = tree.getSupervisorChain(
								Integer.parseInt(inputArray[0]), 
								inputArray[1]);
						if (superVisors.isEmpty()) {
							System.out.println("Employee not found!");
						}
						else {
							for (int i = 0; i < superVisors.size(); i++) {
								System.out.println(superVisors.get(i).getName());
							}

						}
					} catch (CompanyHierarchyException e) {
						System.out.println(e.getMessage());
					}
					break;
				}

				/** d		Display information about the company tree 
				 * by doing the following:
				 * Display on a line: "# of employees in company tree: integer"
				 * This is the number of employees in this company tree.
				 *
				 * Display on a line: "max levels in company tree: integer"
				 * This is the maximum number of levels in the company tree.
				 *
				 * Display on a line: "CEO: name"
				 * This is the CEO in the company tree*/
				case 'd': {
					System.out.println("# of employees in company tree: " 
							+ tree.getNumEmployees());
					System.out.println( "max levels in company tree: " 
							+ tree.getMaxLevels());
					System.out.println("CEO: " + 
							tree.getEmployeeWithTitle("CEO").get(0).getName());
					break;
				}

				/** e title		Print the name(s) of the employee(s) 
				 *  that has the given title. Print the names on 
				 *  separate lines. If no such employee is found, 
				 *  display "Employee not found!" */
				case 'e': {
					// Holds list of employees with title being searched
					List<Employee> searchEmployee;
					searchEmployee = tree.getEmployeeWithTitle(remainder);
					if (searchEmployee.isEmpty()) {
						System.out.println("Employee not found!");
					}
					for (int i = 0; i < searchEmployee.size(); i++) {
						System.out.println(searchEmployee.get(i).getName());
					}
					break;
				}

				/** r id name		Remove the employee with given id 
				 * and name from the company tree and re-assign the 
				 * worker's to the removed employee's supervisor. 
				 * Display "Employee removed" after the removal. 
				 * If there is no such employee in the company tree, 
				 * display "Employee not found!" */
				case 'r': {
					inputArray = remainder.split(",");
					try {
						if (tree.removeEmployee(Integer.parseInt(inputArray[0]), 
								inputArray[1])) {
							System.out.println("Employee removed");
						}
					} catch (CompanyHierarchyException e) {
						System.out.println(e.getMessage());
					}
					break;
				}

				/** c id name		Print the name(s) of the 
				 * co-employees(sharing the same supervisor) of the 
				 * employee with given id and name. Print the names on 
				 * separate lines. If no such employee is found, 
				 * display "Employee not found!". If the employee has 
				 * no co-employee under the same supervisor, display 
				 * "The employee has no co-workers." */
				case 'c': {
					// Holds list of employee being searched's coworkers
					List<Employee> coWorkers;
					inputArray = remainder.split(",");
					try {
						coWorkers = tree.getCoWorkers(Integer.parseInt(inputArray[0]), 
								inputArray[1]);
						if (coWorkers == null) {
							System.out.println("Employee not found!");
						}
						else if (coWorkers.isEmpty()) {
							System.out.println("The employee has no co-workers.");
						}
						else {
							for (int i = 0; i < coWorkers.size(); i++) {
								System.out.println(coWorkers.get(i).getName());
							}
						}
					} catch (CompanyHierarchyException e) {
						System.out.println(e.getMessage());
					}
					break;
				}

				/** u id name newid newname DOJ title		Replace the 
				 * employee with give id and name from the company tree 
				 * with the provided employee details. 
				 * Display "Employee replaced" after the removal. If 
				 * there is no such employee in the company tree, 
				 * display "Employee not found!" */
				case 'u': {
					inputArray = remainder.split(",");
					try {
						if(tree.replaceEmployee(Integer.parseInt(inputArray[0]), 
								inputArray[1], new Employee(inputArray[3], 
										Integer.parseInt(inputArray[2]),
										inputArray[4], inputArray[5]))) {
							System.out.println("Employee replaced");
						}
						else {
							System.out.println("Employee not found!");
						}
					} catch (CompanyHierarchyException e) {
						System.out.println(e.getMessage());
					}

					break;
				}

				/** j startDate endDate		Print the name(s) of the 
				 * employee(s) whose date of joining are between 
				 * startDate and endDate(you may assume that startDate 
				 * is equal to or before end date). Print the names on 
				 * separate lines. If no such employee is found, 
				 * display "Employee not found!" */
				case 'j': {
					// Holds list of employees whose start dates are in the range
					List<Employee> empInDate;
					inputArray = remainder.split(",");
					try {
						empInDate = tree.getEmployeeInJoiningDateRange(
								inputArray[0], inputArray[1]);
						if (empInDate.isEmpty()) {
							System.out.println("Employee not found!");
						}
						for (int i = 0; i < empInDate.size(); i++) {
							System.out.println(empInDate.get(i).getName());
						}
					} catch (ParseException e) {
						System.out.println("Date parsing failed!");
					}
					break;
				}

				//***exits program***
				case 'x':{
					stop = true;
					System.out.println("exit");
					break;
				}
				default:
					break;
				}

			}
		}
	}
}
