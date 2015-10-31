package ar.com.jengibre.core;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;
import static playn.core.PlayN.json;
import static playn.core.PlayN.keyboard;
import static playn.core.PlayN.pointer;

import java.util.ArrayList;
import java.util.List;

import playn.core.Game;
import playn.core.GroupLayer;
import playn.core.Image;
import playn.core.Json;
import playn.core.Key;
import playn.core.Keyboard;
import playn.core.Keyboard.Event;
import playn.core.Layer;
import playn.core.Pointer;
import playn.core.util.Clock;
import pythagoras.f.FloatMath;
import pythagoras.f.Point;

public class QueSabes extends Game.Default {

   private static final int UPDATE_RATE = 40;

   private Clock.Source clock = new Clock.Source(UPDATE_RATE);

   private Sector norte, sur, este, oeste;

   public static List<Pregunta> preguntas;

   public static Image bgIdle, bgRuleta, bgPregunta, btnEmpezar, btnEsperando;

   public static Image p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12;

   public QueSabes() {
      super(UPDATE_RATE); // 24 FPS
   }

   @Override
   public void init() {
      graphics().rootLayer().removeAll();

      bgIdle = assets().getImageSync("images/idle.png");
      bgRuleta = assets().getImageSync("images/ruleta.png");
      bgPregunta = assets().getImageSync("images/pregunta.png");
      btnEmpezar = assets().getImageSync("images/btnEmpezar.png");
      btnEsperando = assets().getImageSync("images/btnEsperando.png");

      try {
         preguntas = new ArrayList<>();
         Json.Array arrayPreguntas = json().parseArray(assets().getTextSync("preguntas/preguntas.json"));

         for (int i = 0; i < arrayPreguntas.length(); i++) {
            Json.Object obj = arrayPreguntas.getObject(i);

            String pregunta = obj.getString("pregunta");
            Json.Array arrayRespuestas = obj.getArray("respuestas");
            List<String> respuestas = new ArrayList<>();

            for (int j = 0; j < arrayRespuestas.length(); j++) {
               respuestas.add(arrayRespuestas.getString(i));
            }

            preguntas.add(new Pregunta(pregunta, respuestas));
         }
      }
      catch (Exception e) {
         System.err.println(e);
         System.exit(1);
      }

      p1 = assets().getImageSync("images/personaje1.png");
      p2 = assets().getImageSync("images/personaje2.png");
      p3 = assets().getImageSync("images/personaje3.png");
      p4 = assets().getImageSync("images/personaje4.png");
      p5 = assets().getImageSync("images/personaje5.png");
      p6 = assets().getImageSync("images/personaje6.png");
      p7 = assets().getImageSync("images/personaje7.png");
      p8 = assets().getImageSync("images/personaje8.png");
      p9 = assets().getImageSync("images/personaje9.png");
      p10 = assets().getImageSync("images/personaje10.png");
      p11 = assets().getImageSync("images/personaje11.png");
      p12 = assets().getImageSync("images/personaje12.png");

      norte = new Sector();
      sur = new Sector();
      este = new Sector();
      oeste = new Sector();

      float gw = graphics().width();
      float gh = graphics().height();

      GroupLayer lnorte = norte.layer();
      lnorte.setRotation(FloatMath.PI);
      graphics().rootLayer().addAt(lnorte, gw, gh / 2);

      GroupLayer loeste = oeste.layer();
      loeste.setRotation(FloatMath.PI / 2);
      graphics().rootLayer().addAt(loeste, gw / 2, 0);

      GroupLayer lsur = sur.layer();
      graphics().rootLayer().addAt(lsur, 0, gh / 2);

      GroupLayer leste = este.layer();
      leste.setRotation(FloatMath.PI * 1.5F);
      graphics().rootLayer().addAt(leste, gw / 2, gh);

      // RELOAD HOOK
      keyboard().setListener(new Keyboard.Adapter() {
         @Override
         public void onKeyUp(Event event) {
            if (event.key() == Key.R) {
               System.out.println("reload!");
               init();
            }
         }
      });
      // RELOAD HOOK

      pointer().setListener(new Pointer.Adapter() {
         @Override
         public void onPointerEnd(Pointer.Event event) {
            float x = event.x();
            float y = event.y();
            Sector sector;

            if (x >= y) {
               if (Math.abs(x - graphics().width() / 2) >= Math.abs(y - graphics().height() / 2)) {
                  sector = este;
               }
               else {
                  sector = norte;
               }
            }
            else {
               if (Math.abs(x - graphics().width() / 2) >= Math.abs(y - graphics().height() / 2)) {
                  sector = oeste;
               }
               else {
                  sector = sur;
               }
            }

            Layer layer = sector.layer();
            Point tf = layer.transform().inverseTransform(new Point(x, y), new Point());
            sector.clicked(tf.x, tf.y);
         }
      });
   }

   @Override
   public void update(int delta) {
      clock.update(delta);

      norte.update(delta);
      sur.update(delta);
      este.update(delta);
      oeste.update(delta);

      // controlador para el inicio del juego
      StartupLatch.update(delta);
   }

   @Override
   public void paint(float alpha) {
      clock.paint(alpha);

      norte.paint(clock);
      sur.paint(clock);
      este.paint(clock);
      oeste.paint(clock);
   }
}
