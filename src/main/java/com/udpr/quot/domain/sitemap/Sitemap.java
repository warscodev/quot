package com.udpr.quot.domain.sitemap;

import com.udpr.quot.service.sitemap.SitemapService;
import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
public class Sitemap {

    private static final SimpleDateFormat SITE_MAP_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private String loc;
    private Date lastmod;
    private String changefreq;

    public Sitemap(String loc) {
        this.loc = loc;
        /*this.lastmod = new Date();
        this.changefreq = SitemapService.CHANGEFREQ_DAILY;*/
    }
    public Sitemap(String loc, Date lastmod, String changefreq) {
        this.loc = loc;
        this.lastmod = lastmod;
        this.changefreq = changefreq;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("<url>");
        sb.append("<loc>").append(loc).append("</loc>");
        if(lastmod != null && changefreq !=null) {
            sb.append("<lastmod>").append(SITE_MAP_DATE_FORMAT.format(lastmod)).append("</lastmod>");
            sb.append("<changefreq>").append(changefreq).append("</changefreq>");
        }
        sb.append("</url>");
        return sb.toString();
    }



}
