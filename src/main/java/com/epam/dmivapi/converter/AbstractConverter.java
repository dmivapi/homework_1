package com.epam.dmivapi.converter;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractConverter <S, T> {
    public abstract T convertForList(S sourceTypeItem);

    public List<T> convert(List<S> sourceTypeList) {
        return sourceTypeList.stream()
                .map(this::convertForList)
                .collect(Collectors.toList());
    }
}
