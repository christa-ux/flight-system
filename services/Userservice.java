package services;

import utils.Database;
import models.*;
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
		if (Database.users.isEmpty()) {
			System.out.println("No users found.");
			return;
		}

		System.out.println("List of Users:");
		for (User user : Database.users) {
			System.out.println("ID: " + user.getId() +
					" | Name: " + user.getName() +
					" | Email: " + user.getEmail() +
					" | Phone: " + user.getPhone() +
					" | Username: " + user.getUsername() +
					" | Password: " + user.getPassword() +
					" | Role: " + user.getClass().getSimpleName());

			if (user instanceof Customer) {
				Customer cust = (Customer) user;
				System.out.println("   Student: " + cust.getStudent() +
						" | DOB: " + cust.getDateOfBirth() +
						" | Nationality: " + cust.getNationality() +
						" | Passport: " + cust.getPassportNumber());
			} else if (user instanceof Admin) {
				System.out.println("   Admin Badge: " + ((Admin) user).getAdminBadge());
			} else if (user instanceof Staff) {
				System.out.println("   Staff Badge: " + ((Staff) user).getStaffBadge());
			}
		}
	}
	
	
	
}
