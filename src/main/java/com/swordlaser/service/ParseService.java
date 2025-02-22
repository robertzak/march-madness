package com.swordlaser.service;

import com.swordlaser.model.YearlyNomination;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class ParseService {
    final String NOMINATIONS_DIR = "/nominations";
    //final String NOMINATION_REGEX = "^(.*)(\\sby\\s)(.*)(\\s-\\s)(.*)(\\s\\((.*)\\))$";
    final String NOMINATION_REGEX = "^(.*)(\\sby\\s)(.*)(\\s-\\s)(\\d+)(\\s.*)$";

    public Map<Integer, List<YearlyNomination>> parseNominations(){
        Map<Integer, List<YearlyNomination>> nominationsByYear = new HashMap<>();

        Pattern pattern = Pattern.compile(NOMINATION_REGEX);

        final URL inputUrl =  ParseService.class.getResource(NOMINATIONS_DIR);

        if(inputUrl == null){
            log.error("Unable to locate directory");
            System.exit(-1);
        }

        try {
            File nomDir = new File(inputUrl.toURI());
            if(nomDir.isDirectory()) {
                for (File file: nomDir.listFiles()){
                    int year = Integer.parseInt(file.getName());
                    List<YearlyNomination> nominations = new ArrayList<>();
                    try(BufferedReader br = new BufferedReader(new FileReader(file))) {

                        for(String line; (line = br.readLine()) != null; ) {
                            Matcher matcher = pattern.matcher(line);

                            if(matcher.find()){

                                String title = matcher.group(1);
                                String author = matcher.group(3);
                                String count = matcher.group(5);

                                // I don't really need the nominators for anything..but since I have them
                                // I don't know why my subgroup didn't work, but I'm not wasting time on it
                                String group6 = matcher.group(6).strip();
                                // this should remove the parenthesis
                                String removeTokens = group6.substring(1, group6.length()-1);
                                String [] nominators = removeTokens.split(",");

                                nominations.add(YearlyNomination.builder()
                                        .title(title)
                                        .author(author)
                                        .year(year)
                                        .numberOfNominations(Integer.parseInt(count))
                                        .nominators(List.of(nominators))
                                        .build());
                            } else {
                                log.error("Regex failed for: {} in file {}", line, file.getName());
                            }
                        }
                    } catch(Exception e){
                        log.error("Error reading input", e);
                    }


                    nominationsByYear.put(year, nominations);
                }

            }
        } catch (Exception e){
            log.error("Failed to process directory: {}", NOMINATIONS_DIR);
        }

        return nominationsByYear;
    }
}
