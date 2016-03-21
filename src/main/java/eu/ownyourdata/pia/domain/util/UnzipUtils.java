package eu.ownyourdata.pia.domain.util;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static org.apache.commons.io.FilenameUtils.concat;

/**
 * Created by michael on 21.03.16.
 */
public class UnzipUtils {
    private static final int BUFFER_SIZE = 4096;

    public static void extract(ZipFile zip, String location) throws IOException {
        FileUtils.forceMkdir(new File(location));

        Enumeration<? extends ZipEntry> entries = zip.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            String filePath = concat(location, entry.getName());

            if (!entry.isDirectory()) {
                // if the entry is a file, extracts it
                extractFile(zip.getInputStream(entry), filePath);
            } else {
                // if the entry is a directory, make the directory
                FileUtils.forceMkdir(new File(filePath));
            }
        }
    }

    private static void extractFile(InputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }
}
