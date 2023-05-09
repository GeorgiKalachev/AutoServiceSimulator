

public class Reporter extends Thread {

    @Override
    public void run() {
        while (true){
            try {
                Thread.sleep(1000*60*60*24*30);

                DBManager.getOrdersCountLast30Days();
                DBManager.countRepairs();
                DBManager.countMaintenance();
                DBManager.busiestDiagnostic();
                DBManager.getVehiclesWithThreeOrMoreServices();
                DBManager.getWarrantyOrdersCount();
                DBManager.sumServices();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
