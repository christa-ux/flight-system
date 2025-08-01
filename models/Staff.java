package models; 
import java.io.Serializable;
public class Staff extends User implements Serializable{
	private static final long serialVersionUID = 1L;
	protected String staffBadge;
	
	public Staff (int id, String name, String email, String phoneNumber, String username, String password, String staffBadge) {
		super (id, name, email, phoneNumber, username, password, "staff");
		this.staffBadge=staffBadge;
	}
	@Override
	public String getType() {
		return "staff";
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
