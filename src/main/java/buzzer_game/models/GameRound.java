package buzzer_game.models;

import buzzer_game.enums.BuzzerStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@Getter
@Setter
public class GameRound {


        private final String roundId;
        private final List<QuestionSession> questionSessions; // Correct type

        public GameRound(String roundId, List<QuestionSession> questionSessions) {
            this.roundId = roundId;
            this.questionSessions = questionSessions;
        }


    // Concurrency control embedded directly in the Round
    private final ReentrantLock buzzerLock = new ReentrantLock();

    // Operational State
    private volatile BuzzerStatus status = BuzzerStatus.OPEN;
    private volatile Player activeBuzzerHolder = null;
    private final Set<Player> lockedOutPlayers = ConcurrentHashMap.newKeySet();

    /**
     * Attempts to acquire the buzzer floor atomically.
     */
    public boolean tryAcquireBuzzer(Player player) {
        if (status != BuzzerStatus.OPEN || lockedOutPlayers.contains(player)) {
            return false;
        }

        if (buzzerLock.tryLock()) {
            this.activeBuzzerHolder = player;
            this.status = BuzzerStatus.LOCKED;
            return true;
        }
        return false;
    }

    /**
     * Releases the buzzer lock safely.
     */
    public void releaseBuzzer() {
        if (buzzerLock.isLocked()) {
            buzzerLock.unlock();
        }
        this.activeBuzzerHolder = null;
        if (this.status != BuzzerStatus.RESOLVED) {
            this.status = BuzzerStatus.OPEN;
        }
    }

    /**
     * Locks out a player permanently for this specific round.
     */
    public void lockOutPlayer(Player player) {
        lockedOutPlayers.add(player);
    }
}