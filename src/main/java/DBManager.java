import java.sql.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;


public class DBManager {
    private static final DBManager instance = new DBManager();
    private static Connection connection;
    private static final String IP = "127.0.0.1";
    private static final String PORT = "3306";
    private static final String USER = "root";
    private static final String PASS = "root";
    private static final String DB_NAME = "s15_test3_carshop";

    private DBManager() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + IP + ":" + PORT + "/" + DB_NAME, USER, PASS);
            System.out.println("connection established");

        } catch (ClassNotFoundException e) {
            System.out.println("driver failed");
        } catch (SQLException e) {
            System.out.println("connection failed: " + e.getMessage());
        }
    }

    public static DBManager getInstance() {
        return instance;
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void saveDiagnostic(Diagnostic diagnostic) {
        try {
            Connection c = getConnection();
            PreparedStatement ps = c.prepareStatement("INSERT INTO diagnosticians (name, age) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, diagnostic.getName());
            ps.setInt(1, diagnostic.getAge());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            keys.next();
            diagnostic.setId(keys.getInt(1));
        } catch (SQLException e) {
            System.out.println("Error adding diagnostic " + e.getMessage());
        }
    }

    public int saveCar(Car car) {

        final String checkCarQuery = "SELECT * FROM cars WHERE license_plate = ?";
        final String addCarQuery = "INSERT INTO cars (license_plate, phone_number) VALUES (?, ?)";
        final String getCarIdQuery = "SELECT LAST_INSERT_ID()";

        try (Connection c = getConnection();
             PreparedStatement checkCarStmt = c.prepareStatement(checkCarQuery);
             PreparedStatement addCarStmt = c.prepareStatement(addCarQuery);
             PreparedStatement getCarIdStmt = c.prepareStatement(getCarIdQuery)) {

            checkCarStmt.setString(1, car.getRegNumber());
            ResultSet carResult = checkCarStmt.executeQuery();

            if (!carResult.next()) {
                // Car does not exist
                addCarStmt.setString(1, car.getRegNumber());
                addCarStmt.setString(2, car.getTelNumber());
                addCarStmt.executeUpdate();

                ResultSet carIdResult = getCarIdStmt.executeQuery();
                if (carIdResult.next()) {
                    int carId = carIdResult.getInt(1);
                    car.setId(carId);
                    return carId;
                } else {
                    throw new SQLException("Unable to retrieve car ID");
                }
            } else {
                // Car already exists
                return carResult.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1; // or throw a custom exception
        }
    }
    public static void saveWorker(Worker worker) {
        try {
            Connection c = getConnection();
            PreparedStatement ps = c.prepareStatement("INSERT INTO workers (name, age) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, worker.getName());
            ps.setInt(1, worker.getAge());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            keys.next();
            worker.setId(keys.getInt(1));
        } catch (SQLException e) {
            System.out.println("Error adding diagnostic " + e.getMessage());
        }
    }
    public static int getOrdersCountLast30Days() {
        int count = 0;
        try {
            Connection c = getConnection();
            LocalDateTime thirtyDaysAgo = LocalDateTime.now().minus(30, ChronoUnit.DAYS);
            PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) FROM orders WHERE date_created >= ?");
            ps.setObject(1, thirtyDaysAgo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error getting orders count: " + e.getMessage());
        }
        return count;
    }

    public static int getOrdersCountByServiceType(String serviceType) {
        int count = 0;
        try {
            Connection c = getConnection();
            PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) FROM orders WHERE service_type = ?");
            ps.setString(1, serviceType);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error getting orders count by service type: " + e.getMessage());
        }
        return count;
    }

    public static String getTopDiagnosticName() {
        String name = null;
        try {
            Connection c = getConnection();
            PreparedStatement ps = c.prepareStatement("SELECT name FROM diagnosticians ORDER BY diagnostics_count DESC LIMIT 1");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                name = rs.getString("name");
            }
        } catch (SQLException e) {
            System.out.println("Error getting top diagnostic name: " + e.getMessage());
        }
        return name;
    }

    public static List<String> getVehiclesWithThreeOrMoreServices() {
        List<String> regNumbers = new ArrayList<>();
        try {
            Connection c = getConnection();
            PreparedStatement ps = c.prepareStatement("SELECT DISTINCT reg_number FROM orders GROUP BY reg_number HAVING COUNT(*) >= 3");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String regNumber = rs.getString("reg_number");
                regNumbers.add(regNumber);
            }
        } catch (SQLException e) {
            System.out.println("Error getting vehicles with three or more services: " + e.getMessage());
        }
        return regNumbers;
    }

    public static int getWarrantyOrdersCount() {
        int count = 0;
        try {
            Connection c = getConnection();
            PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) FROM orders WHERE order_date >= DATE_SUB(NOW(), INTERVAL 30 DAY)");
            ResultSet rs = ps.executeQuery();
            rs.next();
            count = rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("Error counting orders: " + e.getMessage());
        }
        return count;
    }

    public static int countRepairs() {
        int count = 0;
        try {
            Connection c = getConnection();
            PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) FROM orders WHERE service_type = 'REMONT'");
            ResultSet rs = ps.executeQuery();
            rs.next();
            count = rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("Error counting repairs: " + e.getMessage());
        }
        return count;
    }

    public static int countMaintenance() {
        int count = 0;
        try {
            Connection c = getConnection();
            PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) FROM orders WHERE service_type = 'PODRYJKA'");
            ResultSet rs = ps.executeQuery();
            rs.next();
            count = rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("Error counting maintenance: " + e.getMessage());
        }
        return count;
    }

    public static String busiestDiagnostic() {
        String name = "";
        try {
            Connection c = getConnection();
            PreparedStatement ps = c.prepareStatement("SELECT diagnosticians.name FROM diagnosticians JOIN orders ON diagnosticians.id = orders.diagnostic_id GROUP BY diagnosticians.name ORDER BY COUNT(*) DESC LIMIT 1");
            ResultSet rs = ps.executeQuery();
            rs.next();
            name = rs.getString(1);
        } catch (SQLException e) {
            System.out.println("Error finding busiest diagnostic: " + e.getMessage());
        }
        return name;
    }

    public static List<String> carsWithServices(int count) {
        List<String> cars = new ArrayList<>();
        try {
            Connection c = getConnection();
            PreparedStatement ps = c.prepareStatement("SELECT cars.registration_number FROM cars JOIN orders ON cars.id = orders.car_id GROUP BY cars.registration_number HAVING COUNT(*) >= ?");
            ps.setInt(1, count);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                cars.add(rs.getString(1));
            }
        } catch (SQLException e) {
            System.out.println("Error finding cars with services: " + e.getMessage());
        }
        return cars;
    }

    public static int countWarrantyOrders() {
        int count = 0;
        try {
            Connection c = getConnection();
            PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) FROM orders JOIN cars ON orders.car_id = cars.id WHERE cars.warranty = 1");
            ResultSet rs = ps.executeQuery();
            rs.next();
            count = rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("Error counting warranty orders: " + e.getMessage());
        }
        return count;
    }

    public static double sumServices() {
        double sum = 0;
        try {
            Connection c = getConnection();
            PreparedStatement ps = c.prepareStatement("SELECT SUM(CASE WHEN cars.warranty = 1 THEN 0 ELSE services.price END) FROM orders JOIN services ON orders.service_id = services.id JOIN cars ON orders.car_id = cars.id");
            ResultSet rs = ps.executeQuery();
            rs.next();
            sum = rs.getDouble(1);
        } catch (SQLException e) {
            System.out.println("Error calculating sum of services: " + e.getMessage());
        }
        return sum;
    }
}