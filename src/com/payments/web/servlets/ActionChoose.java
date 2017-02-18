package com.payments.web.servlets;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Kostiantyn Dubovik
 */
public interface ActionChoose {
    String NUMBER = "number";
    default String [] getNumberAndAction (HttpServletRequest request){
        String [] numberAndAction = new String[2];
        String billNumber = request.getParameter(NUMBER);
        numberAndAction[0] = billNumber.substring(0, billNumber.length() - 1);
        numberAndAction[1] = billNumber.substring(billNumber.length() - 1);
        return numberAndAction;
    }
}
