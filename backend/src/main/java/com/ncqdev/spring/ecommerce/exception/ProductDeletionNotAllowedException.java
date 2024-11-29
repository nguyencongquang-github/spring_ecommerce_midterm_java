package com.ncqdev.spring.ecommerce.exception;

public class ProductDeletionNotAllowedException extends RuntimeException {

    public ProductDeletionNotAllowedException(String message) {
        super(message);
    }
}
