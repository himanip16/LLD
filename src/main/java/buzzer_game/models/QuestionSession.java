package buzzer_game.models;

import buzzer_game.enums.BuzzerStatus;
import lombok.Getter;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.locks.ReentrantLock;

@Getter
public class QuestionSession {

    private final String sessionId;
    private final Question question;
    private final List<Response> responses = new CopyOnWriteArrayList<>();

    private final ReentrantLock buzzerLock = new ReentrantLock();
    private volatile BuzzerStatus buzzerStatus = BuzzerStatus.OPEN;
    private volatile Player activeBuzzerHolder = null;
    private final Set<Player> lockedOutPlayers = ConcurrentHashMap.newKeySet();
    private volatile long buzzerAcquiredAtTimestamp = 0;

    private ScheduledFuture<?> timeoutFuture = null;

    public QuestionSession(String sessionId, Question question) {
        this.sessionId = sessionId;
        this.question = question;
    }

    public boolean tryAcquireBuzzer(Player player) {
        if (buzzerStatus != BuzzerStatus.OPEN || lockedOutPlayers.contains(player)) {
            return false;
        }

        boolean acquired = false;
        if (buzzerLock.tryLock()) {
            try {
                // Re-verification under the lock
                if (buzzerStatus == BuzzerStatus.OPEN && !lockedOutPlayers.contains(player)) {
                    this.activeBuzzerHolder = player;
                    this.buzzerStatus = BuzzerStatus.LOCKED;
                    this.buzzerAcquiredAtTimestamp = System.currentTimeMillis();
                    acquired = true;
                }
            } finally {
                if (!acquired) {
                    buzzerLock.unlock();
                }
            }
        }
        return acquired;
    }

    public boolean resolveAnswer(Player player, boolean isCorrect) {
        buzzerLock.lock();
        try {
            if (buzzerStatus != BuzzerStatus.LOCKED || !player.equals(activeBuzzerHolder)) {
                return false;
            }

            lockedOutPlayers.add(player);
            activeBuzzerHolder = null;
            buzzerAcquiredAtTimestamp = 0;
            buzzerStatus = isCorrect ? BuzzerStatus.RESOLVED : BuzzerStatus.OPEN;

            // Cancel the timeout task instantly if this player resolved the answer
            if (timeoutFuture != null) {
                timeoutFuture.cancel(false);
                timeoutFuture = null;
            }

            return true;
        } finally {
            buzzerLock.unlock();
        }
    }

    public void setTimeoutFuture(ScheduledFuture<?> future) {
        this.buzzerLock.lock();
        try {
            this.timeoutFuture = future;
        } finally {
            this.buzzerLock.unlock();
        }
    }

    public void recordResponse(Response response) {
        this.responses.add(response);
    }

    public long getElapsedMsSinceBuzz() {
        long startTime = this.buzzerAcquiredAtTimestamp;
        if (startTime == 0) return 0;
        return System.currentTimeMillis() - startTime;
    }
}