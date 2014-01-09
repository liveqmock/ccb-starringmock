import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: zhanrui
 * Date: 13-11-25
 * Time: 上午11:49
 */
public class SocketClient4KFQ {

    private String ip;
    private int port;
    private String txnCode;


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

    private String getTxnFile() {
        String txnFile = "txn" + this.txnCode +".txt";
        InputStream usageStream = getClass().getClassLoader().getResourceAsStream(txnFile);

        if (usageStream == null) {
            System.err.println("交易报文文件不存在：" + txnFile);
            System.exit(1);
        }

        StringBuffer  result = new StringBuffer();
        BufferedReader buf = null;
        try {
            buf = new BufferedReader(new InputStreamReader(usageStream));
            String line;

            while ((line = buf.readLine()) != null) {
                result.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (buf != null) {
                try {
                    buf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result.toString();
    }

    //1561070 入资登记预交易

    private String getRequestMsg4010() {
        String header = "1.0" +
                "123456789012345678" +
                "0000" +
                "1534010" +
                "999999999" +
                "123456789012" +
                "FIS154" +  //userid 6
//                "STAR01" +  //userid 6
                "FISKFQ " +  //appid 6
                new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) +
                "MAC12345678901234567890123456789";

        /**
         * 缴款书样式编码
         票号
         全票面校验码
         收款金额
         年度
         */
        String body = "11|" +  //缴款书样式编码
                "123456|" +  //票号
                "33|" +   //全票面校验码
                "4.44|" +  //收款金额
                "2014|" + //年度
                "";
        //body="101|101000000201|600|162218|5|1||中国建设银行|37101986106051016701|2013|agent001|1234567890|131210000114|";
        body="101|101000000201|3489|600|2013|";
        return header + body;
    }

    private String getRequestMsg4011() {
        String header = "1.0" +
                "123456789012345678" +
                "0000" +
                "1534011" +
                "999999999" +
                "123456789012" +
                "FIS154" +  //userid 6
//                "STAR01" +  //userid 6
                "FISKFQ " +  //appid 6
                new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) +
                "MAC12345678901234567890123456789";

        String body = "11|" +  //缴款书样式编码
                "123456|" +  //票号
                "33|" +   //全票面校验码
                "4.44|" +  //收款金额
                "2014|" + //年度
                "";
        //body="101|101000000201|600|162218|5|1||中国建设银行|37101986106051016701|2013|agent001|1234567890|131210000114|";
        body="101|101000000201|600|162218|5|1||中国建设银行|37101986106051016701|2013|agent001|1234567890|131210000114|";
        //return header + body;
        return "1.022922             99991534011371986106371986106021fis154FISKFQ2013122716221894A7D655881E78DD                {5520D61B-6593-11E3-862B-EDF31243BFF4}|101|101000000201|600|162218|5|1||中国建设银行|37101986106051016701|2013|agent001|1234567890|131210000114|";
    }
    private String getRequestMsg4040() {
        return "1.023318             99991534040371986106371986106021fis154FISKFQ20131227165924F8318ECF4D8A034A                {3F072EB3-6593-11E3-862B-EDF31243BFF4}|101|101000000201|2013|";
    }
    private String getRequestMsg4070() {
        return "1.023318             99991534070371986106371986106021fis154FISKFQ20131227165924F8318ECF4D8A034A                {3F072EB3-6593-11E3-862B-EDF31243BFF4}|101|20131010|20141010|";
    }
    private String getRequestMsg4013() {
        return "1.024018             99991534013371986106371986106021fis154FISKFQ201312271739253B37EDB4AFE223E9                370211|101|101000000286|0286|00|001|测试001|002|测试002|600||371986106021|001|警察叔叔|中国建设银行|37101986106051016701|开发区财政局|中国建设银行|37101986106051016701|1||3|000001,项目名称000001,1,300|000002,项目名称000002,1,200|000003,项目名称000003,1,100|";
    }
    private String getRequestMsg4080() {
        return "1.041825             99991534080371986106371986106021fis154FISKFQ20140107084428D903C0DCB3FC7DF0                1|530004|20140106|20140106|";
    }



    public static void main(String... argv) throws UnsupportedEncodingException {
        SocketClient4KFQ client = new SocketClient4KFQ();
        System.out.println("" + argv.length);
        if (argv.length < 3) {
            System.out.println("命令参数示例：\n" +
                    "    MockClient -ip=127.0.0.1 -port=60004 -txn=1010");
            System.exit(1);
        }

        for (String arg : argv) {
            if (arg.startsWith("-ip=")) {
                client.ip = arg.substring(4);
                continue;
            }
            if (arg.startsWith("-port=")) {
                client.port = Integer.parseInt(arg.substring(6));
                continue;
            }
            if (arg.startsWith("-txn=")) {
                client.txnCode = arg.substring(5);
                continue;
            }
        }

        //client.getTxnFile();

        String message = client.getRequestMsg4010();
//        String message = client.getRequestMsg4011();
//        String message = client.getRequestMsg4013();
//        String message = client.getRequestMsg4040();
//        String message = client.getRequestMsg4070();
//        String message = client.getRequestMsg4080();
        System.out.printf("===请求报文:%s\n", message);

        int len = message.getBytes("GBK").length;
        String strLen = "" + (len + 6);
        for (int i = strLen.length(); i < 6; i++) {
            strLen += " ";
        }
        byte[] recvbuf = client.call((strLen + message).getBytes("GBK"));
        System.out.printf("===服务器返回报文:%s\n", new String(recvbuf, "GBK"));


/*
        message = client.getRequestMsg1070();

        len = message.getBytes("GBK").length;
        strLen = "" + (len + 6);
        for (int i = strLen.length(); i < 6; i++) {
            strLen += " ";
        }
        recvbuf = client.call((strLen + message).getBytes("GBK"));
        System.out.printf("服务器返回报文：%s\n", new String(recvbuf, "GBK"));
*/
    }
}
