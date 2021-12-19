package ext.wisplm.util.third;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

import ext.wisplm.util.WISPLMConstants;
import wt.util.WTException;

public class PDFSign {

	public static BaseFont baseFont      = null;
	static{
		try {
			/**
			 * 服务器上一般放在codebase对应客制化目录下,比如codebase\\ext\\wisplm\\demo\\font\\simsun.ttc
			 * 服务器路径在代码中切记不要写死,通过读取wtproperties文件动态获取
			 */
			
			baseFont = BaseFont.createFont(WISPLMConstants.CODEBASE + File.separator + "ext"  + File.separator +"wisplm" + File.separator + "demo" + File.separator + "font" + File.separator + "simsun.ttc,1", BaseFont.IDENTITY_H, BaseFont.EMBEDDED); 
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws DocumentException, IOException, WTException {
		//签名前的文件,从服务器通过API获取
		String pdf1 = "C:\\Users\\BZH\\Desktop\\总体设计.pdf";
		//签名后的文件,输出到服务器硬盘,需上传为文档附件,上传成功后删除服务器临时文件
		String pdf2 = "C:\\Users\\BZH\\Desktop\\PRINT_总体设计.pdf";
		printPageNumber(pdf1,pdf2);
	}
	
	/**
	 * 向pdf指定位置打印文字
	 * @description
	 * @param pdfPath    源文件
	 * @param newPdfPath 输出的文件
	 * @throws DocumentException
	 * @throws IOException
	 * @throws WTException
	 */
	public static void printPageNumber(String pdfPath,String newPdfPath) throws DocumentException, IOException, WTException{
		PdfReader pdfReader = null;
		PdfStamper stamp    = null;
		FileOutputStream fos = null;
		try{
			pdfReader = new PdfReader(pdfPath);
			fos = new FileOutputStream(newPdfPath);
			stamp = new PdfStamper(pdfReader, fos);
			int pageSize = pdfReader.getNumberOfPages();
			//示例为循环给每页都打印文字,自行调整
			for(int i = 1 ; i <= pageSize ; i ++){
				PdfContentByte over = stamp.getOverContent(i);
				//设置字体文件和字体
				over.setFontAndSize(baseFont,10);  
				//over.setTextMatrix(30, 30);
				over.beginText(); 
				over.showTextAligned(Element.ALIGN_LEFT,"张三 20210426", 157, 462, 0); 
				over.endText();
				
				over.beginText(); 
				over.showTextAligned(Element.ALIGN_LEFT,"李四 20210427", 157, 447, 0); 
				over.endText();
				
				over.beginText(); 
				over.showTextAligned(Element.ALIGN_LEFT,"王五 20210428", 157, 433, 0); 
				over.endText();
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new WTException("工艺规程写入页码和总页数出错:" + e.getLocalizedMessage());
		}finally{
			if(stamp != null){ 
				stamp.close();
			}
			if(pdfReader != null){
				pdfReader.close(); 
			}
		}
		
	}
}
