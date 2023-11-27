package mainEngine.core.utils;

import org.lwjgl.system.MemoryUtil;

import java.io.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Utils {

    public static FloatBuffer storeDataInFloatBuffer(float[] data) {
        FloatBuffer buffer = MemoryUtil.memAllocFloat(data.length);
        buffer.put(data).flip();
        return buffer;
    }

    public static IntBuffer storeDataInIntBuffer(int[] data) {
        IntBuffer buffer = MemoryUtil.memAllocInt(data.length);
        buffer.put(data).flip();
        return buffer;
    }

    public static String loadResource(String filename) throws IOException {
        try (InputStream in = Utils.class.getResourceAsStream(filename)) {
            if (in == null) {
                throw new FileNotFoundException("Resource not found: " + filename);
            }

            try (Scanner scanner = new Scanner(in, StandardCharsets.UTF_8.name())) {
                if (scanner.hasNext()) {
                    return scanner.useDelimiter("\\A").next();
                } else {
                    throw new IOException("Empty file: " + filename);
                }
            }
        }
    }

    public static List<String> readAllLines(String filename) {
        List<String> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(Class.forName(Utils.class.getName()).getResourceAsStream(filename)))) {
            String line;
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return list;
    }
}


