package com.vkstech.authorizationserver.filter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class HttpServletResponseCopier extends HttpServletResponseWrapper {

    private ServletOutputStream outputStream;
    private PrintWriter writer;
    private ServletOutputStreamCopier copier;

    Log LOGGER = LogFactory.getLog(XSSFilter.class);

    public HttpServletResponseCopier(HttpServletResponse response) {
        super(response);
    }

    @Override
    public ServletOutputStream getOutputStream() {
        if ((writer == null) && (outputStream == null)) {
            try {
                outputStream = getResponse().getOutputStream();
                copier = new ServletOutputStreamCopier(outputStream);
            } catch (Exception e) {

            }
        }
        return copier;
    }

    @Override
    public PrintWriter getWriter() {
        if ((outputStream != null) && (writer == null)) {
            try {
                copier = new ServletOutputStreamCopier(getResponse().getOutputStream());
                writer = new PrintWriter(new OutputStreamWriter(copier, getResponse().getCharacterEncoding()), true);
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            }

        }
        return writer;
    }

    @Override
    public void flushBuffer() {
        if (writer != null) {
            writer.flush();
        }

        if (outputStream != null) {
            try {
                copier.flush();
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            }
        }
    }

    public byte[] getCopy() {
        if (copier != null) {
            return copier.getCopy();
        } else {
            return new byte[0];
        }
    }

}