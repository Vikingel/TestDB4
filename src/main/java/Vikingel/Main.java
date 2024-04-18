package Vikingel;

import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {
        // example0();
        //example1();
        //example2();
        // exampleInsert1();
        // exampleInsert2();
        //exampleUpdate1();
        example7();
    }

    private static void exampleUpdate1() throws SQLException {
        Connection conn = connectToDB();
        //выводим список пользователей
        ArrayList<User> users = loadUsers(conn);
        System.out.println("получен список из " + users.size() + " юзеров");
        users.forEach(System.out::println);
        //предоставляем возможность выбрать пользователя из списка
        User selectedUser = selectUser(users);
        //просим ввести новую ФИО
        changeFIO(selectedUser);
        //сохраняем изменение в БД
        updateUserInDB(selectedUser, conn);
        //выводим список пользователей
        System.out.println("Теперь список из " + users.size() + " юзеров");
        users.forEach(System.out::println);
        conn.close();
    }


    private static void changeFIO(User selectedUser) {
        if (selectedUser != null) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("введите новую ФИО");
            String newFIO = scanner.nextLine();
            selectedUser.setFio(newFIO);
        }
    }

    private static User selectUser(ArrayList<User> users) {
        System.out.println("выберете пользователя для редактирования");
        System.out.println("введите id");
        Scanner scanner = new Scanner(System.in);
        int id = scanner.nextInt();
        //User selected = null;
        for (User u : users) {
            if (u.id == id)
                return u;
        }
        System.out.println("такого id нет в списке");
        return null;
    }

    private static void exampleInsert2() throws SQLException {
        Connection conn = connectToDB();
        //запросим пользователя в консоли ввести Фамилию и деньги
        User user = inputFromConsole();
        //получим пользователя, сохраним его в таблице
        saveUserToDB(user, conn);
        //выведем список всех пользователей из таблицы
        ArrayList<User> users = loadUsers(conn);
        System.out.println("получен список из " + users.size() + " юзеров");
        users.forEach(System.out::println);

        conn.close();
    }

    public static User inputFromConsole() {
        int id = -1;
        Scanner scanner = new Scanner(System.in);
        System.out.println("введите ФИО и деньги");
        String name = scanner.next();
        double m = scanner.nextDouble();
        User user = new User(id, name, m);
        return user;
    }

    private static void example2() throws SQLException {
        Connection conn = connectToDB();
        ArrayList<User> users = loadUsers(conn);
        System.out.println("получен список из " + users.size() + " юзеров");
        users.forEach(System.out::println);

        conn.close();
    }


    private static void example1() throws SQLException {
        Connection conn = connectToDB();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM public.user");
        while (rs.next()) {
            int x = rs.getInt("id");
            String fio = rs.getString("fio");
            double m = rs.getDouble("money");
            System.out.println("получена строка: " + x + " " + fio + " " + m);
        }
        rs.close();
        st.close();
        conn.close();
    }

    public static void example0() {
        String url = "jdbc:postgresql://10.10.104.136:5432/test4";
        Properties props = new Properties();
        props.setProperty("user", "postgres");
        props.setProperty("password", "123");
        props.setProperty("ssl", "false");
        try {
            Connection conn = DriverManager.getConnection(url, props);
            System.out.println("удалось подключиться к БД test4");
            conn.close();
        } catch (SQLException e) {
            System.out.println("все пропало");
            e.printStackTrace();
        }
    }

    public static void exampleInsert1() throws SQLException {
        Connection conn = connectToDB();
        User user = new User(-1, "Пупкин", 123456);
        saveUserToDB(user, conn);
        ArrayList<User> users = loadUsers(conn);
        System.out.println("получен список из " + users.size() + " юзеров");
        users.forEach(System.out::println);
        conn.close();
    }

    public static Connection connectToDB() {
        String url = "jdbc:postgresql://10.10.104.136:5432/test4";
        Properties props = new Properties();
        props.setProperty("user", "postgres");
        props.setProperty("password", "123");
        props.setProperty("ssl", "false");
        try {
            Connection conn = DriverManager.getConnection(url, props);
            return conn;
        } catch (SQLException e) {
            System.out.println("все пропало");
            e.printStackTrace();
            return null;
        }
    }

    //следующие методы относятся по смыслу к DAO (обеспечивают связь модели и БД)
    public static ArrayList<User> loadUsers(Connection conn) throws SQLException {
        ArrayList<User> users = new ArrayList<>();
        Statement st = conn.createStatement();
        String s = "SELECT * FROM public.\"Triangle\"";

        ResultSet rs = st.executeQuery("SELECT * FROM public.user");
        while (rs.next()) {
            int x = rs.getInt("id");
            String fio = rs.getString("fio");
            double m = rs.getDouble("money");
            users.add(new User(x, fio, m));
        }
        rs.close();
        st.close();
        return users;
    }

    public static void saveUserToDB(User user, Connection conn) throws SQLException {
        PreparedStatement st = conn.prepareStatement("INSERT INTO public.\"user\"\n" +
                "(fio, \"money\")\n" +
                "VALUES(?, ?);");
        st.setString(1, user.fio);
        st.setDouble(2, user.money);
        int countInserted = st.executeUpdate();
        System.out.println(countInserted + " строк добавлено");
        st.close();
    }

    public static void updateUserInDB(User user, Connection conn) throws SQLException {
        if (user != null && conn != null) {
            PreparedStatement st = conn.prepareStatement("UPDATE public.\"user\"\n" +
                    "SET fio=?, \"money\"=?\n" +
                    "WHERE id=?;");
            st.setString(1, user.fio);
            st.setDouble(2, user.money);
            st.setInt(3, user.id);
            int rowsUpdated = st.executeUpdate();
            System.out.println(rowsUpdated + " строк изменено");
            st.close();
        }
    }

    public static ArrayList<Message> loadMessages(Connection conn) throws SQLException {
        ArrayList<Message> messages = new ArrayList<>();
        Statement st = conn.createStatement();
        String s = "SELECT * FROM public.message";

        ResultSet rs = st.executeQuery("SELECT * FROM public.message");
        while (rs.next()) {
            int x = rs.getInt("id");
            int xs = rs.getInt("id");
            int xt = rs.getInt("id");
            String mes = rs.getString("text");
            messages.add(new Message(x, mes, xs, xt));
        }
        rs.close();
        st.close();
        return messages;
    }

    private static void example7() throws SQLException {
        Scanner scan = new Scanner(System.in);
        Connection conn = connectToDB();
        ArrayList<Message> messages = loadMessages(conn);
        System.out.println("получен список из " + messages.size() + " сообщений");
        messages.forEach(System.out::println);
        System.out.println("Введите, от кого сообщение");
        int from = scan.nextInt();
        System.out.println("Введите, кому сообщение");
        int to = scan.nextInt();
        System.out.println("Введите текст сообщения");
        String m = scan.next();
        Message message = new Message(-1, m, from, to);
        saveMassageToDB(message, conn);
        messages = loadMessages(conn);
        System.out.println("получен список из " + messages.size() + " сообщений");
        messages.forEach(System.out::println);
        conn.close();
    }

    public static void saveMassageToDB(Message message, Connection conn) throws SQLException {
        PreparedStatement st = conn.prepareStatement("INSERT INTO public.message\n" +
                "(\"text\",sender_id, target_id)\n" +
                "VALUES(?, ?, ?);");
        st.setString(1, message.mes);
        st.setInt(2, message.sender_id);
        st.setInt(3, message.target_id);
        int countInserted = st.executeUpdate();
        System.out.println(countInserted + " строк добавлено");
        st.close();
    }
    //INSERT INTO public.message
    //(id, "text", sender_id, target_id)
    //VALUES(0, '', 0, 0);
}