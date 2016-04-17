package ar.com.jengibre.java;

import playn.core.PlayN;
import playn.java.JavaPlatform;

import ar.com.jengibre.core.QueSabes;

public class QueSabesJava {

   public static void main(String[] args) {
      JavaPlatform.Config config = new JavaPlatform.Config();

      config.fullscreen = true;
      config.width = 960 * 2;
      config.height = 540 * 2;

      JavaPlatform platform = JavaPlatform.register(config);
      platform.graphics().registerFont("Benton", "fonts/BentonSans-Book.otf");

      PlayN.run(new QueSabes());
   }
}