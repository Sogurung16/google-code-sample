package com.google;

import java.util.Arrays;
import java.util.Scanner;

public class Run {
  public static final Scanner scanner = new Scanner(System.in);
  public static void main(String[] args){
    System.out.println("Hello and welcome to YouTube, what would you like to do? "
        + "Enter HELP for list of available commands or EXIT to terminate.");
    VideoPlayer videoPlayer = new VideoPlayer();
    CommandParser parser = new CommandParser(videoPlayer);
    while (true) {
      System.out.print("YT> ");
      String input = scanner.nextLine();
      if (input.equalsIgnoreCase("exit")) {
        System.out.println("YouTube has now terminated its execution. " +
            "Thank you and goodbye!");
        return;
      }
      parser.executeCommand(Arrays.asList(input.split("\\s+")));
    }
  }
}
