package chat;

public class Mnsg {
    String dniOrigin;
    String dniDestiny;
    String text;

    public Mnsg(String userOrigin, String userDestiny, String text) {
        this.dniOrigin = userOrigin;
        this.dniDestiny = userDestiny;
        this.text = text;
    }

    public String getUserOrigin() {
        return dniOrigin;
    }

    public String getUserDestiny() {
        return dniDestiny;
    }

    public String getText() {
        return text;
    }

    public void setUserOrigin(String userOrigin) {
        this.dniOrigin = userOrigin;
    }

    public void setUserDestiny(String userDestiny) {
        this.dniDestiny = userDestiny;
    }

    public void setText(String text) {
        this.text = text;
    }
}
