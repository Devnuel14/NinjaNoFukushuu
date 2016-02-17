package mx.itesm.ninjanofukushuu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/*
Desarrollador: Javier Antonio García Roque e Irvin Emmanuel Trujillo Díaz
Descripción: Representa el Hud del juego. (Heads-Up Display) donde se muestra la informacion durante el gameplay
Profesor: Roberto Martinez Román.
*/

public class Hud{
    //OBVIAMENTE HAY QUE QUITAR LOS COMENTARIOS DE MARIO BROS..... (ROQUE, DEBES DE QUITARLOS..)
    //Scene2D.ui Stage and its own Viewport for HUD
    public Stage stage;
    private Viewport vista;

    //Mario score/time Tracking Variables
    private Integer worldTimer;
    private float timeCount;
    private Integer score;

    //Scene2D widgets
    private Label saludPersonaje;
    private Label cantidadPergaminos;
    private Label pergaminoLabel;
    private Label levelLabel;
    private Label worldLabel;
    private Label saludNinjaLetrero;

    public Hud(SpriteBatch sb) {
        //define our tracking variables
        worldTimer = 10;
        timeCount = 0;
        score = 0;


        //setup the HUD vista using a new camera seperate from our gamecam
        //define our stage using that vista and our games spritebatch
        vista = new FitViewport(Principal.ANCHO_MUNDO, Principal.ALTO_MUNDO, new OrthographicCamera());
        stage = new Stage(vista, sb);

        //define a table used to organize our hud's labels
        Table table = new Table();
        //Top-Align table
        table.top();
        //make the table fill the entire stage
        table.setFillParent(true);

        //define our labels using the String, and a Label style consisting of a font and colo

        saludPersonaje = new Label(String.format("%02d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.BLUE));
        cantidadPergaminos = new Label(String.format("%02d", score), new Label.LabelStyle(new BitmapFont(), Color.CORAL));
        pergaminoLabel = new Label("PERGAMINOS", new Label.LabelStyle(new BitmapFont(), Color.CYAN));
        saludNinjaLetrero = new Label("SALUD ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        //AjustandoTamañoLetreros
        pergaminoLabel.setFontScale(pergaminoLabel.getFontScaleX()*2,pergaminoLabel.getFontScaleY()*2);
        saludPersonaje.setFontScale(saludPersonaje.getFontScaleX() * 2, saludPersonaje.getFontScaleY()*2);
        cantidadPergaminos.setFontScale(cantidadPergaminos.getFontScaleX() *2, cantidadPergaminos.getFontScaleY()*2);
        saludNinjaLetrero.setFontScale(saludNinjaLetrero.getFontScaleX() * 2, saludNinjaLetrero.getFontScaleY() * 2);



        table.add(saludNinjaLetrero).expandX().padTop(10);
        table.add(pergaminoLabel).expandX().padTop(10);
        table.row();
        table.add(saludPersonaje).expandX();
        table.add(cantidadPergaminos).expandX();

        stage.addActor(table);
    }
}