package kz.evilteamgenius.chessapp.database.tasks;

import android.content.Context;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import kz.evilteamgenius.chessapp.database.AppDatabase;
import kz.evilteamgenius.chessapp.database.AppDatabaseWrapper;
import kz.evilteamgenius.chessapp.database.dao.GameDao;
import kz.evilteamgenius.chessapp.database.entitys.GameEntity;
import kz.evilteamgenius.chessapp.models.Game;

public class AddGameAsyncTask extends AsyncTask<GameEntity,Void,Void> {

    private Context weakReference;

    public AddGameAsyncTask(Context weakReference) {
        this.weakReference = weakReference;
    }


    @Override
    protected Void doInBackground(GameEntity... games) {
        Context context = weakReference;
        if (context!=null){
            AppDatabase appDatabase = AppDatabaseWrapper.GetDatabase(context);
            GameDao dao = appDatabase.gameDao();
            dao.insert(games[0]);
            AppDatabaseWrapper.CloseDatabase();
        }
        return null;
    }
}
