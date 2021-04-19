package nemetNeveloGyakorlo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Result {
    private int points;
    private String dict;
    private GameType gameType;
    private String name;
    private LocalDateTime ldt;

    public Result(int points, String dict, GameType gameType, String name, LocalDateTime ldt) {
        this.points = points;
        this.dict = dict;
        this.gameType = gameType;
        this.name = name;
        this.ldt = ldt;
    }

    @Override
    public String toString() {
        DateTimeFormatter dtWriteFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
        String date = ldt.format(dtWriteFormatter);
        return date +", "+ points +", "+ dict  +", "+ gameType.getArt() +", "+ name;
    }

    public int getPoints() {
        return points;
    }

    public String getDict() {
        return dict;
    }

    public GameType getGameType() {
        return gameType;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getLdt() {
        return ldt;
    }

}
