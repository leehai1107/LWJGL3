package mainEngine.core.utils;

import org.lwjgl.system.MemoryUtil;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Utils {

    public static FloatBuffer storeDataInFloatBuffer(float[] data) {
        FloatBuffer bufer = MemoryUtil.memAllocFloat(data.length);
        bufer.put(data).flip();
        return bufer;
    }

    public static IntBuffer storeDataInIntBuffer(int[] data) {
        IntBuffer bufer = MemoryUtil.memAllocInt(data.length);
        bufer.put(data).flip();
        return bufer;
    }

    public static String loadResource(String filename) throws Exception {
        String result;
        try (InputStream in = Utils.class.getResourceAsStream(filename)) {
            if (in == null) {
                throw new FileNotFoundException("Resource not found: " + filename);
            }
            try (Scanner scanner = new Scanner(in, StandardCharsets.UTF_8.name())) {
                result = scanner.useDelimiter("\\A").next();
            }
        }
        return result;
    }
}

