package com.swordlaser.service;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.MappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.*;
import com.swordlaser.model.BookStats;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.net.URL;
import java.util.List;
import java.util.Map;

@Slf4j
public class WriteCSVService {
    final static String OUTPUT_FILE = "/output/all_data.csv";
    /**
     * This will write all the books out as a CSV
     * @param nominationsByBook the book data to write
     */
    public void writeCSV(Map<String, BookStats> nominationsByBook){
        final URL outputUrl =  WriteCSVService.class.getResource(OUTPUT_FILE);

        if(outputUrl == null){
            log.error("Failed to get output file: {}", OUTPUT_FILE);
            return;
        }

        // Sort by title. TODO allow parameter for different sorts?
        List<BookStats> sortedBooks = nominationsByBook.values().stream()
                .sorted( (b1, b2) -> String.CASE_INSENSITIVE_ORDER.compare(b1.getTitle(), b2.getTitle()))
                .toList();

        // TODO is there a cleaner way to do the mapping? Ideally with annotations on BookStats
        try (Writer writer = new FileWriter(new File(outputUrl.toURI()))) {
            StatefulBeanToCsv<BookStats> sbc = new StatefulBeanToCsvBuilder<BookStats>(writer)
                    .withMappingStrategy(new MappingStrategy<>() {
                        @Override
                        public void captureHeader(CSVReader csvReader)  {

                        }

                        @Override
                        public String[] generateHeader(BookStats bookStats) {
                            return new String[]{"Title", "Author", "Total Nominations", "Years Nominated"};
                        }

                        @Override
                        public BookStats populateNewBean(String[] strings) {
                            return null;
                        }

                        @Override
                        public void setType(Class<? extends BookStats> aClass) throws CsvBadConverterException {

                        }

                        @Override
                        public String[] transmuteBean(BookStats bookStats)  {
                            List<String> yearList = bookStats.getsYearsNominated().stream().map(year -> Integer.toString(year)).toList();
                            String years = String.join(",", yearList);
                            List<String> row = List.of(bookStats.getTitle(), bookStats.getAuthor(),
                                    Integer.toString(bookStats.getTotalNominations()), years);
                            String [] rowArray = new String[row.size()];
                            return row.toArray(rowArray);
                        }
                    })
                    .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                    .build();


            log.info("Writing {} books to {}", nominationsByBook.size(), outputUrl.getFile());
            sbc.write(sortedBooks);
        } catch( Exception e){
            log.error("Failed to write Book Stats to {}", OUTPUT_FILE, e);
        }
    }
}
