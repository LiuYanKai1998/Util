package ext.wisplm.vo;

import java.io.Serializable;


public class R<T> implements Serializable {
	private static final long serialVersionUID = 1L;


	private int code;


	private String msg;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	private T data;

	public static <T> R<T> ok() {
		return restResult(null, 0, null);
	}

	public static <T> R<T> ok(T data) {
		return restResult(data, 0, null);
	}

	public static <T> R<T> ok(T data, String msg) {
		return restResult(data, 0, msg);
	}

	public static <T> R<T> failed() {
		return restResult(null, 1, null);
	}

	public static <T> R<T> failed(String msg) {
		return restResult(null, 1, msg);
	}

	public static <T> R<T> failed(T data) {
		return restResult(data, 1, null);
	}

	public static <T> R<T> failed(T data, String msg) {
		return restResult(data, 1, msg);
	}

	private static <T> R<T> restResult(T data, int code, String msg) {
		R<T> apiResult = new R<>();
		apiResult.setCode(code);
		apiResult.setData(data);
		apiResult.setMsg(msg);
		return apiResult;
	}
}

