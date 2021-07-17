import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class DataSetMetrics extends Metrics {

	private DataSet dataSet;
	private Double completenessPercentage;
	private HashMap<Integer, Double> finalLab_Distr;
	private String fin_Lab_Distr_Str;
	private Integer numberOfUsersAssigned;
	private HashMap<Integer,Double> comp_Perc_Distr;
	private String comp_Per_Dist;
	private HashMap<Integer,ArrayList<Instance>> listOfInstancesPerLabel;
	private String cons_Per_Dist;
	private String inst_List_Num_Per_Lab;
	
	public DataSetMetrics(ArrayList<User> usersAssigned,DataSet dataSet) {
		
		this.dataSet=dataSet;
		;
		this.completenessPercentage=new Double(100*(((double)dataSet.getLabeledInstances().size())/(((double)dataSet.getLabeledInstances().size())+((double)dataSet.getUnlabeledInstances().size()))));
		this.numberOfUsersAssigned=usersAssigned.size();
		
		try {
			findFinalLabelDistribution(dataSet.getLabeledInstances());
		} catch (Exception e) {
			
		}
		findCompPercDistribution(usersAssigned);
		findListInstancesPerLabel();
		findInst_List_Num_Per_Lab();
		
	
	}
	
	
	private void findListInstancesPerLabel() {
		
		this.listOfInstancesPerLabel=new HashMap<>();
		
		for(Instance instance:dataSet.getLabeledInstances()) {
			
			for(LabelAssignment labelAssignment:instance.getLabelAssignments()) {
				
				for(Label label:labelAssignment.getLabelsOfInstance()) {
					
					
					if(!listOfInstancesPerLabel.containsKey(label.getLabelId())) {
						
						ArrayList<Instance> arrayList=new ArrayList<>();
						arrayList.add(instance);
						listOfInstancesPerLabel.put(label.getLabelId(), arrayList);
						
					}
					
					else {
						
										
						if (!listOfInstancesPerLabel.get(label.getLabelId()).contains(instance)) {
							listOfInstancesPerLabel.get(label.getLabelId()).add(instance);
						}
					}				
				}
			}	
		}
	}
	
	private void findFinalLabelDistribution(ArrayList<Instance> instancesLabeled) throws Exception{
		
		
			
		finalLab_Distr=new HashMap<>();
			
		fin_Lab_Distr_Str="";
		
		for(int i=0;i<instancesLabeled.size();i++) {
			
			Instance instance=instancesLabeled.get(i);
			
			if(!(finalLab_Distr.containsKey(instance.getFinalLabel().getLabelId()))) {
				
				finalLab_Distr.put(instance.getFinalLabel().getLabelId(), 100*(1.0/instancesLabeled.size()));
				
			}
			else {
				
				finalLab_Distr.put(instance.getFinalLabel().getLabelId(), finalLab_Distr.get(instance.getFinalLabel().getLabelId())+(100.0/instancesLabeled.size()));
				
			}	
		}
		
		finalLab_Distr.entrySet().stream().forEach(e->{
			
			
			
			for(Label label:dataSet.getLabelsCanBeUsed()) {
				
				if(label.getLabelId()==e.getKey().intValue()) {
				
					fin_Lab_Distr_Str+=(e.getValue()+"% "+dataSet.getLabelsCanBeUsed().get(dataSet.getLabelsCanBeUsed().indexOf(label))+", ");
					break;
				}
				
			}
			
			
			
			
		});
		
		fin_Lab_Distr_Str="("+fin_Lab_Distr_Str+")";
	}
	
	private void findCompPercDistribution(ArrayList<User> usersAssigned) {

		int size=dataSet.getLabeledInstances().size()+dataSet.getUnlabeledInstances().size();
		
		this.comp_Perc_Distr=new HashMap<>();
		this.comp_Per_Dist="";
		
		for(Instance instance:dataSet.getLabeledInstances()) {
			
			ArrayList<Integer> tempList=new ArrayList<>();
			
			for(LabelAssignment labelAssignment:instance.getLabelAssignments()) {
				
				
				if(!comp_Perc_Distr.containsKey(labelAssignment.getUser().getId())) {
					
					comp_Perc_Distr.put(labelAssignment.getUser().getId(), 100* (1.0/size));
					tempList.add(labelAssignment.getUser().getId());
					
				}
				
				else {
									
					if (!tempList.contains(labelAssignment.getUser().getId())) {
						comp_Perc_Distr.put(labelAssignment.getUser().getId(),
								comp_Perc_Distr.get(labelAssignment.getUser().getId()) + 100 * (1.0 / size));
						tempList.add(labelAssignment.getUser().getId());
					}
					
				}
				
				
			}	
		}
		
		comp_Perc_Distr.entrySet().stream().forEach(e->{
			
			User myUser=null;
			
			for(User user:usersAssigned) {
				
				
				if(user.getId()==e.getKey()) {myUser=user; break;}
				
			}
			
			comp_Per_Dist+=("("+myUser+":"+e.getValue()+"%) ");
			
			
		});
		
		comp_Per_Dist="("+comp_Per_Dist+")";
	}
	
	public void findConsistentPercentageDistribution(ArrayList<User> usersAssigned) {
		
		cons_Per_Dist="";
		
		for(User user:usersAssigned) {
			
			cons_Per_Dist+="("+user+":"+((UserMetrics)user.getMetrics()).getConsistencyPercentage()+"%)";
			
			
			
		}
		
		cons_Per_Dist="("+cons_Per_Dist+")";
		
		
	}
	
	

	public DataSet getDataSet() {
		return dataSet;
	}

	public HashMap<Integer, Double> getCompletenessPercentageDistribution() {
		return comp_Perc_Distr;
	}

	public Double getCompletenessPercentage() {
		return completenessPercentage;
	}

	public HashMap<Integer, Double> getFinalLabelDistribution() {
		return finalLab_Distr;
	}

	public Integer getNumberOfUsersAssigned() {
		return numberOfUsersAssigned;
	}

	public HashMap<Integer, ArrayList<Instance>> getListOfInstancesPerLabel() {
		return listOfInstancesPerLabel;
	}
	
	

	public String getFin_Lab_Dist() {
		return fin_Lab_Distr_Str;
	}
	
	

	public String getComp_Per_Dist() {
		return comp_Per_Dist;
	}
	
	

	public String getInst_List_Num_Per_Lab() {
		return inst_List_Num_Per_Lab;
	}



	private void findInst_List_Num_Per_Lab() {

		inst_List_Num_Per_Lab="";
		listOfInstancesPerLabel.entrySet().forEach(e->{
			
			Label myLabel=null;
			for(Label label:dataSet.getLabelsCanBeUsed()) {
				
				if(label.getLabelId()==e.getKey()) {myLabel=label;break;}
				
			}
			
			
			inst_List_Num_Per_Lab+=(myLabel+":[");
			
			for(Instance instance:e.getValue()) {
				
				inst_List_Num_Per_Lab+=instance.getLineNumber()+" ,";
				
				
			}
			
			inst_List_Num_Per_Lab=inst_List_Num_Per_Lab.substring(0,inst_List_Num_Per_Lab.length()-1);
			
			inst_List_Num_Per_Lab+=("], ");
		});
	}



	public String getCons_Per_Dist() {
		return cons_Per_Dist;
	}

	public String toString(String attributeName) {

	
		
		return "";
	}
	
	
	
}
