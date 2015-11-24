package ar.com.jengibre.core;

import static playn.core.PlayN.graphics;
import playn.core.GroupLayer;
import playn.core.util.Clock;
import ar.com.jengibre.core.etapas.AbstractEtapa;
import ar.com.jengibre.core.etapas.EtapaBonus;
import ar.com.jengibre.core.etapas.EtapaEsperandoOtros;
import ar.com.jengibre.core.etapas.EtapaJugandoOtros;
import ar.com.jengibre.core.etapas.EtapaPregunta;
import ar.com.jengibre.core.etapas.EtapaRuleta;

public class Sector {

   private AbstractEtapa etapa;

   private GroupLayer layer;

   public static final int WIDTH = 960;

   public static final int HEIGHT = 540;

   public Sector() {
      reload();
   }

   public void reload() {
      if (layer == null) {
         layer = graphics().createGroupLayer(WIDTH, HEIGHT);
      }
      else {
         layer.removeAll();
      }

      // etapa = new EtapaIdle(this);
      // etapa = new EtapaRuleta(this);
      // etapa = new EtapaPregunta(this, Randoms.with(new
      // Random()).getInRange(1, 9));
      etapa = new EtapaBonus(this);
      // etapa = new EtapaTest(this);
   }

   public GroupLayer layer() {
      return layer;
   }

   public void onPointerStart(float x, float y) {
      etapa.onPointerStart(x, y);
   }

   public void onPointerEnd(float x, float y) {
      etapa.onPointerEnd(x, y);
   }

   public void update(int delta) {
      etapa.update(delta);
   }

   public void paint(Clock clock) {
      etapa.paint(clock);
   }

   /**
    * Idle -> EsperandoOtros
    */
   public void empezarJuego() {
      StartupLatch.sectorListoParaEmpezar();
      etapa = new EtapaEsperandoOtros(this);
   }

   /**
    * EsperandoOtros -> Ruleta
    */
   public void ruleta() {
      etapa = new EtapaRuleta(this);
   }

   /**
    * EsperandoOtros -> JugandoOtros
    */
   public void jugandoOtros() {
      etapa = new EtapaJugandoOtros(this);
   }

   /**
    * Ruleta -> Pregunta
    */
   public void pregunta(Personaje personaje) {
      etapa = new EtapaPregunta(this, personaje);
   }
}