package com.bueno.application.main;

import com.bueno.application.withuser.PlayAgainstBots;
import com.bueno.application.withbots.features.EvaluateBot;
import com.bueno.application.withbots.features.PlayWithBots;

import java.util.Scanner;
import java.util.logging.LogManager;

public class ConsoleStarter {

    private static final Scanner SCAN = new Scanner(System.in);
    private String option = "";
    public static void main(String[] args) {
        ConsoleStarter console = new ConsoleStarter();
        console.initialMenu();

    }

    private void initialMenu(){

        System.out.println("=+=+= CTRUCO COSOLE =+=+=");
        menu();

    }

    private void menu(){
        while (! option.equals("0")){
            menuOptions();
            option = SCAN.nextLine().trim();
            menuSwitch();
        }
    }
    private void menuOptions(){
        System.out.println("\nWhat do you Wanna Play");
        System.out.println("Player vs Bot...............[1]");
        System.out.println("Simulate Bot vs Bot match...[2]");
        System.out.println("Evaluate a Bot..............[3]");
        System.out.println("exit........................[0]");
    }

    private void menuSwitch(){
        switch (option){
            case "0" ->{}
            case "1" -> {
                LogManager.getLogManager().reset();
                final var cli = new PlayAgainstBots();
                cli.gameCLIStarter();
            }
            case "2" ->{
                final var playBots = new PlayWithBots();
                playBots.playWithBotsConsole();
            }
            case "3" ->{
                final var evaluateBot = new EvaluateBot();
                evaluateBot.againstAll();
            }
            default -> System.out.println("invalid Answer! \n");

        }

    }


}