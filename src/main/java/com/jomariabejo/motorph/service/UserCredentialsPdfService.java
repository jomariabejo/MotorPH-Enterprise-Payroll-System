package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.model.User;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Service for generating PDF documents containing temporary user credentials.
 */
public class UserCredentialsPdfService {

    private static final float MARGIN = 50f;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Generates a PDF document with temporary user credentials.
     *
     * @param user The user whose credentials are being reset
     * @param temporaryPassword The temporary password generated
     * @param outputFile The file where PDF will be saved
     * @throws IOException If PDF generation fails
     */
    public void generateCredentialsPdf(User user, String temporaryPassword, File outputFile) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                PDType1Font titleFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
                PDType1Font headerFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
                PDType1Font normalFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
                PDType1Font passwordFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);

                float pageWidth = PDRectangle.A4.getWidth();
                float pageHeight = PDRectangle.A4.getHeight();
                float yPosition = pageHeight - MARGIN;
                float contentWidth = pageWidth - (2 * MARGIN);

                // Title
                drawCenteredText(contentStream, "MOTORPH ENTERPRISE", titleFont, 24, pageWidth, yPosition);
                yPosition -= 30;

                drawCenteredText(contentStream, "TEMPORARY CREDENTIALS", headerFont, 18, pageWidth, yPosition);
                yPosition -= 40;

                // Draw line separator
                contentStream.setLineWidth(2f);
                contentStream.moveTo(MARGIN, yPosition);
                contentStream.lineTo(pageWidth - MARGIN, yPosition);
                contentStream.stroke();
                yPosition -= 40;

                // Important Notice
                drawText(contentStream, "IMPORTANT NOTICE", headerFont, 14, MARGIN, yPosition);
                yPosition -= 25;

                String noticeText = """
                        This document contains your temporary login credentials.
                        Please keep this document secure and change your password after first login.
                        """;
                drawWrappedText(contentStream, noticeText, normalFont, 11, MARGIN, yPosition, contentWidth);
                yPosition -= 50;

                // Draw line separator
                contentStream.setLineWidth(1f);
                contentStream.moveTo(MARGIN, yPosition);
                contentStream.lineTo(pageWidth - MARGIN, yPosition);
                contentStream.stroke();
                yPosition -= 40;

                // Credentials Section
                drawText(contentStream, "YOUR LOGIN CREDENTIALS", headerFont, 14, MARGIN, yPosition);
                yPosition -= 30;

                // Username
                yPosition = drawDetailRow(contentStream, "Username:", user.getUsername(), 
                        normalFont, passwordFont, MARGIN, yPosition, contentWidth);

                // Temporary Password
                yPosition = drawDetailRow(contentStream, "Temporary Password:", temporaryPassword, 
                        normalFont, passwordFont, MARGIN, yPosition, contentWidth);

                yPosition -= 20;

                // User Information Section
                drawText(contentStream, "USER INFORMATION", headerFont, 14, MARGIN, yPosition);
                yPosition -= 30;

                // Full Name
                yPosition = drawDetailRow(contentStream, "Full Name:", user.getFullName(), 
                        normalFont, normalFont, MARGIN, yPosition, contentWidth);

                // Email
                yPosition = drawDetailRow(contentStream, "Email:", user.getEmail(), 
                        normalFont, normalFont, MARGIN, yPosition, contentWidth);

                // Role
                String roleName = user.getRoleID() != null ? user.getRoleID().getRoleName() : "N/A";
                yPosition = drawDetailRow(contentStream, "Role:", roleName, 
                        normalFont, normalFont, MARGIN, yPosition, contentWidth);

                yPosition -= 30;

                // Draw line separator
                contentStream.setLineWidth(1f);
                contentStream.moveTo(MARGIN, yPosition);
                contentStream.lineTo(pageWidth - MARGIN, yPosition);
                contentStream.stroke();
                yPosition -= 30;

                // Instructions
                drawText(contentStream, "INSTRUCTIONS", headerFont, 14, MARGIN, yPosition);
                yPosition -= 25;

                String instructions = """
                        1. Use the credentials above to log in to the system.
                        2. After logging in, please change your password immediately.
                        3. Keep this document secure and do not share your credentials.
                        4. If you have any questions, please contact your system administrator.
                        """;

                drawWrappedText(contentStream, instructions, normalFont, 11, MARGIN, yPosition, contentWidth);
                yPosition -= 80;

                // Draw line separator
                contentStream.setLineWidth(1f);
                contentStream.moveTo(MARGIN, yPosition);
                contentStream.lineTo(pageWidth - MARGIN, yPosition);
                contentStream.stroke();
                yPosition -= 30;

                // Footer
                String footerText = String.format("Generated on %s | This is a temporary password. Please change it after first login.",
                        LocalDateTime.now().format(DATE_TIME_FORMATTER));
                drawCenteredText(contentStream, footerText, normalFont, 9, pageWidth, 50);
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
                                PDType1Font labelFont, PDType1Font valueFont, 
                                float x, float y, float contentWidth) throws IOException {
        // Helper method to draw a detail row with label and value
        float labelWidth = 150f;
        float fontSize = 12f;

        // Draw label
        contentStream.setFont(labelFont, fontSize);
        contentStream.beginText();
        contentStream.newLineAtOffset(x, y);
        contentStream.showText(label);
        contentStream.endText();

        // Draw value
        contentStream.setFont(valueFont, fontSize);
        String[] valueLines = wrapText(value, contentWidth - labelWidth - 20, valueFont, fontSize);
        
        float currentY = y;
        for (String line : valueLines) {
            contentStream.beginText();
            contentStream.newLineAtOffset(x + labelWidth, currentY);
            contentStream.showText(line);
            contentStream.endText();
            currentY -= 18; // Line spacing
        }

        return currentY - 10; // Return new Y position with spacing
    }

    private void drawWrappedText(PDPageContentStream contentStream, String text, PDType1Font font, 
                                float fontSize, float x, float y, float maxWidth) throws IOException {
        String[] lines = wrapText(text, maxWidth, font, fontSize);
        float currentY = y;
        
        for (String line : lines) {
            contentStream.setFont(font, fontSize);
            contentStream.beginText();
            contentStream.newLineAtOffset(x, currentY);
            contentStream.showText(line);
            contentStream.endText();
            currentY -= 18; // Line spacing
        }
    }

    private String[] wrapText(String text, float maxWidth, PDType1Font font, float fontSize) {
        if (text == null || text.isEmpty()) {
            return new String[]{"N/A"};
        }

        try {
            // Handle newlines in text
            String[] paragraphs = text.split("\n");
            java.util.List<String> allLines = new java.util.ArrayList<>();
            
            for (String paragraph : paragraphs) {
                if (paragraph.trim().isEmpty()) {
                    allLines.add("");
                    continue;
                }
                
                // Simple word wrapping
                java.util.List<String> lines = new java.util.ArrayList<>();
                String[] words = paragraph.split(" ");
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
                
                allLines.addAll(lines);
            }

            return allLines.isEmpty() ? new String[]{"N/A"} : allLines.toArray(new String[0]);
        } catch (IOException e) {
            // If wrapping fails, return text as single line
            return new String[]{text};
        }
    }
}

