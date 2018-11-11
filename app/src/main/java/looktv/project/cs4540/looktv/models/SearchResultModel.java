package looktv.project.cs4540.looktv.models;

public class SearchResultModel {

    public String showId;

    public String showName;

    public String avgRating;

    public String showPoster;

    public SearchResultModel(String showId, String showName, String avgRating, String showPoster) {
        this.showId = showId;
        this.showName = showName;
        this.avgRating = avgRating;
        this.showPoster = showPoster;
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
}
