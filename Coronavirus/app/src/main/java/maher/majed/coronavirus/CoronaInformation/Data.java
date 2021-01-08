package maher.majed.coronavirus.CoronaInformation;

public class Data {
    String title;
    String body;
    String imagePath;

    public Data() {
    }

    public Data(String title, String body, String imagePath) {

        this.title = title;
        this.body = body;
        this.imagePath = imagePath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
