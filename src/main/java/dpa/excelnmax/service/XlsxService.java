package dpa.excelnmax.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.PriorityQueue;

/**
 * Вычисление N-ного максимального числа с помощью метода минимальной кучи (PriorityQueue)
 */

@Slf4j
@Service
public class XlsxService {

    private static final int COLUMN_INDEX = 0; // Индекс столбца
    private PriorityQueue<Integer> minHeap;
    private int nValue;

    public int findNthMax(File file, int n) {

        this.minHeap = new PriorityQueue<>(n);

        this.nValue = n;

        processNumbersFromExcel(file);

        if (minHeap.size() < nValue) {
            throw new IllegalArgumentException("Недостаточно уникальных чисел в файле.");
        }

        Integer nthMax = minHeap.peek();
        if (nthMax == null) {
            throw new IllegalStateException("Не удалось получить n-е максимальное число.");
        }
        return nthMax;
    }

    public void processNumbersFromExcel(File file) {
        int count = 0; // Счетчик чисел
        try (InputStream inputStream = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                Cell cell = row.getCell(COLUMN_INDEX, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

                if (cell.getCellType() == CellType.NUMERIC) {
                    int number = (int) cell.getNumericCellValue();
                    processMinHeap(number);
                    count++;
                } else {
                    log.warn("Ячейка не содержит число: {}", cell);
                }
            }

            if (nValue > count) {
                throw new IllegalArgumentException("n превышает количество чисел в файле: " + count);
            }

        } catch (IOException e) {
            log.error("Ошибка при чтении файла: {}", e.getMessage());
        }
    }

    public void processMinHeap(int number) {

        if (minHeap.size() < nValue) {
            minHeap.offer(number);
        } else if (number > minHeap.peek()) {
            minHeap.poll();
            minHeap.offer(number);
        }
    }
}
