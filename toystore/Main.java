package toystore;

import java.io.IOException;

public class Main {
    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        Controller controller = new Controller();
        System.out.println("Игрушки доступные к розыгрышу:");
        controller.CSVtoArray();
        System.out.println("Хотите добавить еще игрушки? Да/Нет");
        controller.qustionAdd();
        
    }
    
}