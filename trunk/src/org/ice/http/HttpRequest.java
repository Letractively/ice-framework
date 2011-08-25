package org.ice.http;

import java.util.Enumeration;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

public class HttpRequest  {

	protected String[] params;
	protected HttpServletRequest request;
	protected String moduleName;
	protected String taskName;
	private String baseUrl;
	
	public HttpRequest(HttpServletRequest request) {
		super();
		this.request = request;
		int serverPort = request.getServerPort();
		if (serverPort == 80 || serverPort == 443)	{
			baseUrl = request.getScheme() + "://" + request.getServerName() + request.getContextPath();
		} else {
			baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + serverPort + request.getContextPath();
		}
	}

	public void setSession(String name, Object value)	{
		HttpSession session = request.getSession(true);
		session.setAttribute(name, value);
	}
	
	public Object getSession(String name)	{
		HttpSession session = request.getSession(false);
		if (session != null)
			return session.getAttribute(name);
		return null;
	}
	
	public void destroySession()	{
		HttpSession session = request.getSession(false);
		if (session != null)
			session.invalidate();
	}
	
	public void clearSession(String name)	{
		HttpSession session = request.getSession(false);
		if (session != null)
			session.removeAttribute(name);
	}
	
	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	
	public String getParam(String key)	{
		return this.getParam(key, null);
	}
	
	public String getParam(String key, String defaultValue)	{
		String value = request.getParameter(key);
		if (value != null)
			return value;
		return defaultValue;
	}
	
	public boolean hasParam(String key)	{
		return getParam(key) != null;
	}
	
	public Enumeration<String> getParams()	{
		return request.getParameterNames();
	}
	
	/**
	 * Get the request HTTP method
	 */
	public String getMethod()	{
		return request.getMethod();
	}
	
	/**
	 * Get the specified request header
	 * @param String header the header name
	 * @return String the header value or false if we can't retrieve it
	 */
	public String getHeader(String header)	{
		return request.getHeader(header);
	}
	
	/**
	 * Test if this is a AJAX request
	 */
	public boolean isAjaxRequest()	{
        return (this.getHeader("X_REQUESTED_WITH").equals("XMLHttpRequest"));
	}
	
	/**
	 * @param request the HTTP request object
	 * @return the base URL
	 */
	public String getBaseUrl()	{
		return baseUrl;
	}
	
	public StringBuffer getRequestUrl()	{
		return request.getRequestURL();
	}
	
	public void setParams(String[] params)	{
		this.params = params;
	}
	
	public String getParam(int index)	{
		if (index < 0 || index > params.length-1)	{
			return null;
		}
		return params[index];
	}
	
	public Cookie[] getCookies()	{
		return request.getCookies();
	}
	
	public boolean isMultipart() {
		return ServletFileUpload.isMultipartContent(request);
	}
	
	public FileItem getUploadFile(String file) throws Exception {
		ServletFileUpload fileUpload = new ServletFileUpload(new DiskFileItemFactory());
		List<FileItem> list = fileUpload.parseRequest(request);
		for(FileItem fileItem: list) {
			if (fileItem.getFieldName().equals(file)) {
				return fileItem;
			}
		}
		return null;
	}

	public RequestDispatcher getRequestDispatcher(String template) {
		return request.getRequestDispatcher(template);
	}
	
	public HttpServletRequest getUnderlyingRequest()	{
		return request;
	}
}
