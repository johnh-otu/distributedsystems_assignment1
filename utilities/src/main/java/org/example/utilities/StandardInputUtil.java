package org.example.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class StandardInputUtil {
    
    private static BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

    public static String readLine() throws IOException {
        try {
            String input = stdIn.readLine();
            return input;
        } catch (Exception e) {
            throw e;
        }
    }
}
