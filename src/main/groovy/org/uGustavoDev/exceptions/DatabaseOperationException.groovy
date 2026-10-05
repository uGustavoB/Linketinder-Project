package org.uGustavoDev.exceptions

class DatabaseOperationException extends RuntimeException {

    DatabaseOperationException(String message) {
        super(message)
    }

    DatabaseOperationException(String message, Throwable cause) {
        super(message, cause)
    }

}
