package com.payments.utilites;

import org.apache.log4j.Logger;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.util.Properties;

/**
 * @author Kostiantyn Dubovik
 */
class GeneratePdf {

    private static final Logger LOG = Logger.getLogger(GeneratePdf.class);

    private static final int NUM_COLUMNS = 2;
    private static final String AUTHOR = "Kostiantyn Dubovik";
    private static final String CREATOR = "payments.com";
    private static final String KEYWORDS = "Payments, PDF, Report";
    private static final String SUBJECT = "Table with transferring data";
    private static final String TITLE = "Report of money transfers";

    static ByteArrayOutputStream createPdf(Properties properties) {

        ByteArrayOutputStream result = null;
        try {
            Document document = new Document();
            result = new ByteArrayOutputStream();
            PdfWriter.getInstance(document,result);
            document.open();
            addMetaData(document);
            document.add(createTable(properties));
            document.close();
        } catch (Exception e) {
            LOG.error(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    private static void addMetaData(Document document) {
        document.addTitle(TITLE);
        document.addSubject(SUBJECT);
        document.addKeywords(KEYWORDS);
        document.addAuthor(AUTHOR);
        document.addCreator(CREATOR);
    }

    private static PdfPTable createTable(Properties properties) {

        PdfPTable table = new PdfPTable(NUM_COLUMNS);

        table.addCell(properties.getProperty("fromBillMessage"));
        table.addCell(properties.getProperty("fromBill"));
        table.addCell(properties.getProperty("toBillMessage"));
        table.addCell(properties.getProperty("toBill"));
        table.addCell(properties.getProperty("sumMessage"));
        table.addCell(properties.getProperty("sum"));
        table.addCell(properties.getProperty("commissionMessage"));
        table.addCell(properties.getProperty("commission"));
        table.addCell(properties.getProperty("dateMessage"));
        table.addCell(properties.getProperty("date"));
        table.addCell(properties.getProperty("balanceBeforeMessage"));
        table.addCell(properties.getProperty("balanceBefore"));
        table.addCell(properties.getProperty("balanceAfterMessage"));
        table.addCell(properties.getProperty("balanceAfter"));
        table.addCell(properties.getProperty("commentMessage"));
        table.addCell(properties.getProperty("comment"));

        return table;
    }
}