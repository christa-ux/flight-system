package Models;
public class Customer extends User {
	protected String nationality;
	protected String dateOfBirth;
	private String passportNumber;
	protected String student;
	
	public Customer (int id, String name, String email, String phoneNumber, String username, String password, String nationality, String dateOfBirth, String passportNumber, String student)  {
		super (id, name, email, phoneNumber, username, password, "customer" );
		this.nationality=nationality;
		this.dateOfBirth=dateOfBirth;
		this.passportNumber=passportNumber;
		this.student=student;
	}
	
	public String getNationality() {
		return nationality;
	}
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	public String getPassportNumber() {
		return passportNumber;
	}
	
	public String getStudent() {
		return student;
	}
	
	 public void setNationality (String nationality) {
		 this.nationality=nationality;
	 }
	 public void setDateOfBirth(String dateOfBirth) {
		 this.dateOfBirth=dateOfBirth;
	 }
	 public void setPassportNumber(String passportNumber) {
		 this.passportNumber=passportNumber;
	 }
	
	 public void setStudent(String student) {
		 this.student=student;
	 }
	 
	@Override
	public String toString() {
		return super.toString() +  " | DOB: " + dateOfBirth +
	               " | Nationality: " + nationality +
	               " | Passport: " + passportNumber +   " | Student?: "+ student;
	}
}
