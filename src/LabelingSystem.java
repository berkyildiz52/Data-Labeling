import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;



public class LabelingSystem {

	private ConfigInfo configInfo;
	private OutputProducer outputProducer;
	private LogInScreen logInScreen;
	private LabelingMission labelingMission;
	private ArrayList<Record> records;

	public LabelingSystem() {

		outputProducer = OutputProducer.getInstance();
		outputProducer.addIntoExecutionLog("\nLabeling System started!");
		System.out.println("-------------Welcome To The Labeling System-------------\n");

		

	Parser parser=new Parser();
	this.configInfo=parser.parseConfigJson(this);
	

	}
	
	
	private void checkRecords() {
		
		if(!this.records.isEmpty()) {
			
			for (int i = 0; i < configInfo.getDataSets().size(); i++) {
				
				for(int j=0;j<records.size();j++) {
					
					
					if(configInfo.getDataSets().get(i).getDataSetId()==records.get(j).getDataSetId()) {
						
						configInfo.getDataSets().get(i).setLabeledInstances(records.get(j).getInstancesLabeled());
						configInfo.getDataSets().get(i).setUnlabeledInstances(records.get(j).getInstancesUnlabeled());
						
						
					}
					
					
				}
				
				
			}

		}
	
	}

	public void run() {

		
		 logInScreen = new LogInScreen();
		 User user = logInScreen.tryLogIn((configInfo).getHumanUsers());
		 
		 if (user == null) {
			 
			 try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			System.out.println("-------------Enter 'p' to pause and 'r' to resume and e to exit program!-------------\n");
			Scanner scanner = new Scanner(System.in);
			labelingMission= new RandomLabelingMission(0, this,0.6);
			this.outputProducer.setLabelingMission(labelingMission);
			labelingMission.start();
			
			
			

			while (true) {

				String stdInput = scanner.next();

				if (stdInput.equals("p") || stdInput.equals("P")) {

					pauseSystem();

					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					outputProducer.addIntoExecutionLog("Labeling system is paused!");

				}

				else if (stdInput.equals("r") || stdInput.equals("R")) {

					outputProducer.addIntoExecutionLog("Labeling system continues!");

					resumeSystem();

				}

				else if (stdInput.equals("e") || stdInput.equals("E")) {

					System.out.println("Labeling System Switched off , Bye Bye !!!");
					System.exit(0);

				}

			}

		}
		
		 else {
			 
			this.records=new Parser().parseRecord(configInfo.getDataSets(), user);
			
			checkRecords();
			
			labelingMission = new ManuelLabelingMission(0, this,user,1);
			
			
			labelingMission.start();
		}
	}

	public void pauseSystem() {

		((RandomLabelingMission)labelingMission).running=false;

	}

	public void resumeSystem() {

		((RandomLabelingMission)labelingMission).running=false;

	}

	public ConfigInfo getConfigInfo() {
		return configInfo;
	}

	public void setConfigInfo(ConfigInfo configInfo) {
		this.configInfo = configInfo;
	}

	public LabelingMission getLabelingMission() {
		return labelingMission;
	}

	public void setLabelingMission(LabelingMission labelingMission) {
		this.labelingMission = labelingMission;
	}
	
	

}
