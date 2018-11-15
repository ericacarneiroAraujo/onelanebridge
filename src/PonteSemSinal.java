import java.util.*;
import java.lang.Thread;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

public class PonteSemSinal   {


     private Car carro;
     private Caminhao truck;
     private Bridge ponte;
     private Random opt = new Random(); //variável que sorteia a direção a ser criada para carros. Ao gerar 0=left, 1=right;
     private Random opt2 = new Random();

     private List<Car> carrosEsquerda = new ArrayList<>();
     private List<Car> carrosDireita = new ArrayList<>();
     private List<Caminhao> caminhoesEsquerda = new ArrayList<>();
     private List<Caminhao> caminhoesDireita = new ArrayList<>();

     private int direction;
     private int carsDir, carsEsq, camDir, camEsq;
     private int turn;

     private boolean waiting = false;
     private final Object lock = new Object();

     private int before; //informa a diração do veículo anterior.
     private int who; //diz se antes houve carro ou caminhao
     private int carsRight , carsLeft, truckLeft, truckRight;

     public PonteSemSinal () {

          ponte = new Bridge();
          this.carsDir = 50;
          this.carsEsq = 50;
          this.camDir = 3;
          this.camEsq = 3;
          this.carsRight = 0;
          this.carsLeft = 0;
          this.truckLeft = 0;
          this.truckRight = 0;

     }


     public void getSituation (int s) {}

     public void call() {
         if ( before == turn ) {

         }
     }


     public synchronized void await() throws InterruptedException {

             synchronized (lock) {

                 while (!waiting) lock.wait();
                 try {
                     if ((turn == 0) && (((direction == 0))&&(camEsq!=0))) {
                         long ta = caminhoesEsquerda.get(truckLeft).getTime();
                         if (before != turn) // O if gaarnte que se a direção for a mesma ele não vai aguardar.
                         TimeUnit.SECONDS.sleep(ta+20);
                         ponte.setWaiting(ta);
                         ponte.changeTime(ta);
                         ponte.atravessaCam(caminhoesEsquerda.get(truckLeft), 3);

                         truckLeft++;
                         camEsq--;

                         before = 0; // atualiza a direção do fluxo.
                         System.out.println(this.getTotal());


                     }
                     if ((turn == 0) && ((direction == 1)&&(camDir!=0))) {
                         long ta = caminhoesDireita.get(truckRight).getTime();
                         if (before != turn) TimeUnit.SECONDS.sleep(ta+20);
                         ponte.setWaiting(ta);
                         ponte.changeTime(ta);
                         ponte.atravessaCam(caminhoesDireita.get(truckRight), 3);

                         truckRight++;
                         camDir--;

                         before =1;
                         System.out.println(this.getTotal());
                     }

                     if (turn ==1) {
                         if ((direction == 0) && (carsEsq != 0)) {
                             TimeUnit.SECONDS.sleep(2);
                             long ta = carrosEsquerda.get(carsLeft).getTime();
                             if (before != turn)
                                 TimeUnit.SECONDS.sleep(ta);
                             ponte.setWaiting(2 + ta);
                             ponte.changeTime(2 + ta);
                             ponte.atravessaCar(carrosEsquerda.get(carsLeft), 3);

                             carsLeft++;
                             carsEsq--;

                             before = 0;
                             System.out.println(this.getTotal());

                         }

                         if ((direction == 1) && (carsDir != 0)) {
                             TimeUnit.SECONDS.sleep(2);
                             long ta = carrosEsquerda.get(carsRight).getTime();
                             if (before != turn)
                                 TimeUnit.SECONDS.sleep(ta);
                             ponte.setWaiting(2 + ta);
                             ponte.changeTime(2 + ta);
                             ponte.atravessaCar(carrosEsquerda.get(carsRight), 3);


                             carsRight++;
                             carsDir--;

                             before = 1;

                             System.out.println(this.getTotal());
                         }

                     }
                 } catch (Exception e) {
                     e.printStackTrace();
                 }
             }
           }



     public void signal() {
          try {

                Random choose = new Random();
                Random dir = new Random();

                turn = choose.nextInt(2); // Criar carro ou caminhão aleatoriamente.

                if ((turn == 0) && ((camDir != 0) || (camEsq != 0))) {

                    if ((camDir == 0) && (camEsq != 0)) {
                        direction = 0;
                        truck = new Caminhao(ponte, 0);
                        truck.setOp(3);
                        truck.setTime();
                        System.out.println(truck.toString());
                        caminhoesEsquerda.add(truck);


                    }


                    if ((camEsq == 0) && (camDir != 0)) {

                        direction = 1;
                        truck = new Caminhao(ponte, 1);
                        truck.setOp(3);
                        truck.setTime();
                        System.out.println(truck.toString());
                        caminhoesDireita.add(truck);


                    } if( (camDir!=0)&&(camEsq!=0)) {

                        direction = dir.nextInt(2);
                        truck = new Caminhao(ponte, direction);
                        truck.setOp(3);
                        truck.setTime();
                        System.out.println(truck.toString());
                        if (direction == 1) {
                            caminhoesDireita.add(truck);

                        } else {
                            caminhoesEsquerda.add(truck);

                        }

                    }


                }

                if ((turn == 1) && ((carsDir != 0) || (carsEsq != 0))) {

                    if ((carsDir == 0) && (carsEsq != 0)) {
                        direction = 0;
                        carro = new Car(ponte, 0);
                        carro.setOp(3);
                        carro.setTime();
                        System.out.println(carro.toString());
                        carrosEsquerda.add(carro);


                    }


                    if ((carsEsq == 0) && (carsDir != 0)) {
                        direction = 1;
                        carro = new Car(ponte, 0);
                        carro.setOp(3);
                        carro.setTime();
                        System.out.println(carro.toString());
                        carrosDireita.add(carro);


                    } if ((carsDir!=0)&&(carsEsq!=0)){
                        direction = dir.nextInt(2);
                        carro = new Car(ponte, direction);
                        carro.setOp(3);
                        carro.setTime();
                        System.out.println(carro.toString());
                        if (direction == 1) {
                            carrosDireita.add(carro);

                        } else {
                            carrosEsquerda.add(carro);

                        }
                    }


                }

          } catch (Exception e) {
               e.printStackTrace();
          }
        synchronized (lock) {
            waiting = true;
            lock.notifyAll();
        }

     }

     public String getTotal () {
          return (100 - (carsEsq+carsDir)) + " carros atravessaram até então, " + (6 - (camDir+camEsq)) + " caminhões atravessaram até então." ;
     }

     public boolean isEmpty() {
          if ((carsDir==0)&&(carsEsq==0)&&(camEsq==0)&&(camDir==0))
               return true;
          else
               return false;
     }

     public void toStringTime() {
         System.out.println("Tempo total: " + ponte.toStringTotalTime());
         System.out.println("Tempo de utilização da ponte: " + ponte.timeUsed() + " minutos.");
         System.out.println("Tempo de espera mínimo " + ponte.individualWait() + " segundos.");
         System.out.println("Tempo de espera médio: " + ponte.tempoEspera()/106 + " segundos.");

     }

}

