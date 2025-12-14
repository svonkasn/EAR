package cz.cvut.fel.ear.onlinesystem.service;

public interface TestStatisticsExcelService {
    byte[] exportStats(int testId, int n); // n for topN/lowN
}
