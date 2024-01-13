package org.hsgt.utils;

import org.junit.jupiter.api.Test;
import org.utils.IoUtils;
import org.utils.JSON;
import org.utils.PdfUtils;

import java.io.File;
import java.io.IOException;

public class PdfTest {
    @Test
    public void b64ToFileTest() throws IOException {
        String s = IoUtils.readFile("G:\\workspace\\java\\localNet\\data\\labels\\.cache\\mock.json");
        String label = new JSON(s).read("$.labels[0]");
        System.out.println(label);
        String out = "G:\\workspace\\java\\localNet\\data\\labels\\.cache\\mock.pdf";
        PdfUtils.b64ToFile(label, new File(out));

    }

}
