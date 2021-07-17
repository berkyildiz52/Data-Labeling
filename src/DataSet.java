import java.util.ArrayList;

public class DataSet implements Measurable{

	private int dataSetId;
	private String dataSetName;
	private ArrayList<Instance> instancesUnlabeled;
	private ArrayList<Instance> instancesLabeled;
	private int maxNumberOfLabels;
	private ArrayList<Label> labelsCanBeUsed;
	private DataSetMetrics dataSetMetrics;
	

	public DataSet() {
	}

	public DataSet(int dataSetId, String dataSetName, int maxNumberOfLabels, ArrayList<Instance> instances, ArrayList<Label> labels) {

		this.dataSetId = dataSetId;
		this.dataSetName = dataSetName;
		this.maxNumberOfLabels = maxNumberOfLabels;
		this.instancesUnlabeled = instances;
		this.labelsCanBeUsed = labels;
		
	}

	public int getDataSetId() {
		return dataSetId;
	}

	public void setDataSetId(int datSetid) {
		this.dataSetId = datSetid;
	}

	public String getDataSetName() {
		return dataSetName;
	}

	public void setDatasetName(String dataSetName) {
		this.dataSetName = dataSetName;
	}

	public ArrayList<Instance> getUnlabeledInstances() {
		return instancesUnlabeled;
	}

	public void setUnlabeledInstances(ArrayList<Instance> instances) {
		this.instancesUnlabeled = instances;
	}
	
	public ArrayList<Instance> getLabeledInstances() {
		
		if(instancesLabeled==null) {instancesLabeled=new ArrayList<>();}
		return instancesLabeled;
	}

	public void setLabeledInstances(ArrayList<Instance> instances) {
		this.instancesLabeled = instances;
	}

	public int getMaxNumberOfLabels() {
		return maxNumberOfLabels;
	}

	public void setMaxNumberOfLabels(int maxNumberOfLabels) {
		this.maxNumberOfLabels = maxNumberOfLabels;
	}

	public ArrayList<Label> getLabelsCanBeUsed() {
		return labelsCanBeUsed;
	}

	public void setLabelsCanBeUsed(ArrayList<Label> labelsCanBeUsed) {
		this.labelsCanBeUsed = labelsCanBeUsed;
	}
	

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "\""+this.dataSetName+"\"with id "+dataSetId+" ";
	}
	
	@Override
	public boolean equals(Object obj) {

		if(this.getDataSetId()==((DataSet)obj).getDataSetId())return true;
		else return false;
	}

	@Override
	public void setMetrics(Metrics metrics) {

	
			this.dataSetMetrics=(DataSetMetrics)metrics;
	
	}
	

	@Override
	public Metrics getMetrics() {
		// TODO Auto-generated method stub
		return this.dataSetMetrics;
	}
	
	
	

}
