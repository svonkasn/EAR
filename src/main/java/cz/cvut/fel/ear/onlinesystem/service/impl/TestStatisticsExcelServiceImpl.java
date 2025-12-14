package cz.cvut.fel.ear.onlinesystem.service.impl;

import cz.cvut.fel.ear.onlinesystem.dto.LeaderboardEntry;
import cz.cvut.fel.ear.onlinesystem.dto.StatusBucket;
import cz.cvut.fel.ear.onlinesystem.dto.TestStats;
import cz.cvut.fel.ear.onlinesystem.service.TestStatisticsExcelService;
import cz.cvut.fel.ear.onlinesystem.service.TestStatisticsService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class TestStatisticsExcelServiceImpl implements TestStatisticsExcelService {

    private final TestStatisticsService statisticsService;

    public TestStatisticsExcelServiceImpl(TestStatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @Override
    public byte[] exportStats(int testId, int n) {
        TestStats stats = statisticsService.computeStatsForTest(testId);
        List<LeaderboardEntry> top = statisticsService.topN(testId, n);
        List<LeaderboardEntry> low = statisticsService.lowN(testId, n);

        try (XSSFWorkbook wb = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            writeStatsSheet(wb, stats);
            writeStatusHistogramSheet(wb, stats.statusHistogram());
            writeLeaderboardSheet(wb, "Top " + n, top);
            writeLeaderboardSheet(wb, "Low " + n, low);

            wb.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to generate XLSX", e);
        }
    }

    private static void writeStatsSheet(XSSFWorkbook wb, TestStats s) {
        Sheet sh = wb.createSheet("Stats");
        int r = 0;

        r = kv(sh, r, "testId", s.testId());
        r = kv(sh, r, "attempts", s.attempts());
        r = kv(sh, r, "distinctStudents", s.distinctStudents());
        r = kv(sh, r, "maxPoints", s.maxPoints());
        r = kv(sh, r, "sumQuestionPoints", s.sumQuestionPoints());
        r = kv(sh, r, "averageScore", s.averageScore());
        r = kv(sh, r, "minScore", s.minScore());
        r = kv(sh, r, "medianScore", s.medianScore());
        r = kv(sh, r, "p90", s.p90());
        r = kv(sh, r, "maxScore", s.maxScore());

        sh.autoSizeColumn(0);
        sh.autoSizeColumn(1);
    }

    private static int kv(Sheet sh, int rowIdx, String key, Object value) {
        Row row = sh.createRow(rowIdx);
        row.createCell(0).setCellValue(key);
        row.createCell(1).setCellValue(value == null ? "" : String.valueOf(value));
        return rowIdx + 1;
    }

    private static void writeStatusHistogramSheet(XSSFWorkbook wb, List<StatusBucket> buckets) {
        Sheet sh = wb.createSheet("StatusHistogram");

        Row h = sh.createRow(0);
        h.createCell(0).setCellValue("status");
        h.createCell(1).setCellValue("count");

        int r = 1;
        for (StatusBucket b : buckets) {
            Row row = sh.createRow(r++);
            row.createCell(0).setCellValue(b.status().name());
            row.createCell(1).setCellValue(b.count());
        }

        sh.autoSizeColumn(0);
        sh.autoSizeColumn(1);
    }

    private static void writeLeaderboardSheet(XSSFWorkbook wb, String sheetName, List<LeaderboardEntry> items) {
        Sheet sh = wb.createSheet(sheetName);

        Row h = sh.createRow(0);
        h.createCell(0).setCellValue("rank");
        h.createCell(1).setCellValue("userId");
        h.createCell(2).setCellValue("username");
        h.createCell(3).setCellValue("email");
        h.createCell(4).setCellValue("score");

        int r = 1;
        for (LeaderboardEntry e : items) {
            Row row = sh.createRow(r++);
            row.createCell(0).setCellValue(e.rank());
            row.createCell(1).setCellValue(e.userId());
            row.createCell(2).setCellValue(e.username());
            row.createCell(3).setCellValue(e.email());
            row.createCell(4).setCellValue(e.score() == null ? "" : String.valueOf(e.score()));
        }

        for (int i = 0; i < 5; i++) sh.autoSizeColumn(i);
    }
}
