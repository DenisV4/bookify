package example.bookify.server.statistics.service.impl;

import example.bookify.server.statistics.service.FileService;
import example.bookify.server.statistics.service.StatisticsService;
import example.bookify.server.util.FileUtil;
import com.opencsv.exceptions.CsvFieldAssignmentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipOutputStream;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final List<StatisticsService<?>> statisticsServices;

    @Override
    public Mono<ByteArrayOutputStream> generateStatisticsFile() {
        return Mono.fromCallable(() -> {
            try (var byteArrayOut = new ByteArrayOutputStream();
                 var zipOut = new ZipOutputStream(byteArrayOut)) {

                for (var service : statisticsServices) {
                    var dataList = service.findAll().collectList().block();
                    var csvOut = FileUtil.beansToCsv(service.getEntityClass(), dataList);

                    FileUtil.zip(service.getStatisticsFileName() + ".csv", csvOut, zipOut);
                }

                return byteArrayOut;
            } catch (IOException | CsvFieldAssignmentException exc) {
                throw new RuntimeException("Failed to generate statistics file", exc);
            }
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
