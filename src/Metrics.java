
public abstract class Metrics {
	
	
	protected Label getLabelWithId(DataSet dataSet,int labelId) {
		
		Label returningLabel=null;
		
		for(Label label:dataSet.getLabelsCanBeUsed()) {
			
			
			if(label.getLabelId()==labelId) {returningLabel=label;}
			
			
		}
		return returningLabel;
		
	}

}
