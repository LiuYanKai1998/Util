package ext.wisplm.util.sign;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;


public class SignTest {

	
	public static void main(String[] args) throws Exception {
		Map<String,String> signInfo = new HashMap<String,String>();
		signInfo.put("编制",   "user1$张三丰1$20181212");
		//signInfo.put("主管设计", "user2$张三丰2$20181212");
		signInfo.put("校对",   "user3$张三丰3$20181212");
		signInfo.put("审核", "user4$张三丰4$20181212");
		signInfo.put("会签1", "user7$张三丰5$20181212");
		signInfo.put("会签2", "user7$张三丰5$20181212");
		signInfo.put("批准", "user7$张三丰7$20181212");
		
		String key="工艺规程";
		String pdf = "D:\\Document\\2018-625\\05_文件模板\\文档模板库管理模板\\工艺规程";
		String pdfOut = pdf + System.currentTimeMillis()+".pdf";
		pdf +=".pdf";
		List<SignBean> signBeanList = SignUtil.getSignBeanList(new File(pdf), key, signInfo);
		SignUtil.printTextOnPdf(new File(pdf), signBeanList, pdfOut);
	}
}
