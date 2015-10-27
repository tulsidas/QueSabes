package ar.com.jengibre.core;

import static playn.core.PlayN.graphics;
import playn.core.CanvasImage;
import playn.core.ImageLayer;
import playn.core.Layer;
import playn.core.util.Clock;
import ar.com.jengibre.core.etapas.EsperandoOtros;
import ar.com.jengibre.core.etapas.Etapa;
import ar.com.jengibre.core.etapas.Idle;
import ar.com.jengibre.core.etapas.JugandoOtros;
import ar.com.jengibre.core.etapas.Ruleta;

public class Sector {

   private Etapa etapa;

   private ImageLayer layer;

   public static final int WIDTH = 800;

   public static final int HEIGHT = 400;

   public Sector() {
      etapa = new Idle(this);
      layer = graphics().createImageLayer();
   }

   public Layer.HasSize layer() {
      return layer;
   }

   public void clicked(float x, float y) {
      etapa.clicked(x, y);
   }

   public void update(int delta) {
      etapa.update(delta);
   }

   public void paint(Clock clock) {
      etapa.paint(clock);

      CanvasImage cImg = graphics().createImage(WIDTH, HEIGHT);
      cImg.canvas().clear();
      etapa.draw(cImg.canvas());
      layer.setImage(cImg);
   }

   /**
    * Idle -> EsperandoOtros
    */
   public void empezarJuego() {
      StartupLatch.sectorListoParaEmpezar();
      etapa = new EsperandoOtros(this);
   }

   /**
    * EsperandoOtros -> Ruleta
    */
   public void empezoJuego() {
      etapa = new Ruleta(this);
   }

   /**
    * EsperandoOtros -> JugandoOtros
    */
   public void jugandoOtros() {
      etapa = new JugandoOtros(this);
   }
}