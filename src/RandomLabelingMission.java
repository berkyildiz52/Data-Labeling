import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class RandomLabelingMission extends LabelingMission {


	

	public RandomLabelingMission(int labelingMissionId, LabelingSystem labelingSystem,double userLabelingProbablity) {

		super(labelingMissionId, labelingSystem,userLabelingProbablity);
		setUsersAssigned(labelingSystem.getConfigInfo().getBotUsers());
		this.dataSets=labelingSystem.getConfigInfo().getDataSets();
	}
	
	


	@Override
	public void run() {

		startMission();

	}


	@Override
	protected LabelAssignment makeLabelAssignment(Instance instance,ArrayList<Label> labelsCanBeUsed, int maxNumberOfLabels, User user) {

		ArrayList<Label> labelsOfInstance = new ArrayList();
		ArrayList<Label> copyList = new ArrayList(labelsCanBeUsed);

		// for each instance there will be labels with the number of maxNumberOfLabels
		// info

		Random random = new Random();

		int numberOfLabels = random.nextInt(maxNumberOfLabels);

		if (numberOfLabels == 0) {
			numberOfLabels++;
		}

		for (int j = 0; j < numberOfLabels; j++) {

			// random number must be an integer between 0 and number of avaible not used
			// labels
			int randomNumber = random.nextInt(copyList.size());
			labelsOfInstance.add(copyList.get(randomNumber));
			copyList.remove(randomNumber);

		}

		return new LabelAssignment(labelsOfInstance, user);

	}

	public void pauseMission() {

		Thread.currentThread().interrupt();
		running = false;

	}

	public void resumeMission() {

		running = true;

	}
	
	

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "\"Random Labeling Mission with Id:" + labelingMissionId + "\" ";
	}

}
