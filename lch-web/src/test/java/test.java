import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import utils.JwtUtils;

import java.io.File;
import java.io.FileInputStream;

/**
 * @ClassName :test
 * @Description :TODO
 * @Author :16388
 * @Date :2020/5/8 10:51
 * @Version :1.0
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = test.class)
public class test {

    @Test
    public void poiTest() {
        File file = new File("D:\\app\\WeChat\\WeChat Files\\wk18137271673\\FileStorage\\File\\2020-05\\2020-4-18-16-54-32-3023634015599-山东省生态环境厅-政务公开-受理公示-采集的数据-后羿采集器.xlsx");
        Workbook workbook=null;
        try {
            workbook=new XSSFWorkbook(new FileInputStream(file));
            int numberOfSheets = workbook.getNumberOfSheets();
            for (int i = 0; i <numberOfSheets ; i++) {
                Sheet sheet = workbook.getSheetAt(i);
                int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();
                for (int j = 0; j <physicalNumberOfRows ; j++) {
                    if (j == 0) {
                        continue;//跳过标题行
                    }
                    Row row = sheet.getRow(j);
                    if (row == null) {
                        continue;//防止数据中间有空行
                    }
                    int physicalNumberOfCells = row.getPhysicalNumberOfCells();
                    for (int k = 0; k <physicalNumberOfCells ; k++) {
                        Cell cell = row.getCell(k);
                        System.out.println(cell.getCellStyle()+"---" +cell.getStringCellValue());
                    }
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void hash() {
        //加密方式
        String hashAlgorithmName = "MD5";
        //加密次数
        int hashInteractions = 2;

        ByteSource salt = ByteSource.Util.bytes("wangk");

        //原密码
        String pwd = "123456";
        String simpleHash = new SimpleHash(hashAlgorithmName, pwd, salt,hashInteractions).toHex();
        System.out.println(simpleHash);//7bda22baa2d55a0a380e8ff45df9e038

    }

    @Test
    public void jwt() {
        String wangk = JwtUtils.createToekn("wangk", System.currentTimeMillis() + "");
    }


}
