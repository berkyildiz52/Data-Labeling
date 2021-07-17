import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;

public class OutputProducer {

	private static OutputProducer outputProducer;
	private File logFile;
	private PrintWriter logFileWriter;
	private Calendar calendar;
	private LabelingMission labelingMission;
	
	private OutputProducer() {}
	
	public synchronized static OutputProducer getInstance() {
		
		if(outputProducer==null) {
			
			
			outputProducer=new OutputProducer();
		}
		
		
			return outputProducer;
	}
	



	public void produceJsonOutput(DataSet dataSet) {

		
			
			JsonObject dataSetJsonObject=new JsonObject();
			dataSetJsonObject.addProperty("dataset id", dataSet.getDataSetId());
			dataSetJsonObject.addProperty("dataset name", dataSet.getDataSetName());
			dataSetJsonObject.addProperty("maximum number of labels per instance", dataSet.getMaxNumberOfLabels());
			
			dataSetJsonObject.add("class labels", putLabelsInJsonArray(dataSet.getLabelsCanBeUsed()));
			
			dataSetJsonObject.add("instances",putInstancesInJsonArray(dataSet.getLabeledInstances()));
			
			GsonBuilder builder=new GsonBuilder();
			String output=builder.setPrettyPrinting().create().toJson(dataSetJsonObject);
			
			
			try {

				FileWriter fileWriter = new FileWriter("output_d"+dataSet.getDataSetId()+".json");
				fileWriter.write(output);
				
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
	
	
	public void printUserMetrics(Metrics metrics) {
		
		UserMetrics userMetrics=(UserMetrics)metrics;
		
		JsonObject umJsonObject=new JsonObject();
		
		umJsonObject.addProperty("user id", userMetrics.getUser().getId());
		umJsonObject.addProperty("user name", userMetrics.getUser().getUserName());
		umJsonObject.addProperty("user type", userMetrics.getUser().getUserType());
		umJsonObject.addProperty("Number Of DataSets Assigned", userMetrics.getNumberOfDatasets());
		umJsonObject.addProperty("Datasets And Their Completeness Percentages", userMetrics.getDat_Comp_Per_Dist());
		umJsonObject.addProperty("Total Number Of Instances Labeled", userMetrics.getNumberOfLabeledInstances());
		umJsonObject.addProperty("Total Number Of Unique Instances Labeled", userMetrics.getNumberOfUniqueLabeledInstances());
		if(userMetrics.getConsistencyPercentage()==-1.0) {umJsonObject.addProperty("Consistency Percentage", "NaN");}
		else {umJsonObject.addProperty("Consistency Percentage", "%"+userMetrics.getConsistencyPercentage());}
		
		umJsonObject.addProperty("Average Time Spent", userMetrics.getAverageTimeSpent().doubleValue()+" seconds");
		umJsonObject.addProperty("Standart Deviation Of Time Spent", userMetrics.getStdDeviationOfTime().doubleValue());
		
		GsonBuilder gsonBuilder=new GsonBuilder();
		
		File dir=new File("User Metrics");
		
		if(!dir.exists()) {dir.mkdir();}
		
		String output=gsonBuilder.setPrettyPrinting().create().toJson(umJsonObject);
		
		FileWriter fileWriter;
		
		File file=new File(dir,"usermetrics_u_"+userMetrics.getUser().getId()+".json");
		
		try {
			fileWriter = new FileWriter(file);
			
			fileWriter.write(output);
			
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void printInstanceMetrics(Metrics metrics) {
		
		
		InstanceMetrics instanceMetrics=(InstanceMetrics)metrics;
		
		JsonObject imJsonObject=new JsonObject();
		
		imJsonObject.addProperty("instance id", instanceMetrics.getInstance().getInstanceId());
		imJsonObject.addProperty("instance text", instanceMetrics.getInstance().getInstanceText());
		imJsonObject.addProperty("Total Number Of Label Assignments", instanceMetrics.getNumberOfLabelAssignments());
		imJsonObject.addProperty("Number Of Unique LAbel Assignments", instanceMetrics.getNumberOfUniqueLabelAssignments());
		imJsonObject.addProperty("Number Of Unique Users", instanceMetrics.getNumberOfUniqueUsers());
		imJsonObject.addProperty("Most Frequent Label And Its Percentage", "("+instanceMetrics.getMostFrequentLabel()+":"+instanceMetrics.getMostFrequentLabel().getPercentage()+"%)");
		imJsonObject.addProperty("Class Labels and Their Percentages", instanceMetrics.getLab_Per_Dist());
		imJsonObject.addProperty("Entropy", instanceMetrics.getEntropy());
		
		GsonBuilder gsonBuilder=new GsonBuilder();
		
		File dir=new File("Instance Metrics");
		
		if(!dir.exists()) {dir.mkdir();}
		
		String output=gsonBuilder.setPrettyPrinting().create().toJson(imJsonObject);
		
		FileWriter fileWriter;
		
		File file=new File(dir,"instanceMetrics_i_"+instanceMetrics.getInstance().getInstanceId()+".json");
		
		try {
			fileWriter = new FileWriter(file);
			
			fileWriter.write(output);
			
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void printDataSetMetrics(Metrics metrics) {
		
		DataSetMetrics dataSetMetrics=(DataSetMetrics)metrics;
		
		JsonObject dmJsonObject=new JsonObject();
		dmJsonObject.addProperty("dataset id", dataSetMetrics.getDataSet().getDataSetId());
		dmJsonObject.addProperty("dataset name", dataSetMetrics.getDataSet().getDataSetName());
		dmJsonObject.addProperty("completeness percentage", dataSetMetrics.getCompletenessPercentage());
		dmJsonObject.addProperty("final label distribution", dataSetMetrics.getFin_Lab_Dist());
		dmJsonObject.addProperty("List Numbers Of Instances For Each Label", dataSetMetrics.getInst_List_Num_Per_Lab());
		dmJsonObject.addProperty("number of users assigned", dataSetMetrics.getNumberOfUsersAssigned());
		dmJsonObject.addProperty("users and their completeness percentages", dataSetMetrics.getComp_Per_Dist());
		
		
		GsonBuilder gsonBuilder=new GsonBuilder();
		
		File dir=new File("Dataset Metrics");
		
		if(!dir.exists()) {dir.mkdir();}
		
		String output=gsonBuilder.setPrettyPrinting().create().toJson(dmJsonObject);
		
		FileWriter fileWriter;
		
		File file=new File(dir,"dataSetMetrics_d"+dataSetMetrics.getDataSet().getDataSetId()+".json");
		try {
			fileWriter = new FileWriter(file);
			
			fileWriter.write(output);
			
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	

	
	
	public void addIntoExecutionLog(String logInfo) {
		
		this.calendar=Calendar.getInstance();
		
		
		if(logFile==null) {
			
			logFile=new File("executionLogs.txt");
			
			
		}
		
		try {
			logFileWriter=new PrintWriter(new BufferedWriter(new FileWriter(this.logFile,true)));
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		logFileWriter.println(getDateInString(calendar)+"-->> "+logInfo+"\n");
		
		try {
			
			if (this.labelingMission instanceof RandomLabelingMission) {
				System.out.println(getDateInString(calendar) + "-->> " + logInfo + "\n");
			}
		} catch (Exception e) {
			
		}
		
		logFileWriter.close();
	}
	
	private JsonArray putLabelsInJsonArray(ArrayList<Label> labels) {
		
		
		JsonArray labelsJsonArray=new JsonArray();
		
		for(Label label:labels) {
			
			
			JsonObject labelJsonObject=new JsonObject();
			
			labelJsonObject.addProperty("label id", label.getLabelId());
			labelJsonObject.addProperty("label text", label.getLabelText());
			
			labelsJsonArray.add((JsonElement)labelJsonObject);
		}
		
		return labelsJsonArray;
		
		
	}
	
	private JsonArray putInstancesInJsonArray(ArrayList<Instance> instances) {
		
		JsonArray instancesJsonArray=new JsonArray();
		
		
		for(Instance instance:instances) {
			
			JsonObject instanceJsonObject=new JsonObject();
			instanceJsonObject.addProperty("id", instance.getInstanceId());
			instanceJsonObject.addProperty("instance", instance.getInstanceText());
			try {
				instanceJsonObject.add("assignments", putLabelAssignmentsInJson(instance.getLabelAssignments()));
			} catch (Exception e) {
				
			}
			instancesJsonArray.add((JsonElement)(instanceJsonObject));
			
		}
		
		return instancesJsonArray;
		
	}
	
	
	private JsonArray putLabelAssignmentsInJson(ArrayList<LabelAssignment> labelAssignments) throws Exception{
		
		JsonArray labelAssignmentsJson=new JsonArray();
		
		for(LabelAssignment labelAssignment:labelAssignments) {
			
			JsonObject labelAssignmentJson=new JsonObject();
			JsonObject userJson=new JsonObject();
			userJson.addProperty("userId", labelAssignment.getUser().getId());
			userJson.addProperty("userName", labelAssignment.getUser().getUserName());
			userJson.addProperty("userType", labelAssignment.getUser().getUserType());
			userJson.addProperty("consistencyCheckProbablity", labelAssignment.getUser().getConsistencyCheckProbablity());
			
			labelAssignmentJson.add("user", userJson);
			labelAssignmentJson.add("labels",putLabelsInJsonArray(labelAssignment.getLabelsOfInstance()));
			
			try {
				labelAssignmentJson.addProperty("start date", getDateInString(labelAssignment.getStartDate()));
				labelAssignmentJson.addProperty("end date", getDateInString(labelAssignment.getEndDate()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			labelAssignmentsJson.add((JsonElement)labelAssignmentJson);
			
			
		}
		
		return labelAssignmentsJson;
	}
	
	
	public void recordToJson(Record record) {
		
		File dir=new File("Records");
		
		if(!dir.exists()) {dir.mkdir();}
		
		else {
		
			File file=new File(dir,"user"+record.getUserId()+"_"+"dataset"+record.getDataSetId()+".json");
			
			JsonObject jsonObject=new JsonObject();
			
			jsonObject.addProperty("user id", record.getUserId());
			jsonObject.addProperty("dataset id", record.getDataSetId());
			
			JsonArray labeledInsJson=putInstancesInJsonArray(record.getInstancesLabeled());
			JsonArray unlabeledInsJson=putInstancesInJsonArray(record.getInstancesUnlabeled());
			
			jsonObject.add("Labeled Instances", labeledInsJson);
			jsonObject.add("Unlabeled Instances", unlabeledInsJson);
			
			String output=new GsonBuilder().setPrettyPrinting().create().toJson(jsonObject);
			
			try {
			FileWriter	fileWriter = new FileWriter(file,Charset.forName("UTF-8"));
				
				fileWriter.write(output);
				
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
		}
		
	}
	
	private String getDateInString(Calendar calendar) {
		
		
		
		Date date=calendar.getTime();
		DateFormat dateFormat=new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
		String dateInString=dateFormat.format(date);
		
		return dateInString;
		
	}

	public void setLabelingMission(LabelingMission labelingMission) {
		this.labelingMission = labelingMission;
	}
	
	

}
