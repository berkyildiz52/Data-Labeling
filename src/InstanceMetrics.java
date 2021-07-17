import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class InstanceMetrics extends Metrics  {

	private DataSet dataSet;
	private Instance instance;
	private Integer num_Labels;
	private Integer num_UniqueLabels;
	private Integer num_UniqueUsers;
	private FinalLabel mostFrequentLabel;
	private double entropy;
	private HashMap<Integer, HashMap<Integer, Integer>> user_Lab_Distr;
	private HashMap<Integer, Integer> user_Assignment_Distr;
	private HashMap<Integer, Integer> labelDistribution;
	private HashMap<Integer, Double> labelPercentageDistribution;
	private HashMap<Integer,Integer> labelUserDistribution;
	private String lab_Per_Dist;

	public InstanceMetrics(Instance instance,DataSet dataSet) {
		
		this.dataSet=dataSet;
		
		this.instance=instance;
		

		find_User_LabelDistr();
		find_LabelDistr();
		find_User_Assignment_Distr();
		find_Num_Label_Used();
		this.num_UniqueLabels = labelDistribution.size();
		this.num_UniqueUsers = user_Assignment_Distr.size();
		find_Lab_User_Distr();
		find_MostFreqLabel();
		find_Lab_Perc_Distr();
		findEntropy();

	}
	
	
	private void find_MostFreqLabel(){
		
		
		 mostFrequentLabel=new FinalLabel(new Label(-1,""));
		
		labelDistribution.entrySet().stream().forEach(e->{
			
			if(e.getValue()>=mostFrequentLabel.getNumber()) {
				
				
				
				
				mostFrequentLabel=new FinalLabel(getLabelWithId(dataSet, e.getKey()));
				mostFrequentLabel.setNumber(e.getValue());
				mostFrequentLabel.setPercentage(100*((double)(e.getValue()))/num_Labels);
				
			}
			
			
			
		});
		
		
		
	}
	
	private void find_Num_Label_Used() {
		
		num_Labels=0;
		labelDistribution.entrySet().forEach(e->{
			
			
			num_Labels+=(e.getValue());
			
			
		});
		
		
	}
	
	private void findEntropy() {
		
		entropy=new Double(0);
		double probablity=0;
		
		//if number of unique labels equal to 0 or 1 base will be 2 otherwise base will be equal to number of unique labels
		int base=((num_UniqueLabels==0)||(num_UniqueLabels==1)) ? 2:num_UniqueLabels; 
		
		if(labelPercentageDistribution.size()==1) {
			
			Map.Entry<Integer, Double> e=labelPercentageDistribution.entrySet().iterator().next();
			probablity=((double)e.getValue())/100;
			entropy=entropy+(-1*probablity*(Math.log(probablity)/Math.log(num_UniqueLabels)));
					
			
		}
		
		else {
		
			for(Map.Entry<Integer, Double> e:labelPercentageDistribution.entrySet()) {
				
				probablity=e.getValue()/100;
				entropy=entropy+(-1*probablity*(Math.log(probablity)/Math.log(num_UniqueLabels)));
				
			}
		
		}
		
		
	}
	
	private void find_Lab_User_Distr() {
		
		labelUserDistribution=new HashMap<>();
		ArrayList users=new ArrayList<>();
		
		for(LabelAssignment labelAssignment:instance.getLabelAssignments()) {
			
			for(Label label:labelAssignment.getLabelsOfInstance()) {
				
				
				if(!labelUserDistribution.containsKey(label.getLabelId())) {
					
					
					labelUserDistribution.put(label.getLabelId(), 1);
					users.add(labelAssignment.getUser());
					
				}
				
				else {
					
					if(!users.contains(labelAssignment.getUser())) {
					
						labelUserDistribution.put(label.getLabelId(), labelUserDistribution.get(label.getLabelId())+1);
					}
				}
				
				
			}
		}
		
		
		
	}
	
	private void find_Lab_Perc_Distr() {
		
		
		labelPercentageDistribution=new HashMap<>();
		lab_Per_Dist="";
		
		labelDistribution.entrySet().stream().forEach(e->{
			
			double percentage=100*(((double)e.getValue())/num_Labels);
			
				
				
				labelPercentageDistribution.put(e.getKey(),percentage );
				lab_Per_Dist+=("("+getLabelWithId(dataSet, e.getKey())+":"+percentage+"%)");
				
			
		});
		
		lab_Per_Dist="("+lab_Per_Dist+")";
		
		
	}
	
	
	
	
	private void find_LabelDistr() {
		
		this.labelDistribution=new HashMap<>();
		
		user_Lab_Distr.entrySet().stream().forEach(e->{
			
			e.getValue().entrySet().stream().forEach(f->{
				
				
				if(!labelDistribution.containsKey(f.getKey())) {
					
					
					labelDistribution.put(f.getKey(), f.getValue());
					
				}
				
				else {
					
					labelDistribution.put(f.getKey(), labelDistribution.get(f.getKey())+f.getValue());
					
				}
				
				
			});
			
		});
		
		
	}
	

	
	
	public void find_User_Assignment_Distr() {
		
		user_Assignment_Distr=new HashMap<>();
		
		for(LabelAssignment labelAssignment:instance.getLabelAssignments()) {
			
			
			if(!user_Assignment_Distr.containsKey(labelAssignment.getUser().getId())) {
				
				user_Assignment_Distr.put(labelAssignment.getUser().getId(), 1);
				
			}
			
			else {
				
				user_Assignment_Distr.put(labelAssignment.getUser().getId(), user_Assignment_Distr.get(labelAssignment.getUser().getId())+1);
				
				
			}
			
			
		}
		
		
		
	}

	
	
	
	private void find_User_LabelDistr() {
		
		user_Lab_Distr=new HashMap<>();
		
		for(LabelAssignment labelAssignment:instance.getLabelAssignments()) {
			
			
			if (!user_Lab_Distr.containsKey(labelAssignment.getUser().getId())) {
				
				user_Lab_Distr.put(labelAssignment.getUser().getId(), new HashMap<>());
				
				for(Label label:labelAssignment.getLabelsOfInstance()) {
					
					
					if(!user_Lab_Distr.get(labelAssignment.getUser().getId()).containsKey(label.labelId)) {
						
						
						user_Lab_Distr.get(labelAssignment.getUser().getId()).put(label.labelId, 1);
						
					}
					
					else {
						
						
						user_Lab_Distr.get(labelAssignment.getUser().getId()).put(label.labelId, user_Lab_Distr.get(labelAssignment.getUser().getId()).get(label.labelId)+1);
					}
					
					
				}
				
			}
			
			else {
				
				HashMap<Integer, Integer> labelDistribution=new HashMap<>(user_Lab_Distr.get(labelAssignment.getUser().getId()));
				
				for(Label label:labelAssignment.getLabelsOfInstance()) {
					
					
					if(!labelDistribution.containsKey(label.getLabelId())) {
						
						
						labelDistribution.put(label.getLabelId(), 1);
						
					}
					
					else {
						
						
						labelDistribution.put(label.getLabelId(), labelDistribution.get(label.getLabelId())+1);
					}
					
					
				}
				
				user_Lab_Distr.put(labelAssignment.getUser().getId(), labelDistribution);
				
			}
			
			
		}
	}
	
	

	public String getLab_Per_Dist() {
		return lab_Per_Dist;
	}


	public Instance getInstance() {
		return instance;
	}


	public Integer getNumberOfLabelAssignments() {
		return num_Labels;
	}


	public Integer getNumberOfUniqueLabelAssignments() {
		return num_UniqueLabels;
	}


	public Integer getNumberOfUniqueUsers() {
		return num_UniqueUsers;
	}


	public FinalLabel getMostFrequentLabel() {
		return mostFrequentLabel;
	}


	public double getEntropy() {
		return entropy;
	}


	public HashMap<Integer, Integer> getUserLabelAssignmentDistribution() {
		return user_Assignment_Distr;
	}


	public HashMap<Integer, Integer> getLabelDistribution() {
		return labelDistribution;
	}


	public HashMap<Integer, Double> getLabelPercentageDistribution() {
		return labelPercentageDistribution;
	}


	public HashMap<Integer, Integer> getLabelUserDistribution() {
		return labelUserDistribution;
	}


	public HashMap<Integer, HashMap<Integer, Integer>> getUserLabelDistribution() {
		return user_Lab_Distr;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
}
