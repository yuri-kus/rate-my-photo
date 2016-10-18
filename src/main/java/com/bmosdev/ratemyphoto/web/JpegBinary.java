package com.bmosdev.ratemyphoto.web;

import com.bmosdev.ratemyphoto.web.Binary;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JpegBinary implements Binary {
    
    private byte[] data;

    public JpegBinary(byte[] data) {
        this.data = data;
    }

    public void write(HttpServletResponse resp) throws IOException {
//        resp.setDateHeader("Expires", 0L);
//        resp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
//        resp.addHeader("Cache-Control", "post-check=0, pre-check=0");
//        resp.setHeader("Pragma", "no-cache");
        resp.setContentType("image/jpeg");
        
        ServletOutputStream out = resp.getOutputStream();
        out.write(data);
    }
}
