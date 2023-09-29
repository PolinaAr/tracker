package com.andersen.telegram.dao;

import java.util.List;

public interface ChatIDDao {
    Long create(Long chatId);
    Long getById(Long chatId);
    List<Long> getAll();
    boolean delete(Long chatId);
}
