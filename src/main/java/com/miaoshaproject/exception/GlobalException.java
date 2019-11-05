package com.miaoshaproject.exception;

import com.miaoshaproject.result.CodeMsg;

public class GlobalException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private CodeMsg codeMsg;

    public void GlobalException(CodeMsg codeMsg){

    }
}
