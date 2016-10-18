package com.bmosdev.ratemyphoto.web.handlers;

import com.bmosdev.ratemyphoto.web.Binary;
import lime.web.OutcomeHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BinaryOutcomeHandler implements OutcomeHandler {

    public boolean handle(Object action, Object outcome, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        if (!(outcome instanceof Binary)) return false;

        Binary binary = (Binary) outcome;
        
        binary.write(response);

        return true;
    }

}
