package org.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONObject;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class IoUtils {

    public static String JSONFormatter(String s) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String j2 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(mapper.readTree(s));
        return j2;
    }

    /**
     * @param fname: File name
     * @return String
     * @author Lin
     * @description TODO Read file to string
     * @date 10.Dec.2022 010 01:36
     */
    public static String  readFile(String fname)  {
        String content = "";
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(fname));
            content = new String(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    public static String  readFile(File file) {
        return readFile(file.toString());
    }
//    public static String readFile(InputStream in) throws IOException {
//        // return CharStreams.toString(new InputStreamReader(in, StandardCharsets.UTF_8));
//        byte[] bytes = new byte[in.available()];
//        in.read(bytes);
//        return new String(bytes);
//    }

    /**
     * @param fname: File name
     * @param text: Text to write.
     * @return void
     * @author Lin
     * @description TODO Write string to file
     * @date 10.Dec.2022 010 01:37
     */
    public static void writeFile(String fname, String text) {
        try(FileWriter writer = new FileWriter(fname)) {
            writer.write(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeFile(File file, String text) {
         writeFile(file.toString(), text);
    }

    public static void serialization(File file, Object object) throws IOException {
        try(FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream os = new ObjectOutputStream(fos)) {
            os.writeObject(object);
        }
    }

    public static Object deserialization(File file) throws IOException, ClassNotFoundException {
        Object object;
        try(FileInputStream fis = new FileInputStream(file);
            ObjectInputStream is = new ObjectInputStream(fis)) {
            object = is.readObject();
        }
        return object;
    }

    public static String currentDateTime() {
        return currentDateTime("yyyy-MM-dd HH:mm:ss");
    }

    public static String currentDateTime(String format) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern(format);
        String datetime = now.format(myFormatObj);
        return datetime;
    }

    public static JSONObject beanToJson(Object bean)  {
        if (bean == null) {
            return null;
        } else {
            // String s = new Gson().toJson(bean);
            String s = new GsonBuilder().serializeNulls().create().toJson(bean);
            return new JSONObject(s);
        }
    }

    public static void makedir(Path path) {
        if (!Files.exists(path)) {
            path.toFile().mkdirs();
        }
    }

    public static int delay(long millis) {
        try {
            Thread.sleep(millis);
            return 0;
        } catch (InterruptedException e) {
            System.err.println(e);
            return 1;
        }
    }

    public static int delay(long min, long max) {
        long random_int = (int)Math.floor(Math.random() * (max - min + 1) + min);
        return IoUtils.delay(random_int);
    }

    public static String getStackTrace(Exception e) {
        return ExceptionUtils.getStackTrace(e);
    }
}
