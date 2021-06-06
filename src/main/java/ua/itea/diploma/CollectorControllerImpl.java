package ua.itea.diploma;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CollectorControllerImpl implements CollectorController {

    @Autowired
    ResourcesService resourcesService;

    private int maxThreadCount;

    public CollectorControllerImpl() {
        ApplicationProperties props = new ApplicationProperties();
        try {
            maxThreadCount = Integer.parseInt(props.readProperty("max_threads"));
        } catch (NumberFormatException e) {
            maxThreadCount = 1;
            System.out.println("Invalid max_threads property, set to default 1");
        }
    }

    @Override
    public ResponseEntity<String> collect() {
        List<String> resourceList;
        try {
            resourceList = resourcesService.getResources();
        } catch (IOException e) {
            System.out.println("Cannot get resources " + e.getMessage());
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        int threadCount = resourceList.size() > maxThreadCount ? maxThreadCount : resourceList.size();
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        ResourcesContainer resourcesContainer = new ResourcesContainer();
        try {
            for (String resource : resourceList) {
                executorService.execute(new Downloader(resource, resourcesContainer));
            }
        } catch (MalformedURLException e) {
            System.out.println("Invalid resource found: " + e.getMessage());
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } finally {
            executorService.shutdown();
        }

        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            resourcesContainer.writeData();
        } catch (InterruptedException e) {
            System.out.println("Threads were interrupted: " + e.getMessage());
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity("Success", HttpStatus.OK);
    }
}
