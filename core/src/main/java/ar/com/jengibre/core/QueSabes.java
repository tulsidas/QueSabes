package ar.com.jengibre.core;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;
import static playn.core.PlayN.json;
import static playn.core.PlayN.keyboard;
import static playn.core.PlayN.pointer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import playn.core.Game;
import playn.core.Image;
import playn.core.Json;
import playn.core.Key;
import playn.core.Keyboard;
import playn.core.Keyboard.Event;
import playn.core.Layer;
import playn.core.Pointer;
import playn.core.Sound;
import playn.core.json.JsonParserException;
import playn.core.util.Callback;
import playn.core.util.Clock;
import pythagoras.f.Point;
import tripleplay.anim.Flipbook;
import tripleplay.util.PackedFrames;

import com.dgis.input.evdev.EventDevice;
import com.dgis.input.evdev.InputEvent;
import com.dgis.input.evdev.InputListener;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class QueSabes extends Game.Default implements InputListener {

   private static final int FPS = 25;

   private static final int UPDATE_RATE = 1000 / FPS;

   private Clock.Source clock = new Clock.Source(UPDATE_RATE);

   private EventDevice mt;

   private Sector s1, s2, s3, s4;

   public static ImmutableList<Personaje> personajes;

   public static Flipbook papelitos, arquerito, pelota, bgEsperandoFin;

   public static Image bgIdle, bgRuleta, bgPregunta, bgArquerito, bgMedallero;

   public static Image pelota0, bonus, gol, medalla, gracias;

   public static Image pulgarAbajo;

   public static Image botonUp, botonDn;

   public static Image zocalo, zocaloAlto;

   public static ImmutableList<Image> numMedallas;

   public static ImmutableList<Image> reloj;

   public static ImmutableList<Image> bgEsperando;

   public static Sound nogol, gol1, gol2, palo, ruleta, tuc;

   public QueSabes() {
      super(UPDATE_RATE); // 25 FPS

      // try {
      // mt = new EventDevice("/dev/input/event18");
      // mt.addListener(this);
      // }
      // catch (IOException e) {
      // e.printStackTrace();
      // System.exit(1);
      // }
   }

   @Override
   public void init() {
      graphics().rootLayer().removeAll();

      bgIdle = assets().getImageSync("images/bgIdle.png");
      bgRuleta = assets().getImageSync("images/bgRuleta.png");
      bgPregunta = assets().getImageSync("images/bgPregunta.png");
      bgArquerito = assets().getImageSync("images/bgArquerito.png");
      bgMedallero = assets().getImageSync("images/bgMedallero.png");

      pelota0 = assets().getImageSync("images/pelota0.png");
      bonus = assets().getImageSync("images/bonus.png");
      gol = assets().getImageSync("images/gol.png");
      medalla = assets().getImageSync("images/medalla.png");
      gracias = assets().getImageSync("images/gracias.png");

      pulgarAbajo = assets().getImageSync("images/pulgar_abajo.png");

      botonUp = assets().getImageSync("images/boton_up.png");
      botonDn = assets().getImageSync("images/boton_dn.png");

      zocalo = assets().getImageSync("images/zocalo.png");
      zocaloAlto = assets().getImageSync("images/zocaloalto.png");

      List<Image> _num = Lists.newArrayList();
      for (int i = 1; i <= 24; i++) {
         _num.add(assets().getImage("images/x" + i + ".png"));
      }
      numMedallas = ImmutableList.copyOf(_num);

      reloj = ImmutableList.of(assets().getImageSync("images/reloj0.png"),
            assets().getImageSync("images/reloj1.png"), assets().getImageSync("images/reloj2.png"), assets()
                  .getImageSync("images/reloj3.png"), assets().getImageSync("images/reloj4.png"), assets()
                  .getImageSync("images/reloj5.png"), assets().getImageSync("images/reloj6.png"), assets()
                  .getImageSync("images/reloj7.png"), assets().getImageSync("images/reloj8.png"), assets()
                  .getImageSync("images/reloj9.png"), assets().getImageSync("images/reloj10.png"));

      bgEsperando = ImmutableList.of(assets().getImageSync("images/esperando1.png"),
            assets().getImageSync("images/esperando2.png"), assets().getImageSync("images/esperando3.png"),
            assets().getImageSync("images/esperando4.png"));

      final Callback<Sound> sndCallback = new Callback<Sound>() {
         @Override
         public void onSuccess(Sound result) {
            result.prepare();
         }

         @Override
         public void onFailure(Throwable cause) {
            cause.printStackTrace();
            System.exit(1);
         }
      };

      try {
         papelitos = cargarFlipbook("ruleta/papelitos", FPS);
         arquerito = cargarFlipbook("images/arquerito", FPS * 3);
         pelota = cargarFlipbook("images/pelota", FPS * 3);
         bgEsperandoFin = cargarFlipbook("images/esperandoFin", 1000);

         nogol = assets().getSound("sfx/nogol");
         nogol.addCallback(sndCallback);
         gol1 = assets().getSound("sfx/gol1");
         gol1.addCallback(sndCallback);
         gol2 = assets().getSound("sfx/gol2");
         gol2.addCallback(sndCallback);
         palo = assets().getSound("sfx/palo");
         palo.addCallback(sndCallback);
         ruleta = assets().getSound("sfx/ruleta");
         ruleta.addCallback(sndCallback);
         tuc = assets().getSound("sfx/tuc");
         tuc.addCallback(sndCallback);

         // PERSONAJES
         List<Personaje> _personajes = Lists.newArrayList();
         for (String path : Lists.newArrayList("ALFONSIN", "DEMIDI", "GINOBILI", "MARADONA", "MENDEZ",
               "PERON", "PUMA", "VILAS", "FANGIO", "SELFIE")) {

            final Image imgRuleta = assets().getImageSync("ruleta/" + path + "/0.png");
            final Image imgRuletaBN = assets().getImageSync("ruleta/" + path + "/1.png");

            Sound gana = assets().getSound("ruleta/" + path + "/GANA");
            gana.addCallback(sndCallback);
            Sound pierde = assets().getSound("ruleta/" + path + "/PIERDE");
            pierde.addCallback(sndCallback);
            Sound saluda = assets().getSound("ruleta/" + path + "/SALUDA");
            saluda.addCallback(sndCallback);

            _personajes.add(new Personaje(imgRuleta, imgRuletaBN,//
                  null, // FIXME cargarFlipbook("ruleta/" + path + "/GANA",
                        // FPS),//
                  null, // FIXME cargarFlipbook("ruleta/" + path + "/PIERDE",
                        // FPS),//
                  null, // FIXME cargarFlipbook("ruleta/" + path + "/SALUDA",
                        // FPS),//
                  gana, pierde, saluda, cargarPreguntas(path)));

            System.out.println("cargado: " + path);
         }

         personajes = ImmutableList.copyOf(_personajes);
      }
      catch (Exception e) {
         e.printStackTrace();
         System.exit(1);
      }
      // FIN PERSONAJES

      // RELOAD HOOK
      keyboard().setListener(new Keyboard.Adapter() {
         @Override
         public void onKeyUp(Event event) {
            if (event.key() == Key.R) {
               reload();
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

            Point tf = Layer.Util.screenToLayer(sector.layer(), x, y);
            sector.onPointerStart(tf.x, tf.y);
         }

         @Override
         public void onPointerEnd(Pointer.Event event) {
            float x = event.x();
            float y = event.y();
            Sector sector = getSector(x, y);

            Point tf = Layer.Util.screenToLayer(sector.layer(), x, y);
            sector.onPointerEnd(tf.x, tf.y);
         }
      });

      reload();
      System.out.println("listo");
   }

   private void reload() {
      // System.out.println("reload!");

      StartupLatch.reset();

      graphics().rootLayer().removeAll();

      // arriba a la izq
      s1 = new Sector("S1", false /* FIXME true*/);
      graphics().rootLayer().addAt(s1.topLayer(), 0, 0);

      // arriba a la der
      s2 = new Sector("S2", true);
      graphics().rootLayer().addAt(s2.topLayer(), Sector.WIDTH, 0);

      // abajo a la izq
      s3 = new Sector("S3", false);
      graphics().rootLayer().addAt(s3.topLayer(), 0, Sector.HEIGHT);

      // abajo a la der
      s4 = new Sector("S4", false);
      graphics().rootLayer().addAt(s4.topLayer(), Sector.WIDTH, Sector.HEIGHT);
   }

   @Override
   public void update(int delta) {
      // delta = 40; // TODO ???

      clock.update(delta);

      s1.update(delta);
      // FIXME s2.update(delta);
      // FIXME s3.update(delta);
      // FIXME s4.update(delta);

      // controlador para el inicio del juego
      StartupLatch.update(delta);
   }

   @Override
   public void paint(float alpha) {
      clock.paint(alpha);

      s1.paint(clock);
      // FIXME s2.paint(clock);
      // FIXME s3.paint(clock);
      // FIXME s4.paint(clock);
   }

   private Flipbook cargarFlipbook(String path, int fps) throws JsonParserException, Exception {
      // obtengo imagen
      Image img = assets().getImageSync(path + ".png");

      // creo la textura
      img.ensureTexture();

      return new Flipbook(new PackedFrames(img, json().parse(assets().getTextSync(path + ".json"))), fps);
   }

   private List<Pregunta> cargarPreguntas(String path) {
      List<Pregunta> preguntas = new ArrayList<>();
      try {
         Json.Array arrayPreguntas = json().parseArray(
               assets().getTextSync("ruleta/" + path + "/preguntas.json"));

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

      return preguntas;
   }

   private Sector getSector(float x, float y) {
      if (y < graphics().height() / 2) {
         if (x < graphics().width() / 2) {
            return s1;
         }
         else {
            return s2;
         }
      }
      else {
         if (x < graphics().width() / 2) {
            return s3;
         }
         else {
            return s4;
         }
      }
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
         // System.out.println(e.type + "," + code + "," + e.value);

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
