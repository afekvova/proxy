package ua.lil.proxy;

public class ProxyMain {

    public ProxyMain() {
        System.out.println("LiL proxy");
    }

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        new ProxyMain();
        System.out.println("Proxy start in " + (System.currentTimeMillis() - startTime) + "ms");
    }
}
