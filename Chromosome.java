//Tsogkas Evangelos 3150185, Menychta Aikaterini 3150104

import java.util.ArrayList;
import java.util.Random;

public class Chromosome implements Comparable<Chromosome> {
	ArrayList<ArrayList<ArrayList<LessonTeacher []>>> genes; //contains the schedule
	Teacher[] teachers; // array that contains all teachers
	Lesson[] lessons; //array that contains all lessons
	private int fitness=0; //10 for strict restrictions(5) and 1 for flexible restrictions(3). Best 53.
	private boolean restriction1=false, restriction2=false, restriction3=false, restriction4=false, restriction5=false; //used in method printRestrictions() to check if respective restriction is satisfied
	public Chromosome(ArrayList<ArrayList<ArrayList<LessonTeacher []>>> genes, Teacher[] teachers, Lesson[] lessons) {
	    this.genes=genes;	
        this.teachers=teachers;	
		this.lessons=lessons;
        calculateFitness();		
	}
	
	/* Calculates the fitness score of the chromosome. */
	public void calculateFitness() {
		fitness=0;
		restriction1=false;
		restriction2=false;
		restriction3=false;
		restriction4=false;
		restriction5=false;
		int classroomA=genes.get(0).size();
		int classroomB=genes.get(1).size();
		int classroomC=genes.get(2).size();
		int classrooms=classroomA+classroomB+classroomC;
		int[][] dailyHours=new int[classrooms][5];//contains number of hours teached per day for each classroom (used for restriction 3)
		int sum=0; //number of hours teached per day per classroom
		int[][][] lessonHoursPd=new int[classrooms][lessons.length][5];//contains number of hours teached per day, per classroom for a specific lesson (used for restriction 4)
		
		//Checks restriction 1
		boolean res1false=false;//is true when restriction in not satisfied
		for (int n=0; n<3; n++) { //3 grade
			for (int k=0; k<genes.get(n).size(); k++) { //classrooms
				for (int i=0; i<5; i++) { //5 days
				    sum=0;
					for (int j=0; j<7; j++) { 
					
						if (genes.get(n).get(k).get(0) [7*i+j]!=null) { //for array 'dailyHours'
							sum++;
						}
						
					    if (genes.get(n).get(k).get(0) [7*i+j]!=null && j<6 && genes.get(n).get(k).get(0)[7*i+j+1]==null) {
						    for (int p=j+2; p<7; p++) {
								if (genes.get(n).get(k).get(0)[7*i+p]!=null) {								
									res1false=true;
									break;
								}
							}
							if (res1false) break;
					    }
					}
					
					dailyHours[k][i]=sum;
					
					if (res1false) break;
				}									
				if (res1false) break;
			}
			if (res1false) break;
		}
		if (!res1false) {
			fitness+=10;
			restriction1=true;
		}
		//Checks restriction 2
		//+ 3 sub-restrictions
        //2.1: a teacher cannot teach at the same time in a different classroom
		//2.2: a teacher must teach at most their maximum teaching hours per day
		//2.3: a teacher must teach at most their maximum teaching hours per week
		int tid;
		boolean res2false=false; //is true when restriction2 in not satisfied
		boolean res21false=false; //is true when 1st sub-restriction in not satisfied
		boolean res22false=false; //is true when 2nd sub-restriction in not satisfied
		boolean res23false=false; //is true when 3rd sub-restriction in not satisfied
		int[][] teaching=new int[5][7];	//For a teacher it contains 0 if they don't teach, 1 if they do
        int[] teachingHpw=new int[teachers.length]; // contains the number of teaching hours per week for every teacher	(used for restriction 5)
		for (int t=0; t<teachers.length; t++) {// teachers

            for (int d=0; d<5; d++) { //initialize teaching with zeros
				for (int h=0; h<7; h++) {
					teaching[d][h]=0;
				}
			}  
			
			res21false=false;
		    tid=teachers[t].getTid();
			for (int n=0; n<3; n++) { //3 grade
			    for (int k=0; k<genes.get(n).size(); k++) { //classrooms
				    for (int i=0; i<5; i++) { //5 days
					    for (int j=0; j<7; j++) {
							LessonTeacher lt=genes.get(n).get(k).get(0) [7*i+j];
							if (lt!=null && lt.getTeachers().get(lt.getTeacher()).getTid()==tid) {
								teaching[i][j]+=1;								
                                if (teaching[i][j]>1) { //a teacher cannot teach at the same time in a different classroom
									res21false=true;
								}								
							}
						}
					}
				}
			}
			
			//check array 'teaching' to see if the restricitons are satisfied
			res22false=false;
			res23false=false;
			int sumd=0;//number of teaching hours per day
			int sumw=0;//number of teaching hours per week
			for (int i=0; i<5; i++) { // a teacher must teach at most their maximum teaching hours per day and per week
				sumd=0;
			    for (int j=0; j<7; j++) {
				    sumd+=teaching[i][j];
				}
				if (sumd>teachers[t].getMaxhpd()) {
					res22false=true;
				}
				sumw+=sumd;					
			}
			teachingHpw[t]=sumw;
			if (sumw>teachers[t].getMaxhpw()) {
				res23false=true;
			}

			// restriction 2				
			for (int i=0; i<5; i++) {
				for (int j=0; j<5; j++) {
				    if ((teaching[i][j]>0) && (teaching[i][j+1]>0) && (teaching[i][j+2]>0)) {
						res2false=true;
						break;
					}
				}
				if (res2false) break;
			}
		}
		
        if (!res2false) fitness+=10;
		if (!res21false) fitness+=10;
		if (!res22false) fitness+=10;
		if (!res23false) fitness+=10;
		if (!res2false && !res21false && !res22false && !res23false) restriction2=true;
		//Flexible restrictions are cheched only if strict restrictions are satisfied
		if (!res1false && !res2false && !res1false && !res22false && !res23false) {
			//Restriction 3 (if maximum hours per day - minimum hours per day > 2 , then the restriction is not satisfied )
			int minHours;
			int maxHours;
			boolean stop=false; //true if restriction is not satisfied
			for (int i=0; i<classroomA; i++) {
				minHours=dailyHours[i][0];
			    maxHours=dailyHours[i][0];
				for (int j=0; j<5; j++) {
					if (dailyHours[i][j]<minHours) minHours=dailyHours[i][j];
				    if (dailyHours[i][j]>maxHours) maxHours=dailyHours[i][j];
				}
				if (maxHours-minHours>2) {
					stop=true;
					break;
				}
			}
			
			if (!stop) {
				for (int i=classroomA; i<classroomA+classroomB; i++) {
					minHours=dailyHours[i][0];
			        maxHours=dailyHours[i][0];
				    for (int j=0; j<5; j++) {
					    if (dailyHours[i][j]<minHours) minHours=dailyHours[i][j];
				        if (dailyHours[i][j]>maxHours) maxHours=dailyHours[i][j];
				    }
					if (maxHours-minHours>2) {
					    stop=true;
					    break;
				    }
			    }
				
				if (!stop) {
					for (int i=classroomA+classroomB; i<classrooms; i++) {
					    minHours=dailyHours[i][0];
			            maxHours=dailyHours[i][0];
				        for (int j=0; j<5; j++) {
					        if (dailyHours[i][j]<minHours) minHours=dailyHours[i][j];
				            if (dailyHours[i][j]>maxHours) maxHours=dailyHours[i][j];
				        }
						if (maxHours-minHours>2) {
					        stop=true;
					        break;
				        }
			        }
				}
			}
			
			if (!stop) {
				fitness+=1;
				restriction3=true;
			}
			
			//Restriction 4
			for (int i=0; i<classrooms; i++) {
				for (int j=0; j<lessons.length; j++) {
					for (int n=0; n<5; n++) {
						lessonHoursPd[i][j][n]=0;
					}
				}
			}

			for (int n=0; n<3; n++) { //3 grade
			    for (int k=0; k<genes.get(n).size()-1; k++) { //classrooms
				    for (int i=0; i<5; i++) { //5 days
					    for (int j=0; j<7; j++) {
							LessonTeacher lt=genes.get(n).get(k).get(0) [7*i+j];
							if (lt!=null) {
								for (int s=0; s<lessons.length; s++) {
									if(lt.getLesson().getLid()==lessons[s].getLid()) {
										lessonHoursPd[3*n+k][s][i]+=1;
									}
								}
							}
				        }
					}
				}
			}	
            
			stop=false; 
            for (int i=0; i<classrooms; i++) {
				for (int j=0; j<lessons.length; j++) {
					for (int n=0; n<5; n++) {
						if (lessonHoursPd[i][j][n]>1) { //a specific lesson can be teached for at most 1 hour a day
							stop=true;
							break;
						}
					}
					if (stop) break;
				}
				if (stop) break;
			}
 			
			if (!stop) {
				fitness+=1;
				restriction4=true;
			}
		
			//Restriciton 5 (if maximum hours per week - minimum hours per week > 17 , then the restriction is not satisfied )
			int minHpw=teachingHpw[0];
            int maxHpw=teachingHpw[0];	
            for (int i=1; i<teachingHpw.length; i++) {
				if (teachingHpw[i]<minHpw) minHpw=teachingHpw[i];
				if (teachingHpw[i]>maxHpw) maxHpw=teachingHpw[i];
			}
            if (maxHpw-minHpw<=17) {
				fitness+=1;		
				restriction5=true;
			}
		}
	}
	
	/* Randomly changes the teaching hours of the lessons with a given probability. */
	public void mutateHours(double mutationProbabilityHours) {
		Random r=new Random();
		Random mutate=new Random();
		LessonTeacher tempLt;
		int index;
		for (int n=0; n<3; n++) { //3 grade
			for (int k=0; k<genes.get(n).size(); k++) { //classrooms
				for (int i=0; i<5; i++) { //5 days
			        for (int j=0; j<7; j++) {
						LessonTeacher lt=genes.get(n).get(k).get(0) [7*i+j];
						if (lt!=null && mutate.nextDouble()<mutationProbabilityHours) {
						    index=r.nextInt(35);
						    if (genes.get(n).get(k).get(0) [index]==null) {
							    genes.get(n).get(k).get(0) [index]=lt;
							    genes.get(n).get(k).get(0) [7*i+j]=null;
						    }
						    else {
							    tempLt=genes.get(n).get(k).get(0) [index];
							    genes.get(n).get(k).get(0) [index]=lt;
							    genes.get(n).get(k).get(0) [7*i+j]=tempLt;
						    }
						}
					}
				}
			}
		}
	}
	
	/* Randomly chooses for a lesson the teacher that will teach it, with a given probability. */
	public void mutateTeachers(double mutationProbabilityTeachers) {
		Random r=new Random();
		Random mutate=new Random();
		LessonTeacher tempLt;
		int index;
		for (int n=0; n<3; n++) { //3 grade
			for (int k=0; k<genes.get(n).size(); k++) { //classrooms
				for (int i=0; i<5; i++) { //5 days
			        for (int j=0; j<7; j++) {
						LessonTeacher lt=genes.get(n).get(k).get(0) [7*i+j];
						if (lt!=null && mutate.nextDouble()<mutationProbabilityTeachers) {
							lt.setTeacher(r.nextInt(lt.getTeachers().size())); //random teacher
						}
					}
				}
			}
		}
	}
	
	public ArrayList<ArrayList<ArrayList<LessonTeacher []>>> getGenes() {
		return genes;
	}
	
	public int getFitness() {
		return fitness;
	}
	
	public void printRestrictions() {
		if (!restriction1) System.out.println("Restriction 1 is not satisfied");
		if (!restriction2) System.out.println("Restriction 2 is not satisfied");
		if (!restriction3) System.out.println("Restriction 3 is not satisfied");
		if (!restriction4) System.out.println("Restriction 4 is not satisfied");
		if (!restriction5) System.out.println("Restriction 5 is not satisfied");										
	}
	
	public void printSchedule(String file_name) {
		WriteExcelFile wf=new WriteExcelFile();
		wf.writeFile(teachers, genes, file_name);
	}
	
	@Override
    /* The compareTo function has been overriden so sorting can be done according to fitness scores. */
	public int compareTo(Chromosome x) {
		return fitness - x.fitness;
	}
}

