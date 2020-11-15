package com.epam.dmivapi.service.impl;

class ServiceUtils {
    public static int calculateNumOfPages(int numOfRows, int recordsPerPage) {
        int nOfPages = numOfRows / recordsPerPage;

        if (numOfRows % recordsPerPage > 0)
            nOfPages++;

        return nOfPages;
    }
}
