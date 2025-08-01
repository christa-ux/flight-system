package models; 
import java.io.Serializable;
public class Admin extends User implements Serializable{
	private static final long serialVersionUID = 1L;
	protected String adminBadge;
	
	public Admin (int id, String name, String email, String phoneNumber, String username, String password, String adminBadge) {
		super (id, name, email, phoneNumber, username, password, "admin");
		this.adminBadge=adminBadge;
	}
	@Override
	public String getType() {
		return "admin";
	}
	
	public String getAdminBadge() {
		return adminBadge;
	}
	public void setAdminBadge(String staffBadge) {
		this.adminBadge=staffBadge;
	}
	
	@Override
	public String toString() {
		return super.toString()+  " | admin badge: " +adminBadge;
	}
}
