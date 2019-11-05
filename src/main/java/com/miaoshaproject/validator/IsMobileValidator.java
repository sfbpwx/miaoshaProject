package com.miaoshaproject.validator;

import com.alibaba.druid.util.StringUtils;
import com.miaoshaproject.util.ValidateUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsMobileValidator implements ConstraintValidator<IsMobile, String> {
    private boolean required = false;
    public void initialize(IsMobile isMobile){
        required = isMobile.required();
    }

    public boolean isValid(String value, ConstraintValidatorContext context){
        if(required){

        }else{
            if(StringUtils.isEmpty(value)){
                return true;
            }else {
                return ValidateUtil.isMobile(value);
            }
        }
        return false;
    }
}
