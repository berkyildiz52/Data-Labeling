import java.util.ArrayList;
import java.util.Random;

public class User implements Measurable{

	private int userId;
	private String userPassword;
	private String userName;
	private String userType;
	private double consistencyCheckProbablity;
	private UserMetrics userMetrics;
	private Record userRecord;

	public User() {
		
	}

	public User(int userId, String userName, String userType) {
		
		this.userId = userId;
		this.userName = userName;
		this.userType = userType;
	
	}
	
	public User(int userId, String userName, String userPassword, String userType,double consistencyCheckProbablity) {
	
		this.userId = userId;
		this.userPassword = userPassword;
		this.userName = userName;
		this.userType = userType;
		this.consistencyCheckProbablity=consistencyCheckProbablity;
	}



	public int getId() {
		return userId;
	}

	public void setId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public double getConsistencyCheckProbablity() {
		return consistencyCheckProbablity;
	}

	public void setConsistencyCheckProbablity(double consistencyCheckProbablity) {
		this.consistencyCheckProbablity = consistencyCheckProbablity;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return userName+" with id "+this.userId+" ";
	}

	@Override
	public boolean equals(Object obj) {
		
		if(this.userId==((User)obj).getId()) {return true;}
		else {return false;}
	}

	@Override
	public void setMetrics(Metrics metrics) {
		
		this.userMetrics=(UserMetrics)metrics;
		
	}

	@Override
	public Metrics getMetrics() {
		// TODO Auto-generated method stub
		return this.userMetrics;
	}

	public Record getUserRecord() {
		return userRecord;
	}

	public void setUserRecord(Record userRecord) {
		this.userRecord = userRecord;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

}
