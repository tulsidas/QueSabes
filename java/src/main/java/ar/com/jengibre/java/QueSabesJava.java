package ar.com.jengibre.java;

import playn.core.PlayN;
import playn.java.JavaPlatform;

import ar.com.jengibre.core.QueSabes;

public class QueSabesJava {

   public static void main(String[] args) {
      JavaPlatform.Config config = new JavaPlatform.Config();

      // config.fullscreen = true;

      JavaPlatform.register(config);
      PlayN.run(new QueSabes());
   }
}