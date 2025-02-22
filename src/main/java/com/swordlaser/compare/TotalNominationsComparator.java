package com.swordlaser.compare;

import com.swordlaser.model.BookStats;

import java.util.Comparator;

public class TotalNominationsComparator implements Comparator<BookStats> {
    @Override
    public int compare(BookStats book1, BookStats book2) {
        int compare = Integer.compare(book1.getTotalNominations(), book2.getTotalNominations());

        if(compare == 0){
            return Integer.compare(book1.getsYearsNominated().size(), book2.getsYearsNominated().size());
        }

        return compare;
    }
}
