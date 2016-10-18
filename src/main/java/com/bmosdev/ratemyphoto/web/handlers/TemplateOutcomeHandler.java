package com.bmosdev.ratemyphoto.web.handlers;

import java.io.*;
import java.net.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import com.bmosdev.ratemyphoto.web.Template;
import lime.tomato.widget.tooltip.*;
import lime.util.*;
import lime.web.*;
import lime.web.velocity.*;

public class TemplateOutcomeHandler implements OutcomeHandler {

	private final boolean useWriter;

	public TemplateOutcomeHandler(boolean useWriter) {
		this.useWriter = useWriter;
	}

	public boolean handle(Object action, Object outcome, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		if (!(outcome instanceof Template))
			return false;

		Template template = (Template) outcome;

		response.setContentType(template.getContentType() + "; charset=UTF-8");
		response.addHeader("Pragma", "no-cache");
		response.addHeader("Expires", "0");

		String classPath = getTemplatePath(action, template);

		VelocityTemplate velocityTemplate = new VelocityTemplate(classPath);

		HashMap<String, Object> context = new HashMap<String, Object>();
		context.put("action", action);
		context.put("escaper", new Escaper());

		Messages messages = Messages.get();
		context.put("messages", messages);

		context.put("tooltips", Tooltips.get());

		context.put("cacheKey", loadTime);

		context.put("jsessionid", request.getSession().getId());

		context.putAll(template.getContext());

		try {
			Writer writer = getWriter(response);
			velocityTemplate.render(writer, context);
			writer.flush();
		} finally {
			if (messages != null) {
				messages.clear();
			}
		}

		return true;
	}

	private Writer getWriter(HttpServletResponse response) throws IOException {
		if (useWriter)
			return response.getWriter();

		return new OutputStreamWriter(response.getOutputStream(), "utf-8");
	}

	private String getTemplatePath(Object action, Template template) {
		String path = template.getPath();

		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		URL resource = cl.getResource(path);

		if (resource == null) { // try to lookup in the same package, as action
			String packageName = action.getClass().getPackage().getName();

			String specificPath = packageName.replace('.', '/') + '/' + path;
			resource = cl.getResource(specificPath);

			if (resource != null)
				return specificPath;
		}

		return path;
	}

	private final static long loadTime = System.currentTimeMillis();
}
