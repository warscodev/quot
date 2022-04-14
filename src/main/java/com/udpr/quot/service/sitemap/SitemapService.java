package com.udpr.quot.service.sitemap;

import com.udpr.quot.domain.person.repository.PersonRepository;
import com.udpr.quot.domain.remark.repository.RemarkRepository;
import com.udpr.quot.domain.sitemap.Sitemap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SitemapService {

    private final RemarkRepository remarkRepository;
    private final PersonRepository personRepository;

    public static final String BASE_URL = "https://quot.wiki";
    public static final String BEGIN_DOC = "<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">";
    public static final String END_DOC = "</urlset>";
    public static final String CHANGEFREQ_ALWAYS = "always";
    public static final String CHANGEFREQ_HOURLY = "hourly";
    public static final String CHANGEFREQ_DAILY = "daily";
    public static final String CHANGEFREQ_WEEKLY = "weekly";
    public static final String CHANGEFREQ_MONTHLY = "monthly";
    public static final String CHANGEFREQ_YEARLY = "yearly";
    public static final String CHANGEFREQ_NEVER = "never";

    public String getSystemicSiteMap() throws UnsupportedEncodingException {
        Date now = new Date();
        StringBuffer sb = new StringBuffer();

        sb.append(BEGIN_DOC);

        //메인
        sb.append(new Sitemap(BASE_URL, now, CHANGEFREQ_DAILY));

        //발언
        sb.append(new Sitemap(BASE_URL+"/remark", now, CHANGEFREQ_DAILY));

        //카테고리
        List<String> categoryList = List.of("정치","방송연예","사회문화","스포츠");
        categoryList.forEach(list ->{
            String encodedCategory = URLEncoder.encode(list, StandardCharsets.UTF_8);
            sb.append(new Sitemap(BASE_URL+"/remark?category="+encodedCategory));
        });

        //발언페이지
        List<Long> remarkIdList = remarkRepository.findAllId();
        remarkIdList.forEach(id ->{
            sb.append(new Sitemap(BASE_URL+"/remark/" + id));
        });

        //인물페이지
        List<Long> personIdList = personRepository.findAllId();
        personIdList.forEach(id ->{
            sb.append(new Sitemap(BASE_URL+"/person/" + id));
        });

        sb.append(END_DOC);

        return sb.toString();
    }


}
