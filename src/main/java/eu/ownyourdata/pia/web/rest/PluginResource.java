package eu.ownyourdata.pia.web.rest;

import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@RestController
@RequestMapping("/plugins")
public class PluginResource {

    private static final int BUFFER_SIZE = 4096;

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.POST,value = "/upload")
    public void upload(@RequestPart("name") String name, @RequestPart("file") MultipartFile file) throws IOException {
        ZipInputStream zis = new ZipInputStream(file.getInputStream());

        ZipEntry entry = zis.getNextEntry();
        // iterates over entries in the zip file
        while (entry != null) {
            String filePath = "extension/" + entry.getName();
            if (!entry.isDirectory()) {
                // if the entry is a file, extracts it
                extractFile(zis, filePath);
            } else {
                // if the entry is a directory, make the directory
                FileUtils.forceMkdir(new File(filePath));
            }
            zis.closeEntry();
            entry = zis.getNextEntry();
        }
        zis.close();
    }


    /**
     * Extracts a zip entry (file entry)
     * @param zipIn
     * @param filePath
     * @throws IOException
     */
    private void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }
}
