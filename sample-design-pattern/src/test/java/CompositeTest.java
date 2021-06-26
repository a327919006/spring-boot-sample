import com.cn.boot.sample.design.pattern.composite.AbstractRoot;
import com.cn.boot.sample.design.pattern.composite.Folder;
import com.cn.boot.sample.design.pattern.composite.MyFile;
import org.junit.Test;

/**
 * 组合模式
 *
 * @author Chen Nan
 */
public class CompositeTest {

    @Test
    public void test() {
        //创造根文件夹
        AbstractRoot root1 = new Folder("C://");

        //建立子文件
        AbstractRoot desktop = new Folder("桌面");
        AbstractRoot myComputer = new Folder("我的电脑");

        //建立子文件
        AbstractRoot javaFile = new MyFile("HelloWorld.java");

        //建立文件夹关系
        root1.addFile(desktop);
        root1.addFile(myComputer);

        //建立文件关系
        myComputer.addFile(javaFile);

        //从0级开始展示，每下一级，多2条横线
        root1.display(0);

        //另外一个根
        AbstractRoot root2 = new Folder("D://");
        root2.display(0);
    }
}
