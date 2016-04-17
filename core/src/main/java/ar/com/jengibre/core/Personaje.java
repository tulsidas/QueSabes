package ar.com.jengibre.core;

import static playn.core.PlayN.assets;

import java.util.List;

import playn.core.Image;
import playn.core.Sound;
import tripleplay.anim.Flipbook;

import com.google.common.collect.ImmutableList;

public class Personaje {

   private String path;

   private Image ruleta, ruletaBN;

   private Flipbook fbGana, fbPierde, fbSaluda;

   private Sound soundSaluda, soundPierde, soundGana;

   private ImmutableList<Pregunta> preguntas;

   public Personaje(String path, Image ruleta, Image ruletaBN, Flipbook fbGana, Flipbook fbPierde,
         Flipbook fbSaluda, Sound soundGana, Sound soundPierde, Sound soundSaluda, List<Pregunta> preguntas) {
      this.path = path;
      this.ruleta = ruleta;
      this.ruletaBN = ruletaBN;
      this.fbGana = fbGana;
      this.fbPierde = fbPierde;
      this.fbSaluda = fbSaluda;
      //this.soundGana = new Sound.Silence();
      //this.soundPierde = new Sound.Silence();
	this.soundGana = soundGana;
	this.soundPierde = soundPierde;
      this.soundSaluda = soundSaluda;
      this.preguntas = ImmutableList.copyOf(preguntas);
   }

   public Image imgRuleta() {
      return ruleta;
   }

   public Image imgRuletaBN() {
      return ruletaBN;
   }

   public Flipbook fbGana() {
      return fbGana;
   }

   public Flipbook fbPierde() {
      return fbPierde;
   }

   public Flipbook fbSaluda() {
      return fbSaluda;
   }

   public Sound soundGana() {
      return soundGana;
   }

   public Sound soundPierde() {
      return soundPierde;
   }

   public Sound soundSaluda() {
      return soundSaluda;
   }

   public ImmutableList<Pregunta> preguntas() {
      return preguntas;
   }

   public synchronized void cargarSonidos() {
/*
      // si no están cargados (tal vez por otro jugador)
      if (soundGana instanceof Sound.Silence) {
         soundGana = assets().getSound("ruleta/" + path + "/GANA");
      }

      if (soundPierde instanceof Sound.Silence) {
         soundPierde = assets().getSound("ruleta/" + path + "/PIERDE");
      }
*/
   }

   public synchronized void liberarSonidos() {
/*
      soundGana.release();
      soundGana = new Sound.Silence();

      soundPierde.release();
      soundPierde = new Sound.Silence();
*/
   }
}
