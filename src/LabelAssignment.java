import java.util.ArrayList;
import java.util.Calendar;

public class LabelAssignment {

	private Instance instance;
	private ArrayList<Label> labelsOfInstance;
	private Calendar endDate;
	private User user;
	private Calendar startDate;
	private boolean isEnded;
	
	public LabelAssignment() {

		this.startDate=startDate;
		isEnded=false;
		
	}

	public LabelAssignment( ArrayList<Label> labelsOfInstance, User user) {

		
		this.labelsOfInstance = labelsOfInstance;
		this.startDate = Calendar.getInstance();
		this.user = user;
		isEnded=false;
	}
	
	public void endLabelAssignment(Instance instance) {
		
		if(!isEnded) {
			
			this.instance=instance;
			this.endDate=Calendar.getInstance();
			isEnded=true;
		}
	}

	public ArrayList<Label> getLabelsOfInstance() {
		return labelsOfInstance;
	}

	public void setLabelsOfInstance(ArrayList<Label> labelsOfInstance) {
		this.labelsOfInstance = labelsOfInstance;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Calendar getStartDate() {
		return startDate;
	}

	public void setStartDate(Calendar calendar) {
		this.startDate = calendar;
	}
	
	public Calendar getEndDate() throws Exception {
		
		if(endDate==null)throw new Exception();
		else return endDate;
	}

	public void setEndDate(Calendar calendar) {
		this.endDate = calendar;
	}

	public Instance getInstance() {
		return instance;
	}

	
	

}
