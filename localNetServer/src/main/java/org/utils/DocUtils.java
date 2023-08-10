package org.utils;

import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class DocUtils {

    public static boolean wordToPdf(Path inDocx, Path outPdf) {
        try(FileInputStream in = new FileInputStream(inDocx.toString());
            FileOutputStream out = new FileOutputStream(outPdf.toString())) {
            XWPFDocument document = new XWPFDocument(in);
            PdfOptions options = null;
            PdfConverter.getInstance().convert(document, out, options);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static String pdfToBase64(Path pdfPath) throws IOException {
        byte[] inputFile = Files.readAllBytes(pdfPath);
        byte[] encodedBytes = Base64.getEncoder().encode(inputFile);
        String encodedString = new String(encodedBytes);
        return encodedString;
    }

    public static void base64ToPdf(String encodedStr, Path outPdf) throws IOException {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedStr);
        try(FileOutputStream fos = new FileOutputStream(outPdf.toString())) {
            fos.write(decodedBytes);
            fos.flush();
        }
    }

    public static void main(String[] args) throws IOException {
        Path pdf = Paths.get(".temp/O23-738545023207.pdf");
        Path out = Paths.get(".temp/O23-738545023207qqq.pdf");
        String b64 = DocUtils.pdfToBase64(pdf);
        System.out.println(b64);
        DocUtils.base64ToPdf(b64, out);
    }
}
