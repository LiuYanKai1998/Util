package ext.wisplm.util.sign;
import java.io.IOException;

import com.itextpdf.awt.geom.Rectangle2D.Float;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.TextRenderInfo;

/**
 *全文检索pdf文件,获取指定文字的x,y,页码
 *
 *ZhongBinpeng May 19, 2020
 */
public class PDFCoordinate
{
    public static void main(String[] args) {
    	/**
    	 *  关键字:张,x坐标:157,y坐标:462,页码:1
		         关键字:李,x坐标:157,y坐标:447,页码:1
			关键字:王,x坐标:157,y坐标:433,页码:1
    	 */
    	new PDFCoordinate().getCoordinate("C:\\Users\\BZH\\Desktop\\签名.pdf");
	}
    
    /**
     * 要计算坐标的文字
     */
    public static final String []keyWords = {"张","李","王"};
    
    public int i = 0;
    
    public void getCoordinate(String filePath)
    {
        try
        {
            PdfReader pdfReader = new PdfReader(filePath);
            int pageNum = pdfReader.getNumberOfPages();
            PdfReaderContentParser pdfReaderContentParser = new PdfReaderContentParser(
                    pdfReader);
            // 下标从1开始
            for ( i = 1; i <= pageNum; i++)
            {
                pdfReaderContentParser.processContent(i, new RenderListener()
                {
                    @Override
                    public void renderText(TextRenderInfo textRenderInfo)
                    {
                        String text = textRenderInfo.getText();
                        for(String keyWord : keyWords){
                            if (null != text && text.contains(keyWord))
                            {
                                Float boundingRectange = textRenderInfo
                                        .getBaseline().getBoundingRectange();
                                float x  = boundingRectange.x;
                                float y  = boundingRectange.y;
                                int page = i;
                                System.out.println("关键字:"+keyWord+",x坐标:" + x + ",y坐标:" + y + ",页码:" + page);
                            }
                        }
                    }
                    @Override
                    public void renderImage(ImageRenderInfo arg0)
                    {

                    }
                    @Override
                    public void endTextBlock()
                    {

                    }
                    @Override
                    public void beginTextBlock()
                    {

                    }
                });
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}