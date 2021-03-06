package mx.itesm.ninjanofukushuu;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/*
 Descripcion:  Representa el personaje en pantalla
 Profesor: Roberto Martinez Román.
 */
public class Personaje
{
    public static final float VELOCIDAD_Y = -4f;   // Velocidad de caída
    public static final float VELOCIDAD_X = 2;
    private Sprite sprite;  // Sprite cuando no se mueve

    // Animación
    private Animation animacion;    // Caminando
    private float timerAnimacion;

    // Estados del personaje
    private EstadoMovimiento estadoMovimiento;
    private EstadoSalto estadoSalto;
    private Estado estado;

    // SALTO del personaje
    private static final float V0 =45;     // Velocidad inicial del salto
    private static final float G = 9.81f;
    private static final float G_2 = G/2;   // Gravedad
    private float yInicial;         // 'y' donde inicia el salto
    private float tiempoVuelo;       // Tiempo que estará en el aire
    private float tiempoSalto;      // Tiempo actual de vuelo
    boolean banderaVolteadoIzquierda = false;//Para cuando este quieto, voltear el sprite...
    private float tiempoDaniado;

    /*
    Constructor del personaje, recibe una imagen con varios frames, (ver imagen marioSprite.png)
     */
    public Personaje(Texture textura) {
        // Lee la textura como región
        TextureRegion texturaCompleta = new TextureRegion(textura);
        // La divide en frames de 16x32 (ver marioSprite.png)
        TextureRegion[][] texturaPersonajeCorriendo= texturaCompleta.split(16,32);

        // Crea la animación con tiempo de 0.15 segundos entre frames.
        animacion = new Animation(0.10f,texturaPersonajeCorriendo[0][1], texturaPersonajeCorriendo[0][2],  texturaPersonajeCorriendo[0][3],texturaPersonajeCorriendo[0][4],texturaPersonajeCorriendo[0][5],texturaPersonajeCorriendo[0][6]);  //La matriz [0][3] continene un sprite de hataku con el pie adelante y así.. va caminando
        // Animación infinita
        animacion.setPlayMode(Animation.PlayMode.LOOP);
        // Inicia el timer que contará tiempo para saber qué frame se dibuja
        timerAnimacion = 0;
        // Crea el sprite cuando para el personaje quieto (idle)
        sprite = new Sprite(texturaPersonajeCorriendo[0][0]);
        estadoMovimiento = EstadoMovimiento.INICIANDO;
        estadoSalto = EstadoSalto.EN_PISO;
        estado = Estado.SIN_DANIO;
        tiempoDaniado=0;
    }

    // Dibuja el personaje
    public void render(SpriteBatch batch) {
        // Dibuja el personaje dependiendo del estadoMovimiento
        switch (estadoMovimiento) {
            case MOV_DERECHA:
            case MOV_IZQUIERDA:
                // Incrementa el timer para calcular el frame que se dibuja
                timerAnimacion += Gdx.graphics.getDeltaTime();
                // Obtiene el frame que se debe mostrar (de acuerdo al timer)
                TextureRegion region = animacion.getKeyFrame(timerAnimacion);
                // Dirección correcta
                if (estadoMovimiento==EstadoMovimiento.MOV_IZQUIERDA) {
                    if (!region.isFlipX()) {
                        banderaVolteadoIzquierda = true;
                        region.flip(true,false);
                    }
                }
                else { //se mueve a la derecha
                    if (region.isFlipX()) {
                        banderaVolteadoIzquierda = false;
                        region.flip(true,false);
                    }
                }
                // Dibuja el frame en las coordenadas del sprite
                batch.draw(region, sprite.getX(), sprite.getY());
                break;
            case INICIANDO:
            case QUIETO:
                if(banderaVolteadoIzquierda) {
                    if (!sprite.isFlipX()) {
                        sprite.flip(true, false);
                    }

                }
                else{
                    if (sprite.isFlipX()) {
                        sprite.flip(true, false);
                    }
                }
                sprite.draw(batch); // Dibuja el sprite
                break;
        }

    }

    // Actualiza el sprite, de acuerdo al estado
    public void actualizar() {
        float nuevaX = sprite.getX();
        switch (estadoMovimiento) {
            case MOV_DERECHA:
                // Prueba que no salga del mundo
                nuevaX += VELOCIDAD_X;
                if (nuevaX<=PantallaJuego.ANCHO_MAPA-sprite.getWidth()) {
                    sprite.setX(nuevaX);
                }
                break;
            case MOV_IZQUIERDA:
                // Prueba que no salga del mundo
                nuevaX -= VELOCIDAD_X;
                if (nuevaX>=0) {
                    sprite.setX(nuevaX);
                }
                break;
        }
    }

    //Estado daño enemigo
    public void danio(){
        switch (estado){
            case SIN_DANIO:
                sprite.setColor(Color.WHITE);
                break;
            case DANIADO:
                sprite.setColor(Color.RED);
                tiempoDaniado += Gdx.graphics.getDeltaTime();
                if(tiempoDaniado>=5){
                    estado=Estado.SIN_DANIO;
                    tiempoDaniado=0;
                }
                break;
        }
    }

    // Avanza en su caída
    public void caer() {
        sprite.setY(sprite.getY() + VELOCIDAD_Y);
    }

    // Actualiza la posición en 'y', está saltando
    public void actualizarSalto() {
        // Ejecutar movimiento vertical
        float y = V0 * tiempoSalto - G_2 * tiempoSalto * tiempoSalto;  // Desplazamiento desde que inició el salto y se calcula la altura, formula de fisica.., note que G_2 es negativo ya que por física, la gravedad te atrae...
        if (tiempoSalto > tiempoVuelo / 2) { // Llegó a la altura máxima?
            // Inicia caída
            estadoSalto = EstadoSalto.BAJANDO;
        }
        tiempoSalto += 10 * Gdx.graphics.getDeltaTime();  // Actualiza tiempo
        sprite.setY(yInicial + y);    // Actualiza posición
        if (y < 0) {
            // Regresó al piso
            sprite.setY(yInicial);  // Lo deja donde inició el salto
            estadoSalto = EstadoSalto.EN_PISO;  // Ya no está saltando
        }
    }

    // Accesor de la variable sprite
    public Sprite getSprite() {
        return sprite;
    }

    // Accesores para la posición
    public float getX() {
        return sprite.getX();
    }

    public float getY() {
        return sprite.getY();
    }

    public void setPosicion(float x, int y) {
        sprite.setPosition(x,y);
    }

    // Accesor del estado
    public EstadoMovimiento getEstadoMovimiento() {
        return estadoMovimiento;
    }

    public Estado getEstado(){return estado;}

    // Modificador del estado
    public void setEstado(EstadoMovimiento estado) {
        this.estadoMovimiento = estado;
    }

    public void setEstadoSalto(EstadoSalto estadoSalto) {
        this.estadoSalto = estadoSalto;
    }

    // Inicia el salto
    public void saltar() {
        if (estadoSalto==EstadoSalto.EN_PISO) {
            tiempoSalto = 0;
            yInicial = sprite.getY();
            estadoSalto = EstadoSalto.SUBIENDO;
            tiempoVuelo = 2 * V0 / G; //Se establece un tiempo de vuelo, un tiempo maximo.., en fisica, el tiempo es igual a la velocidad inicial sobre la gravedad.. en caida libre...
        }
    }

    public EstadoSalto getEstadoSalto() {
        return estadoSalto;
    }

    public void daniar() {
        if(estado!=Estado.DANIADO){
            estado=Estado.DANIADO;
            tiempoDaniado=0;
        }
    }

    public enum EstadoMovimiento {
        INICIANDO,
        QUIETO,
        MOV_IZQUIERDA,
        MOV_DERECHA,
    }
//..
    public enum EstadoSalto {
        EN_PISO,
        SUBIENDO,
        BAJANDO,
        CAIDA_LIBRE // Cayó de una orilla
    }
    public enum Estado{
        SIN_DANIO,
        DANIADO
    }
}
