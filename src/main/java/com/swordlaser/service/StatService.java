package com.swordlaser.service;

import com.swordlaser.compare.TotalNominationsComparator;
import com.swordlaser.model.BookStats;
import com.swordlaser.model.YearlyNomination;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class StatService {

    public  Map<String, BookStats> combineNominations(Map<Integer, List<YearlyNomination>> nominationsByYear){
        Map<String, BookStats> nominationsByBook = new HashMap<>();

        for(Integer year: nominationsByYear.keySet()){
            List<YearlyNomination> nominations = nominationsByYear.get(year);
            int totalNominations = 0;
            for(YearlyNomination nomination: nominations){
                String title = nomination.getTitle();
                BookStats book = nominationsByBook.get(title);

                if(book == null){
                    book = BookStats.builder()
                            .author(nomination.getAuthor())
                            .title(title)
                            .nominationsByYear(new HashMap<>())
                            .build();
                    nominationsByBook.put(title, book);
                }

                totalNominations+= nomination.getNumberOfNominations();

                // add the year's data
                book.addNomination(nomination);
            }

            log.info("{}: {} books nominated by {} people", year, nominations.size(), totalNominations);
        }

        log.info("Total Books across {} years: {}", nominationsByYear.size(), nominationsByBook.size());


        Map<Integer, List<BookStats>> numYears = new HashMap<>();

        for(BookStats book: nominationsByBook.values()){
            int yearCount = book.getNominationsByYear().size();

            List<BookStats> books = numYears.computeIfAbsent(yearCount, _ -> new ArrayList<>());

            books.add(book);
        }

        for(Map.Entry<Integer, List<BookStats>> entry: numYears.entrySet()){
            int yearCount = entry.getKey();
            List<BookStats> books = entry.getValue();
            log.info("Years: {}, bookCount: {}", entry.getKey(), books.size());
            if(yearCount > 4){
                for(BookStats book: books){
                    String yearList = String.join(", ", book.getsYearsNominated().stream().map(year -> Integer.toString(year)).toList());
                    log.info("\t{} by {} total Nominations: {}, years={}", book.getTitle(), book.getAuthor(),
                            book.getTotalNominations(), yearList);
                }
            }
        }

        List<BookStats> atLeast15 =  nominationsByBook.values().stream()
                .filter(book -> book.getTotalNominations() > 10)
                .sorted(new TotalNominationsComparator())
                .toList();

        log.info("{} books have been nominated more than 10 times", atLeast15.size());
        for(BookStats book: atLeast15){
            log.info("\t{} by {} total Nominations: {}, numYears={}", book.getTitle(), book.getAuthor(),
                    book.getTotalNominations(), book.getNominationsByYear().size());
        }

        return  nominationsByBook;

    }
}
