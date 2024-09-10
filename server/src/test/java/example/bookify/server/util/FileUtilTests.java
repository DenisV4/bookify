package example.bookify.server.util;

import example.bookify.server.statistics.model.AuthenticationStat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("FileUtil Unit Tests")
class FileUtilTests {

    @Test
    @DisplayName("Should correctly convert beans to CSV format")
    void shouldConvertBeansToCsv() throws Exception {
        var stats = List.of(
                AuthenticationStat.builder()
                        .userId(1L)
                        .name("John Doe")
                        .device("iPhone")
                        .os("iOS")
                        .browser("Safari")
                        .remoteAddress("192.168.1.1")
                        .timestamp(LocalDateTime.of(2024, 9, 1, 12, 30))
                        .build(),
                AuthenticationStat.builder()
                        .userId(2L)
                        .name("Jane Doe")
                        .device("Galaxy S21")
                        .os("Android")
                        .browser("Chrome")
                        .remoteAddress("192.168.1.2")
                        .timestamp(LocalDateTime.of(2024, 9, 2, 13, 45))
                        .build()
        );

        var outputStream = FileUtil.beansToCsv(AuthenticationStat.class, stats);
        var csvOutput = outputStream.toString(StandardCharsets.UTF_8);

        var expectedCsv = """
                "user_id","name","device","os","browser","remote_address","time"
                "1","John Doe","iPhone","iOS","Safari","192.168.1.1","2024-09-01T12:30",
                "2","Jane Doe","Galaxy S21","Android","Chrome","192.168.1.2","2024-09-02T13:45",
                """;

        assertThat(csvOutput).isEqualTo(expectedCsv);
    }

    @Test
    @DisplayName("Should skip fields annotated with @CsvIgnore")
    void shouldSkipIgnoredFields() throws Exception {
        var stat = AuthenticationStat.builder()
                .id("abc123")
                .userId(1L)
                .name("John Doe")
                .device("iPhone")
                .os("iOS")
                .browser("Safari")
                .remoteAddress("192.168.1.1")
                .timestamp(LocalDateTime.of(2024, 9, 1, 12, 30))
                .build();

        var outputStream = FileUtil.beansToCsv(AuthenticationStat.class, List.of(stat));
        var csvOutput = outputStream.toString(StandardCharsets.UTF_8);

        var expectedCsv = """
                "user_id","name","device","os","browser","remote_address","time"
                "1","John Doe","iPhone","iOS","Safari","192.168.1.1","2024-09-01T12:30",
                """;
        assertThat(csvOutput).isEqualTo(expectedCsv);
    }

    @Test
    @DisplayName("Should return only headers when data list is null")
    void shouldReturnOnlyHeadersWhenDataIsNull() throws Exception {
        var outputStream = FileUtil.beansToCsv(AuthenticationStat.class, null);
        var csvOutput = outputStream.toString(StandardCharsets.UTF_8);

        var expectedCsv = """
                "user_id","name","device","os","browser","remote_address","time"
                """;
        assertThat(csvOutput).isEqualTo(expectedCsv);
    }

    @Test
    @DisplayName("Should return only headers when data list is empty")
    void shouldReturnOnlyHeadersWhenDataIsEmpty() throws Exception {
        var outputStream = FileUtil.beansToCsv(AuthenticationStat.class, List.of());
        var csvOutput = outputStream.toString(StandardCharsets.UTF_8);

        var expectedCsv = """
                "user_id","name","device","os","browser","remote_address","time"
                """;
        assertThat(csvOutput).isEqualTo(expectedCsv);
    }
}
