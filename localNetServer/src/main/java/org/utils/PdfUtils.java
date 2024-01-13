package org.utils;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import lombok.Data;

import java.io.*;
import java.util.Base64;

public class PdfUtils {

    @Data
    public static class Watermark {
        String text;
        int pageWidth;
        int pageHeight;
        int textPosX;
        int textPosY;
        int[] textColor;  // rgba
        float fontSize;
    }

    public static void b64ToOutputStream(String b64Str, OutputStream outputStream) throws IOException {
        byte[] bytes = Base64.getDecoder().decode(b64Str);
        outputStream.write(bytes);
    }

    public static void b64ToFile(String b64Str, File file) {
        try(FileOutputStream fos = new FileOutputStream(file)) {
            b64ToOutputStream(b64Str, fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createWatermark(Watermark watermark, InputStream inputStream, OutputStream outputStream)  {
        PdfStamper stamper = null;
        PdfReader reader = null;
        try  {
            reader = new PdfReader(inputStream);
            stamper = new PdfStamper(reader, outputStream);
            int totalPages = reader.getNumberOfPages() + 1;
            for (int i = 1; i < totalPages; i++) {
                PdfContentByte content = stamper.getUnderContent(i);

            }
            stamper.close();
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (reader != null) reader.close();
        }
    }
}
