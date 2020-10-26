//Tsogkas Evangelos 3150185, Menychta Aikaterini 3150104

public class Lesson {
	private int lid; //lesson id
	private String lname; //lesson name
	private String grade; //grade name
	private int thours; //teaching hours
	
	/*Default constructor*/
	public Lesson () {
		lid=0;
		String lname=" ";
		String grade=" ";
		thours=0;
	}
	
	/*Parametrized constructor*/
	public Lesson (int lid, String lname, String grade, int thours ) {
		this.lid=lid;
		this.lname=lname;
		this.grade=grade;
		this.thours=thours;
	}
	
    public int getLid() {
		return lid;
	}	
	
	public String getLname() {
		return lname;
	}
	
	public String getGrade() {
		return grade;
	}
	
	public int getThours() {
		return thours;
	}
	
	public String toString() {
	    return lid+"\n"+lname+"\n"+grade+"\n"+thours;
	}
}

