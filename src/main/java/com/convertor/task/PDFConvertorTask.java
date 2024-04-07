package com.convertor.task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.convertor.exception.FileDataNotFoundException;
import com.convertor.model.TaskResult;
import com.convertor.util.FetchRowDetails;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

public class PDFConvertorTask implements Callable<TaskResult> {

	private File file;
	
	public PDFConvertorTask(File file) {
		this.file = file;
	}
	
	@Override
	public TaskResult call() throws FileDataNotFoundException {
		
		Map<String, Object> getSheetDetails = new HashMap<String, Object>();
		
		TaskResult taskResult = new TaskResult();
		taskResult.setTaskName("PDFTask");
        
		FetchRowDetails.getRowDetails(file, getSheetDetails);
		
		try {
			// Here I am using iTextPdf-5.5.9 API for creating document
			
			Document document = new Document(); // It create the Document Objec
			
			// With a give PDF file path It point to the pdf file and create the writer object.
//			File file = File.createTempFile("project_pdf", ".pdf");
			LocalDateTime currentDateTime = LocalDateTime.now();

	        // Format the current date/time as a string
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
	        String formattedDateTime = currentDateTime.format(formatter);
			
			String fileName = "PDF_"+formattedDateTime+".pdf";
			File file = new File(fileName);
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
			
			document.open(); 
			
			for(int i=1, page=1;i <= (int) getSheetDetails.get("rowCount");i++,page++) { // Here we are Iterating the no. of role in sheet.
			
				if(page>1) { // This logic is for write the next row details in new PDF page
					document.newPage();
				}
				
				Row row = ((Sheet)getSheetDetails.get("sheet")).getRow(i); // It get the i th number row from the sheet
				Image image = Image.getInstance("src/main/resources/logo.jpg"); // It Fetch the logo img from given path
				image.scaleToFit(100f, 100f);
				image.setAbsolutePosition(20, document.top()-100); // It gives position to Image
				
				document.add(image); // This add() of document is used to add Image in document.
				
				Paragraph headerText = new Paragraph(); // It create the Paragraph object 
				
				// Below Lines is for create the heading with specified font, size, style & alignment and 
				// added in headText paragraph
				Font headingFont = new Font(Font.FontFamily.TIMES_ROMAN,28, Font.BOLD);
				Paragraph heading = new Paragraph("ABC Company",headingFont);
				heading.setAlignment(Element.ALIGN_CENTER);
				headerText.add(heading);
				
				// Below Lines is for create the tagline with specified font, size, style & alignment and 
				// added in headText paragraph
				Font tagLineFont = FontFactory.getFont(FontFactory.TIMES_ROMAN,18,BaseColor.BLACK);
				Paragraph tagLine = new Paragraph("76 Johnson Ave, Long Island City, NY 11101",tagLineFont);
				tagLine.setAlignment(Element.ALIGN_CENTER);
				headerText.add(tagLine);
				
				// Below Lines is for create the contact details with specified font, size, style & alignment and 
				// added in headText paragraph
				Font contactDetailsFont = FontFactory.getFont(FontFactory.TIMES_ROMAN,14,BaseColor.BLACK);
				Paragraph contactDetails = new Paragraph("Phone: 201-927-0220, 718-415-2350 Email: abc@gmail.com",contactDetailsFont);
				contactDetails.setAlignment(Element.ALIGN_CENTER);
				headerText.add(contactDetails);
				
				document.add(headerText); // Whatever headerText contains added to document. 
				
				
				// Using chunk and lineseparator we can create the Line with width, percentage & color
				// and Added to document
				Paragraph verticalLine = new Paragraph(new Chunk(new LineSeparator(3, 100, BaseColor.BLUE, 0, 0)));
				document.add(verticalLine);
				
				document.add(new Paragraph("\n")); // Added Empty line
				
				Date currentTime = new Date();
				SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy");
				String formattedDate = dateFormat.format(currentTime);
				
				Paragraph bodyContent = new Paragraph(); // Created Bodyparagraph with specified font, size & style
				Font bodyFont = FontFactory.getFont(FontFactory.TIMES_ROMAN,12,BaseColor.BLACK);
				
				// Paragraph class constructor is overloaded. So that we can give Message with font
				bodyContent.add(new Paragraph(formattedDate, bodyFont));
				
				bodyContent.add(new Paragraph("\n"));
				
				// Below are the format of document added in bodycontent paragraph with same body font
				String nameAddress = "Dear "			
							+getCellValue(row, (int) getSheetDetails.get("name"))+",\n"
							+getCellValue(row, (int) getSheetDetails.get("address"))+",\n"
							+getCellValue(row, (int) getSheetDetails.get("city"))+"\n"
							+getCellValue(row, (int) getSheetDetails.get("state"))+" "
							+getCellValue(row, (int) getSheetDetails.get("postalCode"));
				
				
				
				bodyContent.add(new Paragraph(nameAddress, bodyFont));
				
				bodyContent.add(new Paragraph("\n"));
				
				String openingParagraph = "I am writing to express our deepest gratitude for your generous contribution and donations "
				+ "to ABC Your support has made a significant impact on our mission and has allowed us to continue our vital work. "
				+ "Your support, totaling an incredible $"+getCellValue(row, (int) getSheetDetails.get("total"))
				+", has left us both humbled and energized to continue our mission.\n\n";
				
				bodyContent.add(new Paragraph(openingParagraph, bodyFont));
				
				String middleParagraph = "Your donation has been allocated as follows:\n\n";
				middleParagraph = middleParagraph + "Amount : \t $"+getCellValue(row, (int) getSheetDetails.get("amount"))+"\n\n";
				middleParagraph = middleParagraph + "Building Fund : \t $"+getCellValue(row, (int) getSheetDetails.get("buildingFund"))+"\n\n";
				middleParagraph = middleParagraph + "Mission Fund : \t $"+getCellValue(row, (int) getSheetDetails.get("missionFund"))+"\n\n";
				middleParagraph = middleParagraph + "Special Fund : \t $"+getCellValue(row, (int) getSheetDetails.get("specialFund"))+"\n\n";
				middleParagraph = middleParagraph + "Total : \t $"+getCellValue(row, (int) getSheetDetails.get("total"))+"\n\n";
				
				bodyContent.add(new Paragraph(middleParagraph, bodyFont));
				
				String closingParagraph = "Once again, thank you for your belief in our mission and your commitment to making a difference."
				+ " Your support is pivotal in our journey, and we are honored to have you as a valued partner.\n\n"
				+ "If you have any questions or if there is anything else we can provide, please do not hesitate to contact us "
				+ "at 11111111.\n\n";
				
				bodyContent.add(new Paragraph(closingParagraph, bodyFont));
				
				String signature = "In Him,\n"+"John Doe\n"+"ABC\n";
				
				bodyContent.add(new Paragraph(signature, bodyFont));
				
				document.add(bodyContent);
				
			}
			
			document.close();
			
			byte[] newPdfFileBytes = convertFileToByteArray(file);
			
			Path resourcesPathPDFFile = Paths.get("src", "main", "resources", "files");
	        Files.createDirectories(resourcesPathPDFFile);
			
	        File destinationFile = resourcesPathPDFFile.resolve(file.getName()).toFile();
	        Files.move(file.toPath(), destinationFile.toPath());
//			Files.move(newFile.toPath(), resourcesPathPDFFile.resolve(file.getName()).toFile().toPath());
	        
			System.out.println("PDF Document created successfully.");
			taskResult.setTaskFileName(file.getName());
			taskResult.setTaskResult(newPdfFileBytes);
			
		} catch (Exception e) {
			System.out.println("PDF is not Created\n"+e.getMessage());
			throw new FileDataNotFoundException("Conv003", "Please Check the File Data");
		}
		
		return taskResult;
	}
	

	public static String getCellValue(Row row, int cellIndex) {
			
		String nameAddress = "";
		if(row.getCell(cellIndex).getCellTypeEnum() == CellType.BLANK) {
			nameAddress += "-";
		} else if(row.getCell(cellIndex).getCellTypeEnum() == CellType.STRING) {
			nameAddress += row.getCell(cellIndex).getStringCellValue();
		} else {
			nameAddress += (int)row.getCell(cellIndex).getNumericCellValue();
		}
		return nameAddress;
	}
	
	public byte[] convertFileToByteArray(File file) throws IOException {
        // Method 1: Using FileInputStream
		System.out.println(file.getAbsolutePath());
		
//		String absoluteFilePath = "classpath: src/main/resources/files/" + file.getName();
//		Resource resource = resourceLoader.getResource(absoluteFilePath);
        
        FileInputStream fis = new FileInputStream(file);
        byte[] fileBytes = new byte[(int) file.length()];
        fis.read(fileBytes);
        fis.close();

        // Method 2: Using Files.readAllBytes (Java 7+)
        //byte[] fileBytes = Files.readAllBytes(file.toPath());

        return fileBytes;
    }

}
