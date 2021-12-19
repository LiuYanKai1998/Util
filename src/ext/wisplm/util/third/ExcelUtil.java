package ext.wisplm.util.third;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.drools.core.util.StringUtils;

/**
 * 通用Excel报表工具类
 * 
 * @author ZhongBinpeng Apr 26, 2012 10:38:55 AM
 *
 */
@SuppressWarnings("unchecked")
public class ExcelUtil {
	
	public static void main(String args[]) throws Exception{
/*		HSSFWorkbook workbook = ExcelUtil.getWorkbook(new File("C:\\Users\\china\\Desktop\\test.xls"));
		HSSFSheet sheet = workbook.getSheetAt(0);
		//型号 1,1
		//件号 1,3
		//版本1,6
		//上级装配件号1,10
		//所属模块,1,13
		//设计,1,21	
		HSSFRow row	    = sheet.createRow(1);
		HSSFCellStyle cellStyle = ExcelUtil.CreateHSSFCellStyle(workbook, false);
		
		for(int i = 0 ; i < 10 ; i ++){
			HSSFCell cell = row.createCell(i);
			cell.setCellStyle(cellStyle);
		}
		cellStyle.setAlignment(HorizontalAlignment.LEFT);
		//第二行合并显示搜索条件
		ExcelUtil.mergeCell(workbook.getSheetAt(0), 1, 1, 0, 10 - 1);
		HSSFCell cell = ExcelUtil.setCellValue(workbook.getSheetAt(0),1,0, "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\r\nddddddddddd",null,true);
		cell.setCellStyle(cellStyle);
		outputExcel(workbook,"C:\\Users\\china\\Desktop\\test.xls");*/
		//参数1:报表表土
		//参数2:列标题数组
		String columnTitles[] = {"层级","部件编号","部件名称"};
		//参数3:列标题行索引输出到第二行,索引为1
		int columnIndex = 1;
		//参数3:内容集合,一行为一个String[]
		List<String[]> content = new ArrayList<String[]>();
		//参数4:contentStartIndex:内容的行索引,第三行索引为2
		//参数5:excel的输出路径(codebase/temp或windchill/temp),excel格式为xls
		
		ExcelUtil.createExcel("EBOM清册",columnTitles,columnIndex, content, 2, "D:\\test.xls");
	}
	
	//列宽
	private static final short COLUMN_WIDTH = 3000;
	//行高
	private static final short ROW_HEIGHT = 300;
	//边框样式(上下左右)
	//private static final short BORDER_STYLE = HSSFCellStyle.BORDER_THIN; //黑色细线
	//标题及报表头字体样式(粗)
	//private static final short TITLE_FONT_BOLD_WEIGHT = HSSFFont.BOLDWEIGHT_BOLD;
	//报表正文字体样式(细/普通)
	//private static final short CONTENT_FONT_BOLD_WEIGHT = HSSFFont.BOLDWEIGHT_NORMAL;
	//报表使用的字体
	private static final String FONT_NAME = "宋体";
	//标题字体大小
	private static final short FONT_SIZE_TITLE = 14;
	//正文字体大小
	private static final short FONT_SIZE_CONTENT = 11;
	//内容显示不下时是否自动换行
	private static final boolean WARP_TEXT = true;
	
	public static File createExcel_HeadLeft(String reportHead,String[] titles,List<String[]> content, String outPutfilePath) throws Exception {
		HSSFWorkbook wb = createHSSFWorkbook(reportHead,titles,content);
		if (outPutfilePath != null && !"".equals(outPutfilePath)) {
			outputExcel(wb,outPutfilePath);
		}
		File file = new File(outPutfilePath);
		if(!file.exists()){
			throw new Exception("未成功生成Excel到指定路径:" + outPutfilePath);
		}
		return file;
	}
	
	/**
	 * 创建一个excel
	 * @param reportHead 	大标题,如果为空则不设置,如果有值,设置在第一行
	 * @param columnTitles 	列标题
	 * @param columnTitlesIndex 标题行索引
	 * @param content 内容集
	 * @param contentStartIndex 内容起始行索引
	 * @param outPutfilePath    文件输出位置
	 * @return
	 * @throws Exception
	 */
	public static File createExcel(String reportHead,String[] columnTitles,int columnTitlesIndex,List<String[]> content,int contentStartIndex, String outPutfilePath) throws Exception {
		HSSFWorkbook wb = createHSSFWorkbook(reportHead,columnTitles,columnTitlesIndex,content,contentStartIndex);
		if (outPutfilePath != null && !"".equals(outPutfilePath)) {
			outputExcel(wb,outPutfilePath);
		}
		File file = new File(outPutfilePath);
		if(!file.exists()){
			throw new Exception("未成功生成Excel到指定路径:" + outPutfilePath);
		}
		return file;
	}
	
	
	public static HSSFWorkbook createHSSFWorkbook(String reportHead,String[] titles,List<String[]> content){
		// 计算报表列数
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet();
		
		String doc_number = "";
		String part_number = "";
		String version = "";
		String change_type = "";
		int number = titles.length;
		
		// 给工作表列定义列宽(实际应用自己更改列数)
		for (int i = 0; i < number; i++) {
			sheet.setColumnWidth((short) i,COLUMN_WIDTH);
		}
		// 创建报表头标题
		createHead(wb,sheet,reportHead, number);
		
		// 设置报表列标题,第二行
		HSSFCellStyle titleCellStyle = CreateHSSFCellStyle(wb,true);
		HSSFRow titleRow = sheet.createRow(1);
		//HSSFCell cellsequenceTitle = titleRow.createCell((short) 0);
		//cellsequenceTitle.setCellStyle(titleCellStyle);
		//cellsequenceTitle.setCellValue(new HSSFRichTextString("序号"));
		for (int i = 0; i < titles.length; i++) {
			HSSFCell cell = titleRow.createCell((short) i);
			cell.setCellStyle(titleCellStyle);
			cell.setCellValue(new HSSFRichTextString(titles[i]));
		}
		
		//设置正文内容
		HSSFCellStyle contentCellStyle = CreateHSSFCellStyle(wb,false);
		
		//设置正文居左对其contentCellStyle.setAlignment((short) Alignment.ALIGN_LEFT);
		
		for (int i = 0; i < content.size(); i++) {
			
			HSSFRow contentRow = sheet.createRow(i+2); //第三行开始正文
			contentRow.setHeight(ROW_HEIGHT);
			HSSFCell cellsequence = contentRow.createCell((short) 0);
			cellsequence.setCellStyle(contentCellStyle);
			String[] cont = content.get(i); //获取一行内容
			for (int j = 0; j < cont.length; j++) {
				HSSFCell rowCell = contentRow.createCell((short)j); //创建单行单列
				rowCell.setCellStyle(contentCellStyle);
				rowCell.setCellValue(new HSSFRichTextString(cont[j]) == null ? "": cont[j]);
			}
		}
		
		return wb;
	}
	
	public static HSSFWorkbook createHSSFWorkbook(String headerContent,String[] columnTitles,int columnTitlesIndex,List<String[]> content,int contentStartIndex){
		// 计算报表列数
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet();
		
		int megerLength = columnTitles.length;
		
		// 给工作表列定义列宽(实际应用自己更改列数)
		for (int i = 0; i < megerLength; i++) {
			sheet.setColumnWidth((short) i,COLUMN_WIDTH);
		}
		// 创建报表头标题
		if(headerContent != null && !"".equals(headerContent)){
			createHead(wb,sheet,headerContent, megerLength);
		}		
		// 设置报表列标题,第二行
		HSSFCellStyle titleCellStyle = CreateHSSFCellStyle(wb,true);
		HSSFRow titleRow = sheet.createRow(columnTitlesIndex);
		for (int i = 0; i < columnTitles.length; i++) {
			HSSFCell cell = titleRow.createCell((short) i);
			cell.setCellStyle(titleCellStyle);
			cell.setCellValue(new HSSFRichTextString(columnTitles[i]));
		}
		
		//设置正文内容
		HSSFCellStyle contentCellStyle = CreateHSSFCellStyle(wb,false);
		
		//设置正文居左对其contentCellStyle.setAlignment((short) Alignment.ALIGN_LEFT);
		for (int i = 0; i < content.size(); i++) {
			
			HSSFRow contentRow = sheet.createRow(i+contentStartIndex); //第三行开始正文
			contentRow.setHeight(ROW_HEIGHT);
			HSSFCell cellsequence = contentRow.createCell((short) 0);
			cellsequence.setCellStyle(contentCellStyle);
			String[] cont = content.get(i); //获取一行内容
			for (int j = 0; j < cont.length; j++) {
				HSSFCell rowCell = contentRow.createCell((short)j); //创建单行单列
				rowCell.setCellStyle(contentCellStyle);
				rowCell.setCellValue(new HSSFRichTextString(cont[j]) == null ? "": cont[j]);
			}
		}
		
		return wb;
	}
	
	/**
	 * 创建一个单元格
	 * @param contentRow
	 * @param cellIndex
	 * @param content
	 * @param cellStyle
	 */
	public static void createCell(HSSFRow contentRow, int cellIndex,String content,HSSFCellStyle cellStyle){
		HSSFCell cell = contentRow.getCell(cellIndex);
		if(cell == null){
			cell = contentRow.createCell(cellIndex);
		}
		if(cellStyle != null){
			cell.setCellStyle(cellStyle);
		}
		cell.setCellValue(content);
	}
	
	
	public static HSSFCellStyle CreateHSSFCellStyle(HSSFWorkbook wb,boolean isHead){
		HSSFCellStyle cellStyle = wb.createCellStyle();
		// 设置内容单元格字体
		HSSFFont fontContent = wb.createFont();
		fontContent.setFontName(FONT_NAME);
		if(isHead){
			//fontContent.setBoldweight(TITLE_FONT_BOLD_WEIGHT);
		    fontContent.setBold(true);
		    //fontContent.setFontHeight(TITLE_FONT_HEIGHT);
		    fontContent.setFontHeightInPoints(FONT_SIZE_TITLE);
		}else{
			//fontContent.setBoldweight(CONTENT_FONT_BOLD_WEIGHT);
		    fontContent.setBold(false);
			fontContent.setFontHeightInPoints(FONT_SIZE_CONTENT);
		}
		cellStyle.setFont(fontContent);
		//自动换行
		cellStyle.setWrapText(WARP_TEXT);
		setCellStyle(cellStyle);
		return cellStyle;
	}
	
	private static void setCellStyle(HSSFCellStyle cellStyleContent){
		// 单元格居中对齐
		cellStyleContent.setAlignment(HorizontalAlignment.CENTER);
		// 单元格垂直居中对齐
		cellStyleContent.setVerticalAlignment(VerticalAlignment.CENTER);
		// 指定单元格内容显示不下时自动换行
		cellStyleContent.setWrapText(true);
		//设置边框为黑色
		cellStyleContent.setBorderBottom(BorderStyle.THIN); //HSSFCellStyle.BORDER_DASHED-虚线
		cellStyleContent.setBorderLeft(BorderStyle.THIN);
		cellStyleContent.setBorderRight(BorderStyle.THIN);
		cellStyleContent.setBorderTop(BorderStyle.THIN);
	}
	
	
	/**
	 * 创建EXCEL头部
	 * @param head 头部文字内容
	 * @param unitColumnCount 要合并的列数(从第一列开始算起)
	 * Apr 25, 2012 12:11:09 PM
	 */
	private static void createHead(HSSFWorkbook wb,HSSFSheet sheet,String head,int uniteColumnCount) {
		//创建第一行
		HSSFRow row = sheet.createRow(0);
		row.setHeight((short) 400);
		// 指定合并区域
		//int firstRow, int lastRow, int firstCol, int lastCol
		
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, uniteColumnCount - 1));
		//创建第一行第一列和最后一列
		HSSFCell cellFirst = row.createCell((short)0);
		HSSFCell cellEnd = row.createCell((short)(uniteColumnCount - 1));
		
		// 定义单元格为字符串类型
		cellFirst.setCellType(HSSFCell.ENCODING_UTF_16);
		cellFirst.setCellValue(new HSSFRichTextString(head));
		
		HSSFCellStyle css = CreateHSSFCellStyle(wb,true);
		css.setAlignment(HorizontalAlignment.CENTER);
		css.setVerticalAlignment(VerticalAlignment.CENTER);
		cellFirst.setCellStyle(css);
		cellEnd.setCellStyle(css);
		
	}
	
	
	/**
	 * 合并单元格
	 * @param sheet
	 * @param firstRow 行起始
	 * @param lastRow  行截止
	 * @param firstCol 列起始
	 * @param lastCol  列截止
	 */
	public static void mergeCell(HSSFSheet sheet,int firstRow, int lastRow, int firstCol, int lastCol){
		sheet.addMergedRegion(new CellRangeAddress(firstRow,lastRow,firstCol,lastCol));
	}
	
	
	public static void mergeCell(HSSFSheet sheet,List<int[]> megerRowIndexs){
		for(int[] indexs : megerRowIndexs){
			mergeCell(sheet,indexs[0],indexs[1],indexs[2],indexs[3]);
		}
	}
	
	/**
	 * 输入EXCEL文件
	 *
	 * @param fileName
	 *            文件名
	 * @throws IOException 
	 */
	public static void outputExcel(HSSFWorkbook wb,String filePath) throws IOException {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(new File(filePath));
			wb.write(fos);
		}finally{ 
			if(fos != null){
				fos.close();
			}
		}
	}
	
	/**
	 * 遍历单元格，将值写入对应位置的单元格
	 * @param filePath
	 * @param dataList 
	 */
	public static HSSFWorkbook setValueToCell(String filePath,Map<String, String> valueMap) {
		List<HSSFCell> cellList = new ArrayList<HSSFCell>();
		FileInputStream fis = null;
		HSSFWorkbook hssfWorkbook = null;
		try {
			fis = new FileInputStream(filePath);
			if (fis != null) {
				hssfWorkbook = new HSSFWorkbook(fis);
				HSSFSheet sheet = null;
				int count = hssfWorkbook.getNumberOfSheets();
				//标记单元格的次序
				int postion = 0;
				for (int i = 0; i < count; i++) {
					sheet = hssfWorkbook.getSheetAt(i);
					int rowNumber = sheet.getPhysicalNumberOfRows();
					for (int r = 1;r < rowNumber; r++) {
						HSSFRow  row = sheet.getRow(r);
						int cellNumber = row.getPhysicalNumberOfCells();
						boolean flag = false;
						for (int c = 0; c < cellNumber; c++) {
							HSSFCell cell = row.getCell(c);
							String key = String.valueOf(postion);
							if(valueMap.containsKey(key)){
								cell.setCellValue(valueMap.get(key));
							}
							postion++;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hssfWorkbook;
	}
	
	public static HSSFSheet getSheet(File excelFile,int sheetIndex) throws IOException{
		FileInputStream fis     = new FileInputStream(excelFile);
		HSSFWorkbook workbook  = new HSSFWorkbook(fis);		
		HSSFSheet sheet1	   = workbook.getSheetAt(sheetIndex);
		return sheet1;
	}
	
	public static HSSFWorkbook getWorkbook(File excelFile) throws IOException{
		FileInputStream fis     = new FileInputStream(excelFile);
		HSSFWorkbook workbook  = new HSSFWorkbook(fis);	
		return workbook;
	}
	
	public  static String getCellValue(HSSFSheet sheet,int rowIndex,int columnIndex){
		HSSFRow row   = sheet.getRow(rowIndex);
		return row == null ? "" : getCellValue(row,columnIndex);
	}
	
	public  static String getCellCommonValue(HSSFSheet sheet,int rowIndex,int columnIndex){
		HSSFRow row   = sheet.getRow(rowIndex);
		return row == null ? "" : getCellCommonValue(row,columnIndex);
	}
	
	public  static String getCellCommonValue(HSSFRow row,int columnIndex){
		if(row == null){
			return "";
		}
		HSSFCell cell = row.getCell(columnIndex);
		return getCellCommonValue(cell);
	}
	
	public  static String getCellCommonValue(HSSFCell cell){
		String cellValue = "";
		if(cell == null){
			return cellValue;
		}
		try{
			cellValue = cell.getStringCellValue();
			if(!StringUtils.isEmpty(cellValue)){
				cellValue = cellValue.trim();
			}
		}catch(Exception e){
			cellValue =  cell.getNumericCellValue() + "";
		}
		return cellValue;
	}
	
	public  static String getCellValue(HSSFRow row,int columnIndex){
		if(row == null){
			return "";
		}
		HSSFCell cell = row.getCell(columnIndex);
		return getCellValue(cell);
	}
	
	
	
	public  static String getCellValue(HSSFCell cell){
		if(cell == null || cell.getStringCellValue() == null ||"".equals(cell.getStringCellValue())){
			return "";
		}
		return cell.getStringCellValue().trim();
	}
	
	public static HSSFCell setCellValue(HSSFSheet sheet,int rowIndex,int columnIndex,String cellValue,HSSFCellStyle cellStyle){
		HSSFRow row   = sheet.getRow(rowIndex);
		if(row == null){
			row = sheet.createRow(rowIndex);
		}
		HSSFCell cell = row.getCell(columnIndex);
		if(cell == null){
			cell = row.createCell(columnIndex);			
		}
		if(cellStyle != null){
			cell.setCellStyle(cellStyle);
		}
		cell.setCellValue(cellValue);
		return cell;
	}
	
	public static HSSFCell setCellValue(HSSFSheet sheet,int rowIndex,int columnIndex,String cellValue,HSSFCellStyle cellStyle,boolean richText){
		HSSFRow row   = sheet.getRow(rowIndex);
		if(row == null){
			row = sheet.createRow(rowIndex);
		}
		HSSFCell cell = row.getCell(columnIndex);
		if(cell == null){
			cell = row.createCell(columnIndex);			
		}
		if(cellStyle != null){
			cell.setCellStyle(cellStyle);
		}
		if(richText){
			cell.setCellValue(new HSSFRichTextString(cellValue));
		}else{
			cell.setCellValue(cellValue);
		}
		return cell;
	}
}