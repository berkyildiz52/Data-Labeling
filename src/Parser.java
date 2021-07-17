import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class Parser {

	private File configJsonFile;
	private ArrayList<File> dataSetJsonFiles;
	private OutputProducer outputProducer;

	// constructor with a single filename parameter
	public Parser() {

		this.configJsonFile = new File("config.json");
		outputProducer = OutputProducer.getInstance();

	}

	public ConfigInfo parseConfigJson(LabelingSystem labelingSystem) {

		ConfigInfo configInfo=new ConfigInfo();
		

		outputProducer.addIntoExecutionLog("Parsing of config.json started");

		JsonParser jsonParser = new JsonParser();

		try {
			JsonObject jsonObject = (JsonObject) jsonParser
					.parse(new FileReader(configJsonFile, Charset.forName("UTF-8")));

			JsonArray labelingMissionsJson = jsonObject.get("configInfo").getAsJsonArray();

			for (JsonElement jsonElement : labelingMissionsJson) {

				JsonObject jsonObject1 = (JsonObject) jsonElement;

				JsonArray datasetInfoJsonArray = jsonObject1.get("datasets").getAsJsonArray();

				dataSetJsonFiles = new ArrayList();

				for (JsonElement datasetInfoElement : datasetInfoJsonArray) {

					JsonObject datasetInfoJsonObject = (JsonObject) datasetInfoElement;

					String fileName = datasetInfoJsonObject.get("filePath").getAsString();

					this.dataSetJsonFiles.add(new File(fileName));

				}

				ArrayList<DataSet> dataSets = parseDatasetsFromJsonFiles();
				ArrayList<User> humanUsers=new ArrayList<>();
				ArrayList<User> botUsers=new ArrayList<>();

				JsonArray usersInfoJsonArray = jsonObject1.get("users").getAsJsonArray();

				for (JsonElement userInfoElement : usersInfoJsonArray) {

					JsonObject userInfoJsonObject = (JsonObject) userInfoElement;

					int userId = userInfoJsonObject.get("userId").getAsInt();
					String userName = userInfoJsonObject.get("userName").getAsString();
					String userPassword = userInfoJsonObject.get("userPassword").getAsString();
					String userType = userInfoJsonObject.get("userType").getAsString();
					double consistencyCheckProbablity = userInfoJsonObject.get("consistencyCheckProbablity")
							.getAsDouble();
					if(userType.toLowerCase().equals("human")) {humanUsers.add(new User(userId, userName, userPassword, userType, consistencyCheckProbablity));}
					
					else {botUsers.add(new User(userId, userName, userPassword, userType, consistencyCheckProbablity));}

				}

			
				
				
				configInfo.setBotUsers(botUsers);
				configInfo.setHumanUsers(humanUsers);
				configInfo.setDataSets(dataSets);
			}

		} catch (JsonIOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		outputProducer.addIntoExecutionLog("Parsing of config.json ended");

		return configInfo;

	}

	// parses the information related to dataset and generates an arraylist full of
	// Datasets and
	// returns it
	public ArrayList<DataSet> parseDatasetsFromJsonFiles() {

		ArrayList<DataSet> dataSets = new ArrayList<>();

		for (File dataSetJsonFile : dataSetJsonFiles) {

			outputProducer.addIntoExecutionLog("Parsing of Dataset file: " + dataSetJsonFile.getName() + " started!");

			DataSet dataSet = null;

			String fileExtension = configJsonFile.toString().substring(configJsonFile.toString().indexOf('.') + 1);

			if (dataSetJsonFile.exists()) {

				// check that the file is a json file
				if (!fileExtension.equals("json")) {

					System.out.println("Error:The file you want to parse is not a json file!");
					return null;
				}

				else {

					JsonParser jsonParser = new JsonParser();

					try {

						JsonObject jsonObject = (JsonObject) jsonParser
								.parse(new FileReader(dataSetJsonFile, Charset.forName("UTF-8")));

						// get dataset id
						int dataSetId = jsonObject.get("dataset id").getAsInt();

						// get dataset name
						String dataSetName = jsonObject.get("dataset name").getAsString();

						// get max number of labels info
						int maxNumberOfLabels = jsonObject.get("maximum number of labels per instance").getAsInt();

						// get labels as jsonarray
						JsonArray classLabels_Json = jsonObject.get("class labels").getAsJsonArray();

						// get instances as json array
						JsonArray instances_Json = jsonObject.get("instances").getAsJsonArray();

						ArrayList<Label> labels = parseLabelsFromJson(classLabels_Json);
						ArrayList<Instance> instances = parseInstancesFromJson(instances_Json);

						dataSet = new DataSet(dataSetId, dataSetName, maxNumberOfLabels, instances, labels);

						dataSets.add(dataSet);

						outputProducer.addIntoExecutionLog(
								"Dataset file:" + dataSetJsonFile.getName() + " is parsed successfully.");

					} catch (FileNotFoundException e) {
						System.out.println("Error: File  not  found!");
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (JsonParseException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}

				}

			}

			else {

				outputProducer.addIntoExecutionLog(
						"Error: DataSetFile with fileName:" + dataSetJsonFile.getName() + " is not found!");

			}

		}

		return dataSets;

	}
	
	public ArrayList<Record> parseRecord(ArrayList<DataSet> dataSets,User user){
		
		ArrayList<Record> records=new ArrayList<>();
		
		File dir=new File("Records");
		
		if(!dir.exists()) {dir.mkdir();}
		
		else {
			
			for (int i = 0; i < dataSets.size(); i++) {
				
				
					File file=new File(dir,"user"+user.getId()+"_"+"dataset"+dataSets.get(i).getDataSetId()+".json");
					
					if(file.exists()) {
						
						JsonParser jsonParser=new JsonParser();
						
						JsonObject jsonObject;
						try {
							jsonObject = (JsonObject) jsonParser
									.parse(new FileReader(file, Charset.forName("UTF-8")));
							
							JsonArray labeledInsJson=jsonObject.get("Labeled Instances").getAsJsonArray();
							JsonArray unLabeledInsJson=jsonObject.get("Unlabeled Instances").getAsJsonArray();
							ArrayList<Instance> labeledInstances=parseInstancesFromJson(labeledInsJson);
							ArrayList<Instance> unlabeledInstances=parseInstancesFromJson(unLabeledInsJson);
							
							int userId=jsonObject.get("user id").getAsInt();
							
							int dataSetId=jsonObject.get("dataset id").getAsInt();
							
							Record record=new Record(labeledInstances, unlabeledInstances, dataSetId,userId);
							
							records.add(record);
							
						} catch (JsonIOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (JsonSyntaxException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					
					}
					
				}
				
			}
	
		
		
		return records;
	}

	private ArrayList<LabelAssignment> parseLabelAssignmentsFromJson(JsonArray assignments) {

		ArrayList<LabelAssignment> labelAssignments = new ArrayList<>();

		for (JsonElement jsonElement : assignments) {

			LabelAssignment labelAssignment = new LabelAssignment();

			JsonObject assignmentJsonObject = (JsonObject) jsonElement;

			JsonArray labelsJson = assignmentJsonObject.get("labels").getAsJsonArray();

			ArrayList<Label> labels = parseLabelsFromJson(labelsJson);

			JsonObject userJson = assignmentJsonObject.get("user").getAsJsonObject();

			User user = new User(userJson.get("userId").getAsInt(), userJson.get("userName").getAsString(),
					userJson.get("userType").getAsString());

			labelAssignment.setUser(user);
			labelAssignment.setLabelsOfInstance(labels);
			labelAssignment.setStartDate(buildCalendar(assignmentJsonObject.get("start date").getAsString()));
			labelAssignment.setEndDate(buildCalendar(assignmentJsonObject.get("end date").getAsString()));

			labelAssignments.add(labelAssignment);
		}

		return labelAssignments;
	}

	private Calendar buildCalendar(String date) {

		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
		try {
			calendar.setTime(simpleDateFormat.parse(date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return calendar;

	}

	// function takes Json Array containing label info and returns an arraylist with
	// Label Objects
	private ArrayList<Label> parseLabelsFromJson(JsonArray labels_Json) {

		int labelId = 0;
		String labelText = "";
		ArrayList<Label> labels = new ArrayList<>();

		for (JsonElement label_JsonElement : labels_Json) {

			JsonObject label_JsonObject = (JsonObject) label_JsonElement;

			labelId = label_JsonObject.get("label id").getAsInt();
			labelText = label_JsonObject.get("label text").getAsString();
			Label label = new Label(labelId, labelText);
			labels.add(label);

		}

		return labels;

	}

	// function takes Json Array containing instance infos and returns an arraylist
	// with Instance Objects
	private ArrayList<Instance> parseInstancesFromJson(JsonArray instances_Json) {

		int instanceId = 0;
		String instanceText = "";
		ArrayList<Instance> instances = new ArrayList<>();
		ArrayList<LabelAssignment> labelAssignments = null;

		int lineNumber = 1;

		for (JsonElement instance_JsonElement : instances_Json) {

			JsonObject label_JsonObject = (JsonObject) instance_JsonElement;

			instanceId = label_JsonObject.get("id").getAsInt();
			instanceText = label_JsonObject.get("instance").getAsString();
			Instance instance = new Instance(instanceId, instanceText);

			if (label_JsonObject.has("assignments")) {

				labelAssignments = parseLabelAssignmentsFromJson(label_JsonObject.get("assignments").getAsJsonArray());

			}

			instance.setLineNumber(lineNumber);

			instance.setLabelAssignments(labelAssignments);

			instances.add(instance);

			lineNumber++;

		}

		return instances;

	}


}
