package ext.wisplm.util.sign;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

import ext.wisplm.util.WISPLMConstants;
import wt.util.WTProperties;

public class SignProperties {
	
	private static final boolean USECACHE = false; 
	//private static String confFilePath = null;
	private static Logger logger = Logger.getLogger(SignProperties.class);
	private static List<String> FILENAMES =  new ArrayList<String>();
	
	public static void main(String[] args) {
		
	}

	private static Properties settings;

	static {
		try {
			String confFilePath 	= WISPLMConstants.CODEBASE  + "ext" + File.separator + "wisplm" + File.separator + "demo" + File.separator+"config" + File.separator;
			FILENAMES.add(confFilePath + "sign.properties");
			initConfig();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 重新加载配置文件
	 * @throws IOException
	 */
	public static void reloadConfig() throws IOException {
		initConfig();
	}

	private static void initConfig() throws IOException {
		
		if(settings == null || !USECACHE){
			//加载多个配置文件
			Set<InputStream> isSet = new HashSet<InputStream>();
			for (String fileName : FILENAMES) {
				isSet.add(new FileInputStream(fileName));			
			}
			Set<InputStreamReader> isrSet = new HashSet<InputStreamReader>();
			for (InputStream is : isSet) {
				isrSet.add(new InputStreamReader(is,"UTF-8"));
			}
			Set<BufferedReader> bfrSet = new HashSet<BufferedReader>();
			for (InputStreamReader isr : isrSet) {
				bfrSet.add(new BufferedReader(isr));
			}
			settings = new Properties();
			for (BufferedReader r : bfrSet) {				
				settings.load(r);
			}
			//关闭IO流
			closeStream(isSet,isrSet,bfrSet);			
		}
	}
	
    private static void closeStream(Set<InputStream> fis,Set<InputStreamReader> isr,Set<BufferedReader> r) throws IOException{
    	for (BufferedReader bufferedReader : r) {
    		bufferedReader.close();
		}
    	for(InputStreamReader reader:isr){
    		reader.close();
    	}
    	for(InputStream stream:fis){
    		stream.close();
    	}
    }
	
	
	public static String get(String key){
		if(!USECACHE){
			try{
				reloadConfig();
			}catch(IOException e){
				e.printStackTrace();
			}
			
		}
		try{
			String value = settings.getProperty(key);	
			return value;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public static String get(String key, String defaultvalue){
		if(!USECACHE){
			try{
				reloadConfig();
			}catch(IOException e){
				e.printStackTrace();
			}
			
		}
		return settings.getProperty(key) == null ? defaultvalue : settings.getProperty(key);
	}
}
