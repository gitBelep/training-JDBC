package nemetNeveloGyakorlo;

public enum GameType {

    HUN_TO_GERMAN("Némethez Magyart"), GERMAN_TO_HUN("Magyarhoz Németet"), ARTICLE_TO_GERMAN("Névelőt Némethez");

    private final String art;

    GameType(String art){
        this.art = art;
    }

    public String getArt() {
        return art;
    }

}
