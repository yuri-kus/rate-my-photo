package com.bmosdev.ratemyphoto.web.handlers;

import com.bmosdev.ratemyphoto.util.JsonHelper;
import com.bmosdev.ratemyphoto.web.JacksonBean;
import com.bmosdev.ratemyphoto.web.Template;
import lime.tomato.widget.tooltip.Tooltips;
import lime.util.Escaper;
import lime.web.Messages;
import lime.web.OutcomeHandler;
import lime.web.velocity.VelocityTemplate;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.util.HashMap;

public class JacksonBeanOutcomeHandler implements OutcomeHandler {

	public boolean handle(Object action, Object outcome, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		if (!(outcome instanceof JacksonBean) && !(outcome instanceof JacksonBean[])) {
			return false;
		}

		response.setContentType("text/x-json; charset=UTF-8");

		String str = JsonHelper.serializeJson(outcome);

		response.getWriter().write(str);

		return true;
	}

}
