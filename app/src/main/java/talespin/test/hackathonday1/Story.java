package talespin.test.hackathonday1;

/**
 * Created by pragar on 8/31/2014.
 */
public class Story {
    private String title;
    private String info;
    private String draft;

    public Story(String title, String info, String draft) {
        this.title = title;
        this.info = info;
        this.draft = draft;
    }

    public Story(String title, String info) {
        this(title, info, "");
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getDraft() {
        return draft;
    }

    public void setDraft(String draft) {
        this.draft = draft;
    }
}
