
import Models. *; 
import Services.*;
import Utils. *;

import java.util.*; 
public class Main  {
	public static void main (String[] args) {
		Scanner scan=new Scanner (System.in);
		Userservice userservice=new Userservice(); 
		Flightservice flightservice=new Flightservice();
		Bookingservice bookingservice=new Bookingservice();
		
		System.out.println("Select what action you want to perform:\n "
				+ "1: Create User\n"
				+ "2: Create Flight\n"
				+ "3: Create Booking\n"
				+ "4: List Users\n"
				+" 5: List flight\n"
				+ "6: List Booking\n"
				+" 7: Modify User \n"
				+ "8: Modify flight\n"
				+ "9: Modify Booking\n"
				+ "0: Cancel Booking\n"
				+ "10: Exit\n"
				);
		switch (scan.nextInt() ) {
		case 1:
			System.out.println("Enter Full Name: ");
			String name=scan.nextLine(); 
			System.out.println("Enter email address");
			String email=scan.nextLine(); 
			System.out.println("Enter phone number");
			String phoneNumber=scan.nextLine();
			System.out.println("Enter username");
			String username=scan.nextLine();
			System.out.println("Enter password");
			String password=scan.nextLine();
			System.out.println("Enter user role: customer / staff / admin");
			String role=scan.nextLine().toLowerCase();
		
			
			switch(role) {
			case "customer": 
				System.out.println("Student? Yes/No");
				String student=scan.nextLine();
				System.out.println("Enter date of birth in this format: YYYY/MM/DD");
				String dateOfBirth=scan.nextLine();
				System.out.println("Enter nationality");
				String nationality=scan.nextLine(); 
				System.out.println("Enter passport number");
				String passportNumber=scan.nextLine();
				userservice.createCustomer (name,email,phoneNumber,username,password,student,dateOfBirth,nationality,passportNumber);
				break;
			
			case "admin": 
				System.out.println ("Enter badge number");
				String adminBadge=scan.nextLine();
				userservice.createAdmin(name,email,phoneNumber,username,password,adminBadge);
				break;
			
			case "staff":
				System.out.println ("Enter badge number");
				String staffBadge=scan.nextLine();
				userservice.createStaff(name,email,phoneNumber,username,password,staffBadge);
				break;
			}
		
			
			
		case 2: 
			break;
		case 3: 
			break;
		case 4: 
			break;
		case 5:
			break;
		case 6: 
			break;
		case 7: 
			break;
		case 8: 
			break;
		case 9: 
			break;
		case 0: 
			break;
		case 10: 
			System.out.println ("Goodbye!");
			break;
		default: 
			System.out.println("Invalid Input");
		
		}
		
		
		
	}
}
		