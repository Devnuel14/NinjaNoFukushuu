package mx.itesm.ninjanofukushuu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 Pantalla galeria donde se mostrara el arte desbloqueado mediante los pergaminos, tambien muestra el boton si es bloqueado o desbloqueado
 Autores: Irvin Emmanuel Trujillo Díaz
 preferencias para guardar las banderas..
 **/
public class PantallaGaleria implements Screen {
    private final Principal principal;
    private OrthographicCamera camara;
    private Viewport vista;
    private static SpriteBatch batch;

    //static = 1 copia

    //Fondo
    private static Fondo fondo;
    private static Texture texturaFondo;
    //Botones
    private static Boton btnGaleriaTierra;
    private static Texture texturaGaleriaTierraDesbloqueada;
    private static Texture texturaGaleriaTierraBloqueada;
    private boolean banderaPergaminosTierra; //Bandera para saber que textura y el comprotamiendo del boton hacer...

    private static Boton btnGaleriaAgua;
    private static Texture texturaGaleriaAguaDesbloqueada;
    private static Texture texturaGaleriaAguaBloqueada;
    private boolean banderaPergaminosAgua; //Bandera para saber que textura y el comprotamiendo del boton hacer...

    private static Boton btnGaleriaFuego;
    private static Texture texturaGaleriaFuegoDesbloqueada;
    private static Texture texturaGaleriaFuegoBloqueada;
    private boolean banderaPergaminosFuego; //Bandera para saber que textura y el comprotamiendo del boton hacer...

    private static Boton btnRegresar;
    private static Texture texturaRegresar;

    private static final int anchoBoton = 180, altoBoton = 200; //anchoBoton1 = 480 , altoBoton1 = 160;
    //Efectos
    private static  Sound efectoClick, sonidoBloqueado;

    public PantallaGaleria(Principal principal, boolean banderaPergaminosTierra, boolean banderaPergaminosAgua, boolean banderaPergaminosFuego) {
        this.principal = principal;
        //LAS BANDERAS SON FUDAMENTALES YA QUE INDICAN QUE TEXTURA CARGAR (BLOQUEDO O DESBLOQUEADO) Y EL COMPROTAMIENTO DEL BOTON...
        this.banderaPergaminosTierra = banderaPergaminosTierra;
        this.banderaPergaminosAgua = banderaPergaminosAgua;
        this.banderaPergaminosFuego = banderaPergaminosFuego;
    }

    @Override
    public void show() {
        //Crear camara
        camara = new OrthographicCamera(Principal.ANCHO_MUNDO, Principal.ALTO_MUNDO);
        camara.position.set(Principal.ANCHO_MUNDO / 2, Principal.ALTO_MUNDO / 2, 0);
        camara.update();
        vista = new StretchViewport(Principal.ANCHO_MUNDO,Principal.ALTO_MUNDO,camara);
        this.crearObjetos();
        // Indicar el objeto que atiende los eventos de touch (entrada en general)
        Gdx.input.setInputProcessor(new ProcesadorEntrada());

        //DESACTIVANDO BOTONES PARA IMPEDIR QUE INTERRUMPA AL USUARIO EN EL JUEGO.
        Gdx.input.setCatchBackKey(true);
        Gdx.input.setCatchMenuKey(true);

    }



    //crea los objetos de textura y audio
    private void crearObjetos(){
        AssetManager assetManager = principal.getAssetManager();   // Referencia al assetManager
        //Fondo y textura de botones...
        texturaFondo = assetManager.get("imagenesGaleriaArte/fondoGaleriaArte.jpg");
        texturaGaleriaTierraDesbloqueada = assetManager.get("imagenesGaleriaArte/galeriaEarth.png");
        texturaGaleriaTierraBloqueada =  assetManager.get("imagenesGaleriaArte/galeriaEarthLock.png");
        texturaGaleriaAguaDesbloqueada = assetManager.get("imagenesGaleriaArte/galeriaWater.png");
        texturaGaleriaAguaBloqueada =  assetManager.get("imagenesGaleriaArte/galeriaWaterLock.png");
        texturaGaleriaFuegoDesbloqueada =  assetManager.get("imagenesGaleriaArte/galeriaFire.png");
        texturaGaleriaFuegoBloqueada =  assetManager.get("imagenesGaleriaArte/galeriaFireLock.png");

        texturaRegresar = assetManager.get("return.png");
        efectoClick = assetManager.get("sonidoVentana.wav");
        sonidoBloqueado = assetManager.get("bloqueado.wav");

        //Crear fondo
        fondo = new Fondo(texturaFondo);

        //botones
        //NOTA: AQUI  VA DEPENDER DE LA BANDERA, PARA SABER QUE TEXTURA CARGAR, es saber si esta desbloqueado o no la galeria..
        if(banderaPergaminosTierra) //entonces el usuario ya junto los 3 pergaminos del nivel tierra
            btnGaleriaTierra = new Boton(texturaGaleriaTierraDesbloqueada);
        else
            btnGaleriaTierra =  new Boton(texturaGaleriaTierraBloqueada);

        btnGaleriaTierra.setPosicion(Principal.ANCHO_MUNDO / 4 - 310, Principal.ALTO_MUNDO * 2 / 3 - 200);

        if(banderaPergaminosAgua) //entonces el usuario ya junto los 3 pergaminos del nivel agua
            btnGaleriaAgua = new Boton(texturaGaleriaAguaDesbloqueada);
        else
            btnGaleriaAgua = new Boton(texturaGaleriaAguaBloqueada);

        btnGaleriaAgua.setPosicion(Principal.ANCHO_MUNDO / 2 - 135, Principal.ALTO_MUNDO * 2 / 3 - 200);

        if(banderaPergaminosFuego)  //entonces el usuario ya junto los 3 pergaminos del nivel fuego
            btnGaleriaFuego =  new Boton(texturaGaleriaFuegoDesbloqueada);
        else
            btnGaleriaFuego =  new Boton(texturaGaleriaFuegoBloqueada);

        btnGaleriaFuego.setPosicion(Principal.ANCHO_MUNDO * 3 / 4 +10, Principal.ALTO_MUNDO * 2 / 3 - 200);

        btnRegresar = new Boton(texturaRegresar);

        //ajustando tamaño y posiciones del boton reresar...
        btnRegresar.setPosicion(Principal.ANCHO_MUNDO * 7 / 8 , Principal.ALTO_MUNDO * 1 / 5 -130);
        btnRegresar.setTamanio(anchoBoton-30, altoBoton-30 );
        //Batch
        fondo.getSprite().setCenter(Principal.ANCHO_MUNDO / 2, Principal.ALTO_MUNDO / 2);
        fondo.getSprite().setOrigin(1500 / 2, 1500 / 2);

        batch = new SpriteBatch();
    }

    @Override
    public void render(float delta) {
        //Borrar la pantalla
        Gdx.gl.glClearColor(0, 0, 0, 0);    // r, g, b, alpha
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camara.combined);
        //DIBUJAR
        batch.begin();
        fondo.render(batch);
        btnGaleriaTierra.render(batch);
        btnGaleriaAgua.render(batch);
        btnGaleriaFuego.render(batch);
        btnRegresar.render(batch);
        batch.end();
    }

     /*REVISION DE TOUCH*/

    //Clase utilizada para manejar los eventos de touch en la pantalla
    public class ProcesadorEntrada extends InputAdapter {
        private Vector3 coordenadas = new Vector3();
        private float x, y;     // Las coordenadas en la pantalla virtual

        private boolean banderaBotonGaleriaTierra = false, banderaBotonGaleriaAgua = false, banderaBotonGaleriaFuego = false,  banderaBotonRegresar = false;
        /*
        Se ejecuta cuando el usuario pone un dedo sobre la pantalla, los dos primeros parámetros
        son las coordenadas relativas a la pantalla física (0,0) en la esquina superior izquierda
        pointer - es el número de dedo que se pone en la pantalla, el primero es 0
        button - el botón del mouse
         */

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            transformarCoordenadas(screenX, screenY);

            if (btnGaleriaTierra.contiene(x,y)){
                btnGaleriaTierra.setAlfa(.5f);
                btnGaleriaTierra.setTamanio(300, 298); //Lo hago más pequeño
                this.banderaBotonGaleriaTierra = true;
                if(!banderaPergaminosTierra) //esta bloqueado
                    sonidoBloqueado.play(PantallaMenu.volumen);
            }

            else if (btnGaleriaAgua.contiene(x,y)){
                btnGaleriaAgua.setAlfa(.5f);
                btnGaleriaAgua.setTamanio(300, 298); //Lo hago más pequeño
                this.banderaBotonGaleriaAgua = true;
                if(!banderaPergaminosAgua)// esta bloqueado
                    sonidoBloqueado.play(PantallaMenu.volumen);
            }

            else if (btnGaleriaFuego.contiene(x,y)){
                btnGaleriaFuego.setAlfa(.5f);
                btnGaleriaFuego.setTamanio(300, 298); //Lo hago más pequeño
                this.banderaBotonGaleriaFuego = true;
                if(!banderaPergaminosFuego) // esta bloqueado
                    sonidoBloqueado.play(PantallaMenu.volumen);
            }


            else if (btnRegresar.contiene(x,y)){
                btnRegresar.setAlfa(.5f);
                btnRegresar.setTamanio(anchoBoton - 30, altoBoton - 32); //Lo hago más pequeño
                this.banderaBotonRegresar = true;
            }

            return true;    // Indica que ya procesó el evento
        }

        //Se ejecuta cuando el usuario QUITA el dedo de la pantalla.
        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            transformarCoordenadas(screenX, screenY);

            // Preguntar si las coordenadas son de cierto lugar de donde se quito el dedo
            if (btnGaleriaTierra.contiene(x,y) && this.banderaBotonGaleriaTierra && banderaPergaminosTierra ){
                //banderaPergaminosTierra indica que el boton esta desbloqueado porque el usuario ya junto los 3 pergaminos.. en el nivel tierra
                // si no entra al if el boton está bloqueado, no hace ningun sonido, se reproduce el sonido de bloqueado en touchDown
                banderaBotonGaleriaTierra = false;
                btnGaleriaTierra.setAlfa(1);
                btnGaleriaTierra.setTamanio(300, 300); //tamaño original
                efectoClick.play(PantallaMenu.volumen); //efecto de sonido
                // ya se cargaron previamente las imagenes necesarias...
                principal.setScreen(new PantallaImagen(principal, 1)); //galeria de arte de tierra..
            }
            else if (btnGaleriaAgua.contiene(x,y) && this.banderaBotonGaleriaAgua && banderaPergaminosAgua){
                // bandera agua indica que el boton esta desbloqueado porque el usuario ya junto los 3 pergaminos.. en el nivel agua
                // si no entra al if el boton está bloqueado, no hace ningun sonido, se reproduce el sonido de bloqueado en touchdown..
                banderaBotonGaleriaAgua = false;
                btnGaleriaAgua.setAlfa(1);
                btnGaleriaAgua.setTamanio(300, 300); //tamaño original
                efectoClick.play(PantallaMenu.volumen); //efecto de sonido
                // ya se cargaron previamente las imagenes necesarias...
                principal.setScreen(new PantallaImagen(principal, 2)); //galeria de arte de agua..

            }
            else if (btnGaleriaFuego.contiene(x,y) && this.banderaBotonGaleriaFuego && banderaPergaminosFuego ){
                //banderaPergaminosFuego indica que el boton esta desbloqueado porque el usuario ya junto los 3 pergaminos.. en el nivel fuego
                // si no entra al if el boton está bloqueado, no hace ningun sonido, se reproduce el sonido de bloqueado en touchDown..
                efectoClick.play(PantallaMenu.volumen); //efecto de sonido
                banderaBotonGaleriaFuego = false;
                btnGaleriaFuego.setAlfa(1);
                btnGaleriaFuego.setTamanio(300, 300); //tamaño orginal
                // ya se cargaron previamente las imagenes necesarias...
                principal.setScreen(new PantallaImagen(principal, 3)); //galeria de arte de fuego..

            }
            //en la pantalla cargando determinara que cargar... se manda el numero correspondiente para saber que se va cargar en esa clase..
            else if (btnRegresar.contiene(x, y) &&  this.banderaBotonRegresar) {
                efectoClick.play(PantallaMenu.volumen); //efecto de sonido
                principal.setScreen(new PantallaCargando(0,principal,true));  //se manda true porque ya esta la cancion reproduciendose
            }

            else{ //entonces el usuario despego el dedo de la pantalla en otra parte que no sean los botones...
                // se le quita la transparencia y se regresa a su tamaño original
                //niveles bloqueados
                if(banderaBotonGaleriaTierra) {
                    banderaBotonGaleriaTierra = false;
                    btnGaleriaTierra.setAlfa(1);
                    btnGaleriaTierra.setTamanio(300, 300); //tamaño original
                }
                else if(banderaBotonGaleriaAgua) {
                    banderaBotonGaleriaAgua = false;
                    btnGaleriaAgua.setAlfa(1);
                    btnGaleriaAgua.setTamanio(300, 300); //tamaño original
                }
                else if(banderaBotonGaleriaFuego) {
                    banderaBotonGaleriaFuego = false;
                    btnGaleriaFuego.setAlfa(1);
                    btnGaleriaFuego.setTamanio(300, 300); //tamaño orginal
                }
                else if(banderaBotonRegresar) {
                    banderaBotonRegresar = false;
                    btnRegresar.setAlfa(1);
                    btnRegresar.setTamanio(anchoBoton - 30, altoBoton - 30); //tamaño orginal
                }
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
        vista.update(width,height);
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

        texturaGaleriaTierraDesbloqueada.dispose();
        texturaGaleriaTierraBloqueada.dispose();
        texturaGaleriaAguaDesbloqueada.dispose();
        texturaGaleriaAguaBloqueada.dispose();
        texturaGaleriaFuegoBloqueada.dispose();
        texturaGaleriaFuegoDesbloqueada.dispose();

        texturaRegresar.dispose();
        texturaFondo.dispose();
        efectoClick.dispose();
        sonidoBloqueado.dispose();
    }
}