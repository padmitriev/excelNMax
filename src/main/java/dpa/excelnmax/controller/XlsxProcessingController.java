package dpa.excelnmax.controller;

import dpa.excelnmax.service.XlsxService;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.io.File;

@Slf4j
@RestController
@RequestMapping("/api/v1/")
@Validated
public class XlsxProcessingController {

    private final XlsxService xlsxService;

    @Autowired
    public XlsxProcessingController(XlsxService xlsxService) {
        this.xlsxService = xlsxService;
    }

    /**
     * Метод для обработки файла .xlsx и числа N.
     *
     * @param filePath Путь к файлу в формате .xlsx (относительно classpath).
     * @param n        Число N (должно быть больше 0).
     * @return Результат обработки.
     */
    @PostMapping("/nth-max")
    @Operation(summary = "Обработка XLSX-файла", description = "Принимает путь к файлу и число N")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Файл успешно обработан."),
            @ApiResponse(responseCode = "400", description = "Неверные входные данные."),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера.")
    })
    public ResponseEntity<Integer> processXlsxFile(
            @RequestParam String filePath,
            @RequestParam @Min(1) int n) {

        log.info("Запрос на обработку файла: {}, n: {}", filePath, n);

        try {
            ClassPathResource resource = new ClassPathResource(filePath);

            if (!resource.exists()) {
                log.warn("Файл не найден: {}", filePath);
                return ResponseEntity.badRequest().body(-1);
            }

            File file = resource.getFile();
            log.info("Файл успешно загружен: {}", file.getAbsolutePath());

            int result = xlsxService.findNthMax(file, n);
            log.info("Результат обработки: {}", result);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Ошибка при обработке файла: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(-1);
        }
    }
}