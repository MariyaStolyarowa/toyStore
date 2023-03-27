package toystore;

import java.util.StringJoiner;

public class Toy {
    private final int toyId;
    private final int dropChance;
    private final String toyName;
    private int toyQuantity;


    public Toy(int toyId, int dropChance, String toyName, int toyQuantity) {
        this.toyId = toyId;
        this.dropChance = dropChance;
        this.toyName = toyName;
        this.toyQuantity = toyQuantity;
    }

    public String getInfo() {
        return new StringJoiner(", ", this.getClass().getSimpleName() + " [", "]")
                .add("id игрушки = " + toyId)
                .add("Частота выпадения = " + dropChance)
                .add("Название игрушки = " + toyName)
                .add(" Количество = " + toyQuantity)
                .toString();
    }

    @Override
    public String toString() {
        return this.getInfo();
    }

    public int getDropChance() {
        return dropChance;
    }

    public int getToyId() {
        return toyId;
    }

    public String getToyName() {
        return toyName;
    }

    public int getToyQuantity() {
        return toyQuantity;
    }

    public void setToyQuantity(int toyQuantity) {
        this.toyQuantity = toyQuantity;
    }
}

