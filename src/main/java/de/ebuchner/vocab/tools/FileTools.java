package de.ebuchner.vocab.tools;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.attribute.DosFileAttributeView;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileTools {

    private static final Logger LOGGER = Logger.getLogger(FileTools.class.getName());

    private FileTools() {

    }

    public static void setHidden(File file) {
        try {
            DosFileAttributeView view = Files.getFileAttributeView(file.toPath(), DosFileAttributeView.class);
            view.setHidden(true);
        } catch (Exception e) {
            LOGGER.log(Level.INFO, e.toString(), e);
        }
    }

    public static boolean isEmpty(File directory) {
        if (!directory.exists())
            return true;

        return directory.listFiles().length == 0;
    }

    public static void moveDir(File oldDir, File newDir) throws IOException {
        if (!oldDir.exists())
            return;

        if (!newDir.exists()) {
            if (!newDir.mkdirs())
                throw new RuntimeException("Could not create " + newDir);
        }

        File[] oldFiles = oldDir.listFiles();
        if (oldFiles == null)
            return;
        for (File oldFile : oldFiles) {
            File newFile = new File(newDir, oldFile.getName());
            if (oldFile.isDirectory())
                moveDir(oldFile, newFile);
            else {
                byte[] buffer = new byte[4096];
                InputStream in = new FileInputStream(oldFile);
                try {
                    OutputStream out = new FileOutputStream(newFile);
                    try {
                        int len;
                        while ((len = in.read(buffer)) > 0) {
                            out.write(buffer, 0, len);
                        }
                    } finally {
                        out.close();
                    }
                } finally {
                    in.close();
                }
                if (!oldFile.delete())
                    throw new RuntimeException("Could not delete " + oldFile);
            }
        }

        if (!oldDir.delete())
            throw new RuntimeException("Could not delete " + oldDir);
    }
}
