package com.payments.web.servlets;

import com.payments.utilites.PropertyHolder;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

/**
 * @author Dubovik Kostiantyn
 */
public class CaptchaServlet extends HttpServlet {

    /**
     *
     */
    private static final Logger LOG = Logger.getLogger(CaptchaServlet.class);
    /**
     *
     */
    private static final long serialVersionUID = -6773348593583501472L;
    /**
     *
     */
    private static final int COLOR_DIAPASON = 255;
    /**
     *
     */
    private static final PropertyHolder PROPERTY_HOLDER = PropertyHolder.getInstance();

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	doGet(request, response);
    }
    /**
     * @param request s
     * @param response s
     * @throws IOException s
     */
    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("image/jpg");

        int iTotalChars = PROPERTY_HOLDER.getTotalChars();

        int iHeight = PROPERTY_HOLDER.getHeight();
        int iWidth = PROPERTY_HOLDER.getWidth();

        Font fntStyle1 = new Font("Arial", Font.BOLD, 30);

        Random randChars = new Random();
        String sImageCode = (Long.toString(Math.abs(randChars.nextLong()), 36)).substring(0, iTotalChars);

        BufferedImage biImage = new BufferedImage(iWidth, iHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2dImage = (Graphics2D) biImage.getGraphics();


        int iCircle = PROPERTY_HOLDER.getCircle();
        for (int i = 0; i < iCircle; i++) {
            g2dImage.setColor(new Color(randChars.nextInt(COLOR_DIAPASON),
                    randChars.nextInt(COLOR_DIAPASON), randChars.nextInt(COLOR_DIAPASON)));
        }
        g2dImage.setFont(fntStyle1);
        for (int i = 0; i < iTotalChars; i++) {
            g2dImage.setColor(new Color(randChars.nextInt(COLOR_DIAPASON),
                    randChars.nextInt(COLOR_DIAPASON), randChars.nextInt(COLOR_DIAPASON)));
            if (i % 2 == 0) {
                g2dImage.drawString(sImageCode.substring(i, i + 1), 25 * i, 24);
            } else {
                g2dImage.drawString(sImageCode.substring(i, i + 1), 25 * i, 35);
            }
        }
        HttpSession session = request.getSession(true);
        session.setAttribute("dns_security_code", sImageCode);

        OutputStream osImage = response.getOutputStream();
        ImageIO.write(biImage, "jpeg", osImage);
        //osImage.close();

        g2dImage.dispose();


    }
}
