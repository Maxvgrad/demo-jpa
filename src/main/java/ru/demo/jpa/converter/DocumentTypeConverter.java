package ru.demo.jpa.converter;

import ru.demo.jpa.dto.DocumentType;

import javax.persistence.AttributeConverter;

public class DocumentTypeConverter implements AttributeConverter<DocumentType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(DocumentType attribute) {
        return attribute.getId();
    }

    @Override
    public DocumentType convertToEntityAttribute(Integer dbData) {
        return DocumentType.find(dbData);
    }
}
