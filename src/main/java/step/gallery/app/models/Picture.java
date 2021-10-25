package step.gallery.app.models;

public class Picture {
    private String Id;
    private String Name;
    private String Description;
    private String Moment;
    private String PicturePath;


    public Picture(String name, String description, String picturePath) {
        Name = name;
        Description = description;
        PicturePath = picturePath;
    }

    public Picture(String id, String name, String description, String picturePath, String moment) {
        Id = id;
        Name = name;
        Description = description;
        PicturePath = picturePath;
        Moment = moment;
    }


    public Picture(String id, String name, String description, String moment) {
        Id = id;
        Name = name;
        Description = description;
        Moment = moment;
    }

    public Picture(String name, String description) {
        Name = name;
        Description = description;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getMoment() {
        return Moment;
    }

    public void setMoment(String moment) {
        Moment = moment;
    }

    public String getPicturePath() { return PicturePath; }

    public void setPicturePath(String picturePath) { PicturePath = picturePath; }
}