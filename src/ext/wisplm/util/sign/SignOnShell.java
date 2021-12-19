package ext.wisplm.util.sign;

import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;

import wt.fc.ReferenceFactory;
import wt.fc.WTObject;
import wt.method.RemoteMethodServer;
import wt.util.WTException;
import wt.util.WTRuntimeException;

/**
 *
 *Zhong Binpeng Jun 9, 2021
 */
public class SignOnShell {
	
	public static void main(String args[]) throws RemoteException, InvocationTargetException, WTRuntimeException, WTException{
		RemoteMethodServer rms = RemoteMethodServer.getDefault();
		rms.setUserName("wcadmin");
		rms.setPassword("wcadmin");
		WTObject obj = (WTObject) new ReferenceFactory().getReference(args[0]).getObject();
		rms.invoke("sign", SignUtil.class.getName(), 
				null,new Class[]{WTObject.class,String.class} ,new Object[]{obj,"SIGNINFO"});
		
	}

}
