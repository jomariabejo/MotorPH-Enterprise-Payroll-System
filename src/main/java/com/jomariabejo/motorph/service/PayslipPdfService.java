package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.model.Employee;
import com.jomariabejo.motorph.model.Payslip;
import com.jomariabejo.motorph.model.YearToDateFigures;
import com.jomariabejo.motorph.utility.PesoUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.Optional;

/**
 * Service for generating PDF payslips following best practices.
 * Based on Baeldung's Java PDF creation guide.
 */
public class PayslipPdfService {

    private static final float MARGIN = 40f;
    private static final float BOTTOM_MARGIN = 70f;
    private static final float SECTION_SPACING = 25f;
    private static final SimpleDateFormat DATE_FORMAT_FULL = new SimpleDateFormat("MMMM dd, yyyy");
    private static final SimpleDateFormat DATE_FORMAT_SHORT = new SimpleDateFormat("MMM dd, yyyy");

    private final PayslipService payslipService;
    private final UserService userService;

    public PayslipPdfService(PayslipService payslipService, UserService userService) {
        this.payslipService = payslipService;
        this.userService = userService;
    }

    /**
     * Generates a PDF payslip file for the given payslip.
     *
     * @param payslip The payslip to generate PDF for
     * @param outputDirectory Directory where PDF will be saved
     * @return File object representing the generated PDF
     * @throws IOException If PDF generation fails
     */
    public File generatePayslipPdf(Payslip payslip, String outputDirectory) throws IOException {
        // Prepare data
        PayslipData data = preparePayslipData(payslip);
        
        // Create output file
        File pdfFile = createOutputFile(payslip, outputDirectory);
        
        // Generate PDF
        PdfBuilder builder = null;
        try (PDDocument document = new PDDocument()) {
            builder = new PdfBuilder(document);
            builder.buildPayslip(data);
            builder.close(); // Ensure all content streams are closed
            document.save(pdfFile);
        } catch (IOException e) {
            // Ensure builder is closed even if there's an error
            if (builder != null) {
                try {
                    builder.close();
                } catch (IOException closeException) {
                    // Log but don't throw - original exception is more important
                    closeException.printStackTrace();
                }
            }
            throw e; // Re-throw the original exception
        }
        
        return pdfFile;
    }

    private PayslipData preparePayslipData(Payslip payslip) {
        Optional<YearToDateFigures> ytdOpt = payslipService.getYearToDateFigures(
                payslip.getEmployeeID(), 
                Year.from(payslip.getPayrollRunDate().toLocalDate())
        );
        YearToDateFigures ytd = ytdOpt.orElse(new YearToDateFigures(
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO
        ));

        String employeeEmail = userService.fetchEmailByEmployeeId(payslip.getEmployeeID())
                .map(user -> user.getEmail())
                .orElse("N/A");

        return new PayslipData(payslip, ytd, employeeEmail);
    }

    private File createOutputFile(Payslip payslip, String outputDirectory) {
        File dir = new File(outputDirectory);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String employeeName = payslip.getEmployeeID().getLastName().toUpperCase() + "_" 
                + payslip.getEmployeeID().getFirstName().toUpperCase();
        String fileName = "Payslip_" + payslip.getPayslipNumber() + "_" + employeeName + ".pdf";
        return new File(dir, fileName);
    }

    /**
     * Data container for payslip generation.
     */
    private static class PayslipData {
        final Payslip payslip;
        final YearToDateFigures ytd;
        final String employeeEmail;

        PayslipData(Payslip payslip, YearToDateFigures ytd, String employeeEmail) {
            this.payslip = payslip;
            this.ytd = ytd;
            this.employeeEmail = employeeEmail;
        }
    }

    /**
     * PDF Builder class following builder pattern best practices.
     */
    private class PdfBuilder {
        private final PDDocument document;
        private PageManager pageManager;
        private PdfFonts fonts;

        PdfBuilder(PDDocument document) {
            this.document = document;
            this.fonts = new PdfFonts();
        }

        void buildPayslip(PayslipData data) throws IOException {
            try {
                pageManager = new PageManager(document);
                
                drawHeader();
                pageManager.addSpacing(SECTION_SPACING);
                
                drawPayrollInfo(data.payslip);
                pageManager.addSpacing(SECTION_SPACING);
                
                drawEmployeeInfo(data);
                pageManager.addSpacing(SECTION_SPACING);
                
                drawEarnings(data.payslip);
                pageManager.addSpacing(SECTION_SPACING);
                
                drawBenefits(data.payslip);
                pageManager.addSpacing(SECTION_SPACING);
                
                drawDeductions(data.payslip);
                pageManager.addSpacing(SECTION_SPACING);
                
                drawSummary(data.payslip);
                pageManager.addSpacing(SECTION_SPACING);
                
                drawYearToDate(data.ytd);
            } catch (IOException e) {
                // Ensure pageManager is closed even if there's an exception
                if (pageManager != null) {
                    try {
                        pageManager.close();
                    } catch (IOException closeException) {
                        // Log but don't suppress original exception
                        closeException.printStackTrace();
                    }
                }
                throw e; // Re-throw the original exception
            }
        }

        void close() throws IOException {
            if (pageManager != null) {
                try {
                    pageManager.close();
                } catch (IOException e) {
                    // Log but re-throw to ensure caller knows about the issue
                    e.printStackTrace();
                    throw e;
                }
            }
        }

        private void drawHeader() throws IOException {
            pageManager.ensureSpace(120);
            PdfTextHelper textHelper = new PdfTextHelper(pageManager.getContentStream(), fonts, pageManager);
            
            textHelper.drawCenteredText("MOTORPH ENTERPRISE", fonts.titleFont, 22, pageManager.getY());
            pageManager.moveDown(25);
            
            textHelper.drawCenteredText("EMPLOYEE PAYSLIP", fonts.titleFont, 16, pageManager.getY());
            pageManager.moveDown(20);
            
            textHelper.drawCenteredText("7 Jupiter Avenue cor. F. Sandoval Jr., Bagong Nayon, Quezon City", fonts.normalFont, 9, pageManager.getY());
            pageManager.moveDown(14);
            
            textHelper.drawCenteredText("Phone: (028) 911-5071 / (028) 911-5072 / (028) 911-5073", fonts.normalFont, 9, pageManager.getY());
            pageManager.moveDown(14);
            
            textHelper.drawCenteredText("Email: corporate@motorph.com", fonts.normalFont, 9, pageManager.getY());
            pageManager.moveDown(18);
            
            pageManager.drawLine(1.5f);
            pageManager.moveDown(15);
        }

        private void drawPayrollInfo(Payslip payslip) throws IOException {
            pageManager.ensureSpace(60);
            PdfTextHelper textHelper = new PdfTextHelper(pageManager.getContentStream(), fonts, pageManager);
            
            String payrollRunDate = DATE_FORMAT_FULL.format(payslip.getPayrollRunDate());
            String periodStart = DATE_FORMAT_SHORT.format(payslip.getPeriodStartDate());
            String periodEnd = DATE_FORMAT_SHORT.format(payslip.getPeriodEndDate());
            String payPeriod = periodStart + " - " + periodEnd;
            
            float boxWidth = (pageManager.getContentWidth() - 15) / 2;
            float boxHeight = 45;
            
            // Payroll Run Box
            pageManager.drawBox(boxWidth, boxHeight);
            textHelper.drawText("PAYROLL RUN", fonts.headerFont, 10, MARGIN + 12, pageManager.getY() - 18);
            textHelper.drawText(payrollRunDate, fonts.normalFont, 10, MARGIN + 12, pageManager.getY() - 33);
            
            // Pay Period Box
            pageManager.drawBox(boxWidth, boxHeight, MARGIN + boxWidth + 15);
            textHelper.drawText("PAY PERIOD", fonts.headerFont, 10, MARGIN + boxWidth + 27, pageManager.getY() - 18);
            textHelper.drawText(payPeriod, fonts.normalFont, 10, MARGIN + boxWidth + 27, pageManager.getY() - 33);
            
            pageManager.moveDown(boxHeight + 15);
        }

        private void drawEmployeeInfo(PayslipData data) throws IOException {
            pageManager.ensureSpace(150);
            Payslip payslip = data.payslip;
            PdfTextHelper textHelper = new PdfTextHelper(pageManager.getContentStream(), fonts, pageManager);
            
            textHelper.drawText("EMPLOYEE INFORMATION", fonts.headerFont, 11, MARGIN, pageManager.getY());
            pageManager.moveDown(18);
            
            float startY = pageManager.getY();
            float labelWidth = 110;
            float col1X = MARGIN;
            float col2X = MARGIN + pageManager.getContentWidth() / 2 + 10;
            
            // Column 1
            textHelper.drawInfoRow(col1X, pageManager.getY(), "Employee Name:", 
                    payslip.getEmployeeID().getFirstName() + " " + payslip.getEmployeeID().getLastName(), labelWidth);
            pageManager.moveDown(16);
            
            textHelper.drawInfoRow(col1X, pageManager.getY(), "Employee ID:", 
                    String.valueOf(payslip.getEmployeeID().getEmployeeNumber()), labelWidth);
            pageManager.moveDown(16);
            
            textHelper.drawInfoRow(col1X, pageManager.getY(), "Email:", data.employeeEmail, labelWidth);
            pageManager.moveDown(16);
            
            textHelper.drawInfoRow(col1X, pageManager.getY(), "Position:", 
                    payslip.getEmployeePosition() != null ? payslip.getEmployeePosition().getPositionName() : "N/A", labelWidth);
            pageManager.moveDown(16);
            
            textHelper.drawInfoRow(col1X, pageManager.getY(), "Department:", 
                    payslip.getEmployeeDepartment() != null ? payslip.getEmployeeDepartment().getDepartmentName() : "N/A", labelWidth);
            pageManager.moveDown(16);
            
            // Column 2
            pageManager.setY(startY);
            Employee emp = payslip.getEmployeeID();
            
            textHelper.drawInfoRow(col2X, pageManager.getY(), "Hourly Rate:", 
                    toPdfSafePeso(PesoUtility.formatToPeso(String.valueOf(payslip.getHourlyRate()))), labelWidth);
            pageManager.moveDown(16);
            
            textHelper.drawInfoRow(col2X, pageManager.getY(), "Monthly Rate:", 
                    toPdfSafePeso(PesoUtility.formatToPeso(String.valueOf(payslip.getMonthlyRate()))), labelWidth);
            pageManager.moveDown(16);
            
            textHelper.drawInfoRow(col2X, pageManager.getY(), "Date Hired:", 
                    DATE_FORMAT_SHORT.format(emp.getDateHired()), labelWidth);
            pageManager.moveDown(16);
            
            textHelper.drawInfoRow(col2X, pageManager.getY(), "TIN:", 
                    emp.getTINNumber() != null ? emp.getTINNumber() : "N/A", labelWidth);
            pageManager.moveDown(16);
            
            textHelper.drawInfoRow(col2X, pageManager.getY(), "SSS:", 
                    emp.getSSSNumber() != null ? emp.getSSSNumber() : "N/A", labelWidth);
            pageManager.moveDown(16);
            
            textHelper.drawInfoRow(col2X, pageManager.getY(), "Philhealth:", 
                    emp.getPhilhealthNumber() != null ? emp.getPhilhealthNumber() : "N/A", labelWidth);
            pageManager.moveDown(16);
            
            textHelper.drawInfoRow(col2X, pageManager.getY(), "Pag-IBIG:", 
                    emp.getPagibigNumber() != null ? emp.getPagibigNumber() : "N/A", labelWidth);
            pageManager.moveDown(5);
        }

        private void drawEarnings(Payslip payslip) throws IOException {
            pageManager.ensureSpace(120);
            PdfTextHelper textHelper = new PdfTextHelper(pageManager.getContentStream(), fonts, pageManager);
            PdfTableHelper tableHelper = new PdfTableHelper(pageManager.getContentStream(), fonts);
            
            textHelper.drawText("EARNINGS", fonts.headerFont, 11, MARGIN, pageManager.getY());
            pageManager.moveDown(18);
            
            tableHelper.drawTableHeader(new String[]{"Description", "Rate/Hours", "Amount"}, 
                    new float[]{220, 180, 150}, MARGIN, pageManager.getY());
            pageManager.moveDown(18);
            
            BigDecimal regularHoursPay = payslip.getTotalHoursWorked().subtract(payslip.getOvertimeHours())
                    .multiply(payslip.getHourlyRate());
            
            tableHelper.drawTableRow("Regular Hours", 
                    payslip.getTotalHoursWorked().subtract(payslip.getOvertimeHours()) + " hrs @ " 
                    + toPdfSafePeso(PesoUtility.formatToPeso(String.valueOf(payslip.getHourlyRate()))) + "/hr",
                    toPdfSafePeso(PesoUtility.formatToPeso(String.valueOf(regularHoursPay))),
                    new float[]{220, 180, 150}, MARGIN, pageManager.getY());
            pageManager.moveDown(16);
            
            if (payslip.getOvertimeHours().compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal overtimePay = payslip.getOvertimeHours().multiply(payslip.getHourlyRate())
                        .multiply(BigDecimal.valueOf(1.25));
                tableHelper.drawTableRow("Overtime Hours", 
                        payslip.getOvertimeHours() + " hrs @ 1.25x",
                        toPdfSafePeso(PesoUtility.formatToPeso(String.valueOf(overtimePay))),
                        new float[]{220, 180, 150}, MARGIN, pageManager.getY());
                pageManager.moveDown(16);
            }
            
            if (payslip.getBonus() != null && payslip.getBonus().compareTo(BigDecimal.ZERO) > 0) {
                tableHelper.drawTableRow("Taxable Bonus", "-",
                        toPdfSafePeso(PesoUtility.formatToPeso(String.valueOf(payslip.getBonus()))),
                        new float[]{220, 180, 150}, MARGIN, pageManager.getY());
                pageManager.moveDown(16);
            }
            
            pageManager.moveDown(8);
            pageManager.drawLine(1f);
            pageManager.moveDown(12);
            
            textHelper.drawRightAlignedText("GROSS PAY:", 
                    toPdfSafePeso(PesoUtility.formatToPeso(String.valueOf(payslip.getGrossIncome()))),
                    fonts.headerFont, fonts.normalFont, 10, MARGIN, pageManager.getContentWidth(), pageManager.getY());
            pageManager.moveDown(12);
        }

        private void drawBenefits(Payslip payslip) throws IOException {
            pageManager.ensureSpace(100);
            PdfTextHelper textHelper = new PdfTextHelper(pageManager.getContentStream(), fonts, pageManager);
            PdfTableHelper tableHelper = new PdfTableHelper(pageManager.getContentStream(), fonts);
            
            textHelper.drawText("BENEFITS", fonts.headerFont, 11, MARGIN, pageManager.getY());
            pageManager.moveDown(18);
            
            tableHelper.drawTableHeader(new String[]{"Description", "", "Amount"}, 
                    new float[]{400, 0, 150}, MARGIN, pageManager.getY());
            pageManager.moveDown(18);
            
            tableHelper.drawTableRow("Rice Subsidy", "",
                    toPdfSafePeso(PesoUtility.formatToPeso(String.valueOf(payslip.getRiceSubsidy()))),
                    new float[]{400, 0, 150}, MARGIN, pageManager.getY());
            pageManager.moveDown(16);
            
            tableHelper.drawTableRow("Phone Allowance", "",
                    toPdfSafePeso(PesoUtility.formatToPeso(String.valueOf(payslip.getPhoneAllowance()))),
                    new float[]{400, 0, 150}, MARGIN, pageManager.getY());
            pageManager.moveDown(16);
            
            tableHelper.drawTableRow("Clothing Allowance", "",
                    toPdfSafePeso(PesoUtility.formatToPeso(String.valueOf(payslip.getClothingAllowance()))),
                    new float[]{400, 0, 150}, MARGIN, pageManager.getY());
            pageManager.moveDown(8);
            
            pageManager.drawLine(1f);
            pageManager.moveDown(12);
            
            textHelper.drawRightAlignedText("TOTAL BENEFITS:", 
                    toPdfSafePeso(PesoUtility.formatToPeso(String.valueOf(payslip.getTotalBenefits()))),
                    fonts.headerFont, fonts.normalFont, 10, MARGIN, pageManager.getContentWidth(), pageManager.getY());
            pageManager.moveDown(12);
        }

        private void drawDeductions(Payslip payslip) throws IOException {
            pageManager.ensureSpace(120);
            PdfTextHelper textHelper = new PdfTextHelper(pageManager.getContentStream(), fonts, pageManager);
            PdfTableHelper tableHelper = new PdfTableHelper(pageManager.getContentStream(), fonts);
            
            textHelper.drawText("DEDUCTIONS", fonts.headerFont, 11, MARGIN, pageManager.getY());
            pageManager.moveDown(18);
            
            tableHelper.drawTableHeader(new String[]{"Description", "", "Amount"}, 
                    new float[]{400, 0, 150}, MARGIN, pageManager.getY());
            pageManager.moveDown(18);
            
            tableHelper.drawTableRow("SSS", "",
                    toPdfSafePeso(PesoUtility.formatToPeso(String.valueOf(payslip.getSss()))),
                    new float[]{400, 0, 150}, MARGIN, pageManager.getY());
            pageManager.moveDown(16);
            
            tableHelper.drawTableRow("Philhealth", "",
                    toPdfSafePeso(PesoUtility.formatToPeso(String.valueOf(payslip.getPhilhealth()))),
                    new float[]{400, 0, 150}, MARGIN, pageManager.getY());
            pageManager.moveDown(16);
            
            tableHelper.drawTableRow("Pag-IBIG", "",
                    toPdfSafePeso(PesoUtility.formatToPeso(String.valueOf(payslip.getPagIbig()))),
                    new float[]{400, 0, 150}, MARGIN, pageManager.getY());
            pageManager.moveDown(16);
            
            tableHelper.drawTableRow("Withholding Tax", "",
                    toPdfSafePeso(PesoUtility.formatToPeso(String.valueOf(payslip.getWithholdingTax()))),
                    new float[]{400, 0, 150}, MARGIN, pageManager.getY());
            pageManager.moveDown(16);
            
            if (payslip.getOtherDeductions() != null && payslip.getOtherDeductions().compareTo(BigDecimal.ZERO) > 0) {
                tableHelper.drawTableRow("Other Deductions", "",
                        toPdfSafePeso(PesoUtility.formatToPeso(String.valueOf(payslip.getOtherDeductions()))),
                        new float[]{400, 0, 150}, MARGIN, pageManager.getY());
                pageManager.moveDown(16);
            }
            
            pageManager.moveDown(8);
            pageManager.drawLine(1f);
            pageManager.moveDown(12);
            
            textHelper.drawRightAlignedText("TOTAL DEDUCTIONS:", 
                    toPdfSafePeso(PesoUtility.formatToPeso(String.valueOf(payslip.getTotalDeductions()))),
                    fonts.headerFont, fonts.normalFont, 10, MARGIN, pageManager.getContentWidth(), pageManager.getY());
            pageManager.moveDown(12);
        }

        private void drawSummary(Payslip payslip) throws IOException {
            pageManager.ensureSpace(100);
            PdfTextHelper textHelper = new PdfTextHelper(pageManager.getContentStream(), fonts, pageManager);
            
            float boxHeight = 85;
            pageManager.drawBox(pageManager.getContentWidth(), boxHeight);
            pageManager.moveDown(18);
            
            textHelper.drawText("SUMMARY", fonts.headerFont, 11, MARGIN + 12, pageManager.getY());
            pageManager.moveDown(18);
            
            float labelWidth = 120;
            textHelper.drawInfoRow(MARGIN + 12, pageManager.getY(), "Gross Income:", 
                    toPdfSafePeso(PesoUtility.formatToPeso(String.valueOf(payslip.getGrossIncome()))), labelWidth);
            pageManager.moveDown(15);
            
            textHelper.drawInfoRow(MARGIN + 12, pageManager.getY(), "Total Benefits:", 
                    toPdfSafePeso(PesoUtility.formatToPeso(String.valueOf(payslip.getTotalBenefits()))), labelWidth);
            pageManager.moveDown(15);
            
            textHelper.drawInfoRow(MARGIN + 12, pageManager.getY(), "Total Deductions:", 
                    toPdfSafePeso(PesoUtility.formatToPeso(String.valueOf(payslip.getTotalDeductions()))), labelWidth);
            pageManager.moveDown(8);
            
            pageManager.drawLine(1.5f, MARGIN + 12, pageManager.getContentWidth() - 24);
            pageManager.moveDown(12);
            
            textHelper.drawText("NET PAY:", fonts.headerFont, 12, MARGIN + 12, pageManager.getY());
            String netPayAmount = toPdfSafePeso(PesoUtility.formatToPeso(String.valueOf(payslip.getNetPay())));
            float netPayAmountWidth = fonts.headerFont.getStringWidth(netPayAmount) / 1000f * 14;
            textHelper.drawText(netPayAmount, fonts.headerFont, 14, 
                    MARGIN + pageManager.getContentWidth() - 12 - netPayAmountWidth, pageManager.getY());
            pageManager.moveDown(15);
        }

        private void drawYearToDate(YearToDateFigures ytd) throws IOException {
            pageManager.ensureSpace(120);
            PdfTextHelper textHelper = new PdfTextHelper(pageManager.getContentStream(), fonts, pageManager);
            
            float boxHeight = 110;
            pageManager.drawBox(pageManager.getContentWidth(), boxHeight);
            pageManager.moveDown(18);
            
            textHelper.drawText("YEAR TO DATE FIGURES", fonts.headerFont, 11, MARGIN + 12, pageManager.getY());
            pageManager.moveDown(18);
            
            float labelWidth = 110;
            float col1X = MARGIN + 12;
            float col2X = MARGIN + pageManager.getContentWidth() / 2 + 12;
            float startY = pageManager.getY();
            
            textHelper.drawInfoRow(col1X, pageManager.getY(), "Gross Income:", 
                    toPdfSafePeso(PesoUtility.formatToPeso(String.valueOf(ytd.ytdGrossIncome()))), labelWidth);
            pageManager.moveDown(15);
            
            textHelper.drawInfoRow(col1X, pageManager.getY(), "Taxable Income:", 
                    toPdfSafePeso(PesoUtility.formatToPeso(String.valueOf(ytd.ytdTaxableIncome()))), labelWidth);
            pageManager.moveDown(15);
            
            textHelper.drawInfoRow(col1X, pageManager.getY(), "Total Benefits:", 
                    toPdfSafePeso(PesoUtility.formatToPeso(String.valueOf(ytd.ytdTotalBenefits()))), labelWidth);
            pageManager.moveDown(15);
            
            pageManager.setY(startY);
            textHelper.drawInfoRow(col2X, pageManager.getY(), "Withholding Tax:", 
                    toPdfSafePeso(PesoUtility.formatToPeso(String.valueOf(ytd.ytdWitholdingTax()))), labelWidth);
            pageManager.moveDown(15);
            
            textHelper.drawInfoRow(col2X, pageManager.getY(), "Total Deductions:", 
                    toPdfSafePeso(PesoUtility.formatToPeso(String.valueOf(ytd.ytdTotalDeductions()))), labelWidth);
            pageManager.moveDown(15);
            
            textHelper.drawInfoRow(col2X, pageManager.getY(), "Net Pay:", 
                    toPdfSafePeso(PesoUtility.formatToPeso(String.valueOf(ytd.ytdNetPay()))), labelWidth);
            pageManager.moveDown(5);
        }
    }

    /**
     * Manages PDF pages and content streams with proper resource handling.
     */
    private static class PageManager {
        private final PDDocument document;
        private PDPage currentPage;
        private PDPageContentStream contentStream;
        private float yPosition;
        private final float pageWidth;
        private final float pageHeight;
        private final float contentWidth;

        PageManager(PDDocument document) throws IOException {
            this.document = document;
            this.pageWidth = PDRectangle.A4.getWidth();
            this.pageHeight = PDRectangle.A4.getHeight();
            this.contentWidth = pageWidth - (2 * MARGIN);
            createNewPage();
        }

        private void createNewPage() throws IOException {
            if (contentStream != null) {
                contentStream.close();
            }
            currentPage = new PDPage(PDRectangle.A4);
            document.addPage(currentPage);
            contentStream = new PDPageContentStream(document, currentPage);
            yPosition = pageHeight - MARGIN;
        }

        void ensureSpace(float requiredHeight) throws IOException {
            if (yPosition - requiredHeight < BOTTOM_MARGIN) {
                createNewPage();
            }
        }

        void addSpacing(float spacing) {
            yPosition -= spacing;
        }

        void moveDown(float amount) {
            yPosition -= amount;
        }

        void drawLine(float lineWidth) throws IOException {
            drawLine(lineWidth, MARGIN, contentWidth);
        }

        void drawLine(float lineWidth, float x, float width) throws IOException {
            contentStream.setLineWidth(lineWidth);
            contentStream.moveTo(x, yPosition);
            contentStream.lineTo(x + width, yPosition);
            contentStream.stroke();
        }

        void drawBox(float width, float height) throws IOException {
            drawBox(width, height, MARGIN);
        }

        void drawBox(float width, float height, float x) throws IOException {
            contentStream.setLineWidth(1f);
            contentStream.addRect(x, yPosition - height, width, height);
            contentStream.stroke();
        }

        PDPageContentStream getContentStream() {
            return contentStream;
        }

        float getY() {
            return yPosition;
        }

        void setY(float y) {
            this.yPosition = y;
        }

        float getContentWidth() {
            return contentWidth;
        }

        void close() throws IOException {
            if (contentStream != null) {
                contentStream.close();
            }
        }
    }

    /**
     * Manages PDF fonts.
     */
    private static class PdfFonts {
        final PDType1Font titleFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
        final PDType1Font headerFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
        final PDType1Font normalFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
    }

    /**
     * Helper class for text operations in PDF.
     */
    private static class PdfTextHelper {
        private final PDPageContentStream contentStream;
        private final PdfFonts fonts;
        private final PageManager pageManager;

        PdfTextHelper(PDPageContentStream contentStream, PdfFonts fonts, PageManager pageManager) {
            this.contentStream = contentStream;
            this.fonts = fonts;
            this.pageManager = pageManager;
        }

        void drawText(String text, PDType1Font font, float fontSize, float x, float y) throws IOException {
            contentStream.setFont(font, fontSize);
            contentStream.beginText();
            contentStream.newLineAtOffset(x, y);
            contentStream.showText(text);
            contentStream.endText();
        }

        void drawCenteredText(String text, PDType1Font font, float fontSize, float y) throws IOException {
            float textWidth = font.getStringWidth(text) / 1000f * fontSize;
            float x = MARGIN + (pageManager.getContentWidth() - textWidth) / 2;
            drawText(text, font, fontSize, x, y);
        }

        void drawRightAlignedText(String label, String value, PDType1Font labelFont, 
                                  PDType1Font valueFont, float fontSize, float x, float width, float y) throws IOException {
            float labelWidth = labelFont.getStringWidth(label) / 1000f * fontSize;
            float valueWidth = valueFont.getStringWidth(value) / 1000f * fontSize;
            
            drawText(label, labelFont, fontSize, x + width - 150 - labelWidth, y);
            drawText(value, valueFont, fontSize, x + width - valueWidth, y);
        }

        void drawInfoRow(float x, float y, String label, String value, float labelWidth) throws IOException {
            drawText(label, fonts.normalFont, 9, x, y);
            drawText(value, fonts.normalFont, 9, x + labelWidth, y);
        }
    }

    /**
     * Helper class for table operations in PDF.
     */
    private static class PdfTableHelper {
        private final PDPageContentStream contentStream;
        private final PdfFonts fonts;

        PdfTableHelper(PDPageContentStream contentStream, PdfFonts fonts) {
            this.contentStream = contentStream;
            this.fonts = fonts;
        }

        void drawTableHeader(String[] headers, float[] widths, float x, float y) throws IOException {
            float currentX = x;
            float totalWidth = 0;
            for (float w : widths) {
                totalWidth += w;
            }
            
            contentStream.setFont(fonts.normalFont, 9);
            for (int i = 0; i < headers.length; i++) {
                if (widths[i] > 0) {
                    contentStream.beginText();
                    contentStream.newLineAtOffset(currentX + 3, y);
                    contentStream.showText(headers[i]);
                    contentStream.endText();
                }
                currentX += widths[i];
            }
            
            y -= 6;
            contentStream.setLineWidth(1f);
            contentStream.moveTo(x, y);
            contentStream.lineTo(x + totalWidth, y);
            contentStream.stroke();
        }

        void drawTableRow(String description, String rate, String amount, float[] widths, float x, float y) throws IOException {
            contentStream.setFont(fonts.normalFont, 9);
            float currentX = x;
            
            if (widths[0] > 0) {
                contentStream.beginText();
                contentStream.newLineAtOffset(currentX + 3, y);
                contentStream.showText(description);
                contentStream.endText();
            }
            currentX += widths[0];
            
            if (widths.length > 1 && widths[1] > 0) {
                contentStream.beginText();
                contentStream.newLineAtOffset(currentX + 3, y);
                contentStream.showText(rate);
                contentStream.endText();
            }
            currentX += widths.length > 1 ? widths[1] : 0;
            
            if (widths.length > 2 && widths[2] > 0) {
                float amountWidth = fonts.normalFont.getStringWidth(amount) / 1000f * 9;
                contentStream.beginText();
                contentStream.newLineAtOffset(currentX + widths[2] - amountWidth - 3, y);
                contentStream.showText(amount);
                contentStream.endText();
            }
        }
    }

    /**
     * Converts peso-formatted string to PDF-safe format.
     */
    private static String toPdfSafePeso(String pesoFormattedString) {
        if (pesoFormattedString == null) {
            return "";
        }
        return pesoFormattedString.replace("₱", "PHP ");
    }
}
