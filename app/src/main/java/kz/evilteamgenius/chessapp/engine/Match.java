package kz.evilteamgenius.chessapp.engine;

public class Match {

//    TurnBasedMatch turnBasedMatch;

    public boolean isLocal;
    private int numPlayers;
    public int mode;
    public final String id;

    //TODO ONLINE MATCH
//    public Match(final TurnBasedMatch match) {
//        this.turnBasedMatch = match;
//        this.isLocal = false;
//        this.mode = match.getVariant();
//        this.id = match.getMatchId();
//    }

    public Match(final String id, int mode, boolean isLocal) {
        this.isLocal = isLocal;
        this.mode = mode;
        this.id = id;
        this.numPlayers =
                (mode == GameEngine.MODE_2_PLAYER_2_SIDES || mode == GameEngine.MODE_2_PLAYER_4_SIDES) ? 2 : 4;
    }

//    public int getStatus() {
//        return turnBasedMatch.getStatus();
//    }

//    public ArrayList<Participant> getParticipants() {
//        return turnBasedMatch.getParticipants();
//    }

    public int getNumPlayers() {
//        if (turnBasedMatch != null) {
//            return turnBasedMatch.getParticipants().size() +
//                    turnBasedMatch.getAvailableAutoMatchSlots();
//        } else {
//            return numPlayers;
//        }

        return numPlayers;
    }

//    public String getParticipantId(final String playerId) {
//        return turnBasedMatch.getParticipantId(playerId);
//    }

}
