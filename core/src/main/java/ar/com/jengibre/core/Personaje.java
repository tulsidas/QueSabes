package ar.com.jengibre.core;

import java.util.List;

import playn.core.Image;
import playn.core.Sound;
import tripleplay.anim.Flipbook;

import com.google.common.collect.ImmutableList;

public class Personaje {

   private Image ruleta, ruletaBN;

   private Flipbook fbGana, fbPierde, fbSaluda;

   private Sound soundGana, soundPierde, soundSaluda;

   private ImmutableList<Pregunta> preguntas;

   public Personaje(Image ruleta, Image ruletaBN, Flipbook fbGana, Flipbook fbPierde, Flipbook fbSaluda,
         Sound soundGana, Sound soundPierde, Sound soundSaluda, List<Pregunta> preguntas) {
      this.ruleta = ruleta;
      this.ruletaBN = ruletaBN;
      this.fbGana = fbGana;
      this.fbPierde = fbPierde;
      this.fbSaluda = fbSaluda;
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
}