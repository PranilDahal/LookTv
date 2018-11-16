package looktv.project.cs4540.looktv.models;

public class SearchResultModel {

    public String showId;

    public String showName;

    public String avgRating;

    public String showPoster;

    public String website;

    public String language;

    public String premiered;

    public String status;

    public SearchResultModel(String showId, String showName, String avgRating, String showPoster, String website,
                             String language, String premiered, String status) {
        this.showId = showId;
        this.showName = showName;
        this.avgRating = avgRating;
        this.showPoster = showPoster;
        this.website = website;
        this.language = language;
        this.premiered = premiered;
        this.status = status;
    }

    public String getShowId() {
        return showId;
    }

    public void setShowId(String showId) {
        this.showId = showId;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public String getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(String avgRating) {
        this.avgRating = avgRating;
    }

    public String getShowPoster() {
        return showPoster;
    }

    public void setShowPoster(String showPoster) {
        this.showPoster = showPoster;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPremiered() {
        return premiered;
    }

    public void setPremiered(String premiered) {
        this.premiered = premiered;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
