import java.util.ArrayList;

public class Record {

	private int dataSetId;
	private int userId;
	
	private ArrayList<Instance> instancesLabeled;
	
	private ArrayList<Instance> instancesUnlabeled;
	
	
	public Record (ArrayList<Instance> instancesLabeled,ArrayList<Instance> instancesUnlabeled,int dataSetId,int userId) {
		
		this.instancesLabeled=instancesLabeled;
		this.instancesUnlabeled=instancesUnlabeled;
		this.dataSetId=dataSetId;
		this.userId=userId;
		
	}


	public ArrayList<Instance> getInstancesLabeled() {
		return instancesLabeled;
	}


	public void setInstancesLabeled(ArrayList<Instance> instancesLabeled) {
		this.instancesLabeled = instancesLabeled;
	}


	public ArrayList<Instance> getInstancesUnlabeled() {
		return instancesUnlabeled;
	}


	public void setInstancesUnlabeled(ArrayList<Instance> instancesUnlabeled) {
		this.instancesUnlabeled = instancesUnlabeled;
	}


	public int getDataSetId() {
		return dataSetId;
	}


	public void setDataSetId(int dataSetId) {
		this.dataSetId = dataSetId;
	}


	public int getUserId() {
		return userId;
	}


	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	

	
}
