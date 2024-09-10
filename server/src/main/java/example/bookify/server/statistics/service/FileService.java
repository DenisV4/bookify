package example.bookify.server.statistics.service;

import reactor.core.publisher.Mono;

import java.io.ByteArrayOutputStream;

public interface FileService {

    Mono<ByteArrayOutputStream> generateStatisticsFile();
}
