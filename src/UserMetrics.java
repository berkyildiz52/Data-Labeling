import java.util.ArrayList;
import java.util.HashMap;

public class UserMetrics extends Metrics {

	private User user;
	private Integer numberOfDatasets;
	private HashMap<DataSet, Double> dset_CompletenessPercentages;
	private String dat_Comp_Per_Dist;
	private Integer numberOfLabeledInstances;
	private Integer numberOfUniqueLabeledInstances;
	private Double consistencyPercentage;
	private ArrayList<DataSet> dataSetsAssigned;
	private ArrayList<LabelAssignment> allAssignments;
	private ArrayList<Instance> uniqueLabeledInstances;
	private ArrayList<Instance> recurrentInstances;
	private HashMap<Instance, Integer> instanceLabelAssignmentDistribution;
	private Double averageTimeSpent;
	private Double stdDeviationOfTime;
	
	private boolean isConsistent;
	public UserMetrics(User user,LabelingSystem labelingSystem) {
		

		this.user=user;
		
		setNumberOfDatasets(labelingSystem);
		find_DSet_CompletenessPercentage();
		listAllInstances();
		find_Instance_Assignment_Distr();
		find_RecurrentInstances();
		find_ConsistencyPercentage();
		findAverageTimeSpent();
		findStdDevTime();
		
	}
	
	
	private void setNumberOfDatasets(LabelingSystem labelingSystem) {
		
		dataSetsAssigned=new ArrayList<>();
		
	
			
			
			if(labelingSystem.getLabelingMission().getUsersAssigned().contains(user)) {
				
				for(DataSet dataSet:labelingSystem.getLabelingMission().getDataSets()) {
				
					if(!dataSetsAssigned.contains(dataSet)) {
						
						dataSetsAssigned.add(dataSet);
						
					}
						
				}
				
			}
			
		
		
		this.numberOfDatasets=dataSetsAssigned.size();
		
		
	}
	

	private void find_DSet_CompletenessPercentage() {
		
		dset_CompletenessPercentages=new HashMap<>();
		dat_Comp_Per_Dist="";
		
		try {
			for(DataSet dataSet:dataSetsAssigned) {
				
				double percentage=((DataSetMetrics)dataSet.getMetrics()).getCompletenessPercentageDistribution().get(user.getId());
				dset_CompletenessPercentages.put(dataSet, percentage);
				dat_Comp_Per_Dist+=("("+dataSet+":"+percentage+"%)");
				
			}
		} catch (Exception e) {
			
		}
		
		dat_Comp_Per_Dist="("+dat_Comp_Per_Dist+")";
		
	}
	
	
	
	public String getDat_Comp_Per_Dist() {
		return dat_Comp_Per_Dist;
	}


	private void findAverageTimeSpent() {
		
		double totalTime=0;
		
		for(LabelAssignment labelAssignment:allAssignments) {
			
			
			try {
				totalTime+=((labelAssignment.getEndDate().getTimeInMillis()-labelAssignment.getStartDate().getTimeInMillis())/1000.0);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
		averageTimeSpent=((double)totalTime)/allAssignments.size();
		
	}
	
	private void findStdDevTime() {
		
		double varience=0;
		
		for(LabelAssignment labelAssignment:allAssignments) {
		
				
				
				try {	
				
					long timeSpent=(labelAssignment.getEndDate().getTimeInMillis()-labelAssignment.getStartDate().getTimeInMillis())/1000;
					varience+=Math.pow((averageTimeSpent-timeSpent), 2);
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
					
		}
			
		varience/=(allAssignments.size()-1);
		
		this.stdDeviationOfTime=Math.sqrt(varience);
		
		}
		
		
		
	
	
	private void listAllInstances() {
		
		allAssignments=new ArrayList();
		uniqueLabeledInstances=new ArrayList<>();
		
		for(DataSet dataSet:dataSetsAssigned) {
			
			
			for(Instance instance:dataSet.getLabeledInstances()) {
				
				
				for(LabelAssignment labelAssignment:instance.getLabelAssignments()) {
					
					
					if(labelAssignment.getUser().equals(user)) {
						
						
						allAssignments.add(labelAssignment);
						
						if(!uniqueLabeledInstances.contains(instance)) {
							
							uniqueLabeledInstances.add(instance);
						}
						
					}
	
				}

			}
			
			
			
		}
		
		this.numberOfLabeledInstances=allAssignments.size();
		this.numberOfUniqueLabeledInstances=uniqueLabeledInstances.size();
	}
	
	
	
	
	//finds  'instances that is labeled by same user twice or more' and adds them to arraylist
	private void find_RecurrentInstances() {
		
		recurrentInstances=new ArrayList<>();
		
		instanceLabelAssignmentDistribution.entrySet().stream().forEach(e->{
			
			
			if(e.getValue()>1) {
				
				recurrentInstances.add(e.getKey());
				
			}
		});
		
	}
	
	
	private void find_ConsistencyPercentage() {
		
		int numOfConsistentInstances=0;
		
		
		if(recurrentInstances==null||recurrentInstances.isEmpty()) {
			
			consistencyPercentage=-1.0;
			
		}
		
		else {
			for(Instance instance:recurrentInstances) {
				
				
				try {
					numOfConsistentInstances+=isConsistent_AboutInstance(instance);
				} catch (Exception e) {
					
				}
				
				
			}
			
			this.consistencyPercentage=100*(((double)numOfConsistentInstances)/recurrentInstances.size());
		
		}
	}
	
	private void find_Instance_Assignment_Distr() {
		
		instanceLabelAssignmentDistribution=new HashMap<>();
		
		for(LabelAssignment labelAssignment:allAssignments) {
			
			if(!instanceLabelAssignmentDistribution.containsKey(labelAssignment.getInstance())) {
				
				instanceLabelAssignmentDistribution.put(labelAssignment.getInstance(),1);
				
			}
			
			else {
				
				
				instanceLabelAssignmentDistribution.put(labelAssignment.getInstance(), instanceLabelAssignmentDistribution.get(labelAssignment.getInstance())+1);
				
			}
			
			
		}
		
	}
	
	
	private Integer isConsistent_AboutInstance(Instance instance) {
		
		Integer num=0;
		
		isConsistent=true;
		
		((InstanceMetrics)instance.getMetrics()).getUserLabelDistribution().get(user.getId()).entrySet().stream().forEach(e->{
			
			
			if(e.getValue()<2) {isConsistent=false;}
			
		
		});
		
		
		if(isConsistent) {return 1;}else return 0;
		
	}
	
	
	
	public Double getAverageTimeSpent() {
		return averageTimeSpent;
	}


	public User getUser() {
		return user;
	}


	public Integer getNumberOfDatasets() {
		return numberOfDatasets;
	}


	public Integer getNumberOfLabeledInstances() {
		return numberOfLabeledInstances;
	}


	public Integer getNumberOfUniqueLabeledInstances() {
		return numberOfUniqueLabeledInstances;
	}


	public Double getConsistencyPercentage() {
		return consistencyPercentage;
	}





	public Double getStdDeviationOfTime() {
		return stdDeviationOfTime;
	}


	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
}
