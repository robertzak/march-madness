package com.swordlaser;

import com.swordlaser.model.BookStats;
import com.swordlaser.model.YearlyNomination;
import com.swordlaser.service.ParseService;
import com.swordlaser.service.StatService;
import com.swordlaser.service.WriteCSVService;

import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Map<Integer, List<YearlyNomination>> nominationsByYear = new ParseService().parseNominations();
        Map<String, BookStats> nominationsByBook = new StatService().combineNominations(nominationsByYear);
        new WriteCSVService().writeCSV(nominationsByBook);
    }
}