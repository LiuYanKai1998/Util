package ext.wisplm.demo.part.mvc.reference;

import com.ptc.netmarkets.model.NmObject;
import com.ptc.netmarkets.model.NmOid;

import wt.part.WTPart;
import wt.util.WTException;

public class PartBean extends NmObject implements java.io.Serializable {

	private WTPart part;
	private String name;

	private String number;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public WTPart getPart() {
		return part;
	}

	public void setPart(WTPart part) {
		this.part = part;
	}
	
	/**
	 * 自定行数据对象,设置该行主对象oid
	 */
	public NmOid getOid() {
		try {
			return new NmOid(part);
		} catch (WTException e) {
			e.printStackTrace();
		}
		return null;
	}

}
