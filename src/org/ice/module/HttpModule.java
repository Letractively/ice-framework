package org.ice.module;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.http.Cookie;

import org.ice.exception.IceException;
import org.ice.http.HttpRequest;
import org.ice.http.HttpResponse;
import org.ice.view.AbstractView;
import org.ice.view.ScriptView;

public abstract class HttpModule implements IModule {

	protected String content;
	protected AbstractView view;
	private HttpRequest request;
	private HttpResponse response;
	private String template;
	
	public HttpModule()	{
		content = "";
		view = new ScriptView();
	}
	
	@Override
	public void init() {
	}

	@Override
	public void preDispatch() {
		
	}

	@Override
	public void dispatch(String task) throws IceException {
		if (view != null)	{
			view.setRequest(request.getUnderlyingRequest());
			view.setResponse(response.getUnderlyingResponse());
		}
		
		this.preDispatch ();

		try {
			Method method = this.getClass().getMethod(task+"Task", new Class<?>[0]);
			method.invoke(this, new Object[0]);
		} catch(InvocationTargetException ex) {
			throw new IceException(ex.toString()+" caught with root cause: "+ex.getTargetException(), 500);
		} catch (Exception ex)	{
			throw new IceException("Task ["+request.getTaskName()+"] not found for module ["+request.getModuleName()+"]", 404);
		}
		
		this.postDispatch ();
		
		if (isUsingTemplate())	{
			view.setTemplate(template);
			view.render();
		}
	}

	@Override
	public void postDispatch() {
		
	}
	
	public void setTemplate(String template)	{
		this.template = template;
	}
	
	public boolean isUsingTemplate()	{
		return (template != null);
	}

	@Override
	public String getResponse() {
		return content;
	}

	@Override
	public void destroy() {
		
	}
	
	@Override
	public void setResponse(HttpResponse response) {
		this.response = response;
		this.content = response.getBody();
	}
	
	@Override
	public void setRequest(HttpRequest request) {
		this.request = request;
	}

	@Override
	public HttpRequest getRequest() {
		return request;
	}
	
	public void setSession(String name, String value)	{
		request.setSession(name, value);
	}
	
	public Object getSession(String name)	{
		return request.getSession(name);
	}
	
	public void destroySession()	{
		request.destroySession();
	}
	
	public void clearSession(String name)	{
		request.clearSession(name);
	}
	
	public void addCookie(Cookie cookie)	{
		response.addCookie(cookie);
	}
	
	public Cookie[] getCookies()	{
		return request.getCookies();
	}

	@Override
	public void setHeader(String headerName, String value) {
		response.setHeader(headerName, value);
	}

	@Override
	public String getHeader(String headerName) {
		return response.getHeader(headerName);
	}
	
	public void setContentType(String contentType)	{
		response.setContentType(contentType);
	}
	
	public void redirect(String url)	{
		
	}

	public String getBaseUrl()	{
		return request.getBaseUrl();
	}
	
	public void echo(String s)	{
		content += s;
	}
}
