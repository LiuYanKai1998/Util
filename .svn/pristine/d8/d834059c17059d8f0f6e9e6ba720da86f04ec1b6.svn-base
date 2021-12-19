<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.io.*"%>
<%
	String oid = request.getParameter("oid");
	OutputStream os = null;
	BufferedInputStream bis = null;
	FileInputStream fis = null;
	File file = null;
	try {
		//wt.part.WTPart rootPart = (wt.part.WTPart)new wt.fc.ReferenceFactory().getReference(oid).getObject();
		//EBOMList.getEBOMXls(rootPart);
		//此处调整为获取的服务器地址
		String filePath = "";
		file = new File(filePath);
		response.reset();
		response.setContentType("application/x-msdownload");
		response.setContentLength((int) file.length());
		response.setHeader("Content-Disposition", "attachment;" + " filename=" + file.getName());
		os = response.getOutputStream();
		fis = new FileInputStream(file);
		bis = new BufferedInputStream(fis);
		int len = 0;
		byte[] b = new byte[8192];
		while ((len = bis.read(b)) > 0) {
			os.write(b, 0, len);
		}
		os.flush();
		out.clear();
		out = pageContext.pushBody();
	} catch (Exception e) {
		out.println("<html><head><title>文件下载失败<title><head>");
		out.println("<h2>***文件下载失败***</h2>");
		out.println("<p><b>" + e.getMessage() + "<b></p>");
		out.println("</body></html>");
	} finally {
		if (fis != null) {
			fis.close();
		}
		if (bis != null) {
			bis.close();
		}
		if (file != null) {
			//file.deleteOnExit();
		}
	}
%>
