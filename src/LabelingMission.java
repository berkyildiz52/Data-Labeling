import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

public abstract class LabelingMission extends Thread {

	protected int labelingMissionId;
	protected ArrayList<User> usersAssigned;
	protected ArrayList<DataSet> dataSets;
	protected OutputProducer outputProducer;
	protected Calendar missionStartDate;
	protected Calendar missionEndDate;
	protected LabelingSystem labelingSystem;
	protected double userLabelingProbablity;
	public volatile boolean running = true;


	public LabelingMission(int labelingMissionId, LabelingSystem labelingSystem, double userLabelingProbablity) {

		this.labelingSystem = labelingSystem;
		this.labelingMissionId = labelingMissionId;
		outputProducer = OutputProducer.getInstance();
		this.userLabelingProbablity=userLabelingProbablity;
	}


	

	protected void startMission() {
		
		missionStartDate = Calendar.getInstance();

		outputProducer.addIntoExecutionLog(this.toString() + " started ");

		for (int i = 0; i < dataSets.size(); i++) {

			for (int j = 0; j < getUsersAssigned().size(); j++) {


				labelFromUnLabeledList(i, j);

				labelFromLabeledList(i, j);

			}

		}

		outputProducer.addIntoExecutionLog(this.toString() + " ended");
		
	}
	
	
	protected void labelFromLabeledList(int dataSetIndex, int userIndex) {

		for (int k = 0; k < dataSets.get(dataSetIndex).getLabeledInstances().size(); k++) {

			int numberOfLabelings = 0;

			for (LabelAssignment labelAssignment : dataSets.get(dataSetIndex).getLabeledInstances().get(k)
					.getLabelAssignments()) {

				if (labelAssignment.getUser().equals(getUsersAssigned().get(userIndex))) {

					numberOfLabelings++;

				}

			}

			switch (numberOfLabelings) {

			case 2: {
				continue;
			}

			case 1: {

				double randomValue = Math.random();

				if (randomValue < getUsersAssigned().get(userIndex).getConsistencyCheckProbablity()) {


					
					Locale currentLocale = new Locale ("tr","TR");

					BreakIterator wordIterator =
					    BreakIterator.getWordInstance(currentLocale);
					
					//markBoundaries(instanceText, wordIterator);
					
					
					myPrint(dataSets.get(dataSetIndex).getLabeledInstances().get(k),dataSetIndex, wordIterator);
					
					
					LabelAssignment labelAssignment = makeLabelAssignment(
							dataSets.get(dataSetIndex).getLabeledInstances().get(k),
							dataSets.get(dataSetIndex).getLabelsCanBeUsed(),
							dataSets.get(dataSetIndex).getMaxNumberOfLabels(), getUsersAssigned().get(userIndex));

					sleepThreadRandomTime();

					dataSets.get(dataSetIndex).getLabeledInstances().get(k).addLabelAssignment(labelAssignment,
							dataSets.get(dataSetIndex));

					for (int i = 0; i < dataSets.size(); i++) {

						dataSets.get(i).setMetrics(new DataSetMetrics(getUsersAssigned(), dataSets.get(i)));
					}

					for (int i = 0; i < getUsersAssigned().size(); i++) {
						getUsersAssigned().get(i)
								.setMetrics(new UserMetrics(getUsersAssigned().get(i), labelingSystem));
					}

					// add the operation into logs
					String logInfo = "Consistency Check ->"
							+ dataSets.get(dataSetIndex).getLabeledInstances().get(k).toString() + " is labeled with "
							+ labelAssignment.getLabelsOfInstance() + " by "
							+ getUsersAssigned().get(userIndex).toString();

					outputProducer.addIntoExecutionLog(logInfo);

					outputProducer.printDataSetMetrics(dataSets.get(dataSetIndex).getMetrics());

					outputProducer.printUserMetrics(getUsersAssigned().get(userIndex).getMetrics());

					outputProducer.produceJsonOutput(dataSets.get(dataSetIndex));
					
					break;
				}

				else {
					continue;
				}

			}

			case 0: {

				double randomValue = Math.random();

				if (randomValue < userLabelingProbablity) {


					Locale currentLocale = new Locale ("tr","TR");

					BreakIterator wordIterator =
					    BreakIterator.getWordInstance(currentLocale);
					
					
					try {
						myPrint(dataSets.get(dataSetIndex).getUnlabeledInstances().get(k),dataSetIndex, wordIterator);
					} catch (Exception e) {
						
					}
					
					LabelAssignment labelAssignment = makeLabelAssignment(
							dataSets.get(dataSetIndex).getLabeledInstances().get(k),
							dataSets.get(dataSetIndex).getLabelsCanBeUsed(),
							dataSets.get(dataSetIndex).getMaxNumberOfLabels(), getUsersAssigned().get(userIndex));

					sleepThreadRandomTime();

					dataSets.get(dataSetIndex).getLabeledInstances().get(k).addLabelAssignment(labelAssignment,
							dataSets.get(dataSetIndex));

					for (int i = 0; i < dataSets.size(); i++) {

						dataSets.get(i).setMetrics(new DataSetMetrics(getUsersAssigned(), dataSets.get(i)));
					}

					for (int i = 0; i < getUsersAssigned().size(); i++) {
						getUsersAssigned().get(i)
								.setMetrics(new UserMetrics(getUsersAssigned().get(i), labelingSystem));
					}

					// set the log info
					String logInfo = dataSets.get(dataSetIndex).getLabeledInstances().get(k).toString()
							+ " is labeled with " + labelAssignment.getLabelsOfInstance() + " by "
							+ getUsersAssigned().get(userIndex).toString();

					outputProducer.addIntoExecutionLog(logInfo);

					outputProducer.printDataSetMetrics(dataSets.get(dataSetIndex).getMetrics());

					outputProducer.printUserMetrics(getUsersAssigned().get(userIndex).getMetrics());

					outputProducer.produceJsonOutput(dataSets.get(dataSetIndex));
				}
			}
		  }

		}
	}

	
	
	protected void labelFromUnLabeledList(int dataSetIndex, int userIndex) {

		if (!dataSets.get(dataSetIndex).getUnlabeledInstances().isEmpty()) {
			Random random = new Random();
			for (int k = 0; k < dataSets.get(dataSetIndex).getUnlabeledInstances().size(); k++) {


				if (random.nextDouble() < userLabelingProbablity) {

					
					
					Locale currentLocale = new Locale ("tr","TR");

					BreakIterator wordIterator =
					    BreakIterator.getWordInstance(currentLocale);
					
					//markBoundaries(instanceText, wordIterator);
					
					myPrint(dataSets.get(dataSetIndex).getUnlabeledInstances().get(k),dataSetIndex, wordIterator);
					
					
					LabelAssignment labelAssignment = makeLabelAssignment(
							dataSets.get(dataSetIndex).getUnlabeledInstances().get(k),
							dataSets.get(dataSetIndex).getLabelsCanBeUsed(),
							dataSets.get(dataSetIndex).getMaxNumberOfLabels(), getUsersAssigned().get(userIndex));

					sleepThreadRandomTime();

					dataSets.get(dataSetIndex).getUnlabeledInstances().get(k).addLabelAssignment(labelAssignment,
							dataSets.get(dataSetIndex));

					// set the log info
					String logInfo = dataSets.get(dataSetIndex).getUnlabeledInstances().get(k).toString()
							+ " is labeled with " + labelAssignment.getLabelsOfInstance() + " by "
							+ getUsersAssigned().get(userIndex).toString();

					dataSets.get(dataSetIndex).getLabeledInstances()
							.add(dataSets.get(dataSetIndex).getUnlabeledInstances().get(k));

					dataSets.get(dataSetIndex).getUnlabeledInstances().remove(k);
					k--;

					for (int i = 0; i < dataSets.size(); i++) {

						dataSets.get(i).setMetrics(new DataSetMetrics(getUsersAssigned(), dataSets.get(i)));
					}

					for (int i = 0; i < getUsersAssigned().size(); i++) {
						getUsersAssigned().get(i)
								.setMetrics(new UserMetrics(getUsersAssigned().get(i), labelingSystem));
					}

					outputProducer.addIntoExecutionLog(logInfo);

					outputProducer.printDataSetMetrics(dataSets.get(dataSetIndex).getMetrics());

					outputProducer.printUserMetrics(getUsersAssigned().get(userIndex).getMetrics());

					outputProducer.produceJsonOutput(dataSets.get(dataSetIndex));
				}

				else {
					continue;
				}

			}
		}

	}
	
	
	protected abstract LabelAssignment makeLabelAssignment(Instance instance,ArrayList<Label> labelsCanBeUsed, int maxNumberOfLabels, User user);
	
	
	// sleeps the thread in random seconds up to 3 sec.
		public void sleepThreadRandomTime() {

			while (!running) {
				this.yield();
			}

			Random random = new Random();
			int waitingTime = random.nextInt(3000);

			try {
				Thread.sleep(waitingTime);
			} catch (InterruptedException e) {

			}
		}
		

		 
		 protected void myPrint(Instance instance,int dataSetIndex, BreakIterator wordIterator) {
			 	
			 	if (labelingSystem.getLabelingMission() instanceof ManuelLabelingMission) {
					System.out.println("\n\n");
					int totalNum = dataSets.get(dataSetIndex).getLabeledInstances().size()
							+ dataSets.get(dataSetIndex).getUnlabeledInstances().size();
					int instanceNum = (totalNum - dataSets.get(dataSetIndex).getUnlabeledInstances().size()) + 1;
					System.out.print("Instance (" + instanceNum + "//" + totalNum + "): \"");
					wordIterator.setText(instance.getInstanceText());
					int start = wordIterator.first();
					int end = wordIterator.next();
					int i = 0;
					while (end != BreakIterator.DONE) {

						String word = instance.getInstanceText().substring(start, end);
						if (i != 0 && i % 25 == 0) {
							System.out.println();
							System.out.print("\t\t  " + word);
						}

						else {
							System.out.print(word);
						}
						i++;
						start = end;
						end = wordIterator.next();
					}
					System.out.print("\"");
				}
			}
	
	
	public boolean addDataset(DataSet dataSet) {

		boolean returnValue = false;

		if (this.dataSets == null) {

			this.dataSets = new ArrayList<>();

		}

		boolean isDatasetAddedBefore = false;

		for (int i = 0; i < dataSets.size(); i++) {

			if (dataSets.get(i).getDataSetId() == dataSet.getDataSetId()) {

				isDatasetAddedBefore = true;
				break;

			}
		}

		if (isDatasetAddedBefore) {

			outputProducer.addIntoExecutionLog(("Error:" + dataSet.toString() + "is tried to be included into \n"
					+ this.toString() + " but this dataSet is already in the mission"));
			returnValue = false;
		}

		else {

			dataSets.add(dataSet);
			outputProducer.addIntoExecutionLog(dataSet.toString() + "is  included into " + this.toString());
			returnValue = true;
		}

		return returnValue;
	}

	public int getLabelingMissionId() {
		return labelingMissionId;
	}

	public void setLabelingMissionId(int labelingMissionId) {
		this.labelingMissionId = labelingMissionId;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "\"labeling Mission with Id:" + labelingMissionId + "\" ";
	}

	public boolean addUser(User user) {

		boolean returnValue = false;

		if (this.usersAssigned == null) {

			usersAssigned = new ArrayList<>();

		}

		boolean isUserAddedBefore = false;

		for (int i = 0; i < usersAssigned.size(); i++) {

			if (usersAssigned.get(i).getId() == user.getId()) {

				isUserAddedBefore = true;
				break;

			}

		}

		if (isUserAddedBefore) {

			outputProducer.addIntoExecutionLog(("Error:" + user.toString() + "is tried to be assigned to \n"
					+ this.toString() + " but this user is already in the mission"));
			returnValue = false;
		}

		else {

			usersAssigned.add(user);
			outputProducer.addIntoExecutionLog(user.toString() + "is  assigned to " + this.toString());
			returnValue = true;
		}       

		return returnValue;

	}

	
	public ArrayList<User> getUsersAssigned() {
		return usersAssigned;
	}

	public void setUsersAssigned(ArrayList<User> usersAssigned) {
		
 		if (this.usersAssigned == null) {

			this.usersAssigned = new ArrayList<>();

		}
		
		for(User user:usersAssigned) {addUser(user);}

		
	}
	
	
	public ArrayList<DataSet> getDataSets() {
		return dataSets;
	}

	public void setDataSets(ArrayList<DataSet> dataSets) {

		if(this.dataSets==null) {this.dataSets=new ArrayList<>();}
		
		for(DataSet dataSet:dataSets) {
			
			addDataset(dataSet);
			
			
		}
		
		
	}

}
