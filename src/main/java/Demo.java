import java.util.Random;

public class Demo {
    public static void main(String[] args) {


        Serviz serviz = new Serviz();


        Car car1 = getRandomCar();
        serviz.comingCar(car1);

        Car car2 = getRandomCar();
        serviz.comingCar(car2);

        Car car3 = getRandomCar();
        serviz.comingCar(car3);

        Car car4 = getRandomCar();
        serviz.comingCar(car4);

        Car car5 = getRandomCar();
        serviz.comingCar(car5);


        serviz.comingCar(car1);
        serviz.comingCar(car1);
        serviz.comingCar(car1);
        serviz.comingCar(car3);
        serviz.comingCar(car3);
        serviz.comingCar(car3);
        serviz.comingCar(car2);
        serviz.comingCar(car2);
        serviz.comingCar(car5);
        serviz.comingCar(car5);

        Reporter rep = new Reporter();
        rep.start();



    }

    private static Car getRandomCar(){
        Car car = new Car(getRandomRegNum(),getRandomModel(),getRandomOwnerName(),getRandomNumber(),
                new Random().nextInt(2000,2022),new Random().nextBoolean());
        return car;
    }

    private static String getRandomNumber() {
        return String.valueOf(new Random().nextInt(222222222,999999999));
    }

    private static String getRandomOwnerName() {
        String[] owners = {"Pesho","Gosho","Ivan","Penka","Stoika","Lelka"};
        return owners[new Random().nextInt(owners.length)-1];
    }

    private static String getRandomModel() {
        String[] owners = {"Audi","BMW","VW","Opel","Mazda","Mercedes"};
        return owners[new Random().nextInt(owners.length)-1];

    }

    private static String getRandomRegNum() {
        return String.valueOf(new Random().nextInt(1111,8888));
    }
}
