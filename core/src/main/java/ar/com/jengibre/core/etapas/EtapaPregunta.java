package ar.com.jengibre.core.etapas;

import static playn.core.PlayN.graphics;

import java.util.List;

import playn.core.CanvasImage;
import playn.core.Font;
import playn.core.GroupLayer;
import playn.core.ImageLayer;
import playn.core.Layer;
import playn.core.TextFormat;
import playn.core.TextWrap;
import playn.core.util.Clock;
import playn.core.util.TextBlock;
import playn.core.util.TextBlock.Align;
import pythagoras.f.Point;
import tripleplay.anim.Flipbook;
import tripleplay.util.Colors;
import ar.com.jengibre.core.Personaje;
import ar.com.jengibre.core.Pregunta;
import ar.com.jengibre.core.QueSabes;
import ar.com.jengibre.core.Sector;

import com.google.common.collect.Lists;

public class EtapaPregunta extends AbstractEtapa {
   private ImageLayer preguntaLayer, respuesta1, respuesta2, respuesta3;

   private GroupLayer flipbookGroup, papelitosGroup; // flipbooks

   private Personaje personaje;

   public EtapaPregunta(Sector sector, Personaje personaje) {
      super(sector);

      this.personaje = personaje;

      flipbookGroup = graphics().createGroupLayer();
      papelitosGroup = graphics().createGroupLayer();

      layer.add(graphics().createImageLayer(QueSabes.bgRuleta));

      // FIXME Pregunta pregunta = QueSabes.preguntas.get(personaje);
      Pregunta pregunta = rnd.pick(QueSabes.preguntas, null);

      CanvasImage cImg = graphics().createImage(Sector.WIDTH, Sector.HEIGHT);
      cImg.canvas().setStrokeColor(Colors.YELLOW);
      cImg.canvas().setStrokeWidth(2);
      cImg.canvas().strokeRect(100, 80, 750, 375);

      Font font = graphics().createFont("Benton", Font.Style.PLAIN, 24);
      TextFormat format = new TextFormat().withAntialias(true).withFont(font);

      TextBlock texto = new TextBlock(graphics()
            .layoutText(pregunta.getPregunta(), format, new TextWrap(750)));
      cImg.canvas().setFillColor(Colors.WHITE);
      texto.fill(cImg.canvas(), Align.CENTER, (Sector.WIDTH - texto.textWidth()) / 2, 100);

      preguntaLayer = graphics().createImageLayer(cImg);
      layer.add(preguntaLayer);

      List<Point> posRespuestas = Lists.newArrayList(new Point(150, 350), new Point(300, 380), new Point(450,
            410));
      // FIXME rnd.shuffle(posRespuestas);

      TextWrap wrap = new TextWrap(400);
      respuesta1 = graphics().createImageLayer(
            new TextBlock(graphics().layoutText("· " + pregunta.getRespuestas().get(0), format, wrap))
                  .toImage(Align.CENTER, Colors.WHITE));
      respuesta1.setInteractive(true);
      Point p = posRespuestas.remove(0);
      layer.addAt(respuesta1, p.x, p.y);

      respuesta2 = graphics().createImageLayer(
            new TextBlock(graphics().layoutText("· " + pregunta.getRespuestas().get(1), format, wrap))
                  .toImage(Align.CENTER, Colors.WHITE));
      respuesta2.setInteractive(true);
      p = posRespuestas.remove(0);
      layer.addAt(respuesta2, p.x, p.y);

      respuesta3 = graphics().createImageLayer(
            new TextBlock(graphics().layoutText("· " + pregunta.getRespuestas().get(2), format, wrap))
                  .toImage(Align.CENTER, Colors.WHITE));
      respuesta3.setInteractive(true);
      p = posRespuestas.remove(0);
      layer.addAt(respuesta3, p.x, p.y);

      // timeout que avanza el juego por si abandonan o tardan mucho
      // anim.delay(TIMEOUT).then().action(() -> timeout = true);
   }

   @Override
   public void doPaint(Clock clock) {}

   @Override
   public void update(int delta) {}

   @Override
   public void onPointerEnd(float x, float y) {
      Layer hit = layer.hitTest(new Point(x, y));

      if (hit == respuesta1) {
         showAnim(personaje.gana(), true);
      }
      else if (hit == respuesta2 || hit == respuesta3) {
         showAnim(personaje.pierde(), false);
      }
   }

   private void showAnim(Flipbook flipbook, boolean papelitos) {
      preguntaLayer.destroy();
      respuesta1.destroy();
      respuesta2.destroy();
      respuesta3.destroy();

      layer.add(flipbookGroup);
      anim.flipbook(flipbookGroup, flipbook);

      if (papelitos) {
         anim.play(QueSabes.gana);
         // layer.add(papelitosGroup);
         // anim.flipbook(papelitosGroup, QueSabes.papelitos);
      }
      else {
         anim.play(QueSabes.pierde);
      }

      anim.addBarrier();

      anim.action(() -> {
         sector.arquerito();
      });
   }
}