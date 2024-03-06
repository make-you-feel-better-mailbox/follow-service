package com.onetwo.followservice.adapter.out.persistence.repository.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class BooleanNumberConverter implements AttributeConverter<Boolean, Integer> {

    /**
     * Boolean 값을 1 또는 0 으로 컨버트
     *
     * @param attribute boolean 값
     * @return String true 인 경우 1 또는 false 인 경우 0
     */
    @Override
    public Integer convertToDatabaseColumn(Boolean attribute) {
        return (attribute != null && attribute) ? 1 : 0;
    }

    /**
     * 1 또는 0 을 Boolean 으로 컨버트
     *
     * @param yn 1 또는 0
     * @return Boolean 1 인 경우 true, 0 인 경우 false
     */
    @Override
    public Boolean convertToEntityAttribute(Integer yn) {
        return 1 == yn;
    }
}
