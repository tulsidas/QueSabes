package ar.com.jengibre.core.etapas;

import static ar.com.jengibre.core.QueSabes.bgPregunta;
import static playn.core.PlayN.graphics;

import java.util.List;

import playn.core.CanvasImage;
import playn.core.ImageLayer;
import playn.core.Layer;
import playn.core.TextFormat;
import playn.core.TextLayout;
import playn.core.TextWrap;
import playn.core.util.Clock;
import playn.core.util.TextBlock;
import playn.core.util.TextBlock.Align;
import pythagoras.f.Point;
import tripleplay.util.Colors;
import ar.com.jengibre.core.Pregunta;
import ar.com.jengibre.core.QueSabes;
import ar.com.jengibre.core.Sector;

import com.google.common.collect.Lists;

public class EtapaPregunta extends AbstractEtapa {
   private ImageLayer respuesta1, respuesta2, respuesta3;

   public EtapaPregunta(Sector sector, int personaje) {
      super(sector);

      List<Float> ANCHOS = Lists.newArrayList(100F, 150F, 300F);

      Pregunta pregunta = QueSabes.preguntas.get(personaje);

      CanvasImage cImg = graphics().createImage(Sector.WIDTH, Sector.HEIGHT);
      cImg.canvas().drawImage(bgPregunta, 0, 0);

      TextFormat format = new TextFormat().withAntialias(false);

      TextBlock texto = new TextBlock(triangularTexto(pregunta.getPregunta(), format, ANCHOS).toArray(
            new TextLayout[] {}));
      texto.fill(cImg.canvas(), Align.CENTER, 390, 100);

      layer.add(graphics().createImageLayer(cImg));

      List<Float> yRespuestas = Lists.newArrayList(380F, 400F, 420F);
      rnd.shuffle(yRespuestas);

      TextWrap wrap = new TextWrap(400);
      respuesta1 = graphics().createImageLayer(
            new TextBlock(graphics().layoutText(pregunta.getRespuestas().get(0), format, wrap)).toImage(
                  Align.CENTER, Colors.BLUE));
      respuesta1.setInteractive(true);
      layer.addAt(respuesta1, 350, yRespuestas.remove(0));

      respuesta2 = graphics().createImageLayer(
            new TextBlock(graphics().layoutText(pregunta.getRespuestas().get(1), format, wrap)).toImage(
                  Align.CENTER, Colors.PINK));
      respuesta2.setInteractive(true);
      layer.addAt(respuesta2, 350, yRespuestas.remove(0));

      respuesta3 = graphics().createImageLayer(
            new TextBlock(graphics().layoutText(pregunta.getRespuestas().get(2), format, wrap)).toImage(
                  Align.CENTER, Colors.ORANGE));
      respuesta3.setInteractive(true);
      layer.addAt(respuesta3, 350, yRespuestas.remove(0));

      // timeout que avanza el juego por si abandonan o tardan mucho
      // anim.delay(TIMEOUT).then().action(() -> timeout = true);
   }

   @Override
   public void doPaint(Clock clock) {
   }

   @Override
   public void update(int delta) {
   }

   @Override
   public void onPointerEnd(float x, float y) {
      Layer hit = layer.hitTest(new Point(x, y));

      if (hit == respuesta1) {
         System.out.println("r1");
      }
      else if (hit == respuesta2) {
         System.out.println("r2");
      }
      else if (hit == respuesta3) {
         System.out.println("r3");
      }
   }

   private List<TextLayout> triangularTexto(String texto, TextFormat format, List<Float> anchos) {
      TextLayout[] layouts = graphics().layoutText(texto, format, new TextWrap(anchos.remove(0)));

      if (anchos.size() == 0) {
         return Lists.newArrayList(layouts);
      }
      else {
         // me quedo con la primer linea nomas
         TextLayout linea = layouts[0];
         List<TextLayout> ret = Lists.newArrayList(linea);

         ret.addAll(triangularTexto(texto.substring(linea.text().length()), format, anchos));

         return ret;
      }
   }
}