package io.github.untactorder.data;

/**
 * 고객
 * @author 유채민
 */
public class Customer {
    protected static String IP;
    protected static Integer PORT;
    protected static Integer ID;
    protected static String STATUS;
    protected static String PW;

    public synchronized static String getIp() {
        return IP;
    }

    public synchronized static Integer getPort() {
        return PORT;
    }

    public synchronized static Integer getId() {
        return ID;
    }

    public synchronized static String getStatus() {
        return STATUS;
    }

    public synchronized static String getPw() {
        return PW;
    }

    public synchronized static void setIp(String ip) {
        Customer.IP = ip;
    }

    public synchronized static void setPort(Integer port) {
        Customer.PORT = port;
    }

    public synchronized static void setId(Integer id) {
        Customer.ID = id;
    }

    public synchronized static void setStatus(String status) {
        Customer.STATUS = status;
    }

    public synchronized static void setPw(String pw) {
        Customer.PW = pw;
    }

    public synchronized static void reset() {
        IP = null; PORT = null; ID = null; STATUS = null; PW = null;
    }
}
