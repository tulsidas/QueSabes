package ar.com.jengibre.core;

import static playn.core.PlayN.graphics;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Layer;
import playn.core.util.Clock;
import ar.com.jengibre.core.etapas.Etapa;
import ar.com.jengibre.core.etapas.Idle;

public class Sector {

   private Etapa etapa;

   private ImageLayer layer;

   public Sector() {
      etapa = new Idle(this);
      layer = graphics().createImageLayer(draw());
   }

   public Layer.HasSize layer() {
      return layer;
   }

   public void clicked(float x, float y) {
      etapa.clicked(x, y);
   }

   public Image draw() {
      return etapa.draw();
   }

   public void repaint() {
      layer.setImage(draw());
   }

   public void update(int delta) {
      etapa.update(delta);
   }

   public void paint(Clock clock) {
      etapa.paint(clock);
   }
}