package com.shurpo.ghsync.ghsync.utils;

import android.util.Log;

import java.io.*;
import java.nio.charset.Charset;

/**
 * Created by Maksim on 12.05.2014.
 */
public class IOUtils {

    public static final int EOF = -1;
    public static final int BUFFER_SIZE = 64 * 1024;

    private IOUtils() {
    }

    public static void closeQuietly(Closeable closeable) {
        try {
            closeable.close();
        }catch (IOException e){
            Log.wtf(IOUtils.class.getSimpleName(), e);
        }

    }

    public static String toString(InputStream is) throws IOException{
        StringBuilder result = new StringBuilder();
        Reader reader = new InputStreamReader(is, Charset.defaultCharset());
        char[] buffer = new char[BUFFER_SIZE];
        try {
            int bytes;
            while ((bytes = reader.read(buffer)) != EOF) {
                result.append(buffer, 0, bytes);
            }
        }finally {
            closeQuietly(reader);
        }
        return result.toString();
    }

    public static String toStringQuietly(InputStream is){
        try {
            return toString(is);
        }catch (IOException e){
            return "";
        }
    }
}
