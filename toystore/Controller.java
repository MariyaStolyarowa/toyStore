package toystore;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;


public class Controller {
    Queue<Toy> toysQueue = new ArrayDeque<>();
    private final String csvToys = "StorageToys.csv";
    private ArrayList<Toy> toysList;

    protected void run() throws IOException {
        dropChance();
        int numberOfPrizes = 10;
        for (int i = 1; i <= numberOfPrizes; i++) {
            Toy chosenToy = chooseToy();
            manageToy(chosenToy);
        }
        writeWinners();
        System.out.println();
        System.out.println("Розыгрыш завершён. Результаты сохранены в файле Winners.csv и выведелы в консоль.");
    }

    private void dropChance() {
        exportListToCSV();
    }

    public Toy chooseToy() {
        int SumWt = 0;
        ArrayList<Toy> selToys = new ArrayList<>();
        while (true) {
            for (Toy toy : toysList) {
                if (toysList.size() > 0) {
                    selToys.add(toy);
                    SumWt += toy.getDropChance(); //сумма весов
                }
            }
            if (selToys.size() == 0) {
                System.out.println("Игрушки для выдачи призов закончились!");
                return null;
            }
            // 2. Берем случайное значение от 0 до TotalWt
    // вес случайный от 0 до TotalWt
    int RndWt = new Random().nextInt(SumWt + 1);
    // 3. Ищем элемент, который попал под это значение
    SumWt=0; //текущая сумма весов
    for (Toy toy : selToys) {
        SumWt += toy.getDropChance(); //сумма весов от 0 до текущего элемента
        if (SumWt >= RndWt) {
            return toy;
        }
    }
    return null;
        }
    }
    
    protected void manageToy(Toy toy) {
        toy.setToyQuantity(toy.getToyQuantity() - 1);
        String info = '\n' +
                "Выбрана игрушка: " +
                toy.getToyName() +
                ". " +
                "Осталось таких игрушек: " +
                toy.getToyQuantity() +
                '\n';
        System.out.println(info);
        if (toy.getToyQuantity() <= 0) {
            toysList.remove(toy);
        }
        toysQueue.add(toy);
        exportListToCSV();
    }
    // Вопрос о добавлении игрушки
    public void qustionAdd() throws IOException{
        Scanner sc = new Scanner(System.in, "cp866");
        
        switch (sc.nextLine()) {
            case ("Да"): 
            addToy();
            System.out.println("Игрушки доступные к розыгрышу:");
            CSVtoArray();
            System.out.println("Хотите начать розыгрыш? Да/Нет");
            qustionLot();
            case ("Нет"):
            System.out.println("Хотите начать розыгрыш? Да/Нет");
            qustionLot();
            break;
            default:
            System.out.println("Некорректный ответ. Нужно ответить Да или Нет");
            qustionAdd();
        }
    }
// Вопрос о начале лотереи
public void qustionLot() throws IOException{
    Scanner sc = new Scanner(System.in, "cp866");
    
    switch (sc.nextLine()) {
        case ("Да"):
        run();
        case ("Нет"):
        System.out.println("Завершить работу программы?");
        qustionFinal();
        break;
        default:
        System.out.println("Некорректный ответ. Нужно ответить Да или Нет");
        qustionLot();
    }
}
// Вопрос о завершении работы
public void qustionFinal() throws IOException{
    try (Scanner sc = new Scanner(System.in, "cp866")) {
        switch (sc.nextLine()) {
            case ("Да"): 
            System.out.println("Завершение работы программы");
            break;
            case ("Нет"):
            System.out.println("Хотите добавить еще игрушки? Да/Нет");
            qustionAdd();
            break;
            default:
            System.out.println("Некорректный ответ. Нужно ответить Да или Нет");
            qustionFinal();
        }
    }
}
    private String makeStringForCSV(Toy toy) {
        return String.valueOf(toy.getToyId()) +
                ';' +
                toy.getDropChance() +
                ';' +
                toy.getToyName() +
                ';' +
                toy.getToyQuantity() +
                '\n';
    }

    protected void writeWinners() throws IOException {
        String csvWinners = "Winners.csv";
        File winCSV = new File(csvWinners);
        boolean fileCreated = winCSV.createNewFile();
        final Path path = Paths.get(csvWinners);
        if (!fileCreated && !winCSV.exists()) {
            throw new IOException("Unable to create a file at specified path.");
        }
        while (!toysQueue.isEmpty()) {
            Toy toy = toysQueue.poll();
            String str = makeStringForCSV(toy);
            try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8, StandardOpenOption.APPEND)) {
                writer.append(str);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected void exportListToCSV() {
        String str1 = makeStringForCSV(toysList.get(0));
        Path path = Paths.get(csvToys);
        try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            writer.append(str1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 1; i < toysList.size(); i++) {
            String str2 = makeStringForCSV(toysList.get(i));
            try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8, StandardOpenOption.APPEND)) {
                writer.append(str2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected void CSVtoArray() {
        toysList = new ArrayList<>();
        File csvFile = new File(csvToys);
        if (csvFile.isFile()) {
            BufferedReader csvReader;
            try {
                csvReader = new BufferedReader(new FileReader(csvToys));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            String row;
            while (true) {
                try {
                    if ((row = csvReader.readLine()) == null) break;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                String[] data = row.split(";");
                toysList.add(new Toy(Integer.parseInt(data[0]), Integer.parseInt(data[1]), data[2], Integer.parseInt(data[3])));
            }
            try {
                csvReader.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            for (Toy toy : toysList) {
                System.out.println(toy);
            }
        
        }
        
    }


    private Toy addToyInstance() {
        Scanner sc = new Scanner(System.in, "cp866");

        System.out.println("Введите id новой игрушки:");
        int id = Integer.parseInt(sc.nextLine());

        System.out.println("Введите частоту выпадения в % от 1 до 100:");
        int dropChance = Integer.parseInt(sc.nextLine());

        System.out.println("Введите название игрушки:");
        String name = sc.nextLine();

        System.out.println("Введите количество:");
        int quantity = Integer.parseInt(sc.nextLine());

        // sc.close();
        
        return new Toy(id,dropChance, name, quantity);
    }

    private void addToyToList(Toy toy) {
        toysList.add(toy);
        
    }

    private void addToyToCSV(Toy toy) {
        String str = makeStringForCSV(toy);
        try (Writer writer = Files.newBufferedWriter(Paths.get(csvToys), StandardCharsets.UTF_8, StandardOpenOption.APPEND)) {
            writer.append(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void addToy() throws IOException {
        Toy newToy = this.addToyInstance();
        this.addToyToCSV(newToy);
        this.addToyToList(newToy);
        System.out.println("Хотите добавить еще игрушки? Да/Нет");
        qustionAdd();
        
    }
    
}
