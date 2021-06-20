import com.cn.boot.sample.design.pattern.factory.abstrac.FactoryProducer;
import com.cn.boot.sample.design.pattern.factory.abstrac.OrderFactory;
import com.cn.boot.sample.design.pattern.factory.method.AliPayFactory;
import com.cn.boot.sample.design.pattern.factory.method.Pay;
import com.cn.boot.sample.design.pattern.factory.method.PayFactory;
import com.cn.boot.sample.design.pattern.factory.method.WechatPayFactory;
import com.cn.boot.sample.design.pattern.factory.simple.SimplePayFactory;
import org.junit.Test;

/**
 * 工厂模式
 *
 * @author Chen Nan
 */
public class FactoryTest {

    /**
     * 简单工厂模式
     */
    @Test
    public void simple() {
        Pay pay = SimplePayFactory.createPay(0);
        assert pay != null;
        pay.unifiedOrder();
    }

    /**
     * 工厂方法模式
     */
    @Test
    public void method() {
        PayFactory aliPayFactory = new AliPayFactory();
        Pay alipay = aliPayFactory.createPay();
        alipay.unifiedOrder();

        PayFactory wechatPayFactory = new WechatPayFactory();
        Pay wechatPay = wechatPayFactory.createPay();
        wechatPay.unifiedOrder();
    }

    /**
     * 抽象工厂模式
     */
    @Test
    public void abstrac() {
        OrderFactory orderFactory = FactoryProducer.getOrderFactory(0);
        assert orderFactory != null;
        orderFactory.createPay().pay();
        orderFactory.createRefund().refund();
    }
}
