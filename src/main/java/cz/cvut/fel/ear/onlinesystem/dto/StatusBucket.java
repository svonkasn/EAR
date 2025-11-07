package cz.cvut.fel.ear.onlinesystem.dto;

import cz.cvut.fel.ear.onlinesystem.model.StatusTest;

public record StatusBucket(StatusTest status, long count) {
}
