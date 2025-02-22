package com.swordlaser;

import com.swordlaser.model.YearlyNomination;
import com.swordlaser.service.ParseService;
import com.swordlaser.service.StatService;

import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Map<Integer, List<YearlyNomination>> nominationsByYear = new ParseService().parseNominations();
        new StatService().combineNominations(nominationsByYear);
    }
}