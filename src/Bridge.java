import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Bridge {

    private final Semaphore semaforo;
    private long totalTime; //Controle de tempo.
    private long waitTime;
    private long waiting;
    long tempoFinal = System.currentTimeMillis()/60000; //tempo de execução == minutos.


    public Bridge(){

        semaforo = new Semaphore (1);
        this.totalTime = 0;
        this.waiting = 0;
        this.waitTime = 0;
    }


    public void atravessaCar(Car carro, int op) {
        //No caso de op == 0 então não há sinalizador, caso contrário, há sinalizador.
        if ((op==1)||(op==3)) {

        try {
            totalTime = totalTime + carro.getTime();
            System.out.println("Carro está tentando atravessar pela " + carro.toStringDir() +"!");
            semaforo.acquire();
            System.out.println("Carro está atravessando a ponte!");

        }


        catch (InterruptedException e ) {
            e.printStackTrace();
        }

        finally {

            semaforo.release();
        } }
        else {
            try {
                totalTime = totalTime + carro.getTime();
                System.out.println("Carro está tentando atravessar pela " + carro.toStringDir() +"!");
                semaforo.acquire();
                System.out.println("Carro está atravessando a ponte!");
                long tc = (long)(10);

            }


            catch (InterruptedException e ) {
                e.printStackTrace();
            }

            finally {

                semaforo.release();
            }
        }
    }

    public void atravessaCam (Caminhao cam, int op) {

        //No caso de op == 0 não há sinalizador...
        if (op == 3) {
            try {
                System.out.println("Caminhao está tentando atravessar pela " + cam.toStringDir() + "!");
                totalTime = totalTime + cam.getTime();
                semaforo.acquire();
                System.out.println("Caminhao está atravessando a ponte!");
                long tc = (long) (20);
                totalTime = tc + totalTime;
                this.setWaitTime(20);


            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println("Caminhao atravessou a ponte!");
                semaforo.release();
            }
        } else {

            try {
                System.out.println("Caminhao está tentando atravessar pela " + cam.toStringDir() + "!");
                totalTime = totalTime + cam.getTime();
                semaforo.acquire();
                System.out.println("Caminhao está atravessando a ponte!");
                long tc = (long) (20);
                this.setWaitTime(20);
                TimeUnit.SECONDS.sleep(tc);

                totalTime = tc + totalTime;

            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {

                semaforo.release();
            }

        }


    }

    public void setWaitTime(long time) {
        this.waitTime = waitTime + time;
    }
    public long getWaitTime() {
        return this.waitTime;
    }

    public void setWaiting ( long time ) {

        if (this.waiting > time)
            waiting = time;
        this.waitTime = waitTime + time;
    }

    public long individualWait () {
        return waiting;
    }



    public void changeTime (long t) {
        this.totalTime = this.totalTime + t;
    }
    public long getTime () {
        return this.totalTime;
    }

    public String toStringTotalTime() {
        return "Tempo total na ponte de "+this.getTime()/60 +" minutos";
    }

    public long timeUsed () {
        return  (totalTime/tempoFinal);
    }
    public long tempoEspera () {
        return waitTime;
    }



}
