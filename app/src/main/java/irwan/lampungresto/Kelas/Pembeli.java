package irwan.lampungresto.Kelas;

/**
 * Created by Glory on 30/09/2018.
 */

public class Pembeli {
    public String uid;
    public String displayName;
    public String token;
    public String last_login;
    public String check;
    public String phone;

    public Pembeli(String uid, String displayName, String token, String last_login, String check, String phone) {
        this.uid = uid;
        this.displayName = displayName;
        this.token = token;
        this.last_login = last_login;
        this.check = check;
        this.phone = phone;
    }
}
