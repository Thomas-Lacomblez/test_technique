package com._4dconcept.evaluation;

import java.io.IOException;

public class BusinessException extends Exception {

    public BusinessException(IOException e) {
        super(e.getMessage(), e);
    }
}
