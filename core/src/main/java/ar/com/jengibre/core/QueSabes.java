package ar.com.jengibre.core;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;
import static playn.core.PlayN.keyboard;
import playn.core.Game;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Key;
import playn.core.Keyboard;
import playn.core.Keyboard.Event;
import playn.core.Keyboard.TypedEvent;
import playn.core.util.Clock;
import tripleplay.anim.Animator;
import tripleplay.ui.Background;
import tripleplay.ui.Button;
import tripleplay.ui.Interface;
import tripleplay.ui.Label;
import tripleplay.ui.Root;
import tripleplay.ui.SimpleStyles;
import tripleplay.ui.Style;
import tripleplay.ui.Stylesheet;
import tripleplay.ui.layout.AbsoluteLayout;
import tripleplay.util.Colors;

public class QueSabes extends Game.Default {

   private static final int UPDATE_RATE = 33;

   private Clock.Source clock = new Clock.Source(UPDATE_RATE);

   private Animator anim;

   private Interface iface;

   private Root root;

   public QueSabes() {
      super(UPDATE_RATE); // call update every 33ms (30 times per second)
   }

   @Override
   public void init() {
      graphics().rootLayer().removeAll();
      // create and add background image layer
      Image bgImage = assets().getImage("images/bg.png");
      ImageLayer bgLayer = graphics().createImageLayer(bgImage);
      graphics().rootLayer().add(bgLayer);

      // //
      // RELOAD HOOK
      keyboard().setListener(new Keyboard.Listener() {
         @Override
         public void onKeyUp(Event event) {
            if (event.key() == Key.R) {
               System.out.println("reload!");
               init();
            }
         }

         @Override
         public void onKeyTyped(TypedEvent event) {}

         @Override
         public void onKeyDown(Event event) {}
      });
      // RELOAD HOOK

      anim = new Animator();
      iface = new Interface();

      Stylesheet rootSheet = SimpleStyles.newSheetBuilder()
            .add(Label.class, Style.COLOR.is(Colors.RED)/*, Style.FONT.is(font)*/).add(Button.class, /*Style.FONT.is(font),*/
            Style.BACKGROUND.is(Background.roundRect(Colors.GREEN, 2).inset(3)))
            // .add(Button.class, Button.DEBOUNCE_DELAY.is(50))
            // .add(Button.class, Style.Mode.SELECTED, Style.FONT.is(font),
            // Style.BACKGROUND.is(Background.roundRect(Colors.darker(VERDE), 2).inset(3)))
            .create();

      root = iface.createRoot(new AbsoluteLayout(), rootSheet, graphics().rootLayer()).setSize(
            graphics().width(), graphics().height());

      root.add(AbsoluteLayout.centerAt(new Label("Hola"), 130, graphics().height() - 100));
      root.add(AbsoluteLayout.at(new Label("Pepe"), graphics().width() - 70, 10));
      root.add(AbsoluteLayout.at(new Button("coco"), 90, graphics().height() - 50, 80, 35));
   }

   @Override
   public void update(int delta) {
      clock.update(delta);
   }

   @Override
   public void paint(float alpha) {
      clock.paint(alpha);
      iface.paint(clock);
      anim.paint(clock);
   }
}
