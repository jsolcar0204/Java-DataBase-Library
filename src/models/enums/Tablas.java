package src.models.enums;

public enum Tablas {
    DESARROLLADOR("desarrollador", 2),
    EDITOR("editor", 2),
    GENERO("genero", 2),
    PLATAFORMA("plataforma", 1),
    VIDEOJUEGO("videojuego", 5),
    VIDEOJUEGO_GENERO("videojuego_genero", 2),
    VIDEOJUEGO_PLATAFORMA("videojuego_plataforma", 2);

    private String tableName;
    private int numColumnas;

    Tablas(String tableName, int numColumnas) {
        this.tableName = tableName;
        this.numColumnas = numColumnas;
    }

    @Override
    public String toString() {
        return tableName;
    }

    public String getTableName() {
        return tableName;
    }

    public int getNumColumnas() {
        return numColumnas;
    }
}