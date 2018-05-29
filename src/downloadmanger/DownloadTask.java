package downloadmanger;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import javafx.concurrent.Task;

/**
 *
 * @author Komla Adzam
 */
public class DownloadTask extends Task<Void> {

    private URL url;
    private String fileName;

    /**
     *
     * @param url URL of file to download
     * @param fileName name of the file for saving
     */
    public DownloadTask(URL url, String fileName) {
        this.url = url;
        this.fileName = fileName;
    }

    @Override
    protected Void call() throws Exception {
        try {
            URLConnection openConnection = url.openConnection();
            int fileSize = openConnection.getContentLength();

            ByteArrayOutputStream out;
            try (InputStream in = new BufferedInputStream(url.openStream())) {
                out = new ByteArrayOutputStream();
                byte[] buf = new byte[50 * 1024];
                int n = 0;
                long downloaded = 0;
                while (-1 != (n = in.read(buf))) {
                    downloaded += n;
                    out.write(buf, 0, n);
                    updateProgress(downloaded, fileSize);
                    updateMessage(String.format("Downloaded: %.2f MB",
                            downloaded / (1024F * 1024F)));
                }
                out.close();
            }

            byte[] response = out.toByteArray();
            try (FileOutputStream fos = new FileOutputStream(fileName)) {
                fos.write(response);
            }

            updateMessage("Complete");
        } catch (IOException ex) {
            updateMessage("Can't connect to the internet");
        }
        return null;
    }

}
