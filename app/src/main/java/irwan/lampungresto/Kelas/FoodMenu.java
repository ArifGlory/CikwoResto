package irwan.lampungresto.Kelas;

/**
 * Created by Glory on 01/10/2018.
 */

public class FoodMenu {

    public String namaMenu;
    public String harga;
    public String key;
    public String downloadUrl;

    public FoodMenu(){

    }

    public FoodMenu(String namaMenu, String harga, String key, String downloadUrl) {
        this.namaMenu = namaMenu;
        this.harga = harga;
        this.key = key;
        this.downloadUrl = downloadUrl;
    }
}
