package sample;

public class Music
{
    private int id;
    private String name;
    private String duration;
    private boolean played;

    public Music(int id,String name, String duration)
    {
        this.id=id;
        this.name = name;
        this.duration = duration;
        this.played=false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public boolean isPlayed() {
        return played;
    }

    public void setPlayed(boolean played) {
        this.played = played;
    }
}
