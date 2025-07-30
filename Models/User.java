package models;
import java.io.Serializable;
public abstract class User implements Serializable {
	private static final long serialVersionUID = 1L;

	protected int id; 
	protected String name,email,phoneNumber,username, role;
	private String password;
	
	
	public User (int id, String name, String email, String phoneNumber, String username, String password, String role) {
		this.id=id; 
		this.name=name;
		this.email=email;
		this.phoneNumber=phoneNumber; 
		this.username=username;
		this.password=password;
		this.role=role;
	}
	
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getEmail() {
		return email;
	}
	public String getPhone() {
		return phoneNumber;
	}
	public String getRole() {
		return role;
	}
	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}
	
	public void setId(int id) {
		this.id=id;
		
	}
	public void setName(String name) {
		this.name=name;
	}
	public void setEmail(String email) {
		this.email=email;
	}
	public void setPhone(String phoneNumber) {
		this.phoneNumber=phoneNumber;
	}
	public void setRole(String role) {
		this.role=role;
	}
	public void setUsername(String username) {
		this.username=username;
	}
	public void setPassword (String password) {
		this.password=password;
	}
	
	@Override
	public String toString() {
		return role + ": " + name + " (ID: " + id + ")"
	             + " | Email: " + email
	             + " | Phone: " + phoneNumber
	             + " | Username: " + username;
	}
	public abstract String getType();
}
