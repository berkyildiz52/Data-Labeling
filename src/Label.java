
public class Label {

	protected int labelId;
	protected String labelText;

	public Label() {
		// TODO Auto-generated constructor stub
	}

	public Label(int labelId, String labelText) {
		this.labelId = labelId;
		this.labelText = labelText;
	}

	public int getLabelId() {
		return labelId;
	}

	public void setLabelId(int labelId) {
		this.labelId = labelId;
	}

	public String getLabelText() {
		return labelText;
	}

	public void setLabelText(String labelText) {
		this.labelText = labelText;
	}

	@Override
	public String toString() {
		
		
		
		// TODO Auto-generated method stub
		return labelText;
	}

	@Override
	public boolean equals(Object label) {
		// TODO Auto-generated method stub
		if(this.labelId==((Label)label).labelId)return true;
		else return false;
	}
	
	
	
}
