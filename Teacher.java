//Tsogkas Evangelos 3150185, Menychta Aikaterini 3150104

public class Teacher {
	private int tid; //teacher id
	private String tname; //teacher name
	private int[] lessons; //lessons teached by teacher
	private int maxhpd; //max teaching hours per day
	private int maxhpw; //max teaching hours per week
	
	/*Default constructor*/
	public Teacher() {
		tid=0;
		String tname=" ";
		lessons= new int[1];
		lessons[0]=0;
		maxhpd=0;
		maxhpw=0;
	}
	
	/*Parametrized constructor*/
	public Teacher(int tid, String tname, int[] lessons, int maxhpd, int maxhpw ) {
		this.tid=tid;
		this.tname=tname;
		this.lessons=lessons;
		this.maxhpd=maxhpd;
		this.maxhpw=maxhpw;
	}
	
    public int getTid() {
		return tid;
	}	
	
	public String getTname() {
		return tname;
	}
	
	public int[] getLessons() {
		return lessons;
	}
	
	public int getMaxhpd() {
		return maxhpd;
	}
	
	public int getMaxhpw() {
		return maxhpw;
	}
	
	public String toString() {
		String str;
		str=tid+"\n"+tname+"\n";
		for (int i=0; i<lessons.length; i++) {
			str+=lessons[i]+"\n";
		}
		str+=maxhpd+"\n"+maxhpw;
		
		return str;
	}
}

