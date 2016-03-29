package mx.itesm.ninjanofukushuu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import javax.xml.soap.Text;

/*
 Pantalla seleccion de nivel
 * Descripción: Pantalla para seleccionar el nivel, dependiendo del progreso, el usuario desbloqueara los niveles...
 * Profesor: Roberto Martinez Román
 * Autores: Irvin Emmanuel Trujillo Díaz y Javier.

* */
public class SeleccionDeNivel implements  Screen{
    private final Principal principal;
    private OrthographicCamera camara;
    private Viewport vista;
    private SpriteBatch batch;
    //Fondo
    private Fondo fondo;
    private Texture texturaFondo;


    private Boton btnRegresar;
    private Boton btnNivelUno;
    private Boton btnNivelDos;
    private Boton btnNivelTres;
    private Texture texturaRegresar;
    private Texture texturaN1;
    private Texture texturaN2;
    private Texture texturaN3;
    private static final int anchoBoton = 180, altoBoton = 200; //anchoBoton1 = 480 , altoBoton1 = 160;

    //Efectos
    private Sound efectoClick, sonidoBloqueado;

    public SeleccionDeNivel(Principal principal) {
        this.principal = principal;
    }

    @Override
    public void show() {
        //Crear camara
        camara = new OrthographicCamera(Principal.ANCHO_MUNDO, Principal.ALTO_MUNDO);
        camara.position.set(Principal.ANCHO_MUNDO / 2, Principal.ALTO_MUNDO / 2, 0);
        camara.update();

        vista = new StretchViewport(Principal.ANCHO_MUNDO, Principal.ALTO_MUNDO, camara);
        this.crearObjetos();
        // Indicar el objeto que atiende los eventos de touch (entrada en general)
        Gdx.input.setInputProcessor(new ProcesadorEntrada());

        //Crear fondo
        fondo = new Fondo(texturaFondo);

        btnRegresar = new Boton(texturaRegresar);
        btnRegresar.setPosicion(Principal.ANCHO_MUNDO * 7 / 8, Principal.ALTO_MUNDO * 1 / 5 - 150);
        btnRegresar.setTamanio(anchoBoton, altoBoton);
        btnNivelUno=new Boton(texturaN1);
        btnNivelUno.setPosicion(100, Principal.ALTO_MUNDO/2-100);
        btnNivelUno.setTamanio(anchoBoton, altoBoton);
        btnNivelDos=new Boton(texturaN2);
        btnNivelDos.setPosicion(550, Principal.ALTO_MUNDO/2-100);
        btnNivelDos.setTamanio(anchoBoton, altoBoton);
        btnNivelTres=new Boton(texturaN3);
        btnNivelTres.setPosicion(1000, Principal.ALTO_MUNDO/2-100);
        btnNivelTres.setTamanio(anchoBoton, altoBoton);

        //Batch
        fondo.getSprite().setCenter(Principal.ANCHO_MUNDO / 2, Principal.ALTO_MUNDO / 2);
        fondo.getSprite().setOrigin(1500 / 2, 1500 / 2);
        batch = new SpriteBatch();
    }

    //crea los objetos de textura y audio
    private void crearObjetos() {
        AssetManager assetManager = principal.getAssetManager();   // Referencia al assetManager
        //Fondo
        this.texturaFondo = assetManager.get("seleccionNivel/fondoSeleccionNivel.jpg");
        this.texturaRegresar = assetManager.get("return.png");

        //el primer nivel siempre esta desbloqueadp
        this.texturaN1=assetManager.get("seleccionNivel/galeriaEarth.png");
        /*Aqui hay que evaluar las banderas para saber si esta desbloqueado el nivel...*/
        if(PantallaCargando.banderaNivelAguaDesbloqueado)
            this.texturaN2=assetManager.get("seleccionNivel/galeriaWater.png");
        else //entonces esta bloqueado
            this.texturaN2=assetManager.get("seleccionNivel/galeriaWaterLock.png");

        if(PantallaCargando.banderaNivelFuegoDesbloqueado)
            this.texturaN3=assetManager.get("seleccionNivel/galeriaFire.png");
        else
            this.texturaN3=assetManager.get("seleccionNivel/galeriaFireLock.png");

        this.efectoClick = assetManager.get("sonidoVentana.wav");
        this.sonidoBloqueado = assetManager.get("bloqueado.wav");
    }


    @Override
    public void render(float delta) {
        //Borrar la pantalla
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camara.combined);

        //DIBUJAR
        batch.begin();
        fondo.render(batch);
        btnRegresar.render(batch);
        btnNivelUno.render(batch);
        btnNivelDos.render(batch);
        btnNivelTres.render(batch);
        batch.end();
    }


    //Clase utilizada para manejar los eventos de touch en la pantalla
    public class ProcesadorEntrada extends InputAdapter {
        private Vector3 coordenadas = new Vector3();
        private float x, y;     // Las coordenadas en la pantalla virtual

        private boolean banderaBotonRegresar = false, banderaBotonNivel1=false, banderaBotonNivel2=false, banderaBotonNivel3=false;
        /*
        Se ejecuta cuando el usuario pone un dedo sobre la pantalla, los dos primeros parámetros
        son las coordenadas relativas a la pantalla física (0,0) en la esquina superior izquierda
        pointer - es el número de dedo que se pone en la pantalla, el primero es 0
        button - el botón del mouse*/

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            transformarCoordenadas(screenX, screenY);

            if (btnRegresar.contiene(x, y)) {
                btnRegresar.setAlfa(.5f);
                btnRegresar.setTamanio(anchoBoton, altoBoton - 2); //Lo hago más pequeño
                this.banderaBotonRegresar = true;

            }

            if (btnNivelUno.contiene(x, y)) {
                btnNivelUno.setAlfa(.5f);
                btnNivelUno.setTamanio(anchoBoton, altoBoton - 2); //Lo hago más pequeño
                this.banderaBotonNivel1 = true;
            }

            if (btnNivelDos.contiene(x, y)) {
                btnNivelDos.setAlfa(.5f);
                btnNivelDos.setTamanio(anchoBoton, altoBoton - 2); //Lo hago más pequeño
                this.banderaBotonNivel2 = true;
                if(!PantallaCargando.banderaNivelAguaDesbloqueado)
                    sonidoBloqueado.play(PantallaMenu.volumen);
            }

            if (btnNivelTres.contiene(x, y)) {
                btnNivelTres.setAlfa(.5f);
                btnNivelTres.setTamanio(anchoBoton, altoBoton - 2); //Lo hago más pequeño
                this.banderaBotonNivel3 = true;
                if(!PantallaCargando.banderaNivelFuegoDesbloqueado)
                    sonidoBloqueado.play(PantallaMenu.volumen);
            }

            return true;    // Indica que ya procesó el evento
        }

        //Se ejecuta cuando el usuario QUITA el dedo de la pantalla.
        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            transformarCoordenadas(screenX, screenY);

            // Preguntar si las coordenadas son de cierto lugar de donde se quito el dedo

            //en la pantalla cargando determinara que cargar... se manda el numero correspondiente para saber que se va cargar en esa clase..
            if (btnRegresar.contiene(x, y) && this.banderaBotonRegresar) {
                efectoClick.play(PantallaMenu.volumen); //efecto de sonido
                principal.setScreen(new PantallaCargando(0, principal, true));  //se manda true porque ya esta la cancion reproduciendose
            }
            if (btnNivelUno.contiene(x, y) && this.banderaBotonNivel1) {
                efectoClick.play(PantallaMenu.volumen); //efecto de sonido
                principal.setScreen(new PantallaCargando(5, principal, true));  //nivel de tierra..
            }
            if (btnNivelDos.contiene(x, y) && this.banderaBotonNivel2 && PantallaCargando.banderaNivelAguaDesbloqueado) {
                efectoClick.play(PantallaMenu.volumen); //efecto de sonido
                principal.setScreen(new PantallaCargando(6, principal, true));  //nivel de agua.. tiene que ser un 6.
            }
            if (btnNivelTres.contiene(x, y) && this.banderaBotonNivel3 &&  PantallaCargando.banderaNivelFuegoDesbloqueado) {
                efectoClick.play(PantallaMenu.volumen); //efecto de sonido
                principal.setScreen(new PantallaCargando(7, principal, true));  //nivel de fuego... tiene que ser un 7
            }
            else { //entonces el usuario despego el dedo de la pantalla en otra parte que no sean los botones...
                // se le quita la transparencia y se regresa a su tamaño original
                banderaBotonRegresar = false;
                banderaBotonNivel1=false;
                banderaBotonNivel2=false;
                banderaBotonNivel3=false;
                btnRegresar.setAlfa(1);
                btnNivelUno.setAlfa(1);
                btnNivelDos.setAlfa(1);
                btnNivelTres.setAlfa(1);
                btnRegresar.setTamanio(anchoBoton, altoBoton); //tamaño orginal
                btnNivelUno.setTamanio(anchoBoton,altoBoton);
                btnNivelDos.setTamanio(anchoBoton,altoBoton);
                btnNivelTres.setTamanio(anchoBoton,altoBoton);
            }
            return true;    // Indica que ya procesó el evento
        }

        private void transformarCoordenadas(int screenX, int screenY) {
            // Transformar las coordenadas de la pantalla física a la cámara
            coordenadas.set(screenX, screenY, 0);
            camara.unproject(coordenadas); //camaraHUD es para los botones
            // Obtiene las coordenadas relativas a la pantalla virtual
            x = coordenadas.x;
            y = coordenadas.y;
        }
    }


    @Override
    public void resize(int width, int height) {
        vista.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        //Eliminar basura
        principal.dispose();
        batch.dispose();
        texturaRegresar.dispose();
        texturaN1.dispose();
        texturaN2.dispose();
        texturaN3.dispose();
        texturaFondo.dispose();
    }
}
