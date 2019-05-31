package ru.demo.jpa.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum DocumentType {

    INVOICE(1), ACT(2);

    private final Integer id;

    public static DocumentType find(Integer id) {
        return Arrays.stream(DocumentType.values()).filter(d -> d.getId().equals(id)).findFirst()
                     .orElseThrow(IllegalArgumentException::new);
    }
}

