package ar.com.jengibre.android;

import playn.android.GameActivity;
import playn.core.PlayN;

import ar.com.jengibre.core.QueSabes;

public class QueSabesActivity extends GameActivity {

  @Override
  public void main(){
    PlayN.run(new QueSabes());
  }
}
