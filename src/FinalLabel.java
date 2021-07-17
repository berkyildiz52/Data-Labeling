
public class FinalLabel extends Label{

	private Integer number;
	private Double percentage;
	
	public FinalLabel(Label label) {
		

		super(label.labelId,label.labelText);
		
		number=new Integer(0);
		percentage=new Double(0);
		
		
	}
	

	
	
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
	public Double getPercentage() {
		return percentage;
	}
	public void setPercentage(Double percentage) {
		this.percentage = percentage;
	}




	@Override
	public boolean equals(Object label) {
		// TODO Auto-generated method stub

		if(((FinalLabel)label).getLabelId()==this.labelId)return true;
		else return false;
		
	}




	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
	
	
}
