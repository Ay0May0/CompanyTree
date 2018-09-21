/** The TreeNode class represents a single node in the company tree. A node has 
 * the information of an employee (as a Employee) and also the information 
 * about its reporting supervisor node (as a TreeNode) and report nodes (as a 
 * List of TreeNodes).
 * 
 * DO NOT MODIFY THIS CLASS
 */
import java.util.*;

public class TreeNode {
	private Employee employee;
	private TreeNode supervisorNode;
	private List<TreeNode> worker;
	
	/** Constructs a TreeNode with employee and supervisorNode. */
	public TreeNode (Employee employee, TreeNode supervisorNode) {
		this.employee = employee;
		this.supervisorNode = supervisorNode;
		worker = new ArrayList<TreeNode>();
	}
	
	/** Return the employee in this node */
	public Employee getEmployee() {
		return employee;
	}
	
	/** Return the reporting supervisor for the employee in this node */
	public TreeNode getSupervisor()	{
		return supervisorNode;
	}
	
	/** Return the worker list for the employee in this node */
	public List<TreeNode> getWorkers() {
		return worker;
	}
	
	/** Add new worker to this employee */
	public void addWorker(TreeNode workerNode) {
		worker.add(workerNode);
	}
	
	/** Updates supervisor of an employee TreeNode */
	public void updateSupervisor(TreeNode supervisorNode) {
		this.supervisorNode = supervisorNode;
	}
	
	/** Updates employee of the current TreeNode */
	public void updateEmployee(Employee employee) {
		this.employee = employee;
	}
}