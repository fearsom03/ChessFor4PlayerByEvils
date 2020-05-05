package kz.evilteamgenius.chessapp.activity;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.Arrays;

import kz.evilteamgenius.chessapp.BoardView;
import kz.evilteamgenius.chessapp.Const;
import kz.evilteamgenius.chessapp.R;
import kz.evilteamgenius.chessapp.StompUtils;
import kz.evilteamgenius.chessapp.engine.Board;
import kz.evilteamgenius.chessapp.engine.Coordinate;
import kz.evilteamgenius.chessapp.engine.Match;
import kz.evilteamgenius.chessapp.fragments.GameFragment;
import kz.evilteamgenius.chessapp.fragments.NavigationPageFragment;
import kz.evilteamgenius.chessapp.models.Game;
import kz.evilteamgenius.chessapp.models.MatchMakingMessage;
import kz.evilteamgenius.chessapp.models.MoveMessage;
import kz.evilteamgenius.chessapp.models.enums.MatchMakingMessageType;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import timber.log.Timber;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompCommand;
import ua.naiksoftware.stomp.dto.StompHeader;
import ua.naiksoftware.stomp.dto.StompMessage;

import static kz.evilteamgenius.chessapp.Constants.URL_MAKE_MOVE;
import static kz.evilteamgenius.chessapp.extensions.CheckExtensionKt.checkInternet;
import static kz.evilteamgenius.chessapp.extensions.CheckExtensionKt.getUsername;
import static kz.evilteamgenius.chessapp.extensions.ViewExtensionsKt.toast;

@SuppressWarnings({"FieldCanBeLocal", "ResultOfMethodCallIgnored", "CheckResult"})

public class MainActivity extends AppCompatActivity {

    public static StompClient stompClient;
    Thread thread;
    private Fragment fragment;
    static String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        token = getToken();
        fragment = new NavigationPageFragment();
        replaceFragment(fragment);
    }

    //todo need to change this shit))
    public static void sendMove(Coordinate old_pos, Coordinate new_pos, boolean ifOver) {
        Timber.d("sendMove 2");
        old_pos = new Coordinate(old_pos.x, old_pos.y, Board.rotations);
        new_pos = new Coordinate(new_pos.x, new_pos.y, Board.rotations);
        kz.evilteamgenius.chessapp.engine.Game.game.setMade_by(kz.evilteamgenius.chessapp.engine.Game.myPlayerUserame);
        kz.evilteamgenius.chessapp.engine.Game.game.setFrom_x(old_pos.x);
        kz.evilteamgenius.chessapp.engine.Game.game.setFrom_y(old_pos.y);
        kz.evilteamgenius.chessapp.engine.Game.game.setTo_x(new_pos.x);
        kz.evilteamgenius.chessapp.engine.Game.game.setTo_y(new_pos.y);
        if (ifOver)
            kz.evilteamgenius.chessapp.engine.Game.game.setResult(kz.evilteamgenius.chessapp.engine.Game.myPlayerUserame + " wins!");
        new MakeMoveToServer().execute();
    }

    public static class MakeMoveToServer extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            Timber.d("sendMove 3");
            Game game = kz.evilteamgenius.chessapp.engine.Game.game;
            OkHttpClient okHttpClient = new OkHttpClient();

            RequestBody formBody = new FormBody.Builder()
                    .add("id", String.valueOf(game.getId()))
                    .add("player1", game.getPlayer1())
                    .add("player2", game.getPlayer2())
                    .add("player3", game.getPlayer1())
                    .add("player4", game.getPlayer2())
                    .add("FEN", game.getFEN() == null ? "" : game.getFEN())
                    .add("result", game.getResult() == null ? "" : game.getResult())
                    .add("from_x", String.valueOf(game.getFrom_x()))
                    .add("from_y", String.valueOf(game.getFrom_y()))
                    .add("to_x", String.valueOf(game.getTo_x()))
                    .add("to_y", String.valueOf(game.getTo_y()))
                    .add("made_by", game.getMade_by() == null ? "" : game.getMade_by())
                    .add("type", String.valueOf(game.getType()))
                    .build();

            Request request = new Request.Builder()
                    .url(URL_MAKE_MOVE)
                    .post(formBody)
                    .addHeader("cache-control", "no-cache")
                    .addHeader("Authorization", "Bearer " + token)
                    .build();

            Response response = null;
            try {
                response = okHttpClient.newCall(request).execute();
                if (response.isSuccessful()) {
                    String result = response.body().string();
                    if (!result.isEmpty()) {
                        JSONObject jsonObject = new JSONObject(result);
                        Game receivedGame = new Game(jsonObject.getLong("id"),
                                jsonObject.getString("play1"),
                                jsonObject.getString("play2"),
                                jsonObject.getString("play3"),
                                jsonObject.getString("play4"),
                                jsonObject.getString("FEN"),
                                jsonObject.getString("result"),
                                jsonObject.getInt("from_x"),
                                jsonObject.getInt("from_y"),
                                jsonObject.getInt("to_x"),
                                jsonObject.getInt("to_y"),
                                jsonObject.getString("made_by"),
                                jsonObject.getInt("type"));
                        //showToast(receivedGame.toString());
                    } else {
//                        toast(this,"Make move to server failed in Game Activity");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    public void replaceFragment(Fragment fragment) {
        FragmentTransaction tr = getSupportFragmentManager().beginTransaction();
        tr.replace(R.id.frame, fragment);
        tr.commitNow();
    }


    public void connectAndMakeMatch(int LAST_SELECTED_MATCH_MODE) {
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, Const.address);
        StompUtils.lifecycle(stompClient);
        Timber.d("Start connecting to server");
        // Connect to WebSocket server
        stompClient.connect();
        int numberOfPlayers = LAST_SELECTED_MATCH_MODE == 1 || LAST_SELECTED_MATCH_MODE == 2 ? 2 : 4;
        MatchMakingMessage matchMakingMessageToSend = new MatchMakingMessage(MatchMakingMessageType.CONNECT, LAST_SELECTED_MATCH_MODE, null, null);
        Gson gson = new Gson();
        String json = gson.toJson(matchMakingMessageToSend);
        if (!checkInternet(this)) {
            Timber.e("INTERNET CONNECTION LOST");
        }
        stompClient.send(new StompMessage(
                // Stomp command
                StompCommand.SEND,
                // Stomp Headers, Send Headers with STOMP
                // the first header is required, and the other can be customized by ourselves
                Arrays.asList(
                        new StompHeader(StompHeader.DESTINATION, Const.makeMatchAddress),
                        new StompHeader("authorization", getUsername(this))
                ),
                // Stomp payload
                json)
        ).subscribe();
//
        String dest = Const.makeMatchResponse.replace(Const.placeholder, getUsername(this));
        stompClient.topic(dest).subscribe(stompMessage -> {
            MatchMakingMessage matchMakingMessageReceived = new Gson().fromJson(stompMessage.getPayload(), MatchMakingMessage.class);
            //JSONObject jsonObject = new JSONObject(stompMessage.getPayload());
            Timber.d("Received: *****\n %s *****\n", matchMakingMessageReceived.toString());
            if (matchMakingMessageReceived.getMessageType() == MatchMakingMessageType.CONNECTED) {
                Match match = new Match(String.valueOf(System.currentTimeMillis()),
                        LAST_SELECTED_MATCH_MODE, false);
                kz.evilteamgenius.chessapp.engine.Game.newGame(match, matchMakingMessageReceived.getPlayers(), getUsername(this), matchMakingMessageReceived.getRoom_id());
                startGame(match.id);
            }
        }, throwable -> {
            Timber.e("Throwable %s", throwable.getMessage());
            if (!checkInternet(this)) {
                Timber.e("ITS INTERNET CONNECTION kuka ");
            }
        });
    }

    public void getMove(BoardView board) {
        stompClient.topic(kz.evilteamgenius.chessapp.engine.Game.roomAdress).subscribe(stompMessage -> {
            MoveMessage message = new Gson().fromJson(stompMessage.getPayload(), MoveMessage.class);
            if (message.getPlayerID().equals(kz.evilteamgenius.chessapp.engine.Game.myPlayerUserame))
                return;
            Timber.d("Received: *****\n %s ***** \n", message.toString());
            Coordinate pos1 = new Coordinate(message.getFrom_x(), message.getFrom_y(), Board.rotations);
            Coordinate pos2 = new Coordinate(message.getTo_x(), message.getTo_y(), Board.rotations);
            Board.moveWhenReceived(pos1, pos2);
            // Stuff that updates the UI
            this.runOnUiThread(board::invalidate);
        }, throwable -> {
            Timber.e("Throwable %s", throwable.getMessage());
            if (!checkInternet(this)) {
                Timber.e("Kuka its internet connection");
            }
        });
    }


    public void startGame(final String matchID) {
        Timber.d("startGame");
        fragment = new GameFragment();
        Bundle b = new Bundle();
        b.putString("matchID", matchID);
        fragment.setArguments(b);
        replaceFragment(fragment);
    }

    public String getToken() {
        SharedPreferences preferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        return preferences.getString("token", "");
    }
}