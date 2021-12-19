package ext.wisplm.util.third;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.zip.CRC32;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 *
 *Zhong Binpeng Jun 15, 2020
 */
public class ZipFileUtil {

	private static final Logger logger = LoggerFactory.getLogger(ZipFileUtil.class);
    
    public static void main(String arg[]) throws Exception {
    	
    } 

    /**
     * 将多个文件压缩到指定路径压缩包,不建立子目录,全部平铺压缩到根路径下
     * @description
     * @param zipFileName 压缩包全路径
     * @param filePaths   源文件全路径集合
     * @throws Exception
     */
    public static void zip(String zipFileName,List<String> filePaths) throws Exception{
    	 ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
         out.setEncoding("UTF-8");
         for(String filePath : filePaths){
        	 zip(out,new File(filePath),"");
         }
         out.close();
    }
    
    /**
     * 压缩文件或文件夹
     * @param inputFile   源文件全路径,第一层子目录压缩到zip包
     * @param zipFileName 输出的zip文件全路径
     * @throws Exception  
     */
    public static void zip(File inputFile,String zipFileName) throws Exception {
        zip(inputFile, zipFileName, "");
    }
    
    /**
     * 
     * @description
     * @param inputFile   源文件全路径
     * @param zipFileName 输出的zip文件全路径
     * @param basePath    源文件在zip文件中的位置,压缩到根目录传"",如果要压缩到其他目录,则传递具体路径如:技术文件/工艺
     * @throws Exception
     */
    public static void zip(File inputFile,String zipFileName,String basePath) throws Exception {
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
        out.setEncoding("UTF-8");
        zip(out, inputFile,basePath);
        out.close();
    }
    
    public static void zip(ZipOutputStream out, File f, String base) throws Exception {
        if (f.isDirectory()) {
            File[] fl = f.listFiles();
            // out.putNextEntry(new org.apache.tools.zip.ZipEntry(base + "/"));
            base = base.length() == 0 ? "" : base + "/";
            for (int i = 0; i < fl.length; i++) {
                zip(out, fl[i], base + fl[i].getName());
            }
        } else {
            ZipEntry zipentry = new org.apache.tools.zip.ZipEntry(base);
            // zipentry.setMethod(ZipEntry.STORED);//不压缩
            zipentry.setMethod(ZipEntry.DEFLATED);
            zipentry.setSize(f.length());
            zipentry.setCrc(calcChecksum(f));
            out.putNextEntry(zipentry);
            FileInputStream in = new FileInputStream(f);
            int b;
            byte[] buf = new byte[2048];
            while ((b = in.read(buf)) > 0) {
                out.write(buf, 0, b);
            }
            /*
             * while ((b = in.read()) != -1) { out.write(b); }
             */
            in.close();
        }
    }

    /**
     * 将某个文件添加到结果jar文件中
     *
     * @param jarOut
     * @param afile
     * @param dir
     * @throws IOException 
     */
    public static void addFileToJar(JarOutputStream jarOut, File afile, String dir) throws IOException {
        byte[] buf = new byte[1024];
        JarEntry entry = new JarEntry(dir + afile.getName());
        FileInputStream inStream = new FileInputStream(afile);
        jarOut.putNextEntry(entry);
        int len;
        while ((len = inStream.read(buf)) > 0)
            jarOut.write(buf, 0, len);

        jarOut.flush();
        jarOut.closeEntry();
        inStream.close();
    }

    public static String UnZipFile(String zipFileName, String outputDirectory) throws Exception {
    	
    	String endinfo = null;
        File fdir = new File(outputDirectory);
        fdir.mkdirs();

        org.apache.tools.zip.ZipFile zipFile = new org.apache.tools.zip.ZipFile(zipFileName);

        try {
            java.util.Enumeration e = zipFile.getEntries();
            org.apache.tools.zip.ZipEntry zipEntry = null;
            while (e.hasMoreElements()) {
                zipEntry = (org.apache.tools.zip.ZipEntry) e.nextElement();
                if (zipEntry.getName() == null || zipEntry.getName().trim().length() == 0
                        || zipEntry.getName().equalsIgnoreCase("/") || zipEntry.getName().equalsIgnoreCase("\\")) {
                    continue;
                }
                if (zipEntry.isDirectory()) {
                    // System.out.println("目录...");
                    String name = zipEntry.getName();
                    name = name.substring(0, name.length() - 1);
                    File f = new File(outputDirectory + File.separator + name);
                    f.mkdir();
                } else {
                    // System.out.println("文件...");
                    String fileName = zipEntry.getName();
                    fileName = fileName.replace('\\', '/');
                    if (fileName.endsWith("/") || fileName.endsWith("\\"))
                        fileName = fileName.substring(0, fileName.length() - 1);
                    if ((fileName.indexOf("/") > -1)) {
                        // createDirectory(outputDirectory, fileName.substring(0, fileName.lastIndexOf("/")));
                    	
                    	String dirString = fileName.substring(0, fileName.lastIndexOf("/"));
                    	String [] dirArray = dirString.split("/");
                    	String tempOutputDirectory = outputDirectory;
                    	for(int i =0; i<dirArray.length; i++){
                    		String dirStr = tempOutputDirectory + dirArray[i];
                            File f1 = new File(dirStr);
                            f1.mkdir();
                            tempOutputDirectory += dirArray[i]+"/";
                    	}
                        fileName = fileName.substring(fileName.lastIndexOf("/") + 1, fileName.length());
                    } else if (fileName.indexOf("\\") > -1) {
                    	String dirString = fileName.substring(0, fileName.lastIndexOf("\\"));
                    	String [] dirArray = dirString.split("\\");
                    	for(int i =0; i<dirArray.length; i++){
                    		String dirStr = outputDirectory + dirArray[i];
                            File f1 = new File(dirStr);
                            f1.mkdir();
                    	}
                        fileName = fileName.substring(fileName.lastIndexOf("\\") + 1, fileName.length());
                    }
                    File f = new File(outputDirectory + zipEntry.getName());
                    f.createNewFile();
                    InputStream in = zipFile.getInputStream(zipEntry);
                    FileOutputStream out = new FileOutputStream(f);

                    byte[] by = new byte[2048];
                    int c;
                    while ((c = in.read(by)) != -1) {
                        out.write(by, 0, c);
                    }
                    out.close();
                    in.close();
                }
            }
        }finally {
             zipFile.close();
        }
        return endinfo;

    }

    /*
     * Necessary in the case where you add a entry that is not compressed.
     */
    private static long calcChecksum(File f) throws IOException {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(f));

        return calcChecksum(in, f.length());
    }

    /*
     * Necessary in the case where you add a entry that is not compressed.
     */
    private static byte[] buffer = new byte[8192];

    private static long calcChecksum(InputStream in, long size) throws IOException {
        CRC32 crc = new CRC32();
        int len = buffer.length;
        int count = -1;
        int haveRead = 0;

        while ((count = in.read(buffer, 0, len)) > 0) {
            haveRead += count;
            crc.update(buffer, 0, count);
        }
        in.close();
        return crc.getValue();
    }
    
    /**
     * 删除文件或文件夹
     * @param inputPath
     * @return
     */
    public static boolean deleteFiles(String inputPath) {
        try {
            File f = new File(inputPath);
            if (f.isDirectory()) {
                File[] flist = f.listFiles();
                for (int i = 0; i < flist.length; i++) {
                    File tmpfile = flist[i];
                    deleteFiles(tmpfile.getAbsolutePath());
                }
                f.delete();
            } else
                f.delete();

        } catch (Exception e) {
        	e.printStackTrace();
            return false;
        }
        return true;
    }

}
