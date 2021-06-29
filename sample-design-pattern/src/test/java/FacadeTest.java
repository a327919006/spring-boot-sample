import com.cn.boot.sample.design.pattern.facade.MessageFacade;
import com.cn.boot.sample.design.pattern.facade.MessageManager;
import org.junit.Test;

/**
 * 外观模式
 *
 * @author Chen Nan
 */
public class FacadeTest {

    @Test
    public void test() {
        MessageManager messageManager = new MessageFacade();
        messageManager.pushMessage();
    }
}
