package edu.gatech.seclass.sdpscramble.models;

public class Statistics {
    public String playername;
    public String scrambleIdentifier;
    public int status;
    public String statIdentifier;

    public Statistics(String playername,
                      String scrambleIdentifier,
                      int status){
        this.playername = playername;
        this.scrambleIdentifier = scrambleIdentifier;
        this.status = status;
        this.statIdentifier = playername + "_" + scrambleIdentifier;
    }

    public String getPlayername(){
        return this.playername;
    }

    public String getScrambleIdentifier(){
        return this.scrambleIdentifier;
    }

    public String getStatIdentifier(){
        return this.statIdentifier;
    }
}
