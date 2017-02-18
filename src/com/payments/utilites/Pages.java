package com.payments.utilites;

import com.payments.service.api.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class Pages {

    private static final String PAGE = "page";
    private static final String ORDER_BY = "order_by";
    private static final String COUNT_OF_PAGES = "countOfPages";
    private static final String CURRENT_PAGE = "currentPage";

    /**
     * Get count of pages.
     *
     * @param countOfRecords count of selected records
     * @return count of pages
     */
    private static int getCountOfPages(int countOfRecords) {
        if (countOfRecords <= 20) {
            return 1;
        } else if (countOfRecords % 20 == 0) {
            return countOfRecords / 20;
        } else {
            return (countOfRecords / 20) + 1;
        }
    }

    private static int getStartPosition(int pageNumber) {
        return (pageNumber - 1) * 20;
    }

    public static void doPagination(HttpServletRequest request, Service<?> service,
                                    int criteria, String defaultOrderBy, String attributeName) {
        int page = 1;
        if (request.getParameter(PAGE) != null) {
            page = Integer.parseInt(request.getParameter(PAGE));
        }
        String orderBy = request.getParameter(ORDER_BY);
        if (orderBy == null) {
            orderBy = defaultOrderBy;
        }
        int countOfPages = getCountOfPages(service.getCountOfRecords(criteria));
        request.setAttribute(COUNT_OF_PAGES, countOfPages);
        request.setAttribute(CURRENT_PAGE, page);
        request.setAttribute(ORDER_BY, orderBy);
        List<?> list = service.getAll(criteria, getStartPosition(page), orderBy);
        request.setAttribute(attributeName, list);
    }

}
