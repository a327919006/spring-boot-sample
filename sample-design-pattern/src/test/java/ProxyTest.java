import com.cn.boot.sample.design.pattern.proxy.PhoneSell;
import com.cn.boot.sample.design.pattern.proxy.PhoneSellProxy;
import org.junit.Test;

/**
 * 代理模式
 *
 * @author Chen Nan
 */
public class ProxyTest {

    @Test
    public void test() {
        PhoneSell phoneSell = new PhoneSellProxy();
        phoneSell.sell();
    }
}
