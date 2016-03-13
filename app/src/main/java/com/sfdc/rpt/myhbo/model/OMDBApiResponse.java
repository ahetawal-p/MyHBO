package com.sfdc.rpt.myhbo.model;

/**
 * Simple POJO for storing the json api response to a Java object
 * Created by ahetawal on 3/7/16.
 */
public class OMDBApiResponse {
    private String title;
    private String year;
    private String posterUrl;
    private String imdbId;
    private boolean isFavorite;


    public boolean isFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public String getImdbId() {
        return imdbId;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("OMDBApiResponse{");
        sb.append("title='").append(title).append('\'');
        sb.append(", year='").append(year).append('\'');
        sb.append(", posterUrl='").append(posterUrl).append('\'');
        sb.append(", imdbId='").append(imdbId).append('\'');
        sb.append(", isFavorite=").append(isFavorite);
        sb.append('}');
        return sb.toString();
    }
}
