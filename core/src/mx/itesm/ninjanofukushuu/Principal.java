package mx.itesm.ninjanofukushuu;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;


/*
Clase 1
Desarrollador: Irvin Emmanuel Trujillo Díaz y Javier Antonio García Roque
Descripcion: clase representa un objeto juego, este es el punto de entrada a la aplicación, lo único que hace es poner la pantallaJuego
Profesor: Roberto Martinez Román.
*/


public class Principal extends Game  {

    public static final float ANCHO_MUNDO = 1280;
    public static final float ALTO_MUNDO = 720;
    public static final float ANCHO_CAMARA = 640;
    public static final float ALTO_CAMARA = 480;
    public static boolean sonidoConMute = false; //para saber si el usuario le puso mute a los efectos del juego..

    // Administra la carga de los assets del juego
    private final AssetManager assetManager = new AssetManager();



    @Override
    public void create() {
        // Agregamos un loader para los mapas
        assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        // Pantalla inicial
        setScreen(new PantallaCargando(0,this,false)); //Se envia falso ya que no se esta escuchando ningun musica ahorita.. se envia 0 ya que se quiere cargar la pantalla principal
    }

    // Método accesor de assetManager
    public AssetManager getAssetManager() {
        return assetManager;
    }

}