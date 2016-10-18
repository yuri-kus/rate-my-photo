package com.bmosdev.ratemyphoto.web;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import lime.web.*;

public class ClientErrorOutcomeHandler implements OutcomeHandler {

    public boolean handle(Object action, Object outcome, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        if (!(outcome instanceof ClientError)) return false;

        response.sendError(((ClientError) outcome).errorCode);

        return true;
    }

}
