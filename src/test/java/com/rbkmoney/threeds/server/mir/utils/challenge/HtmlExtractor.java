package com.rbkmoney.threeds.server.mir.utils.challenge;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.http.ResponseEntity;

public class HtmlExtractor {

    public static String extractUrlFromHtmlResponseForNextRequestWithHtmlResult(ResponseEntity<String> response) {
        String html = response.getBody();
        Document document = Jsoup.parse(html);
        Element element = document.select("form[name=form]").first();
        return element.attr("action");
    }

    public static String extractUrlFromHtmlResponseForCResErrorResult(ResponseEntity<String> response) {
        String html = response.getBody();
        Document document = Jsoup.parse(html);
        Element element = document.select("a.cancel").first();
        return element.attr("href");
    }

    public static String extractEncodedCResFromHtmlResponse(ResponseEntity<String> response) {
        String html = response.getBody();
        Document document = Jsoup.parse(html);
        Element element = document.select("input[name=cres]").first();
        return element.attr("value");
    }
}
