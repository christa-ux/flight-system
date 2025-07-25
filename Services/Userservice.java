
package Services;

import Models.*;
import Utils.*;
public class Userservice {
	public User createCustomer (String name, String email, String phoneNumber, String username, String password, String student, String dateOfBirth, String nationality, String passportNumber) {
		int id=Database.userCount++;
		Customer c= new Customer (id, name, email, phoneNumber, username, password, nationality,dateOfBirth, passportNumber, student);
	
	Database.users.add(c);
	return c;
	}
	
	public User createAdmin (String name, String email, String phoneNumber, String username, String password, String adminBadge) {
		
		int id=Database.userCount++;
		Admin a=new Admin(id, name, email, phoneNumber, username, password, adminBadge);
		Database.users.add(a);
		return a;
		
		
	}
	public User createStaff(String name, String email, String phoneNumber, String username, String password, String staffBadge) {
		int id=Database.userCount++;
		Staff s=new Staff (id, name, email, phoneNumber, username, password, staffBadge);
		Database.users.add(s);
		return s;
	}
	
	public void listUsers() {
		for (User user: Database.users) {
			System.out.println(user);
		}
	}
	
	
	
}
