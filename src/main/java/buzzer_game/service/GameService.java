package buzzer_game.service;

import buzzer_game.enums.GameStatus;
import buzzer_game.models.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class GameService {
    private static final int MIN_PLAYERS_REQUIRED = 2;
    private static final int ANSWER_TIMEOUT_SECONDS = 10;

    private final LeaderboardService leaderboard;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(4, r -> {
        Thread t = Executors.defaultThreadFactory().newThread(r);
        t.setDaemon(true);
        return t;
    });

    public GameService(LeaderboardService leaderboard) {
        this.leaderboard = leaderboard;
    }

    /**
     * Creates a fully isolated QuizGame from the static questions grouped into rounds.
     */
    public QuizGame createQuizGame(List<List<Question>> roundQuestions) {
        List<GameRound> rounds = new ArrayList<>();

        for (int i = 0; i < roundQuestions.size(); i++) {
            List<QuestionSession> sessions = new ArrayList<>();
            for (Question q : roundQuestions.get(i)) {
                String sessionId = "session_" + System.nanoTime() + "_" + q.getQuestionId();
                sessions.add(new QuestionSession(sessionId, q));
            }
            rounds.add(new GameRound("Round_" + (i + 1), sessions));
        }

        return new QuizGame(rounds);
    }

    public void joinGame(QuizGame game, Player player) {
        synchronized (game) {
            if (game.getStatus() != GameStatus.WAITING_FOR_PLAYERS) {
                System.out.println("Cannot join. Game already in progress.");
                return;
            }
            if (!game.getPlayers().contains(player)) {
                game.getPlayers().add(player);
                game.registerPlayer(player); // Registers on ScoreCard
                System.out.println(player.getName() + " joined the game room.");
            }
        }
    }

    public void startGame(QuizGame game) {
        synchronized (game) {
            if (game.getPlayers().size() < MIN_PLAYERS_REQUIRED) {
                throw new IllegalStateException("Cannot start. Minimum players not met.");
            }
            game.setStatus(GameStatus.IN_PROGRESS);
            System.out.println("\n=================================");
            System.out.println("  GAME HAS STARTED OFFICIALLY!   ");
            System.out.println("=================================");
        }
    }

    public boolean buzz(QuizGame game, QuestionSession session, Player player) {
        // Enforce that a player can only buzz on the active question session of the game
        if (!session.equals(game.getActiveQuestionSession()) || game.getStatus() != GameStatus.IN_PROGRESS) {
            System.out.println("Buzzer rejected. Question session is not active.");
            return false;
        }

        boolean acquired = session.tryAcquireBuzzer(player);
        if (acquired) {
            System.out.println("[BUZZ SECURED] " + player.getName() + " holds the floor.");

            ScheduledFuture<?> timeoutFuture = scheduler.schedule(
                    () -> handleTimeout(game, session, player),
                    ANSWER_TIMEOUT_SECONDS,
                    TimeUnit.SECONDS
            );
            session.setTimeoutFuture(timeoutFuture);
            return true;
        }
        return false;
    }

    public boolean submitAnswer(QuizGame game, QuestionSession session, Option option, Player player) {
        if (!session.equals(game.getActiveQuestionSession())) {
            System.out.println("Submission rejected. Question session is not active.");
            return false;
        }

        long timeTaken = session.getElapsedMsSinceBuzz();

        // Atomic evaluation of the answer inside the model
        boolean resolved = session.resolveAnswer(player, option.isCorrect());
        if (!resolved) {
            System.out.println("Submission rejected for " + player.getName());
            return false;
        }

        if (option.isCorrect()) {
            System.out.println("[CORRECT] " + player.getName() + " answered correctly!");
            session.recordResponse(new Response(player, option, timeTaken));

            // Record points on game scorecard
            leaderboard.addScoreForCorrectAnswer(game, session, player);

            // Advance the game to the next question automatically
            advanceGameFlow(game);
            return true;
        } else {
            System.out.println("[INCORRECT] Buzzer released for other players.");

            // Check if all active players are locked out of this question
            if (session.getLockedOutPlayers().containsAll(game.getPlayers())) {
                System.out.println("All players are locked out. Moving to the next question.");
                advanceGameFlow(game);
            }
            return false;
        }
    }

    private void handleTimeout(QuizGame game, QuestionSession session, Player player) {
        boolean resolved = session.resolveAnswer(player, false);
        if (resolved) {
            System.out.println("\n[TIMEOUT] Player " + player.getName() + " expired.");

            // Check if everyone is now locked out because of this timeout
            if (session.getLockedOutPlayers().containsAll(game.getPlayers())) {
                System.out.println("All players are locked out. Moving to the next question.");
                advanceGameFlow(game);
            }
        }
    }

    private void advanceGameFlow(QuizGame game) {
        synchronized (game) {
            game.advanceToNextQuestion();
            if (game.getActiveQuestionSession() == null) {
                game.setStatus(GameStatus.COMPLETED);
                System.out.println("\n=================================");
                System.out.println("    GAME COMPLETED OFFICIALLY!   ");
                System.out.println("=================================");
            } else {
                System.out.println("\n--- Advanced to Round " + (game.getCurrentRoundIndex() + 1)
                        + ", Question " + (game.getCurrentQuestionIndex() + 1) + " ---");
            }
        }
    }

    public void shutdown() {
        scheduler.shutdown();
    }
}