package com.csii.simpleone;

import java.io.Closeable;
import java.io.IOException;

public class Util {

    private Util() {

    }

    public static void closeable(Closeable cloneable) {
        if (null != cloneable) {
            try {
                cloneable.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                cloneable = null;
            }

        }
    }
}
