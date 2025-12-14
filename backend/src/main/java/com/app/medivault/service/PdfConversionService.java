package com.app.medivault.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class PdfConversionService {

    /**
     * Convert text file to PDF
     */
    public byte[] convertTextToPdf(byte[] textBytes) throws IOException {
        String text = new String(textBytes);
        
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(50, 750);
                
                // Split text into lines and add to PDF
                String[] lines = text.split("\n");
                float leading = 14.5f;
                
                for (String line : lines) {
                    // Handle line wrapping for long lines
                    if (line.length() > 80) {
                        List<String> wrappedLines = wrapText(line, 80);
                        for (String wrappedLine : wrappedLines) {
                            contentStream.showText(wrappedLine);
                            contentStream.newLineAtOffset(0, -leading);
                        }
                    } else {
                        contentStream.showText(line);
                        contentStream.newLineAtOffset(0, -leading);
                    }
                }
                
                contentStream.endText();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document.save(baos);
            return baos.toByteArray();
        }
    }

    /**
     * Convert single image to PDF
     */
    public byte[] convertImageToPdf(byte[] imageBytes) throws IOException {
        try (PDDocument document = new PDDocument()) {
            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imageBytes));
            
            PDPage page = new PDPage(new PDRectangle(bufferedImage.getWidth(), bufferedImage.getHeight()));
            document.addPage(page);

            PDImageXObject pdImage = PDImageXObject.createFromByteArray(document, imageBytes, null);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.drawImage(pdImage, 0, 0);
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document.save(baos);
            return baos.toByteArray();
        }
    }

    /**
     * Merge multiple images into a single PDF
     */
    public byte[] mergeImagesToPdf(List<byte[]> imageBytesList) throws IOException {
        try (PDDocument document = new PDDocument()) {
            for (byte[] imageBytes : imageBytesList) {
                BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imageBytes));
                
                PDPage page = new PDPage(new PDRectangle(bufferedImage.getWidth(), bufferedImage.getHeight()));
                document.addPage(page);

                PDImageXObject pdImage = PDImageXObject.createFromByteArray(document, imageBytes, null);

                try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                    contentStream.drawImage(pdImage, 0, 0);
                }
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document.save(baos);
            return baos.toByteArray();
        }
    }

    /**
     * Merge multiple PDF files into a single PDF
     */
    public byte[] mergePdfs(List<byte[]> pdfBytesList) throws IOException {
        try (PDDocument mergedDocument = new PDDocument()) {
            for (byte[] pdfBytes : pdfBytesList) {
                try (PDDocument document = org.apache.pdfbox.Loader.loadPDF(pdfBytes)) {
                    for (PDPage page : document.getPages()) {
                        mergedDocument.addPage(page);
                    }
                }
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            mergedDocument.save(baos);
            return baos.toByteArray();
        }
    }

    /**
     * Check if file is an image
     */
    public boolean isImageFile(String filename) {
        String lower = filename.toLowerCase();
        return lower.endsWith(".jpg") || lower.endsWith(".jpeg") || 
               lower.endsWith(".png") || lower.endsWith(".gif") || 
               lower.endsWith(".bmp") || lower.endsWith(".tiff");
    }

    /**
     * Check if file is a text file
     */
    public boolean isTextFile(String filename) {
        String lower = filename.toLowerCase();
        return lower.endsWith(".txt") || lower.endsWith(".text");
    }

    /**
     * Check if file is already a PDF
     */
    public boolean isPdfFile(String filename) {
        return filename.toLowerCase().endsWith(".pdf");
    }

    /**
     * Wrap text for PDF display
     */
    private List<String> wrapText(String text, int maxLength) {
        List<String> lines = new java.util.ArrayList<>();
        String[] words = text.split(" ");
        StringBuilder currentLine = new StringBuilder();
        
        for (String word : words) {
            if (currentLine.length() + word.length() + 1 > maxLength) {
                lines.add(currentLine.toString());
                currentLine = new StringBuilder(word);
            } else {
                if (currentLine.length() > 0) {
                    currentLine.append(" ");
                }
                currentLine.append(word);
            }
        }
        
        if (currentLine.length() > 0) {
            lines.add(currentLine.toString());
        }
        
        return lines;
    }
}
