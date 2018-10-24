package irwan.lampungresto.Kelas;

/**
 * Created by Glory on 02/10/2018.
 */

public class Resep {

    public String namaResep;
    public String deskripsi;
    public String detailResep;
    public String key;
    public String downloadUrl;


    public Resep(){

    }

    public Resep(String namaResep, String deskripsi, String key, String downloadUrl,String detailResep) {
        this.namaResep = namaResep;
        this.deskripsi = deskripsi;
        this.key = key;
        this.downloadUrl = downloadUrl;
        this.detailResep = detailResep;
    }
}
