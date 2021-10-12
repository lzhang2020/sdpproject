package edu.gatech.seclass.sdpscramble.models;

public class Player {
    public String userName;
    public String firstName;
    public String lastName;
    public String email;
    public int isLogin;
    public int createdNumber;
    public int solvedNumber;
    public int solvedByNumber;
    public String processingScramble;

    public Player(String userName,
                  String firstName,
                  String lastName,
                  String email,
                  int isLogin,
                  int createdNumber,
                  int solvedNumber,
                  int solvedByNumber,
                  String processingScramble){
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.isLogin = isLogin;
        this.createdNumber = createdNumber;
        this.solvedNumber = solvedNumber;
        this.solvedByNumber = solvedByNumber;
        this.processingScramble = processingScramble;
    }

    public String getUsername(){
        return this.userName;
    }
}