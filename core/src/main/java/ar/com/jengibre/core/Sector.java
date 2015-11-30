package ar.com.jengibre.core;

import static playn.core.PlayN.graphics;
import playn.core.GroupLayer;
import playn.core.util.Clock;
import ar.com.jengibre.core.etapas.AbstractEtapa;
import ar.com.jengibre.core.etapas.EtapaBonus;
import ar.com.jengibre.core.etapas.EtapaEsperandoOtros;
import ar.com.jengibre.core.etapas.EtapaIdle;
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
      setEtapa(new EtapaEsperandoOtros(this));
   }

   /**
    * EsperandoOtros -> Ruleta
    */
   public void ruleta() {
      setEtapa(new EtapaRuleta(this));
   }

   /**
    * EsperandoOtros -> JugandoOtros
    */
   public void jugandoOtros() {
      setEtapa(new EtapaJugandoOtros(this));
   }

   /**
    * Ruleta -> Pregunta
    */
   public void pregunta(Personaje personaje) {
      setEtapa(new EtapaPregunta(this, personaje));
   }

   /**
    * Pregunta -> Arquerito
    */
   public void arquerito() {
      setEtapa(new EtapaBonus(this));
   }

   private void setEtapa(AbstractEtapa nueva) {
      etapa = nueva;
   }
}