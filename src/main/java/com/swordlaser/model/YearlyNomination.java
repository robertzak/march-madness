package com.swordlaser.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

/**
 * This is a single nomination for a given year
 */
@Builder
@Getter
@EqualsAndHashCode
public class YearlyNomination {
    private int year;
    private String title;
    private String author;
    private int numberOfNominations;
    private List<String> nominators;
}
