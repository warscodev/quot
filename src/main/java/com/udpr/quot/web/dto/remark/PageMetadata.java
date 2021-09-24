package com.udpr.quot.web.dto.remark;

import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class PageMetadata {

    private int size;
    private long totalElements;
    private int totalPages;
    private int page;
    private boolean isFirst;
    private boolean isLast;
    private int blockSize;
    private int startBlock;
    private int blockSize_sm;
    private int startBlock_sm;
    private int endBlock;
    private int endBlock_sm;

    public PageMetadata(Page page) {
        this.size = page.getSize();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.page = page.getNumber()+1;
        this.isFirst = page.isFirst();
        this.isLast = page.isLast();
        this.blockSize = 10;
        this.startBlock = calStartBlock(getPage(),getBlockSize());
        this.endBlock = calEndBlock(getStartBlock(), getBlockSize(), getTotalPages());
        this.blockSize_sm = 5;
        this.startBlock_sm = calStartBlock(getPage(),getBlockSize_sm());
        this.endBlock_sm = calEndBlock(getStartBlock_sm(), getBlockSize_sm(), getTotalPages());
    }

    int calStartBlock(int page, int blockSize){
            int startBlock;
        if(page>blockSize/2){
            startBlock = Math.round(page-(blockSize/2));
        }else if(getTotalElements()==0){
            startBlock = 0;
        }else{
            startBlock = 1;
        }
        return startBlock;
    }

    int calEndBlock(int startBlock, int blockSize, int totalPages){
        int endBlock = startBlock + blockSize - 1;
        if(endBlock>totalPages){
            endBlock = totalPages;
        }
        if(getTotalElements() ==0 ){
            endBlock = 0;
        }
        return endBlock;
    }

}
