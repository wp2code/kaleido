package com.lzx.kaleido.test;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class GitHubTrendingScraper {
    
    public static void main(String[] args) {
        String url = "https://github.com/trending";
        try {
            // Fetch the document from the URL
            Document doc = Jsoup.connect(url).get();
            Elements repoElements = doc.select("article.Box-row");
            
            List<Map<String, Object>> repoList = new ArrayList<>();
            
            for (Element repoElement : repoElements) {
                Map<String, Object> repoData = new HashMap<>();
                
                // Extract repository name
                String repoName = repoElement.select("h1.h3 a").text();
                repoData.put("name", repoName);
                
                // Extract repository URL
                String repoUrl = "https://github.com" + repoElement.select("h1.h3 a").attr("href");
                repoData.put("url", repoUrl);
                
                // Extract description
                String description = repoElement.select("p.col-9").text();
                repoData.put("description", description);
                
                // Extract language
                String language = repoElement.select("[itemprop=programmingLanguage]").text();
                repoData.put("language", language);
                
                // Extract stars
                String stars = repoElement.select(".Link--muted.d-inline-block.mr-3").first().text();
                repoData.put("stars", stars);
                
                // Extract today's stars
                String todayStars = repoElement.select(".d-inline-block.float-sm-right").text().replace(" stars today", "");
                repoData.put("today_stars", todayStars);
                
                repoList.add(repoData);
            }
            
            // Convert list to JSON
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(repoList);
            System.out.println(json);
            
            // Save to Excel
            saveToExcel(repoList);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static void saveToExcel(List<Map<String, Object>> repoList) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Trending Repos");
        
        // Create header row
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Name", "URL", "Description", "Language", "Stars", "Today Stars"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }
        
        // Fill data
        int rowNum = 1;
        for (Map<String, Object> repoData : repoList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue((String) repoData.get("name"));
            row.createCell(1).setCellValue((String) repoData.get("url"));
            row.createCell(2).setCellValue((String) repoData.get("description"));
            row.createCell(3).setCellValue((String) repoData.get("language"));
            row.createCell(4).setCellValue((String) repoData.get("stars"));
            row.createCell(5).setCellValue((String) repoData.get("today_stars"));
        }
        
        // Save to file
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        try (FileOutputStream fileOut = new FileOutputStream("trending_" + date + ".xlsx")) {
            workbook.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}