//Tsogkas Evangelos 3150185, Menychta Aikaterini 3150104

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.*;
import java.io.File;
import java.util.*;
import java.util.concurrent.TimeUnit;
public class CreateSchedule {
	
	public static void main(String[] args) {
		Lesson[] lessons=new Lesson[1]; // array that contains all lessons
		Teacher[] teachers=new Teacher[1]; // array that contains all teachers
		LessonTeacher[] lesson_teachers=new LessonTeacher[1]; //array that contains LessonTeacher objects
		int A=0, B=0, C=0;
		
		//Read lessons
		try {

	        File fXmlFile = new File("Data/lessons.xml");
	        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	        Document doc=dBuilder.parse(fXmlFile);

	        doc.getDocumentElement().normalize();

	        NodeList list=doc.getElementsByTagName("lesson");

	        int lid;
            String lname;
	        String grade;
	        int thours;
	        Lesson l;
			lessons=new Lesson[list.getLength()];
	        for (int i=0; i<list.getLength(); i++) {
                Node n=list.item(i);

		        if (n.getNodeType()==Node.ELEMENT_NODE) {
                    Element eElement=(Element) n;
					
			        lid=Integer.parseInt(eElement.getElementsByTagName("lesson_id").item(0).getTextContent());
			        lname=eElement.getElementsByTagName("lesson_name").item(0).getTextContent();
			        grade=eElement.getElementsByTagName("grade").item(0).getTextContent();
			        thours=Integer.parseInt(eElement.getElementsByTagName("teaching_hours").item(0).getTextContent());
				    l=new Lesson(lid, lname, grade, thours);
					lessons[i]=l;
		        }
			
	        }
        } 
	    catch (Exception e) {
	        e.printStackTrace();
        }
		
		//Read teachers
		try {

	        File fXmlFile=new File("Data/teachers.xml");
	        DocumentBuilderFactory dbFactory=DocumentBuilderFactory.newInstance();
	        DocumentBuilder dBuilder=dbFactory.newDocumentBuilder();
	        Document doc=dBuilder.parse(fXmlFile);

	        doc.getDocumentElement().normalize();

	        NodeList list1 = doc.getElementsByTagName("teacher");

	        int tid;
            String tname;
            int[] teached_lessons;
			int maxhpd;
			int maxhpw;
	        Teacher t;
			teachers=new Teacher[list1.getLength()];
	        for (int i=0; i<list1.getLength(); i++) {				
                Node n1=list1.item(i);

		        if (n1.getNodeType()==Node.ELEMENT_NODE) {
                    Element eElement1=(Element) n1;
					
			        tid=Integer.parseInt(eElement1.getElementsByTagName("teacher_id").item(0).getTextContent());
			        tname=eElement1.getElementsByTagName("teacher_name").item(0).getTextContent();
					
					NodeList list2=eElement1.getElementsByTagName("lesson");
					teached_lessons=new int[list2.getLength()];
					for (int j=0; j<list2.getLength(); j++) {
						teached_lessons[j]=Integer.parseInt(list2.item(j).getTextContent());																		
					}
				    
			        maxhpd=Integer.parseInt(eElement1.getElementsByTagName("max_daily_teaching_hours").item(0).getTextContent());
					maxhpw=Integer.parseInt(eElement1.getElementsByTagName("max_weekly_teaching_hours").item(0).getTextContent());
					
					t=new Teacher(tid, tname, teached_lessons, maxhpd, maxhpw);
					teachers[i]=t;
		        }
			
	        }
        } 
	    catch (Exception e) {
	        e.printStackTrace();
        }
		
		//Read grades (number of classrooms for each grade)
		try {

	        File fXmlFile = new File("Data/grades.xml");
	        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	        Document doc=dBuilder.parse(fXmlFile);

	        doc.getDocumentElement().normalize();

	        NodeList list=doc.getElementsByTagName("grade");

	        for (int i=0; i<list.getLength(); i++) {
                Node n=list.item(i);

		        if (n.getNodeType()==Node.ELEMENT_NODE) {
                    Element eElement=(Element) n;
					String name=eElement.getElementsByTagName("name").item(0).getTextContent();
					if (name.equals("A")) A=Integer.parseInt(eElement.getElementsByTagName("classrooms").item(0).getTextContent());
			        else if (name.equals("B")) B=Integer.parseInt(eElement.getElementsByTagName("classrooms").item(0).getTextContent());
                    else C=Integer.parseInt(eElement.getElementsByTagName("classrooms").item(0).getTextContent());                  
		        }
			
	        }
        } 
	    catch (Exception e) {
	        e.printStackTrace();
        }
		
		//Initialise lesson_teachers
        LessonTeacher lt;
		int[] tempLessons; //lessons that a teacher can teach
		lesson_teachers=new LessonTeacher[lessons.length];
		ArrayList<Teacher> lessonTeachers;//containes the teachers that can teach this lesson
		Random r=new Random();// index for lessonTeachers. select random teacher from possible teachers to teach the lesson
		for (int i=0; i<lessons.length; i++) {
			lessonTeachers=new ArrayList<Teacher>();
			for (int j=0; j<teachers.length; j++) {
				tempLessons=teachers[j].getLessons();
				for (int n=0; n<tempLessons.length; n++) {
					if (lessons[i].getLid()==tempLessons[n]) {
                        lessonTeachers.add(teachers[j]);
					}
				}
			}
            int index=r.nextInt(lessonTeachers.size());			
			lt=new LessonTeacher(lessons[i], lessonTeachers, index);
			lesson_teachers[i]=lt;
		}
		
		Genetic g = new Genetic(lesson_teachers, lessons, teachers, A, B, C);
		long start = System.currentTimeMillis();
		Chromosome x = g.geneticAlgorithm(6, 0.05, 0.01, 53, 100000);
		long end = System.currentTimeMillis();
		System.out.println("Time: "+TimeUnit.MILLISECONDS.toMinutes(end-start));
		x.printSchedule("Schedule/schedule.xlsx");
		System.out.println("Fitness: "+x.getFitness());
		x.printRestrictions();
	}
}

