package com.drunkenlion.alcoholfriday.domain.customerservice.util;

import com.drunkenlion.alcoholfriday.domain.customerservice.enumerated.QuestionStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class QuestionStatusConverter implements AttributeConverter<QuestionStatus, String> {

    @Override
    public String convertToDatabaseColumn(QuestionStatus attribute) {
        return attribute.getLabel();
    }

    @Override
    public QuestionStatus convertToEntityAttribute(String dbData) {
        return QuestionStatus.ofStatus(dbData);
    }
}
