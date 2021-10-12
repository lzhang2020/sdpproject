package edu.gatech.seclass.sdpscramble.webservice.remote;


import java.util.Map;

import edu.gatech.seclass.sdpscramble.models.Player;
import edu.gatech.seclass.sdpscramble.models.Scramble;
import edu.gatech.seclass.sdpscramble.models.Statistics;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface WebServiceInterface {

    @PUT("/players/{username}.json")
    Call<Player> createPlayer(
            @Path("username") String userIdentifier,
            @Body Player player);

    @PUT("/players/{username}/createdNumber.json")
    Call<Integer> setCreatedNumber(
            @Path("username") String username,
            @Body Integer created);

    @PUT("/players/{username}/solvedNumber.json")
    Call<Integer> setSolvedNumber(
            @Path("username") String username,
            @Body Integer solved);

    @PUT("/players/{username}/solvedByNumber.json")
    Call<Integer> setSolvedByNumber(
            @Path("username") String username,
            @Body Integer solvedBy);

    @PUT("/players/{username}/processing.json")
    Call<String> setProcessing(
            @Path("username") String username,
            @Body String processingScramble);

    @PUT("/scrambles/{identifier}.json")
    Call<Scramble> createScramble(
            @Path("identifier") String scrambleIdentifier,
            @Body Scramble scramble);

    @PUT("/scrambles/{identifier}/correct.json")
    Call<Integer> setCorrect(
            @Path("identifier") String scrambleIdentifier,
            @Body Integer correct);

    @PUT("/stats/{identifier}.json")
    Call<Statistics> createStats(
            @Path("identifier") String statIdentifier,
            @Body Statistics statistics);

}
