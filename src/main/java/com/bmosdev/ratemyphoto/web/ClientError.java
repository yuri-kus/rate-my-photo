package com.bmosdev.ratemyphoto.web;

import javax.servlet.http.HttpServletResponse;

public enum ClientError {
    NOT_FOUND(HttpServletResponse.SC_NOT_FOUND), 
    UNAUTHORIZED(HttpServletResponse.SC_UNAUTHORIZED), 
    FORBIDDEN(HttpServletResponse.SC_FORBIDDEN), 
    BAD_REQUEST(HttpServletResponse.SC_BAD_REQUEST);

    public final int errorCode;

    ClientError(int errorCode) {
        this.errorCode = errorCode;
    }
}
