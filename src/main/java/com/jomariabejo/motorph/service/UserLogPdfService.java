package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.model.UserLog;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

/**
 * Service for generating PDF documents for individual user logs.
 */
public class UserLogPdfService {

    private static final float MARGIN = 50f;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Generates a PDF document for a single user log.
     *
     * @param log The user log to generate PDF for
     * @param outputFile The file where PDF will be saved
     * @throws IOException If PDF generation fails
     */
    public void generateLogPdf(UserLog log, File outputFile) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                PDType1Font titleFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
                PDType1Font headerFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
                PDType1Font normalFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA);

                float pageWidth = PDRectangle.A4.getWidth();
                float pageHeight = PDRectangle.A4.getHeight();
                float yPosition = pageHeight - MARGIN;
                float contentWidth = pageWidth - (2 * MARGIN);

                // Title
                drawCenteredText(contentStream, "MOTORPH ENTERPRISE", titleFont, 24, pageWidth, yPosition);
                yPosition -= 30;

                drawCenteredText(contentStream, "USER LOG DETAILS", headerFont, 18, pageWidth, yPosition);
                yPosition -= 40;

                // Draw line separator
                contentStream.setLineWidth(2f);
                contentStream.moveTo(MARGIN, yPosition);
                contentStream.lineTo(pageWidth - MARGIN, yPosition);
                contentStream.stroke();
                yPosition -= 30;

                // Log Details Section
                drawText(contentStream, "LOG INFORMATION", headerFont, 14, MARGIN, yPosition);
                yPosition -= 25;

                // Log ID
                yPosition = drawDetailRow(contentStream, "Log ID:", String.valueOf(log.getId()), 
                        normalFont, MARGIN, yPosition, contentWidth);

                // User Information
                String userInfo = log.getUserID() != null 
                        ? log.getUserID().getFullName() + " (" + log.getUserID().getUsername() + ")"
                        : "N/A";
                yPosition = drawDetailRow(contentStream, "User:", userInfo, 
                        normalFont, MARGIN, yPosition, contentWidth);

                // Action
                String action = log.getAction() != null ? log.getAction() : "N/A";
                yPosition = drawDetailRow(contentStream, "Action:", action, 
                        normalFont, MARGIN, yPosition, contentWidth);

                // IP Address
                String ipAddress = log.getIPAddress() != null ? log.getIPAddress() : "N/A";
                yPosition = drawDetailRow(contentStream, "IP Address:", ipAddress, 
                        normalFont, MARGIN, yPosition, contentWidth);

                // Date/Time
                String dateTime = log.getLogDateTime() != null 
                        ? log.getLogDateTime().format(DATE_TIME_FORMATTER)
                        : "N/A";
                yPosition = drawDetailRow(contentStream, "Date/Time:", dateTime, 
                        normalFont, MARGIN, yPosition, contentWidth);

                yPosition -= 20;

                // Draw line separator
                contentStream.setLineWidth(1f);
                contentStream.moveTo(MARGIN, yPosition);
                contentStream.lineTo(pageWidth - MARGIN, yPosition);
                contentStream.stroke();
                yPosition -= 30;

                // Footer
                String footerText = "Generated on " + java.time.LocalDateTime.now().format(DATE_TIME_FORMATTER);
                drawCenteredText(contentStream, footerText, normalFont, 10, pageWidth, 50);
            }

            document.save(outputFile);
        }
    }

    private void drawText(PDPageContentStream contentStream, String text, PDType1Font font, 
                          float fontSize, float x, float y) throws IOException {
        contentStream.setFont(font, fontSize);
        contentStream.beginText();
        contentStream.newLineAtOffset(x, y);
        contentStream.showText(text);
        contentStream.endText();
    }

    private void drawCenteredText(PDPageContentStream contentStream, String text, PDType1Font font, 
                                  float fontSize, float pageWidth, float y) throws IOException {
        float textWidth = font.getStringWidth(text) / 1000f * fontSize;
        float x = (pageWidth - textWidth) / 2;
        drawText(contentStream, text, font, fontSize, x, y);
    }

    private float drawDetailRow(PDPageContentStream contentStream, String label, String value, 
                                PDType1Font font, float x, float y, float contentWidth) throws IOException {
        float labelWidth = 120f;
        float fontSize = 11f;

        // Draw label
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), fontSize);
        contentStream.beginText();
        contentStream.newLineAtOffset(x, y);
        contentStream.showText(label);
        contentStream.endText();

        // Draw value (wrap if too long)
        contentStream.setFont(font, fontSize);
        String[] valueLines = wrapText(value, contentWidth - labelWidth - 20, font, fontSize);
        
        float currentY = y;
        for (String line : valueLines) {
            contentStream.beginText();
            contentStream.newLineAtOffset(x + labelWidth, currentY);
            contentStream.showText(line);
            contentStream.endText();
            currentY -= 15; // Line spacing
        }

        return currentY - 10; // Return new Y position with spacing
    }

    private String[] wrapText(String text, float maxWidth, PDType1Font font, float fontSize) {
        if (text == null || text.isEmpty()) {
            return new String[]{"N/A"};
        }

        try {
            // Simple word wrapping
            java.util.List<String> lines = new java.util.ArrayList<>();
            String[] words = text.split(" ");
            StringBuilder currentLine = new StringBuilder();

            for (String word : words) {
                String testLine = currentLine.isEmpty() ? word : currentLine + " " + word;
                float lineWidth = font.getStringWidth(testLine) / 1000f * fontSize;

                if (lineWidth <= maxWidth) {
                    currentLine.append(currentLine.isEmpty() ? "" : " ").append(word);
                } else {
                    if (!currentLine.isEmpty()) {
                        lines.add(currentLine.toString());
                        currentLine = new StringBuilder(word);
                    } else {
                        // Word is too long, add it anyway
                        lines.add(word);
                    }
                }
            }

            if (!currentLine.isEmpty()) {
                lines.add(currentLine.toString());
            }

            return lines.isEmpty() ? new String[]{"N/A"} : lines.toArray(new String[0]);
        } catch (IOException e) {
            // If wrapping fails, return text as single line
            return new String[]{text};
        }
    }
}

