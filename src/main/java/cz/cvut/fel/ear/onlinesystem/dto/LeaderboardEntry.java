package cz.cvut.fel.ear.onlinesystem.dto;

public record LeaderboardEntry(int rank, int userId, String username,
                               String email, Integer score) {
}
