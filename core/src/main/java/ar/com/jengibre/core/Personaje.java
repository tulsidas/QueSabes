package ar.com.jengibre.core;

import playn.core.Image;
import tripleplay.anim.Flipbook;

public class Personaje {

   private Image ruleta;

   private Flipbook saluda, pierde, gana;

   public Personaje(Image ruleta, Flipbook gana, Flipbook pierde, Flipbook saluda) {
      this.ruleta = ruleta;
      this.gana = gana;
      this.pierde = pierde;
      this.saluda = saluda;
   }

   public Image imgRuleta() {
      return ruleta;
   }

   public Flipbook gana() {
      return pierde;
   }

   public Flipbook pierde() {
      return gana;
   }

   public Flipbook saluda() {
      return saluda;
   }
}