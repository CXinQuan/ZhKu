package event;

/**
 * Created by 陈鑫权  on 2019/4/7.
 */

public class AddOrChangePhoneEvent {
    public AddOrChangePhoneEvent(String phone) {
        this.phone = phone;
    }

    String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
