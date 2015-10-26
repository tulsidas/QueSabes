package ar.com.jengibre.core.etapas;

import playn.core.Image;
import ar.com.jengibre.core.Sector;

public class Bonus extends Etapa {

   public Bonus(Sector sector) {
      super(sector);
   }

   @Override
   public Image draw() {
      return null;
   }

   @Override
   public void update(int delta) {
   }

   @Override
   public void clicked(float x, float y) {
   }
}