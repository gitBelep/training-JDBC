package nemetNeveloGyakorlo;

public class BestPlayer {
    private String name;
    private int sumScores;
    private int sumRounds;

    public BestPlayer(String name, int sumScores, int sumRounds) {
        this.name = name;
        this.sumScores = sumScores;
        this.sumRounds = sumRounds;
    }

    public String getName() {
        return name;
    }

    public int getSumScores() {
        return sumScores;
    }

    public int getSumRounds() {
        return sumRounds;
    }

}
