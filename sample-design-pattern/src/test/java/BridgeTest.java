import com.cn.boot.sample.design.pattern.bridge.ApplePhone;
import com.cn.boot.sample.design.pattern.bridge.BlueColor;
import com.cn.boot.sample.design.pattern.bridge.MiPhone;
import com.cn.boot.sample.design.pattern.bridge.RedColor;
import org.junit.Test;

/**
 * 桥接模式
 *
 * @author Chen Nan
 */
public class BridgeTest {

    @Test
    public void test() {
        ApplePhone applePhone = new ApplePhone(new RedColor());
        applePhone.run();

        MiPhone miPhone = new MiPhone(new BlueColor());
        miPhone.run();
    }
}
