package nemetNeveloGyakorlo;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class FileOperations {
    static String PATH_STR = "c:\\NemetNeveloGyakorlo";
    static List<Result> OLD_RESULTS;
    static List<BestPlayer> BEST_PLAYERS = new ArrayList<>();

    public FileOperations() {
        gainOldScores();
        gainBestPlayers();
    }

//Make Word-list for the game   *   *   *
    public List<Word> provideWordList() {
        List<Word> allWords = readDictFile();
        List<Word> result = new ArrayList<>(choseWords(allWords));
        allWords = null;
        return result;
    }

    private List<Word> readDictFile() {
        try (BufferedReader br = Files.newBufferedReader(Menu.SELECTED_DictFile.toPath())
        ) {
            return readLines(br);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read file (SELECTED_DictFile): " + Menu.SELECTED_DictFile.getName(), e);
        }
    }

    private List<Word> readLines(BufferedReader br) throws IOException {
        String line;
        List<Word> allWords = new ArrayList<>();
        List<String> corruptLines = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(";");
            if (parts.length == 3) {
                allWords.add(new Word(parts[0], parts[1], parts[2]));
            } else {
                corruptLines.add("*" + line + " ");
            }
        }
        if (!corruptLines.isEmpty()) {
            ProblemPopUp pp = new ProblemPopUp("Corrupt line(s): " + corruptLines.toString());
        }
        return allWords;
    }

    private List<Word> choseWords(List<Word> allWords) {
        Set<Integer> nr = new LinkedHashSet<>();
        Random rnd = new Random();
        int rounds = allWords.size() < 15 ? allWords.size() : 15; //if dict is smaller than 15
        while (nr.size() < rounds) {
            nr.add(rnd.nextInt(allWords.size()));
        }
        List<Word> selectedWords = new ArrayList<>();
        for (int i : nr) {
            selectedWords.add(allWords.get(i));
        }
        return selectedWords;
    }

//Make list of dictionaries     *   *   *
    public List<File> getDictionaryFiles() {
        File pathOfFiles = new File(PATH_STR);
        List<File> dictionaries = new ArrayList<>();
        try {
            dictionaries = Arrays.asList(pathOfFiles.listFiles(
                    f -> f.isFile()
                    && f.getName().startsWith("w_")
                    && f.getName().endsWith(".csv")));
            if (dictionaries.isEmpty()) {
                throw new IllegalStateException("");
            }
            dictionaries.sort(Comparator.comparing(File::getName));
        } catch (Exception e) {
            throw new IllegalStateException("Please provide some dictionary files in folder: \"c:/NemetNeveloGyakorlo\" ! Then reastart!", e);
        }
        return dictionaries;
    }

//Read the old scores       *   *   *
    public void gainOldScores(){
        Path scoresFile = Path.of(PATH_STR + "\\Scores_encrypted.adat");
        String encriptedScores = readFile(scoresFile);
        List<Result> scores = buildResults( breakTheCodeAndGainResults(encriptedScores) );
        scores.sort((r1, r2) -> {
            if(r1.getLdt().isBefore(r2.getLdt())){
                return 1;
            } else if(r1.getLdt().isAfter(r2.getLdt())) {
            return -1;
            } else {
              return 0;
            }
        });
        OLD_RESULTS = scores;
    }

    public void gainBestPlayers(){
        Path playersFile = Path.of(PATH_STR + "\\BestPlayers_encrypted.adat");
        String encriptedPlayers = readFile(playersFile);
        String playersData =  breakTheCodeAndGainResults(encriptedPlayers);
        String[] bestPlayers = playersData.split(";");
        List<BestPlayer> players = new ArrayList<>();
        for(String p : bestPlayers){
            String[] actualP = p.split(",");
            int score = Integer.parseInt(actualP[1]);
            int rounds = Integer.parseInt(actualP[2]);
            players.add(new BestPlayer(actualP[0], score, rounds));
        }
        players.sort((p1, p2) -> p2.getSumScores() - p1.getSumScores());
        BEST_PLAYERS = players;
    }

    private String readFile(Path path) {
        Charset utf8 = StandardCharsets.UTF_8;
        try {
            return Files.readString(path, utf8);
            } catch (IOException ioe) {
            throw new IllegalStateException("Cannot read file \""+ path.getFileName() +"\"");
        }
    }

    private String breakTheCodeAndGainResults(String encriptedScores) {
        StringBuilder sb = new StringBuilder();
        for (char c : encriptedScores.toCharArray()) {
            sb.append( (char) (((int)c)-10) );
        }
        return (sb.toString());
    }

    private List<Result> buildResults(String whole) {
        List<Result> results = new ArrayList<>();
        String[] elements = whole.split(";");
        for (String s : elements) {
            String[] parts = s.split(",");
            if(parts.length > 5){         //if name contains ","
                String correctName = parts[4];
                for(int i = 5; i < parts.length; i++){
                    correctName += parts[i];
                }
            }
            DateTimeFormatter dtWriteFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd.HH.mm");
            LocalDateTime ldt = LocalDateTime.parse(parts[0], dtWriteFormatter);
            int points = Integer.parseInt(parts[1]);
            GameType type = GameType.valueOf( parts[3] );
            results.add(new Result(points, parts[2], type, parts[4], ldt));
        }
        return results;
    }

//Overwrite with the new scores    *   *   *
    public void notingResults(int points, String dict, GameType gameArt, String name) {
        Result actual = new Result(points, dict.substring(2, dict.lastIndexOf(".")), gameArt, name, LocalDateTime.now());
        if (OLD_RESULTS == null) {
            OLD_RESULTS = new ArrayList<>();
        }
        OLD_RESULTS.add(0, actual);

        noting20MostSuccesfulPlayers();

        StringBuilder encriptedScores = new StringBuilder();
        int counter = 0;
        for (Result r : OLD_RESULTS) {
            DateTimeFormatter dtWriteFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd.HH.mm");
            String date = r.getLdt().format(dtWriteFormatter);
            String whole = date +","+ r.getPoints() +","+ r.getDict() +","+ r.getGameType().toString() +","+ r.getName() +";";

            encriptedScores.append(encryptString(whole));

            counter++;
            if(counter >= 25){
                break;
            }
        }
        Path scoresFile = Path.of(PATH_STR + "\\Scores_encrypted.adat");
        rewriteEncryptedFile(scoresFile, encriptedScores.toString());
    }

    private void noting20MostSuccesfulPlayers() {
        String players = getMostSuccesfulPlayers();
        String encryptedPlayers = encryptString(players);
        Path playersFile = Path.of(PATH_STR + "\\BestPlayers_encrypted.adat");
        rewriteEncryptedFile(playersFile, encryptedPlayers);
    }

    private String getMostSuccesfulPlayers() {
        Map<String, List<Integer>> players = new HashMap<>();
        for (Result r : OLD_RESULTS) {
            if (!players.containsKey(r.getName())) {
                players.put(r.getName(), new ArrayList<>(List.of(r.getPoints(), 1)));
            } else {
                List<Integer> data = players.get(r.getName());
                data.set(0, data.get(0) + r.getPoints());
                data.set(1, data.get(1) + 1);
            }
        }
        return makeString(players);
    }

    private String makeString(Map<String, List<Integer>> players){
        StringBuilder sb = new StringBuilder();
        int score;
        int rounds;
        int counter = 0;
        for(String key : players.keySet()){
            score = players.get(key).get(0);
            rounds = players.get(key).get(1);
            sb.append(key +","+ score +","+ rounds +";");
            counter++;
            if(counter > 20){
                break;
            }
        }
        return sb.toString();
    }

    private String encryptString(String whole) {
        StringBuilder sb = new StringBuilder();
        for(char c : whole.toCharArray()){
            sb.append( (char) (((int) c) +10) );
        }
        return sb.toString();
    }

    private void rewriteEncryptedFile(Path path, String toWrite) {
        Charset utf8 = StandardCharsets.UTF_8;
        try {
            Files.writeString(path, toWrite, utf8, StandardOpenOption.TRUNCATE_EXISTING);
        }catch (IOException ioe) {
            throw new IllegalStateException("Cannot write file \""+ path.getFileName() +"\"");
        }
    }

//other   *   *   *

}
