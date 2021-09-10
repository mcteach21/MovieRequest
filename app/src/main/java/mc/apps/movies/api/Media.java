package mc.apps.movies.api;

import java.io.Serializable;

public abstract class Media implements Serializable {

    public abstract int getId();
    public abstract String getTitle();
    public abstract String getSubTitle();
    public abstract String getOverview();

    public abstract String getLogoPath();

}
