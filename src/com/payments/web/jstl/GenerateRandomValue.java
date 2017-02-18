package com.payments.web.jstl;

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import java.io.*;
import java.util.Random;

/**
 * @author Kostiantyn Dubovik
 */
public class GenerateRandomValue extends SimpleTagSupport {

    private static final int BOUND = 10000000;

    @Override
    public void doTag() throws JspException, IOException {
        JspWriter out = getJspContext().getOut();
        out.println(new Random(System.currentTimeMillis()).nextInt(BOUND));
    }

}
