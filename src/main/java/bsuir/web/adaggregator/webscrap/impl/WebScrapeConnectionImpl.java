package bsuir.web.adaggregator.webscrap.impl;

import bsuir.web.adaggregator.webscrap.WebScrapeConnection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.util.Base64;

public class WebScrapeConnectionImpl implements WebScrapeConnection {

    private final String url;
    private final String username;
    private final String password;
    private final String proxyAddress;
    private final int proxyPort;

    public WebScrapeConnectionImpl(String url, WebScrapeConnectionImpl webScrapeConnection) {
        this(
            url,
            webScrapeConnection.username,
            webScrapeConnection.password,
            webScrapeConnection.proxyAddress,
            webScrapeConnection.proxyPort
        );
    }
    public WebScrapeConnectionImpl(String url, String username, String password, String proxyAddress, int proxyPort) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.proxyAddress = proxyAddress;
        this.proxyPort = proxyPort;
    }

    @Override
    public Document getDocument() throws IOException {
        disableTunnellingAuth();
        setAuthenticator();
        String authHeader = String.format(
            "Basic %s",
            Base64.getEncoder().encodeToString(String.format("%s:%s", username, password).getBytes())
        );
        Document document = Jsoup.connect(url)
            .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyAddress, proxyPort)))
            .header("Authorization", authHeader)
            .header("Proxy-Authorization", authHeader)
            .get();
        enableTunnelingAuth();
        return document;
    }

    private void disableTunnellingAuth() {
        System.setProperty("jdk.http.auth.tunneling.disabledSchemes", "false");
        System.setProperty("jdk.http.auth.proxying.disabledSchemes", "false");
    }

    private void enableTunnelingAuth() {
        System.setProperty("jdk.http.auth.tunneling.disabledSchemes", "true");
        System.setProperty("jdk.http.auth.proxying.disabledSchemes", "true");
    }

    private void setAuthenticator() {
        Authenticator.setDefault(
            new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(
                        username,
                        password.toCharArray()
                    );
                }
            }
        );
    }
}
