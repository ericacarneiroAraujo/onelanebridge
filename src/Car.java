import java.util.Random;
import java.lang.Comparable;
import java.lang.Runnable;


public class Car implements Runnable, Comparable<Car> {

    private Random random;
    private Bridge ponte;
    private int direcao; // crio um sinalizador da direção, 0 - left, 1 = right;
    private int ta = 0; //Tempo de chegada de carro.
    private int op;
    public Car (Bridge bridge, int direction) {

        this.ponte = bridge;
        random = new Random();
        this.direcao = direction;

    }

    public void run () {
        ponte.atravessaCar(this, this.getOp());
    }

    public void setOp (int op) {
        this.op = op; //Método que especifica qual caso é o tratado no momento.
    }

    public int getOp () {
        return this.op;
    }

    public void setTime( ) {

        this.ta = (int)(2 + random.nextInt(5));
    }

    public int getTime( ) {

        return ta;
    }

    public void setDirecao (int dir) {

        this.direcao = dir;
    }

    public int getDirecao () {
        return direcao;
    }

    public String toString() {
        if(direcao == 1)
        return "Carro vindo da direita";
        else
            return "Carro vindo da esquerda";
    }

    public String toStringDir() {
        if(direcao == 1)
            return "direita";
        else
            return "esquerda";
    }

    @Override
    public int compareTo(Car outroCarro) {
        if (this.ta > outroCarro.getTime()) {
            return -1;
        }
        if (this.ta < outroCarro.getTime()) {
            return 1;
        }
        return 0;

    }


}
