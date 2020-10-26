//Tsogkas Evangelos 3150185, Menychta Aikaterini 3150104

import java.util.ArrayList;
import java.io.FileOutputStream;
import java.io.IOException; 
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.*;

public class WriteExcelFile {
	
	public void writeFile(Teacher[] teachers, ArrayList<ArrayList<ArrayList<LessonTeacher []>>> genes, String file_name) {
		
       	Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet();		
				
		Row row1=sheet.createRow(0);
		Cell cellT=row1.createCell(0);
		cellT.setCellValue("DIDASKONTES");
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));

		//write days
		int fromCol=2; //column to start merge
		int toCol=15; //column to end merge
		for (int i=2; i<=6; i++) { //5 days
			Cell cellDay=row1.createCell(fromCol);			
			if (i==2) cellDay.setCellValue("DEYTERA");
			else if (i==3) cellDay.setCellValue("TRITH");
			else if (i==4) cellDay.setCellValue("TETARTH");
			else if (i==5) cellDay.setCellValue("PEMPTH");
			else if (i==6) cellDay.setCellValue("PARASKEYH");
			sheet.addMergedRegion(new CellRangeAddress(0, 0, fromCol, toCol));
			fromCol=toCol+1;
			toCol=fromCol+13;
		}
		//write hours
		Row row2=sheet.createRow(1);
		fromCol=0;
		toCol=1;
		for (int i=0; i<=70; i+=2) {
			Cell cellHour=row2.createCell(fromCol);
			sheet.addMergedRegion(new CellRangeAddress(1, 1, fromCol, toCol));
			fromCol=toCol+1;
			toCol=fromCol+1;
		}
		int hour=1;
		Cell cell;
		for (int i=2; i<=70; i+=2) {
		    cell=row2.getCell(i);
			if (hour>7) hour=1;
            cell.setCellValue(String.valueOf(hour));
            hour++;			
		}
		
		//creates all rows and cells, merges pairs of cells and writes data
		for (int i=2; i<teachers.length+2; i++) {
			Row row=sheet.createRow(i);
			fromCol=0;
			toCol=1;
			for (int j=0; j<=70; j+=2) {
			    cell=row.createCell(fromCol);
			    sheet.addMergedRegion(new CellRangeAddress(i, i, fromCol, toCol));
			    fromCol=toCol+1;
			    toCol=fromCol+1;
			}
			cell=row.getCell(0);
			cell.setCellValue(teachers[i-2].getTname());
			
			int tid=teachers[i-2].getTid();
			String classroom="";
			int excelDay;
			int excelHour;
			for (int n=0; n<3; n++) { //3 grade
			    for (int k=0; k<genes.get(n).size(); k++) { //classrooms
				    for (int l=0; l<5; l++) { //5 days
					    for (int j=0; j<7; j++) {
							LessonTeacher lt=genes.get(n).get(k).get(0) [7*l+j];						
							if (lt!=null && lt.getTeachers().get(lt.getTeacher()).getTid()==tid) {
								if (n==0) classroom="A";
								else if (n==1) classroom="B";
								else if (n==2) classroom="C";
								classroom+=String.valueOf(k+1);
								excelDay=14*l+2;
								excelHour=excelDay+2*j;
								//System.out.println("Row: "+i+" Cell: "+excelHour+" "+classroom+" "+lt.getLesson().getLname()+" Day: "+l+" Hour: "+j);//DEBUGGING
								cell=row.getCell(excelHour);
			                    cell.setCellValue(classroom+" / "+lt.getLesson().getLname());
							}
						}
					}
				}
			}
		}
		
		try (FileOutputStream outputStream = new FileOutputStream(file_name)) {
            workbook.write(outputStream);
        }
		catch (IOException e) {
            e.printStackTrace();
        }
	}
}

