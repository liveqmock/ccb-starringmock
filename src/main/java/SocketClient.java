import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: zhanrui
 * Date: 13-11-25
 * Time: 上午11:49
 */
public class SocketClient {

    private String ip;
    private int port;

    public SocketClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public byte[] call(byte[] sendbuf) {
        Socket socket = null;
        OutputStream os = null;
        byte[] recvbuf = null;
        try {
            socket = new Socket(ip, port);
            socket.setSoTimeout(60000);

            os = socket.getOutputStream();
            os.write(sendbuf);
            os.flush();

            InputStream is = socket.getInputStream();
            recvbuf = new byte[6];
            int readNum = is.read(recvbuf);
            if (readNum < 6) {
                throw new RuntimeException("读取报文长度出错！");
            }
            int msgLen = Integer.parseInt(new String(recvbuf).trim());
            recvbuf = new byte[msgLen];

            readNum = is.read(recvbuf);
            if (readNum != msgLen - 6) {
                throw new RuntimeException("读取报文长度出错！");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                assert os != null;
                os.close();
                socket.close();
            } catch (IOException e) {
                //
            }
        }
        return recvbuf;
    }

    //1561070 入资登记预交易
    private String getRequestMsg1070(){
        String header = "1.0" +
                "123456789012345678" +
                "0000" +
                "1561070" +
                "999999999" +
                "123456789012" +
                "STAR01" +
                "AICQDE" +
                new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) +
                "MAC12345678901234567890123456789";
        String body = "02|1111|22|12345678901234567890123456789012|";
        return header+body;
    }

    //1561010  入资登记
    private String getRequestMsg1010(){
        String header = "1.0" +
                "123456789012345678" +
                "0000" +
                "1561010" +
                "999999999" +
                "123456789012" +
                "STAR01" +  //userid 6
                "AICQDE" +  //appid 6
                new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) +
                "MAC12345678901234567890123456789";
        String body = "02|" +  //银行代码
                "1111|" +  //地区码
                "22|" +   //工商局编号
                "12345678901234567890123456789012|" +  //预登记号	32	CHAR	右补空格
                "工商局名称|" + //工商局名称	32	CHAR	右补空格
                "8010000011111111|" + //入资帐号	22	CHAR	右补空格
                "1234567.89|" + //入资帐户余额	21.2	CHAR	左补空格
                "企业预核准名称|" + //企业预核准名称	72	CHAR	右补空格
                "入资银行名称|" + //入资银行名称	50	CHAR	右补空格
                "3|" + //出资人循环个数	4	CHAR	右补空格
                "张三1,80101111222,123.45,20131212,出资人开户行,370101199707070030|" + //出资人名称,出资人帐号,出资金额,入资日期,出资人开户行,证件号
                "张三2,80101111222,123.45,20131212,出资人开户行,370101199707070030|" + //出资人名称,出资人帐号,出资金额,入资日期,出资人开户行,证件号
                "张三3,80101111222,123.45,20131212,出资人开户行,370101199707070030|" + //出资人名称,出资人帐号,出资金额,入资日期,出资人开户行,证件号
                "";
        return header+body;
    }

    //1561071 增资登记预交易
    private String getRequestMsg1071(){
        String header = "1.0" +
                "123456789012345678" +
                "0000" +
                "1561070" +
                "999999999" +
                "123456789012" +
                "STAR01" +
                "AICQDE" +
                new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) +
                "MAC12345678901234567890123456789";
        String body = "02|1111|22|12345678901234567890123456789012|";
        return header+body;
    }

    //1561011  增资登记
    private String getRequestMsg1011(){
        String header = "1.0" +
                "123456789012345678" +
                "0000" +
                "1561010" +
                "999999999" +
                "123456789012" +
                "STAR01" +  //userid 6
                "AICQDE" +  //appid 6
                new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) +
                "MAC12345678901234567890123456789";
        String body = "02|" +  //银行代码
                "1111|" +  //地区码
                "22|" +   //工商局编号
                "12345678901234567890123456789012|" +  //预登记号	32	CHAR	右补空格
                "工商局名称|" + //工商局名称	32	CHAR	右补空格
                "8010000011111111|" + //入资帐号	22	CHAR	右补空格
                "1234567.89|" + //入资帐户余额	21.2	CHAR	左补空格
                "企业预核准名称|" + //企业预核准名称	72	CHAR	右补空格
                "入资银行名称|" + //入资银行名称	50	CHAR	右补空格
                "3|" + //出资人循环个数	4	CHAR	右补空格
                "张三1,80101111222,123.45,20131212,出资人开户行,370101199707070030|" + //出资人名称,出资人帐号,出资金额,入资日期,出资人开户行,证件号
                "张三2,80101111222,123.45,20131212,出资人开户行,370101199707070030|" + //出资人名称,出资人帐号,出资金额,入资日期,出资人开户行,证件号
                "张三3,80101111222,123.45,20131212,出资人开户行,370101199707070030|" + //出资人名称,出资人帐号,出资金额,入资日期,出资人开户行,证件号
                "";
        return header+body;
    }

    public static void main(String... argv) throws UnsupportedEncodingException {
        System.out.println("" + argv.length);
        if (argv.length <= 3) {
            System.out.println("命令参数示例：\n" +
                    "    MockClient -ip=127.0.0.1 -port=60004 -txn=1010");
            System.exit(1);
        }

        SocketClient mock = new SocketClient("127.0.0.1", 60004);

        String message = mock.getRequestMsg1010();

        int len = message.getBytes("GBK").length;
        String strLen = "" + (len + 6);
        for (int i = strLen.length(); i < 6; i++) {
            strLen += " ";
        }
        byte[] recvbuf = mock.call((strLen + message).getBytes("GBK"));
        System.out.printf("服务器返回报文：%s\n", new String(recvbuf, "GBK"));
    }
}
