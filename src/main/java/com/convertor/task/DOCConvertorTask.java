package com.convertor.task;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
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

import javax.imageio.ImageIO;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.Borders;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFPicture;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import com.convertor.exception.FileDataNotFoundException;
import com.convertor.model.TaskResult;
import com.convertor.util.FetchRowDetails;


public class DOCConvertorTask implements Callable<TaskResult> {

	private File file;
	
	public DOCConvertorTask(File file) {
		this.file = file;
	}
	
	@Override
	public TaskResult call() throws FileDataNotFoundException {
		
		Map<String, Object> getSheetDetails = new HashMap<String, Object>();
		
		TaskResult taskResult = new TaskResult();
		taskResult.setTaskName("DOCTask");
        
		FetchRowDetails.getRowDetails(file, getSheetDetails);
		
		try (XWPFDocument document = new XWPFDocument()) {
		
			LocalDateTime currentDateTime = LocalDateTime.now();

	        // Format the current date/time as a string
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
	        String formattedDateTime = currentDateTime.format(formatter);
			
	        String fileName = "DOC_"+formattedDateTime+".doc";
	        File file = new File(fileName);
//			File file = File.createTempFile("project_pdf", ".docx");
			FileOutputStream out = new FileOutputStream(file);
			
			
			for(int i=1, page=1;i<= (int) getSheetDetails.get("rowCount");i++,page++) {
				
				if(page>1) {
					XWPFParagraph breakPage = document.createParagraph();
					breakPage.setPageBreak(true);
				}
				Row row = ((Sheet) getSheetDetails.get("sheet")).getRow(i);
				
//				InputStream imageStream  = DOCConvertorTask.class.getResourceAsStream("logo.jpg");
//				BufferedImage bufferedImage = ImageIO.read(imageStream);
				
//				javafx.scene.image.Image image = new javafx.scene.image.Image(imageStream);
				
//				BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
//				ByteArrayInputStream imageStream1 = new ByteArrayInputStream(getImageBytes(bufferedImage));
				
//					URI uri = Resources.class.getResource(ConstantString.LOGOFILEPATH).toURI();	
//					File imageFile = new File(uri);
//					FileInputStream fis = new FileInputStream(imageFile);
				File imageFile = new File("src/main/resources/logo.jpg");
				FileInputStream fis = new FileInputStream(imageFile);
				XWPFParagraph imageParagraph = document.createParagraph();
				imageParagraph.setAlignment(ParagraphAlignment.CENTER);
				XWPFRun imageRun = imageParagraph.createRun();
				XWPFPicture picture = imageRun.addPicture(fis, XWPFDocument.PICTURE_TYPE_JPEG, "image.jpg", Units.toEMU(60), Units.toEMU(60));
				imageRun.setText("ABC Company");
				imageRun.setFontSize(28);
				imageRun.setFontFamily("Times New Roman");
				imageRun.setBold(true);
				
				XWPFParagraph tagParagraph = document.createParagraph();
				tagParagraph.setAlignment(ParagraphAlignment.CENTER);
				XWPFRun tagRun = tagParagraph.createRun();
				tagRun.setText("76 Johnson Ave, Long Island City, NY 11101");
				tagRun.setFontSize(18);
				tagRun.setFontFamily("Times New Roman");
				
				XWPFParagraph contactDetailsParagraph = document.createParagraph();
				contactDetailsParagraph.setAlignment(ParagraphAlignment.CENTER);
				
				XWPFRun contactDetails = contactDetailsParagraph.createRun();
				contactDetails.setText("Phone: 201-927-0220, 718-415-2350 Email: abc@gmail.com");
				contactDetails.setFontSize(14);
				contactDetails.setFontFamily("Times New Roman");
				
				
				XWPFParagraph verticalLine = document.createParagraph();
				XWPFRun lineRun = verticalLine.createRun();
				
				verticalLine.setBorderTop(Borders.SINGLE);
				
				
				Date currentTime = new Date();
				SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy");
				String formattedDate = dateFormat.format(currentTime);
				
				XWPFParagraph nameAddrParagraph = document.createParagraph();
				XWPFRun nameAddrRun = nameAddrParagraph.createRun();
				
				nameAddrRun.setText(formattedDate);
				nameAddrRun.addBreak();
				nameAddrRun.addBreak();
				nameAddrRun.setText("Dear "+getCellValue(row, (int) getSheetDetails.get("name")));
				nameAddrRun.addBreak();
				nameAddrRun.setText(getCellValue(row, (int) getSheetDetails.get("address"))+",");
				nameAddrRun.addBreak();
				nameAddrRun.setText(getCellValue(row, (int) getSheetDetails.get("city")));
				nameAddrRun.addBreak();
				nameAddrRun.setText(getCellValue(row, (int) getSheetDetails.get("state"))+" "+getCellValue(row, (int) getSheetDetails.get("postalCode")));
				nameAddrRun.addBreak();
				nameAddrRun.setFontSize(12);
				nameAddrRun.setFontFamily("Times New Roman");
				
				String openingText = "I am writing to express our deepest gratitude for your generous contribution and donations "
				+ "to ABC Your support has made a significant impact on our mission and has allowed us to continue our vital work. "
				+ "Your support, totaling an incredible $"+getCellValue(row, (int) getSheetDetails.get("total"))
				+", has left us both humbled and energized to continue our mission.";
				
				XWPFParagraph openingParagraph = document.createParagraph();
				XWPFRun openingRun = openingParagraph.createRun();
				openingRun.setText(openingText);
				openingRun.addBreak();
				openingRun.setFontSize(12);
				openingRun.setFontFamily("Times New Roman");
				
				
				XWPFParagraph middleParagraph = document.createParagraph();
				XWPFRun middleRun = middleParagraph.createRun();
				middleRun.setText("Your donation has been allocated as follows:");
				middleRun.addBreak();
				middleRun.setText("Amount :");
				middleRun.addTab();
				middleRun.addTab();
				middleRun.setText("$"+getCellValue(row, (int) getSheetDetails.get("amount")));
				middleRun.addBreak();
				middleRun.setText("Building Fund :");
				middleRun.addTab();
				middleRun.setText("$"+getCellValue(row, (int) getSheetDetails.get("buildingFund")));
				middleRun.addBreak();
				middleRun.setText("Mission Fund :");
				middleRun.addTab();
				middleRun.setText("$"+getCellValue(row, (int) getSheetDetails.get("missionFund")));
				middleRun.addBreak();
				middleRun.setText("Special Fund :");
				middleRun.addTab();
				middleRun.addTab();
				middleRun.setText("$"+getCellValue(row, (int) getSheetDetails.get("specialFund")));
				middleRun.addBreak();
				middleRun.setText("Total :");
				middleRun.addTab();
				middleRun.addTab();
				middleRun.addTab();
				middleRun.setText("$"+getCellValue(row, (int) getSheetDetails.get("total")));
				middleRun.addBreak();
				middleRun.setFontSize(12);
				middleRun.setFontFamily("Times New Roman");
				
				XWPFParagraph closingParagraph = document.createParagraph();
				XWPFRun closingRun = closingParagraph.createRun();
				closingRun.setText("Once again, thank you for your belief in our mission and your commitment to making a difference. "
				+ "Your support is pivotal in our journey, and we are honored to have you as a valued partner.");
				closingRun.addBreak();
				closingRun.setText("If you have any questions or if there is anything else we can provide, please do not hesitate to contact us at 11111111.");
				closingRun.addBreak();
				closingRun.addBreak();
				closingRun.setFontSize(12);
				closingRun.setFontFamily("Times New Roman");
				
				XWPFParagraph signatureParagraph = document.createParagraph();
				XWPFRun signatureRun = closingParagraph.createRun();
				signatureRun.setText("In Him,");
				signatureRun.addBreak();
				signatureRun.setText("John Doe");
				signatureRun.addBreak();
				signatureRun.setText("ABC");
				signatureRun.setFontSize(12);
				signatureRun.setFontFamily("Times New Roman");
				
			}
			document.write(out);
			
			byte[] newDocFileBytes = convertFileToByteArray(file);
			
			Path resourcesPathDOCFile = Paths.get("src", "main", "resources", "files");
	        Files.createDirectories(resourcesPathDOCFile);
			
	        File destinationFile = resourcesPathDOCFile.resolve(file.getName()).toFile();
	        Files.move(file.toPath(), destinationFile.toPath());
			
			System.out.println("Word Document Created Successfully");
			taskResult.setTaskFileName(file.getName());
			taskResult.setTaskResult(newDocFileBytes);
			
		} catch (Exception e) {
			System.out.println("Word Document is not Created \n"+e.getMessage());
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
	
	private  static byte[] getImageBytes(BufferedImage bufferedImage) throws IOException {
        // Convert BufferedImage to byte array
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ImageIO.write(bufferedImage, "png", outputStream);
            return outputStream.toByteArray();
        }
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
