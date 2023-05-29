package com.jkallich;

public class DataRow implements Comparable<DataRow> {
    public String fileName;
    public String isoform;
    public double queryCover; // COMPARE FIRST
    public String hspEvalue; // on website, E Value
    public double percentageIdentity;
    public int hitLen; // on website, Acc. Len
    public String hitAccession; // on website, Accession
    public int hspScore; // on website, Total Score; COMPARE SECOND

    @Override
    public int compareTo (DataRow row) {

        // Think of it like this: you are almost subtracting this object from the row (row - this)
        // Descending Sort: from greatest to least query cover/hsp (total) score

        if (row.queryCover > this.queryCover) {
            return 1;
        } else if (row.queryCover < this.queryCover) {
            return -1;
        }

        if (row.hspScore > this.hspScore) {
            return 1;
        } else if (row.hspScore < this.hspScore) {
            return -1;
        }

        return 0;
    }
}
