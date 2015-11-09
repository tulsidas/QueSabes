package ar.com.jengibre.core;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;
import static playn.core.PlayN.json;
import static playn.core.PlayN.keyboard;
import static playn.core.PlayN.pointer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import pythagoras.f.Point;

import com.dgis.input.evdev.EventDevice;
import com.dgis.input.evdev.InputEvent;
import com.dgis.input.evdev.InputListener;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

public class QueSabes extends Game.Default implements InputListener {

   private static final int UPDATE_RATE = 40;

   private Clock.Source clock = new Clock.Source(UPDATE_RATE);

   private EventDevice mt;

   // private Sector norte, sur, este, oeste;

   private Sector sur;

   public static List<Pregunta> preguntas;

   public static Image bgIdle, bgRuleta, bgPregunta, bgBonus;

   public static Image btnEmpezar, btnEsperando, pelota;

   public static Image p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12;

   public QueSabes() {
      super(UPDATE_RATE); // 24 FPS

      try {
         mt = new EventDevice("/dev/input/event18");
         mt.addListener(this);
      }
      catch (IOException e) {
         e.printStackTrace();
         System.exit(1);
      }
   }

   @Override
   public void init() {
      graphics().rootLayer().removeAll();

      bgIdle = assets().getImageSync("images/idle.png");
      bgRuleta = assets().getImageSync("images/ruleta.png");
      bgPregunta = assets().getImageSync("images/pregunta.png");
      bgBonus = assets().getImageSync("images/bonus.png");

      btnEmpezar = assets().getImageSync("images/btnEmpezar.png");
      btnEsperando = assets().getImageSync("images/btnEsperando.png");

      pelota = assets().getImageSync("images/pelota.png");

      try {
         preguntas = new ArrayList<>();
         Json.Array arrayPreguntas = json().parseArray(assets().getTextSync("preguntas/preguntas.json"));

         for (int i = 0; i < arrayPreguntas.length(); i++) {
            Json.Object obj = arrayPreguntas.getObject(i);

            String pregunta = obj.getString("pregunta");
            Json.Array arrayRespuestas = obj.getArray("respuestas");
            List<String> respuestas = new ArrayList<>();

            for (int j = 0; j < arrayRespuestas.length(); j++) {
               respuestas.add(arrayRespuestas.getString(j));
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

      // norte = new Sector();
      sur = new Sector();
      // este = new Sector();
      // oeste = new Sector();

      // float gw = graphics().width();
      // float gh = graphics().height();

      // GroupLayer lnorte = norte.layer();
      // lnorte.setRotation(FloatMath.PI);
      // graphics().rootLayer().addAt(lnorte, gw, gh / 2);

      // GroupLayer loeste = oeste.layer();
      // loeste.setRotation(FloatMath.PI / 2);
      // graphics().rootLayer().addAt(loeste, gw / 2, 0);

      GroupLayer lsur = sur.layer();
      // graphics().rootLayer().addAt(lsur, 0, gh / 2);
      graphics().rootLayer().addAt(lsur, 0, 0);

      // GroupLayer leste = este.layer();
      // leste.setRotation(FloatMath.PI * 1.5F);
      // graphics().rootLayer().addAt(leste, gw / 2, gh);

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
         public void onPointerStart(playn.core.Pointer.Event event) {
            float x = event.x();
            float y = event.y();
            Sector sector = getSector(x, y);

            Layer layer = sector.layer();
            Point tf = layer.transform().inverseTransform(new Point(x, y), new Point());
            sector.onPointerStart(tf.x, tf.y);
         }

         @Override
         public void onPointerEnd(Pointer.Event event) {
            float x = event.x();
            float y = event.y();
            Sector sector = getSector(x, y);

            Layer layer = sector.layer();
            Point tf = layer.transform().inverseTransform(new Point(x, y), new Point());
            sector.onPointerEnd(tf.x, tf.y);
         }
      });
   }

   @Override
   public void update(int delta) {
      clock.update(delta);

      // norte.update(delta);
      sur.update(delta);
      // este.update(delta);
      // oeste.update(delta);

      // controlador para el inicio del juego
      StartupLatch.update(delta);
   }

   @Override
   public void paint(float alpha) {
      clock.paint(alpha);

      // norte.paint(clock);
      sur.paint(clock);
      // este.paint(clock);
      // oeste.paint(clock);
   }

   private Sector getSector(float x, float y) {
      // if (x >= y) {
      // if (Math.abs(x - graphics().width() / 2) >= Math.abs(y -
      // graphics().height() / 2)) {
      // return este;
      // }
      // else {
      // return norte;
      // }
      // }
      // else {
      // if (Math.abs(x - graphics().width() / 2) >= Math.abs(y -
      // graphics().height() / 2)) {
      // return oeste;
      // }
      // else {
      return sur;
      // }
      // }
   }

   // ////////////////////////////////////////////////////////////////
   // evdev InputListener
   // ////////////////////////////////////////////////////////////////
   private static final short SYN_REPORT = 0;

   private static final short SYN_CONFIG = 1;

   private static final short BTN_TOUCH = 0x14a;

   private static final short ABS_MT_SLOT = 0x2f;

   private static final short ABS_MT_TOUCH_MAJOR = 0x30;

   private static final short ABS_MT_TOUCH_MINOR = 0x31;

   private static final short ABS_MT_ORIENTATION = 0x34;

   private static final short ABS_MT_POSITION_X = 0x35;

   private static final short ABS_MT_POSITION_Y = 0x36;

   private static final short ABS_MT_TRACKING_ID = 0x39;

   ImmutableMap<Short, String> EVENTS = ImmutableMap
         .<Short, String> builder()
         .put(SYN_REPORT, "SYN_REPORT")
         // .put(SYN_CONFIG, "SYN_CONFIG")
         // .put(BTN_TOUCH, "BTN_TOUCH")
         .put(ABS_MT_SLOT, "ABS_MT_SLOT")
         // .put(ABS_MT_TOUCH_MAJOR, "ABS_MT_TOUCH_MAJOR")
         // .put(ABS_MT_TOUCH_MINOR, "ABS_MT_TOUCH_MINOR")
         // .put(ABS_MT_ORIENTATION, "ABS_MT_ORIENTATION")
         .put(ABS_MT_POSITION_X, "ABS_MT_POSITION_X").put(ABS_MT_POSITION_Y, "ABS_MT_POSITION_Y")
         .put(ABS_MT_TRACKING_ID, "ABS_MT_TRACKING_ID").build();

   // <slot, tracking id>
   private Map<Integer, Point> slots = Maps.newHashMap();

   private int currentSlot = 0;

   public void event(InputEvent e) {
      String code = EVENTS.get(e.code);
      if (e.type == 3 && code != null) {
//         System.out.println(e.type + "," + code + "," + e.value);

         if (e.code == ABS_MT_SLOT) {
            currentSlot = e.value;
         }
         else if (e.code == ABS_MT_TRACKING_ID) {
            if (e.value == -1) {
               // XXX onTouchEnd
               System.out.println("onTouchEnd " + currentSlot);

               slots.remove(currentSlot);
            }
            else {
               // XXX onTouchStart
               System.out.println("onTouchStart " + currentSlot);
            }
         }
         else if (e.code == ABS_MT_POSITION_X) {
            getCurrentPoint(currentSlot).x = e.value;
         }
         else if (e.code == ABS_MT_POSITION_Y) {
            getCurrentPoint(currentSlot).y = e.value;
         }
         else if (e.code == SYN_REPORT) {
            // aviso que se movio cada uno de los slots
            // XXX onTouchMove
            for (Map.Entry<Integer, Point> entry : slots.entrySet()) {
               Point p = entry.getValue();
               System.out.println("onTouchMove " + entry.getKey() + "(" + p.x + "," + p.y + ")");
            }
         }
      }
   }

   private Point getCurrentPoint(int slot) {
      Point p = slots.getOrDefault(currentSlot, new Point());
      slots.put(currentSlot, p);

      return p;
   }
}
