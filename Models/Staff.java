package Models; 

public class Staff extends User {
	protected String staffBadge;
	
	public Staff (int id, String name, String email, String phoneNumber, String username, String password, String role, String staffBadge) {
		super (id, name, email, phoneNumber, username, password, "staff");
		this.staffBadge=staffBadge;
	}
	
	public String getStaffBadge() {
		return staffBadge;
	}
	public void setStaffBadge(String staffBadge) {
		this.staffBadge=staffBadge;
	}
	
	@Override
	public String toString() {
		return super.toString()+  " | staff badge: " +staffBadge;
	}
}
