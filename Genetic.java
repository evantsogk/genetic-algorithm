//Tsogkas Evangelos 3150185, Menychta Aikaterini 3150104

import java.util.*;

public class Genetic {
	private ArrayList<Chromosome> population; //ArrayList that contains the current population of chromosomes
	private LessonTeacher[] lesson_teachers; //Array that contains LessonTeacher objects
	private Lesson[] lessons; // array that contains all lessons
	private Teacher[] teachers; // array that contains all teachers
	private int A, B, C; //number of classrooms for each grade
	
	public Genetic(LessonTeacher[] lesson_teachers, Lesson[] lessons, Teacher[] teachers, int A, int B, int C) {
		this.population = null;
		this.lesson_teachers=lesson_teachers;
		this.lessons=lessons;
		this.teachers=teachers;
		this.A=A;
		this.B=B;
		this.C=C;
	}	
	
	/* 
     * populationSize: The size of the population in every step
     * mutationPropabilityHours: The probablility with which every teaching hour might change
	 * mutationPropabilityTeachers: The probablility with which another teacher will be chosen to teach a lesson
     * minimumFitness: The minimum fitness value of the solution we wish to find
     * maximumSteps: The maximum number of steps we will search for a solution
     */
	public Chromosome geneticAlgorithm(int populationSize, double mutationProbabilityHours, double mutationProbabilityTeachers, int minimumFitness, int maximumSteps) {
        
		initializePopulation(populationSize);//We initialize the population
		Chromosome[] childs; //contains childs of reproduction
		Collections.sort(population); // Sorting population according to fitness
		Random r = new Random();
		
		for(int step=0; step < maximumSteps; step++) {
            //Initialize the new generated population			
			ArrayList<Chromosome> newPopulation = new ArrayList<Chromosome>();
			for(int i=populationSize-1; i>1; i-=2) {
				childs=reproduce(population.get(i), population.get(i-1));
			    childs[0].mutateHours(mutationProbabilityHours);
				childs[0].mutateTeachers(mutationProbabilityTeachers);
				childs[1].mutateHours(mutationProbabilityHours);
				childs[1].mutateTeachers(mutationProbabilityTeachers);
				
                //adds childs to the new population
				newPopulation.add(childs[0]);
				newPopulation.add(childs[1]);
			}
			if (populationSize%2==1) {
				newPopulation.remove(populationSize-1);
			}
			//keeps the 2 best chromosomes of the old population
			newPopulation.add(population.get(populationSize-1));
			newPopulation.add(population.get(populationSize-2));
			
			population = new ArrayList<Chromosome>(newPopulation);
			for (int i=0; i<population.size(); i++) {
				population.get(i).calculateFitness();
			}
			Collections.sort(population);
			if(population.get(populationSize-1).getFitness() >= minimumFitness) {
				System.out.println("Steps: " + step);
				return population.get(populationSize-1);
			}
		}
		System.out.println("Reached max steps.");
		return population.get(populationSize-1);
	}
	
	//Initialize population
	public void initializePopulation(int populationSize) {
		this.population = new ArrayList<Chromosome>();
		ArrayList<ArrayList<ArrayList<LessonTeacher []>>> genes; //contains the schedule
		for(int n=0; n<populationSize; n++) {
			genes=new ArrayList<ArrayList<ArrayList<LessonTeacher []>>>();
			
			//add arraylists to genes
			int i, j;
		    for (i=0; i <3; i++) {
			    genes.add(new ArrayList<ArrayList <LessonTeacher []>>());
		    }
    	    for (i=0; i < 3; i++) {
			    if (i==0) {
			        for (j=0; j<A; j++) {
			            genes.get(i).add(new ArrayList<LessonTeacher []>());	
				    }
			    }
			    else if (i==1) {
				    for (j=0; j<B; j++) {
					    genes.get(i).add(new ArrayList<LessonTeacher []>());
				    }
			    }
			    else if (i==2) {
				    for (j=0; j<C; j++) {
					    genes.get(i).add(new ArrayList <LessonTeacher[]>());
				    }
			    }
		    }
		
		    for (i=0; i<3; i++) {
			    for (j=0; j<genes.get(i).size(); j++) {
				    genes.get(i).get(j).add(new LessonTeacher[35]);
			    }
		    }
			
			//initialize schedule
			Random r=new Random();
			LessonTeacher lt=new LessonTeacher();
			int random;
			for (i=0; i<lessons.length; i++) {
				int hours=lessons[i].getThours(); //teaching hours
				for (int p=0; p<lesson_teachers.length; p++) {
					if (lesson_teachers[p].getLesson().getLid()==lessons[i].getLid()) {
						lt=lesson_teachers[p];
						break;
					}
				}
				if (lessons[i].getGrade().equals("A")) {
					for (j=0; j<A; j++) {
						for (int k=0; k<hours; k++) {
							while (true) {
							    random=r.nextInt(35);							
							    if (genes.get(0).get(j).get(0)[random]==null) {
									genes.get(0).get(j).get(0)[random]=lt;
									break;
								}
						    }
						}
					}
				}
				else if (lessons[i].getGrade().equals("B")) {
					for (j=0; j<B; j++) {
						for (int k=0; k<hours; k++) {
							while (true) {
							    random=r.nextInt(35);							
							    if (genes.get(1).get(j).get(0)[random]==null) {
									genes.get(1).get(j).get(0)[random]=lt;
									break;
								}
						    }
						}
					}
				}
				else if (lessons[i].getGrade().equals("C")) {
					for (j=0; j<C; j++) {
						for (int k=0; k<hours; k++) {
							while (true) {
							    random=r.nextInt(35);							
							    if (genes.get(2).get(j).get(0)[random]==null) {
									genes.get(2).get(j).get(0)[random]=lt;
									break;
								}
						    }
						}
					}
				}
			}			
			
			this.population.add(new Chromosome(genes, teachers, lessons));
		}
	}
	
	//"Reproduces" two chromosomes and generates their "child"
	public Chromosome[] reproduce(Chromosome x, Chromosome y) {
		Chromosome child1, child2;
        Chromosome[] childs=new Chromosome[2]; //childs
		Random r=new Random();
		int random=r.nextInt(2);
		ArrayList<ArrayList<ArrayList<LessonTeacher []>>> genes1=new ArrayList<ArrayList<ArrayList<LessonTeacher []>>>();
		ArrayList<ArrayList<ArrayList<LessonTeacher []>>> genes2=new ArrayList<ArrayList<ArrayList<LessonTeacher []>>>();
		if (random==0) {
			//child1=A of x and B,C of y
			genes1.add(x.getGenes().get(0));
			genes1.add(y.getGenes().get(1));
			genes1.add(y.getGenes().get(2));
			//child2=A of y and B, C of x
			genes2.add(y.getGenes().get(0));
			genes2.add(x.getGenes().get(1));
			genes2.add(x.getGenes().get(2));		
		}
		else {
			//child1=A,B of x and C of y
			genes1.add(x.getGenes().get(0));
			genes1.add(x.getGenes().get(1));
			genes1.add(y.getGenes().get(2));
			//child2=A,B of y and C of x
			genes2.add(y.getGenes().get(0));
			genes2.add(y.getGenes().get(1));
			genes2.add(x.getGenes().get(2));
		}		
        child1=new Chromosome(genes1, teachers, lessons);	
        child2=new Chromosome(genes2, teachers, lessons);		
		childs[0]=child1;
		childs[1]=child2;	
		
		return childs;
	}
}

