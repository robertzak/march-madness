package com.swordlaser.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Map;
import java.util.Set;

@Builder
@Getter
@EqualsAndHashCode
public class BookStats {
    @CsvBindByName(column = "Title", required = true)
    @CsvBindByPosition(position = 0)
    private String title;
    @CsvBindByName(column = "Author", required = true)
    @CsvBindByPosition(position = 1)
    private String author;
    private Map<Integer, YearlyNomination> nominationsByYear;


    public void addNomination(YearlyNomination nomination){
        nominationsByYear.put(nomination.getYear(), nomination);
    }

    public Set<Integer> getsYearsNominated(){
        return nominationsByYear.keySet();
    }

    public int getTotalNominations(){
        int count = 0;
        for(YearlyNomination nomination: nominationsByYear.values()){
            count += nomination.getNumberOfNominations();
        }

        return count;
    }
}
