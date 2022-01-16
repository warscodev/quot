package com.udpr.quot.domain.remark.search;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class RemarkSearchCondition {
    String keyword;
    int tab;
    long personId;
    String sort;
    //session Id
    Long sid;
    int page;
    String category;
    String prevPageLink;
    String remarkDetailParameters;

    public RemarkSearchCondition() {
        this.tab = 1;
        this.personId = 0L;
        this.sort = "cd_d";
        this.page = 1;
    }

    public void setPrevPageLink(){
        StringBuilder url = new StringBuilder("/remark");
        setUrl(url);
        this.prevPageLink = url.toString();
    }

    public void setRemarkDetailParameters(){
        StringBuilder url = new StringBuilder("");
        setUrl(url);
        this.remarkDetailParameters = url.toString();
    }

    private void setUrl(StringBuilder url) {
        List<String> paramList = new ArrayList<>();

        if(getKeyword() != null){
            paramList.add("keyword="+getKeyword());
            System.out.println(getKeyword());
        }

        if(getPage() > 1){
            paramList.add("page="+getPage());
            System.out.println(getPage());
        }

        if(getTab() > 1){
            paramList.add("tab="+getTab());
            System.out.println(getTab());
        }

        if(getCategory() != null){
            paramList.add("category="+getCategory());
            System.out.println(getCategory());
        }

        if(getSort() != null && !getSort().equals("cd_d")){
            paramList.add("sort="+getSort());
            System.out.println(getSort());

        }

        if(paramList.size() > 0){
            url.append("?").append(paramList.get(0));

            if(paramList.size()>1){
                for (int i=1; i < paramList.size(); i++){
                    url.append("&").append(paramList.get(i));
                }
            }
        }
    }



}
