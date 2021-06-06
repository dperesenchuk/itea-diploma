package ua.itea.diploma;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Downloader implements Runnable {
    private URL url;
    private ResourcesContainer container;

    public Downloader(String urlString, ResourcesContainer container) throws MalformedURLException {
        this.url = new URL(urlString);
        this.container = container;
    }

    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();
        System.out.println("Start " + threadName + "; url: " + url.toString());
        try {
            String htmlContent = getURLContent();
            container.addResource(url.toString(), htmlContent);
        } catch (IOException e) {
            System.out.println("IOException in " + threadName + ":::" + e.getMessage());
        }
        System.out.println("End " + threadName);
    }

    private String getURLContent() throws IOException {
        try (Scanner scanner = new Scanner(this.url.openStream(), StandardCharsets.UTF_8.toString()))
        {
            scanner.useDelimiter("\\A");
            return scanner.hasNext() ? scanner.next() : "";
        }
    }
}
