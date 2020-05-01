package kz.evilteamgenius.chessapp.database.asyncTasksDB;

import android.content.Context;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import kz.evilteamgenius.chessapp.database.AppDatabase;
import kz.evilteamgenius.chessapp.database.AppDatabaseWrapper;
import kz.evilteamgenius.chessapp.database.dao.GameDao;
import kz.evilteamgenius.chessapp.database.entitys.GameEntity;

public class GetAllGamesAsyncTask extends AsyncTask<Void, Void, List<GameEntity>> {
    private GameRetrieveResultProcess process;
    private WeakReference<Context> weakReference;

    public GetAllGamesAsyncTask(GameRetrieveResultProcess process, WeakReference<Context> context) {
        this.process = process;
        this.weakReference = context;
    }

    @Override
    protected List<GameEntity> doInBackground(Void... voids) {
        List<GameEntity> arr = new ArrayList<>();
        Context context = weakReference.get();
        if (context != null) {
            AppDatabase appDatabase = AppDatabaseWrapper.GetDatabase(context);
            GameDao dao = appDatabase.gameDao();
            arr = dao.getAllGame();
            AppDatabaseWrapper.CloseDatabase();
        }
        return arr;
    }

    @Override
    protected void onPostExecute(List<GameEntity> games) {
        process.process(games);
        super.onPostExecute(games);
    }

    public interface GameRetrieveResultProcess {
        void process(List<GameEntity> games);
    }
}
