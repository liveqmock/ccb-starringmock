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
public class StarMock {

    private String ip;
    private int port;

    public StarMock(String ip, int port) {
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


    public static void main(String... argv) throws UnsupportedEncodingException {
        StarMock mock = new StarMock("127.0.0.1", 60010);

        String header = "1.0" +
                "123456789012345678" +
                "0000" +
                "1561070" +
                "999999999" +
                "123456789012" +
                "STARRING" +
                "AICQDE" +
                new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) +
                "MAC12345678901234567890123456789";


        String body = "02|1111|22|12345678901234567890123456789012|";

        String message = header + body;
        int len = message.getBytes("GBK").length;
        String strLen = "" + (len + 6);

        for (int i = strLen.length(); i < 6; i++) {
            strLen += " ";
        }

        byte[] recvbuf = mock.call((strLen + message).getBytes("GBK"));
        System.out.printf("服务器返回报文：%s\n", new String(recvbuf, "GBK"));
    }
}
