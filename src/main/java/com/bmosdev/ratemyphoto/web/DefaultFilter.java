package com.bmosdev.ratemyphoto.web;

import java.io.*;

import javax.servlet.*;

public class DefaultFilter implements Filter {

	private RequestDispatcher defaultRequestDispatcher;

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		defaultRequestDispatcher.forward(request, response);
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		this.defaultRequestDispatcher = filterConfig.getServletContext().getNamedDispatcher("default");
	}
}
