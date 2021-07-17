import java.io.Console;
import java.io.IOException;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class ManuelLabelingMission extends LabelingMission {

	private Scanner scanner;
	private int dataSetIndex;
	

	public ManuelLabelingMission(int labelingMissionId, LabelingSystem labelingSystem,User user, double userLabelingProbablity) {
		
		super(labelingMissionId,labelingSystem,userLabelingProbablity);
		
		setDataSets(labelingSystem.getConfigInfo().getDataSets());
		addUser(user);
		scanner=new Scanner(System.in);
		this.userLabelingProbablity=userLabelingProbablity;

	}
	

	
	@Override
	public void run() {
		
		startMission();
	}
	
	protected LabelAssignment makeLabelAssignment(Instance instance,ArrayList<Label> labelsCanBeUsed, int maxNumberOfLabels, User user) {
		
		System.out.println("\n\n\nEnter label's number for selection!");
		System.out.println("Enter 'ok' to end selection!");
		System.out.println("Enter 'exit' to save your assignments and exit!");

		ArrayList<Label> labelsChosen=new ArrayList<>();
		
		
		System.out.println("\n\n-------------------------------------------------------------------------");
		
		for(int i=0;i<labelsCanBeUsed.size();i++) {
			
			if(i!=0&&i%5==0) {System.out.println("\n");}
			System.out.print(labelsCanBeUsed.get(i).getLabelText()+": "+(i+1)+" ,  ");
			
		}
		
		System.out.println("\n------------------------------------------------------------------------------");
		
		
		while(true) {
			
			if(labelsCanBeUsed.size()==maxNumberOfLabels) {break;}
			
			System.out.print("\nYour Selection: ");
			String selection=scanner.next();
			
			if(selection.toLowerCase().equals("ok")) {System.out.println();break;}
			if(selection.toLowerCase().equals("exit")) {
				
				outputProducer.recordToJson(new Record(dataSets.get(dataSetIndex).getLabeledInstances(),
						dataSets.get(dataSetIndex).getUnlabeledInstances(), 
						dataSets.get(dataSetIndex).getDataSetId(), usersAssigned.get(0).getId()));
					System.out.println("\nYour assignments are saved!");
					System.out.println("Bye bye !!!");
					System.exit(0);
			}
			
			
			
			boolean isValid=true;
			
			for(int i=0;i<selection.length();i++) {
				
				if(!Character.isDigit(selection.charAt(i))) {
					
					
					System.out.println("Invalid input! Enter again!");
					
					isValid=false;
					break;
					
				}
			
			}
			
			if(!isValid) {continue;}
			else {
				
				if(labelsCanBeUsed.size()<Integer.parseInt(selection)||1>Integer.parseInt(selection)) {
					
					
					System.out.println("Invalid input! Enter again!");
					continue;
					
				}
				else {
					
					if(labelsChosen.contains(labelsCanBeUsed.get(Integer.parseInt(selection)))) {
						
						System.out.println("you have already assigned this label to the instance,");
						continue;
						
					}
					
					else {labelsChosen.add(labelsCanBeUsed.get(Integer.parseInt(selection)));}
					
				}
				
			}
			
		}
		
		LabelAssignment labelAssignment=new LabelAssignment(labelsChosen,user);
		
		return labelAssignment;
	}
	
	private void save(User user, ArrayList<Instance> instancesLab) {}

}
