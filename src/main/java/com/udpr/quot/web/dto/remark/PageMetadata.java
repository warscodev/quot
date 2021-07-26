package com.udpr.quot.web.dto.remark;

import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class PageMetadata {

    private int size;
    private long totalElements;
    private int totalPages;
    private int number;
    private boolean isFirst;
    private boolean isLast;

    public PageMetadata(Page page) {
        this.size = page.getSize();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.number = page.getNumber();
        this.isFirst = page.isFirst();
        this.isLast = page.isLast();
    }
}
