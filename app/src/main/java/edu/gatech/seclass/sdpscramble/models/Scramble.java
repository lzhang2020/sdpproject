package edu.gatech.seclass.sdpscramble.models;

public class Scramble {
    public String phrase;
    public String clue;
    public String answer;
    public String author;
    public int isSolved;
    public String identifier;
    public int correct;

    public Scramble (String phrase,
                     String clue,
                     String answer,
                     String authorName,
                     int isSolved,
                     String identifier,
                     int correct){
        this.phrase = phrase;
        this.clue = clue;
        this.answer = answer;
        this.author = authorName;
        this.isSolved = isSolved;
        this.identifier = identifier;
        this.correct = correct;
    }

    public String getScrambleIdentifier(){
        return this.identifier;
    }
}
