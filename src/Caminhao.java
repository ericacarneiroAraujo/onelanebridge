import java.util.Random;
import java.lang.Comparable;

public class Caminhao implements Runnable, Comparable<Caminhao> {

    private Random random;
    private Bridge ponte;
    private int ta=0; //tempo de chegada de caminhão;
    private int direcao; // atribui 0 se for esquerda e 1 se for direita!!
    private int op;

    public Caminhao (Bridge bridge, int direction) {

        this.ponte = bridge;
        random = new Random();
        this.direcao = direction;
    }

    public int getTime () {

        return this.ta;
    }

    public void setTime ( ) {

        this.ta = (int)(2 + random.nextInt(5));
    }

    public void run() {

        ponte.atravessaCam(this, this.getOp());
    }


    public void setOp (int op) {
        this.op = op;
    }

    public int getOp () {
        return this.op;
    }

    public void setDirection ( int dir ) {

        this.direcao=dir;
    }

    public int getDirection() {

        return this.direcao;

    }

    public String toString() {
        if(direcao == 1)
            return "Caminhao chegando da direita";
        else
            return "Caminhao chegando da esquerda";
    }

    public String toStringDir() {
        if(direcao == 1)
            return "direita";
        else
            return "esquerda";
    }

    @Override
    public int compareTo(Caminhao outroCaminhao) {
        if (this.ta > outroCaminhao.getTime()) {
            return -1;
        }
        if (this.ta < outroCaminhao.getTime()) {
            return 1;
        }
        return 0;

    }


}



