import java.util.ArrayList;

public class ConfigInfo {

	
	private ArrayList<User> humanUsers;
	private ArrayList<User> botUsers;
	protected ArrayList<DataSet> dataSets;
	
	public ConfigInfo() {
		this.humanUsers = new ArrayList<>();
		this.botUsers = new ArrayList<>();
		this.dataSets = new ArrayList<>();
	}
	public void start() {
		// TODO Auto-generated method stub

	}

	public ArrayList<User> getHumanUsers() {
		return humanUsers;
	}
	
	public ArrayList<User> getBotUsers() {
		return botUsers;
	}
	
	public ArrayList<DataSet> getDataSets() {
		return dataSets;
	}
	public void setHumanUsers(ArrayList<User> humanUsers) {
		this.humanUsers = humanUsers;
	}
	public void setBotUsers(ArrayList<User> botUsers) {
		this.botUsers = botUsers;
	}
	public void setDataSets(ArrayList<DataSet> dataSets) {
		this.dataSets = dataSets;
	}

}
