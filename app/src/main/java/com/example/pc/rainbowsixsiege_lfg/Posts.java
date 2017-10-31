package com.example.pc.rainbowsixsiege_lfg;

/**
 * Created by pc on 2017-07-10.
 */

public class Posts {

    private String description;
    private String name;
    private String rank;
    private String platform;
    private String IGN;
    private String kdwl;
    private String mode;

//    public Posts(){
//
//    }

    public Posts(String name, String rank, String description, String platform, String IGN, String kdwl, String mode) {
        this.description = description;
        this.name = name;
        this.rank = rank;
        this.platform = platform;
        this.IGN = IGN;
        this.kdwl = kdwl;
        this.mode = mode;
    }

    public String getMode() {
        return mode;
    }

    public String getPlatform() {
        return platform;
    }

    public String getIGN() {
        return IGN;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public String getRank() {
        return rank;
    }

    public String getKdwl() {
        return kdwl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Posts posts = (Posts) o;

        if (description != null ? !description.equals(posts.description) : posts.description != null)
            return false;
        if (name != null ? !name.equals(posts.name) : posts.name != null) return false;
        if (rank != null ? !rank.equals(posts.rank) : posts.rank != null) return false;
        if (platform != null ? !platform.equals(posts.platform) : posts.platform != null)
            return false;
        if (IGN != null ? !IGN.equals(posts.IGN) : posts.IGN != null) return false;
        if (kdwl != null ? !kdwl.equals(posts.kdwl) : posts.kdwl != null) return false;
        return mode != null ? mode.equals(posts.mode) : posts.mode == null;

    }

    @Override
    public int hashCode() {
        int result = description != null ? description.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (rank != null ? rank.hashCode() : 0);
        result = 31 * result + (platform != null ? platform.hashCode() : 0);
        result = 31 * result + (IGN != null ? IGN.hashCode() : 0);
        result = 31 * result + (kdwl != null ? kdwl.hashCode() : 0);
        result = 31 * result + (mode != null ? mode.hashCode() : 0);
        return result;
    }
}
