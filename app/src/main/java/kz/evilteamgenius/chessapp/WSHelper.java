package kz.evilteamgenius.chessapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONObject;

import kz.evilteamgenius.chessapp.models.MatchMakingMessage;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

import static android.content.Context.MODE_PRIVATE;

@SuppressWarnings({"FieldCanBeLocal", "ResultOfMethodCallIgnored", "CheckResult"})
public class WSHelper {

    public StompClient stompClient;

    public WSHelper(){
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, Const.address);
        StompUtils.lifecycle(stompClient);
        System.out.println("Start connecting to server");
        // Connect to WebSocket server
        stompClient.connect();
    }

    public void subscribeToMakeMatch(TextView textView, Context context){
        System.out.println("Subscribe broadcast endpoint to receive response");
        String dest = Const.makeMoveResponse.replace(Const.placeholder,getUsername(context));
        System.out.println(dest + "   12321312321312312");
        stompClient.topic(dest).subscribe(stompMessage -> {
            System.out.println("1");
            MatchMakingMessage matchMakingMessage = new Gson().fromJson(stompMessage.getPayload(),MatchMakingMessage.class);
            JSONObject jsonObject = new JSONObject();
            System.out.println("2");

            System.out.println("Receive: " + jsonObject.toString());
            System.out.println("3");

            textView.append(jsonObject.toString() + "\n");
            System.out.println("4");

        },throwable -> Log.e("WebsocketMakematch", "Throwable " + throwable.getMessage()));
    }

    public String getUsername(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("myPrefs", MODE_PRIVATE);
        return preferences.getString("username", null);
    }


}
