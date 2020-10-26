//Tsogkas Evangelos 3150185, Menychta Aikaterini 3150104

import java.util.ArrayList;
/* Class used for representing an object that links a lesson with the teacher that teaches it. **/
public class LessonTeacher {
	private Lesson lesson; //Reference to lesson
	private ArrayList<Teacher> teachers; //containes the teachers that can teach this lesson
	private int teacher; // index of 'teachers' to point which  teacher will teach the lesson
	
	/*Default constructor*/
	public LessonTeacher() {
		lesson= new Lesson();
		teachers=new ArrayList<Teacher>();
		teacher=0;
	}
	
	/*Parametrized constructor*/
	public LessonTeacher(Lesson lesson, ArrayList<Teacher> teachers, int teacher) {
		this.lesson=lesson;
		this.teachers=teachers;
		this.teacher=teacher;
	}
	
	public Lesson getLesson() {
		return lesson;
	}
	
	public ArrayList<Teacher> getTeachers() {
		return teachers;
	}
	
	public int getTeacher() {
		return teacher;
	}
	
	public void setTeacher(int teacher) {
		this.teacher=teacher;
	}
}

