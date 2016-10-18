package com.bmosdev.ratemyphoto.web;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface Binary {
    
    void write(HttpServletResponse response) throws IOException;

}
