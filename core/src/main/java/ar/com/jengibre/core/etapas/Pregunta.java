package ar.com.jengibre.core.etapas;

import playn.core.Image;
import playn.core.util.Clock;
import ar.com.jengibre.core.Sector;

public class Pregunta extends Etapa {
   public Pregunta(Sector sector) {
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
   public void doPaint(Clock clock) {
   }

   @Override
   public void clicked(float x, float y) {
   }
}