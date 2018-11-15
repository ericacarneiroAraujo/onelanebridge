import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import java.lang.Thread;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import java.util.Scanner;

public class Main {

    private Car carro;
    private Caminhao truck;
    private Bridge ponte;
    private Random opt = new Random(); //variável que sorteia a direção a ser criada para carros. Ao gerar 0=left, 1=right;
    private Random opt2 = new Random();// Variável que sorteia a direção a ser criada para acminhões. The same as cars.

    //Esses dois atributos funcionam para a minha política de troca, afinal, caminhão passará sozinho.
    private Random mobile; //variável que sorteia se eu iniciarei por caminhao ou por carro.
    private int come; //variável que libera a entrada do carro ou do caminhão.


    private List<Car> carrosEsquerda = new ArrayList<>(); //updated, but not queried = dead code.
    private List<Car> carrosDireita = new ArrayList<>();
    private List<Caminhao> caminhoesEsquerda = new ArrayList<>();
    private List<Caminhao> caminhoesDireita = new ArrayList<>();


    //****************************************************
    private boolean disponivel;

    private int turn; // variável que dá a vez. turn to...
    private int direction;

    private int carsDir, carsEsq;
    private int camDir, camEsq;
   //******************************************************

    private int carsRight = 50;
    private int carsLeft = 50;
    private int truckRight = 3;
    private int truckLeft = 3;

    private int situation; // Variável que sinaliza ao programa qual das 3 situações é a escolhida.


    public void criaSituacao () {

            System.out.println("Hello! Vamos iniciar nossa ponte. \n Escolha uma opção abaixo: \n" +
                    "1 - Carros com sinalizadores.\n2 - Carros e caminhões com sinalizador.\n " +
                    "3 - Carros e caminhões sem sinalizador.");
            Scanner sc = new Scanner(System.in);
            this.setSituation(sc.nextInt()); //não faço acesso direto a um atributo privado.


    }

    public void setSituation (int i) {

        this.situation = i;
    }

    public int getSituation (){return situation;}

    public void criarCarros () {
    new Thread()  {
        @Override
                public void run() {

                int direcao;

                do {

                    direcao =  (opt.nextInt(2));
                    if ((direcao == 0) && (carsLeft > 0)) {
                        carro = new Car(ponte, direcao);
                        carro.setOp(getSituation());
                        carro.setDirecao(direcao);
                        carro.setTime();
                        System.out.println(carro.toString());
                        carrosEsquerda.add(carro);
                        carsLeft--;
                        System.out.println(carsLeft);

                    } else if (carsRight > 0) {
                        carro = new Car(ponte, direcao);
                        carro.setOp(situation);
                        carro.setTime();
                        carro.setDirecao(direcao);
                        System.out.println(carro.toString());
                        carrosDireita.add(carro);
                        carsRight--;
                        System.out.println(carsRight);

                    }


                } while ((carsRight > 0) || (carsLeft > 0));

            }

        }.start();

        //Vamos ordenar os carros por tempo para ver quem chega primeiro na fila.
        Collections.sort(carrosEsquerda);
        Collections.sort(carrosDireita);

    }

    public void criarCaminhoes () {
        new Thread() {
            @Override
            public void run() {

                int direcao;

                do {

                    direcao = (opt2.nextInt(2));
                    if ((direcao == 0) && (truckLeft > 0)) {
                        truck = new Caminhao(ponte, direcao);
                        truck.setOp(situation);
                        truck.setTime();
                        System.out.println(truck.toString());
                        caminhoesEsquerda.add(truck);
                        truckLeft--;
                    } else if (truckRight > 0) {
                        truck = new Caminhao(ponte, direcao);
                        truck.setOp(situation);
                        truck.setDirection(direcao);
                        truck.setTime();
                        System.out.println(truck.toString());
                        caminhoesDireita.add(truck);
                        truckRight--;

                    }


                } while ((truckRight > 0) || (truckLeft > 0));



            }
        }.start();





    }

//Vamoa dotar a seguinte política, sortearemos aleatoriamente.
    public void atravessarSecond () {



                ponte = new Bridge();
                Random inicio = new Random();
                mobile = new Random();

                int dirCar = 50;
                int esqCar = 50;
                int dirCam = 3;
                int esqCam = 3;

                try {
                    do {
                        come = mobile.nextInt(2);//Caminhao(0) ou Carro(1).
                        turn = inicio.nextInt(2);//Direita(1) ou esquerda(0).
                        if ((come == 0) && (turn == 0)) {
                            long ta = caminhoesEsquerda.get(3 - esqCam).getTime();
                            TimeUnit.SECONDS.sleep(ta);
                            ponte.setWaiting(ta);
                            ponte.setWaiting(2+ta);
                            ponte.changeTime(2+ta);
                            ponte.atravessaCam(caminhoesEsquerda.get(3 - esqCam), situation);
                            esqCam--;
                            System.out.println((3 - esqCam)+ " caminhões atravessaram pela esquerda, " +( 3 - dirCam )+ " atravessaram pela direita.");

                        }
                        if ((come == 0) && (turn == 1)) {
                            long ta = caminhoesDireita.get(3 - dirCam).getTime();
                            TimeUnit.SECONDS.sleep(ta);
                            ponte.setWaiting(2+ta);
                            ponte.changeTime(2+ta);
                            ponte.setWaiting(ta);
                            ponte.atravessaCam(caminhoesDireita.get(3 - dirCam), situation);
                            dirCam--;
                            System.out.println((3 - esqCam)+ " caminhões atravessaram pela esquerda, " +( 3 - dirCam )+ " atravessaram pela direita.");
                        }

                        if (come == 1) {
                            if ((turn == 0) && (esqCar > 0)) {

                                long ta = carrosEsquerda.get(50 - esqCar).getTime();
                                TimeUnit.SECONDS.sleep(ta);
                                ponte.setWaiting(ta);
                                ponte.atravessaCar(carrosEsquerda.get(50 - esqCar), situation);
                                esqCar--;
                                for (int i = 0; i < 4; i++) {
                                    TimeUnit.SECONDS.sleep(2);
                                    ta = carrosEsquerda.get(50 - esqCar).getTime();
                                    TimeUnit.SECONDS.sleep(ta);
                                    ponte.setWaiting(ta);
                                    ponte.atravessaCar(carrosEsquerda.get(50 - esqCar), situation);
                                    esqCar--;
                                    System.out.println((50 - esqCar )+ " carros atravessaram pela esquerda, " +( 50 - dirCar )+ " atravessaram pela direita.");
                                }
                                TimeUnit.SECONDS.sleep(10);
                                ponte.changeTime(10);
                                turn = 1;

                            } else if (turn == 1) {
                                long ta = carrosDireita.get(50 - dirCar).getTime();
                                TimeUnit.SECONDS.sleep(ta);
                                ponte.setWaiting(ta);
                                ponte.atravessaCar(carrosDireita.get(50 - dirCar), situation);
                                dirCar--;
                                for (int i = 0; i < 4; i++) {
                                    TimeUnit.SECONDS.sleep(2);
                                    ta = carrosDireita.get(50 - dirCar).getTime();
                                    ponte.setWaiting(2+ta);
                                    ponte.changeTime(2+ta);
                                    TimeUnit.SECONDS.sleep(ta);
                                    ponte.atravessaCar(carrosDireita.get(50 - dirCar), situation);
                                    dirCar--;
                                    System.out.println((50 - esqCar )+ " carros atravessaram pela esquerda, " +( 50 - dirCar )+ " atravessaram pela direita.");
                                }
                                turn = 0;
                                TimeUnit.SECONDS.sleep(10);
                                ponte.changeTime(10);
                                turn = 1;

                            }
                        }
                    } while ((dirCam != 0) || (esqCam != 0));

                    do {

                        if ((turn == 1) && (dirCar != 0)) {
                            TimeUnit.SECONDS.sleep(2);
                            long ta = carrosDireita.get(50 - dirCar).getTime();
                            TimeUnit.SECONDS.sleep(ta);
                            ponte.setWaiting(2+ta);
                            ponte.changeTime(2+ta);
                            ponte.atravessaCar(carrosDireita.get(50 - dirCar), situation);
                            dirCar--;
                            System.out.println((50 - esqCar )+ " carros atravessaram pela esquerda, " +( 50 - dirCar )+ " atravessaram pela direita.");


                        }

                        if ((turn == 1) && (esqCar != 0)) { //garante que todos vão atravessar.

                            TimeUnit.SECONDS.sleep(2);
                            long ta = carrosDireita.get(50 - dirCar).getTime();
                            TimeUnit.SECONDS.sleep(ta);
                            ponte.setWaiting(2+ta);
                            ponte.changeTime(2+ta);
                            ponte.atravessaCar(carrosDireita.get(50 - dirCar), situation);
                            esqCar--;
                            System.out.println((50 - esqCar )+ " carros atravessaram pela esquerda, " +( 50 - dirCar )+ " atravessaram pela direita.");

                            turn = 1;
                        }

                    } while ((dirCar != 0) || (esqCar != 0));


                    System.out.println("Tempo total: " + ponte.toStringTotalTime());
                    System.out.println("Tempo de utilização da ponte: " + ponte.timeUsed() + " minutos.");
                    System.out.println("Tempo de espera mínimo " + ponte.individualWait() + " segundos.");
                    System.out.println("Tempo de espera médio: " + (ponte.tempoEspera()/106 )+ " minutos.");



                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


        }




//Carros com sinalizadores
    public void atravessar() {
        new Thread() {
            @Override
            public void run() {
                try {


                    ponte = new Bridge();
                    Random inicio = new Random();
                    int dir = 50; // Ao início de atravessar eu tenho 50 para cada direção, depois decremento.
                    int esq = 50;
                    turn = (inicio.nextInt(2));

                    do {
                        if ((turn == 0) && (esq > 0)) {

                            long ta = carrosEsquerda.get(50 - esq).getTime();
                            TimeUnit.SECONDS.sleep(ta);
                            ponte.atravessaCar(carrosEsquerda.get(50 - esq), situation);
                            esq--;
                            for (int i = 0; i < 4; i++) {
                                TimeUnit.SECONDS.sleep(2);
                                ta = carrosEsquerda.get(50 - esq).getTime();
                                TimeUnit.SECONDS.sleep(ta);
                                ponte.setWaiting(2+ta);
                                ponte.changeTime(2+ta);
                                ponte.atravessaCar(carrosEsquerda.get(50 - esq), situation);
                                System.out.println("Carro atravessou a ponte!");
                                esq--;
                                System.out.println((50 - esq )+ " carros atravessaram pela esquerda, " +( 50 - dir )+ " atravessaram pela direita.");
                            }
                            TimeUnit.SECONDS.sleep(10);
                            ponte.changeTime(10);
                            turn = 1;

                        } else if ((turn == 1) && (dir > 0)) {
                            long ta = carrosDireita.get(50 - dir).getTime();
                            TimeUnit.SECONDS.sleep(ta);
                            ponte.atravessaCar(carrosDireita.get(50 - dir), situation);
                            dir--;
                            for (int i = 0; i < 4; i++) {
                                TimeUnit.SECONDS.sleep(2);
                                ta = carrosDireita.get(50 - dir).getTime();
                                TimeUnit.SECONDS.sleep(ta);
                                ponte.setWaiting(2+ta);
                                ponte.changeTime(2+ta);
                                ponte.atravessaCar(carrosDireita.get(50 - dir), situation);
                                System.out.println("Carro atravessou a ponte!");
                                dir--;
                                System.out.println((50 - esq )+ " carros atravessaram pela esquerda, " +( 50 - dir )+ " atravessaram pela direita.");
                            }

                            TimeUnit.SECONDS.sleep(10);
                            ponte.changeTime(10);
                            turn = 0;

                        }
                    } while ((dir >= 5) || (esq >= 5));

                    if ((turn == 1) && (dir != 0)) {

                        do {
                            TimeUnit.SECONDS.sleep(2);
                            long ta = carrosDireita.get(50 - dir).getTime();
                            TimeUnit.SECONDS.sleep(ta);
                            ponte.setWaiting(2+ta);
                            ponte.changeTime(2+ta);
                            ponte.atravessaCar(carrosDireita.get(50 - dir), situation);
                            System.out.println("Carro atravessou a ponte!");

                            dir--;
                            System.out.println((50 - esq )+ " carros atravessaram pela esquerda, " +( 50 - dir )+ " atravessaram pela direita.");
                        } while (dir != 0);
                        TimeUnit.SECONDS.sleep(10);
                        ponte.changeTime(10);
                        turn = 0;


                    }

                    if ((turn == 0) && (esq != 0)) {
                        do {
                            TimeUnit.SECONDS.sleep(2);
                            long ta = carrosEsquerda.get(50 - esq).getTime();
                            TimeUnit.SECONDS.sleep(ta);
                            ponte.setWaiting(2+ta);
                            ponte.changeTime(2+ta);
                            ponte.atravessaCar(carrosEsquerda.get(50 - esq), situation);
                            System.out.println("Carro atravessou a ponte!");

                            esq--;

                            System.out.println((50 - esq )+ " carros atravessaram pela esquerda, " +( 50 - dir )+ " atravessaram pela direita.");

                        } while (esq != 0);
                        TimeUnit.SECONDS.sleep(10);
                        ponte.changeTime(10);
                        turn = 1;
                    }


                    System.out.println("Tempo total: " + ponte.toStringTotalTime());
                    System.out.println("Tempo de utilização da ponte: " + ponte.timeUsed()/60 + " minutos.");
                    System.out.println("Tempo de espera mínimo " + ponte.individualWait() + " segundos.");
                    System.out.println("Tempo de espera médio: " + ponte.tempoEspera()/(106) + " segundos.");




                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        }.start();
    }



    public int getCarsRight(){
        return carsRight;
    }
    public int getCarsLeft(){
        return carsLeft;
    }


    public static void main(String[] args) {

         Main teste = new Main();
         PonteSemSinal ponte = new PonteSemSinal();
         int option;
         final Object lock = new Object();

         teste.criaSituacao();
         option = teste.getSituation();
         if(option==2) {
             teste.criarCarros();
             teste.criarCaminhoes();
             teste.atravessarSecond();
         }
         if(option == 1) {


             teste.criarCarros();
             teste.atravessar();
         }

         if (option == 3) {
             try {
                do {
                    ponte.signal();
                    ponte.await();


                }while(!ponte.isEmpty());
                ponte.toStringTime();
             }catch (Exception e) {
                 e.printStackTrace();
             }

         }

        System.out.println("THE END!! ");

    }


}
