package cz.cvut.fel.ear.onlinesystem.rest.exception;

public record RestError(int status, String message) {
}
