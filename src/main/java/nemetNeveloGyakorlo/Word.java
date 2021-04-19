package nemetNeveloGyakorlo;

public class Word {
    private String article;
    private String germanWord;
    private String hunWord;

    public Word(String article, String germanWord, String hunWord) {
        this.article = article;
        this.germanWord = germanWord;
        this.hunWord = hunWord;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public String getGermanWord() {
        return germanWord;
    }

    public void setGermanWord(String germanWord) {
        this.germanWord = germanWord;
    }

    public String getHunWord() {
        return hunWord;
    }

    public void setHunWord(String hunWord) {
        this.hunWord = hunWord;
    }

}
