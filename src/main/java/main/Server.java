package main;

import spark.Route;
import spark.Spark;
import spark.debug.DebugScreen;

public class Server {

  public static void main(String[] args) {
    DebugScreen.enableDebugScreen();

    Spark.init();

  }


}

