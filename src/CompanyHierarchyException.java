///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Main Class File:  CompanyHierarchyMain.java
// File:             CompanyHierarchyException.java
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

public class CompanyHierarchyException extends RuntimeException {
	/**
	 * Default constructor
	 */
	public CompanyHierarchyException() {
		
	}
	
	/**
	 * Constructor with message passed
	 * @param message return message when exception occurs
	 */
	public CompanyHierarchyException(String message) {
		super(message);
	}
}