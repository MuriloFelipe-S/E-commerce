package starter.enumCategorias;

public enum SubCategory {

    //HARDWARE
    PROCESSADOR(ProductCategory.HARDWARE),
    PLACA_MAE(ProductCategory.HARDWARE),
    MEMORIA_RAM(ProductCategory.HARDWARE),
    PLACA_DE_VIDEO(ProductCategory.HARDWARE),
    SSD(ProductCategory.HARDWARE),
    HD(ProductCategory.HARDWARE),
    FONTE(ProductCategory.HARDWARE),
    COOLER(ProductCategory.HARDWARE),
    GABINETE(ProductCategory.HARDWARE),

    //PERIFERICOS
    TECLADO(ProductCategory.PERIFERICOS),
    MOUSE(ProductCategory.PERIFERICOS),
    HEADSET(ProductCategory.PERIFERICOS),
    WEBCAM(ProductCategory.PERIFERICOS),
    MICROFONE(ProductCategory.PERIFERICOS),
    CADEIRA(ProductCategory.PERIFERICOS),

    //MONITOR
    MONITOR(ProductCategory.MONITOR),
    MONITOR_CURVO(ProductCategory.MONITOR),
    MONITOR_4K(ProductCategory.MONITOR),
    MONITOR_ULTRAWIDE(ProductCategory.MONITOR),

    //NOTEBOOK
    NOTEBOOK(ProductCategory.NOTEBOOK),
    NOTEBOOK_GAMER(ProductCategory.NOTEBOOK),

    //SMARTPHONE
    SMARTPHONE(ProductCategory.SMARTPHONE),
    SAMSUNG(ProductCategory.SMARTPHONE),
    APPLE(ProductCategory.SMARTPHONE),
    XIAOMI(ProductCategory.SMARTPHONE),
    MOTOROLA(ProductCategory.SMARTPHONE),
    LG(ProductCategory.SMARTPHONE),
    ASUS(ProductCategory.SMARTPHONE),

    //GAMES
    JOGOS_PC(ProductCategory.GAMES),
    JOGOS_PLAYSTATION(ProductCategory.GAMES),
    JOGOS_XBOX(ProductCategory.GAMES),
    JOGOS_NINTENDO(ProductCategory.GAMES),
    ACESSORIOS_GAMES(ProductCategory.GAMES),

    //REDES
    ROTEADOR(ProductCategory.REDES),
    SWITCH(ProductCategory.REDES),
    REPEATER(ProductCategory.REDES),
    PONTO_DE_ACESSO(ProductCategory.REDES),
    CABO_REDE(ProductCategory.REDES);


    private final  ProductCategory categoria;

    SubCategory(ProductCategory categoria) {
        this.categoria = categoria;
    }

    public ProductCategory getCategoria() {
        return categoria;
    }

}
