import com.cn.boot.sample.design.pattern.factory.Pay;
import com.cn.boot.sample.design.pattern.factory.method.AliPayFactory;
import com.cn.boot.sample.design.pattern.factory.method.PayFactory;
import com.cn.boot.sample.design.pattern.factory.method.WechatPayFactory;
import com.cn.boot.sample.design.pattern.factory.simple.SimplePayFactory;
import com.cn.boot.sample.design.pattern.singleton.SingletonHungry;
import com.cn.boot.sample.design.pattern.singleton.SingletonLazy;
import org.junit.Test;

/**
 * 设计模式测试类
 *
 * @author Chen Nan
 */
public class DesignPatternTest {

    @Test
    public void singleton() {
        SingletonLazy lazy = SingletonLazy.getInstance();
        lazy.business();

        SingletonHungry hungry = SingletonHungry.getInstance();
        hungry.business();
    }

    @Test
    public void factory() {
        Pay pay = SimplePayFactory.createPay(0);
        assert pay != null;
        pay.unifiedOrder();

        PayFactory aliPayFactory = new AliPayFactory();
        Pay alipay = aliPayFactory.createPay();
        alipay.unifiedOrder();

        PayFactory wechatPayFactory = new WechatPayFactory();
        Pay wechatPay = wechatPayFactory.createPay();
        wechatPay.unifiedOrder();
    }
}
