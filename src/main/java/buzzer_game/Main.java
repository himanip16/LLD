package buzzer_game;

import buzzer_game.leaderBoard.GlobalLeaderboard;
import buzzer_game.leaderBoard.RoundLeaderboard;
import buzzer_game.models.*;
import buzzer_game.score.FixedScoreStrategy;
import buzzer_game.score.ScoreStrategy;
import buzzer_game.service.*;
import buzzer_game.service.GameLeaderboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) {
        // 1. Initialize services and real-time leaderboards
        ScoreStrategy strategy = new FixedScoreStrategy(10);
        LeaderboardService leaderboardService = new LeaderboardService(strategy);

        // Instantiating our decoupled multi-scope leaderboards
        RoundLeaderboard roundBoard = new RoundLeaderboard();
        GameLeaderboard gameBoard = new GameLeaderboard();
        GlobalLeaderboard globalBoard = new GlobalLeaderboard();

        // Register observers to the master orchestrator
        leaderboardService.registerLeaderboard(roundBoard);
        leaderboardService.registerLeaderboard(gameBoard);
        leaderboardService.registerLeaderboard(globalBoard);

        GameService gameService = new GameService(leaderboardService);

        // Explicit thread pool
        ExecutorService testExecutor = Executors.newCachedThreadPool();

        try {
            // 2. Set up reusable questions
            Option opt1 = new Option(1, "Java", true);
            Option opt2 = new Option(2, "Python", false);
            Question question1 = new Question("Q1", "Which language uses the JVM?", Arrays.asList(opt1, opt2));

            List<Question> staticQuestions = new ArrayList<>();
            staticQuestions.add(question1);

            Player aman = new Player("1", "Aman");
            Player chaman = new Player("2", "Chaman");

            // 3. Set up 5 concurrent players
            List<Player> players = Arrays.asList(
                    aman, chaman,
                    new Player("3", "Baman"),
                    new Player("4", "Daman"),
                    new Player("5", "Eman")
            );

            // 4. Initialize Game & Sessions with our new multi-round structure
            QuizGame game = gameService.createQuizGame(Collections.singletonList(staticQuestions));
            System.out.println("Quiz Game created with ID: " + game.getId());

            // 5. Players join the lobby AND initialize their scorecards
            System.out.println("\n--- 5 Players Joining Lobby ---");
            for (Player p : players) {
                gameService.joinGame(game, p);
            }

            // 6. Start the Game
            gameService.startGame(game);

            // Fetch the active question session using the new state engine traversal
            QuestionSession activeSession = game.getActiveQuestionSession();

            System.out.println("\n--- Starting 5-Way Simultaneous Buzzer Race ---");

            int totalPlayers = players.size();
            CountDownLatch startSignal = new CountDownLatch(1);
            CountDownLatch readySignal = new CountDownLatch(totalPlayers);

            List<Future<Boolean>> futures = new ArrayList<>();

            for (Player player : players) {
                futures.add(testExecutor.submit(() -> {
                    readySignal.countDown();
                    startSignal.await(); // Hold until main pulls down the gate
                    return gameService.buzz(game, activeSession, player);
                }));
            }

            // Wait for threads to load up
            readySignal.await();
            // Pull trigger down to 0
            startSignal.countDown();

            int successfulBuzzes = 0;
            Player successfulBuzzer = null;

            for (int i = 0; i < totalPlayers; i++) {
                try {
                    boolean didBuzzSucceed = futures.get(i).get();
                    if (didBuzzSucceed) {
                        successfulBuzzes++;
                        successfulBuzzer = players.get(i);
                    }
                } catch (Exception e) {
                    System.err.println("Thread error during buzz: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            System.out.println("\n--- Results Analysis ---");
            System.out.println("Total players attempted to buzz: " + totalPlayers);
            System.out.println("Total successful buzzes recorded: " + successfulBuzzes);

            if (successfulBuzzes == 1 && successfulBuzzer != null) {
                System.out.println("SUCCESS: Concurrency handled correctly. Only " + successfulBuzzer.getName() + " secured the floor.");

                // Winner submits the correct answer
                boolean isCorrect = gameService.submitAnswer(game, activeSession, opt1, successfulBuzzer);
                System.out.println("Was " + successfulBuzzer.getName() + "'s submission accepted? " + isCorrect);
            } else {
                System.out.println("FAILURE: Multiple threads breached the buzzer lock! Race condition detected.");
            }

            // Display our game leaderboard using the game board observer
            gameBoard.displayGame(game.getId());

            // ==========================================================
            // ISOLATED PER-GAME LEADERBOARD TEST (Game 1 vs Game 2)
            // ==========================================================
            System.out.println("\n==================================================");
            System.out.println("  STARTING PER-GAME ISOLATION TEST (G1 vs G2)     ");
            System.out.println("==================================================");

            // --- Setup Game 1 ---
            QuizGame game1 = gameService.createQuizGame(Collections.singletonList(staticQuestions));
            gameService.joinGame(game1, aman);
            gameService.joinGame(game1, chaman);
            gameService.startGame(game1);

            QuestionSession sessionGame1 = game1.getActiveQuestionSession();
            gameService.buzz(game1, sessionGame1, aman);
            gameService.submitAnswer(game1, sessionGame1, opt1, aman);

            // --- Setup Game 2 ---
            QuizGame game2 = gameService.createQuizGame(Collections.singletonList(staticQuestions));
            gameService.joinGame(game2, aman);
            gameService.joinGame(game2, chaman);
            gameService.startGame(game2);

            QuestionSession sessionGame2 = game2.getActiveQuestionSession();
            gameService.buzz(game2, sessionGame2, chaman);
            gameService.submitAnswer(game2, sessionGame2, opt1, chaman);

            // Display individual boards to verify absolute isolation
            gameBoard.displayGame(game1.getId());
            gameBoard.displayGame(game2.getId());

            // Let's also print our Round and Global scope leaderboards to show complete coverage
            roundBoard.displayRound("Round_1");
            globalBoard.display();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Guarantee clean teardown to release runtime processes
            testExecutor.shutdown();
            gameService.shutdown();
        }
    }
}