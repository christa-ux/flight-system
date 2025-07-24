package Models; 

public class Admin extends User {
	protected String adminBadge;
	
	public Admin (int id, String name, String email, String phoneNumber, String username, String password, String role, String adminBadge) {
		super (id, name, email, phoneNumber, username, password, "admin");
		this.adminBadge=adminBadge;
	}
	
	public String getStaffBadge() {
		return adminBadge;
	}
	public void setStaffBadge(String staffBadge) {
		this.adminBadge=staffBadge;
	}
	
	@Override
	public String toString() {
		return super.toString()+  " | admin badge: " +adminBadge;
	}
}
