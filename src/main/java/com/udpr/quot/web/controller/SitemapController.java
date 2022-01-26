package com.udpr.quot.web.controller;

import com.udpr.quot.service.sitemap.SitemapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

@Controller
@RequiredArgsConstructor
public class SitemapController {

    private final SitemapService sitemapService;


    @RequestMapping(value = "/sitemap.xml", produces = {"application/xml"})
    @ResponseBody
    public ResponseEntity<String> sitemap(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        return ResponseEntity.ok(sitemapService.getSystemicSiteMap());
    }
}
