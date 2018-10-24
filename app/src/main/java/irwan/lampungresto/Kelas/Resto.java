package irwan.lampungresto.Kelas;

/**
 * Created by Glory on 30/09/2018.
 */

public class Resto {
    public String uid;
    public String displayName;
    public String token;
    public String last_login;
    public String check;
    public String phone;
    public String alamat;

    public Resto(String uid, String displayName, String token, String last_login, String check, String phone, String alamat) {
        this.uid = uid;
        this.displayName = displayName;
        this.token = token;
        this.last_login = last_login;
        this.check = check;
        this.phone = phone;
        this.alamat = alamat;
    }
}
