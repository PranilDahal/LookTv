package looktv.project.cs4540.looktv.models;

public class StoredShowModel {

    public String id;
    public String name;
    public String url;

    public StoredShowModel(String id, String name, String url) {
        this.id = id;
        this.name = name;
        this.url = url;
    }

    public StoredShowModel(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String toString(){
        return (this.getName()+" "+this.getUrl());
    }
}
