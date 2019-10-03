package MarvelProject.DTO;

public class ComicRawDTO {
    private int id;
    private String title;
    private String description;
    private String modified;
    private String format;
    private int pageCout;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public int getPageCout() {
        return pageCout;
    }

    public void setPageCout(int pageCout) {
        this.pageCout = pageCout;
    }
}
