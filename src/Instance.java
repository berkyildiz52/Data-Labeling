import java.util.ArrayList;

public class Instance implements Measurable {

	private int instanceId;
	private String instanceText;
	private ArrayList<LabelAssignment> labelAssignments;
	private FinalLabel finalLabel;
	private boolean isLabeled;
	private InstanceMetrics instanceMetrics;
	private OutputProducer outputProducer;
	private int lineNumber;
	

	public Instance() {

	}

	public Instance(int instanceId, String instanceText) {
		this.instanceId = instanceId;
		this.instanceText = instanceText;
		this.labelAssignments = new ArrayList<>();
		outputProducer=OutputProducer.getInstance();
		

	}

	public int getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(int instanceId) {
		this.instanceId = instanceId;
	}

	public String getInstanceText() {
		return instanceText;
	}

	public void setInstanceText(String instanceText) {
		this.instanceText = instanceText;
	}

	public ArrayList<LabelAssignment> getLabelAssignments() {
		return labelAssignments;
	}

	public void addLabelAssignment(LabelAssignment labelAssignment,DataSet dataSet) {

		if (this.labelAssignments == null) {

			labelAssignments = new ArrayList<>();

		} 
			labelAssignment.endLabelAssignment(this);
			labelAssignments.add(labelAssignment);
			
			try {
				setMetrics(new InstanceMetrics(this,dataSet));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			this.finalLabel=instanceMetrics.getMostFrequentLabel();
			
			//print instance metrics
			outputProducer.printInstanceMetrics(instanceMetrics);

	}
	
	public void setLabelAssignments(ArrayList<LabelAssignment> labelAssignments) {
		
		this.labelAssignments=labelAssignments;
		
	}

	public FinalLabel getFinalLabel() {
		return finalLabel;
	}

	public boolean isLabeled() {
		return isLabeled;
	}

	public void setLabeled(boolean isLabeled) {
		this.isLabeled = isLabeled;
	}


	@Override
	public String toString() {
		return "Instance:  \"" + instanceText + "\" ";
	}

	@Override
	public boolean equals(Object obj) {

		if (this.getInstanceId() == ((Instance) obj).getInstanceId())
			return true;
		else
			return false;
	}

	@Override
	public void setMetrics(Metrics metrics) {

		this.instanceMetrics = (InstanceMetrics) metrics;
		

	}
	
	

	public int getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

	@Override
	public Metrics getMetrics() {
		// TODO Auto-generated method stub
		return this.instanceMetrics;
	}

}
