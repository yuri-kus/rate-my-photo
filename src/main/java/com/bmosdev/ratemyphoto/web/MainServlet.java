package com.bmosdev.ratemyphoto.web;

import com.bmosdev.ratemyphoto.web.actions.BaseAction;
import com.bmosdev.ratemyphoto.web.actions.FileTreeAction;
import lime.web.Lime;
import lime.web.urls.PathActionIdentifier;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends LimeServlet {

	private static LimePathGatherer gatherer = new LimePathGatherer();

	static {
		gatherer.gatherAll(BaseAction.class);
		gatherer.gatherAll(FileTreeAction.class);
	}

	@Override
	protected void addActionIdentifiers(Lime lime) {
		// adding actions which was collected using reflection by gatherer
		for (PathActionIdentifier identifier : gatherer) {
			lime.addActionIdentifier(identifier);
		}
	}

	@Override
	protected void addInterceptors(Lime lime) {
		// lime.addInterceptor(new CommitBeforeRedirectInterceptor());
	}

	@Override
	protected void addActions(HttpServletRequest request, HttpServletResponse response, Lime lime) {
		// actions
		lime.addAction(new BaseAction(request.getSession()));
		lime.addAction(new FileTreeAction());
	}
}
