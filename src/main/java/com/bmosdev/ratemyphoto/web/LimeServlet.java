package com.bmosdev.ratemyphoto.web;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import com.bmosdev.ratemyphoto.web.handlers.BinaryOutcomeHandler;
import com.bmosdev.ratemyphoto.web.handlers.JacksonBeanOutcomeHandler;
import com.bmosdev.ratemyphoto.web.handlers.TemplateOutcomeHandler;
import lime.web.*;

public abstract class LimeServlet extends HttpServlet {

    protected Lime lime(HttpServletRequest request, HttpServletResponse response) {
        Lime lime = new Lime();

        addConverters(lime);

        addCollectors(lime);

        addInterceptors(lime);

        addOutcomeHandlers(lime);

        lime.addActionIdentifier(new ClassMethodActionIdentifier());
        addActionIdentifiers(lime);

        addActions(request, response, lime);

        performCustomRoutine(request, response, lime);

        return lime;
    }

    protected void performCustomRoutine(HttpServletRequest request, HttpServletResponse response, Lime lime) {
    }

    protected void addConverters(Lime lime) {
//        lime.addConverter(new SwDateConverter());
    }

    protected void addCollectors(Lime lime) {
    }

    protected void addOutcomeHandlers(Lime lime) {
        lime.addOutcomeHandler(new TemplateOutcomeHandler(true));
        lime.addOutcomeHandler(new JacksonBeanOutcomeHandler());
        lime.addOutcomeHandler(new BinaryOutcomeHandler());
//        lime.addOutcomeHandler(new XmlOutcomeHandler());
        lime.addOutcomeHandler(new ClientErrorOutcomeHandler());
        lime.addOutcomeHandler(new OutcomeHandler() {
            public boolean handle(Object action, Object outcome, HttpServletRequest request,
                                  HttpServletResponse response) throws IOException, ServletException {
                return outcome == null;
            }

        });
    }

    @Override
    protected final void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Lime lime = lime(request, response);
        lime.process(request, response);
    }

    protected abstract void addInterceptors(Lime lime);

    protected abstract void addActions(HttpServletRequest request, HttpServletResponse response, Lime lime);

    protected abstract void addActionIdentifiers(Lime lime);

}
