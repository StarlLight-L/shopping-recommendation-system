package com.example.backend.config.alipay;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 阿里支付公共参数配置类(老版本)
 * @author
 * @date 2021-6-7 10:56:55
 */
@Configuration
@Slf4j
public class AlipayConfig {
    public static final String APP_ID = "9021000126665111";
    public static final String APP_PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDMHg6AkFigSk5MIBA7iwiWCjEhGUwvL7Fpj2vPv5FIPdDoI+JkcciHk8aNLtl1pM9hzLnXRJ1oHzpNGNJLG1XwlHXlYyLdg1zdpndEWJMfDT6KeabTdCIyX61jWhtjWSswayOsge4Zw/EP8Eej7aPgd5k27kOV5ZWcXFMiQKzmlMYo56f/SlX2iBqWu4E0a6XWoYFvDohr9YZHo9YPByeSJl2/nf933ReA/+poI/dHbgGWAjHJqv3ZdlFoHTMmdMhTj64w/1xFkPr5oVYn/j5JmIwxN3prs12sovQZKbwnTdhZaHT+cFI3bxrU0p/9iL9Sd4PBWuOIMukZMvGBnhZpAgMBAAECggEAaEvnXxvinSy6qz0q7jHRllDo35+8hNPa1DRbg1DAXGhxqVxEnsxhCZ2hs5w/s1QePzvLrco6n+xs7t4C/iuekwpY33fzZbpspjkb8glMS26KCVm1u5A4tQKcZwqwuefjIJ9uIARX9ERhlaJSCNoxhU23NREz6ZDhDH9EX1kqv7pVjXakBJNQifMso6DnBH2PGci4qnqFLsALCoam8qzUkyMMnBoy4yZfoVu/bCFamDnuxjauZtfmu7QFk/fzkCa5X8fmiBS73qboUF/J1HtByshzj6LqyIKxZODnE4GL1ms/Gwd6TqHaqDJqgp9LtCXseMCYYO/ugHaGU7j+tHlscQKBgQDowa/tZkPhXcvAEZ1yyqvirhbQSYbOSEMfoo+HuUZiB4z8rYCWQw3wdoW3ArIAR+vj6K+oFodv1SokfCfikqGtZ8d9uZdnIod0ynx5o1v2vwTGBI2k7z/fFjcX9dvizh4rOH9i7cButqpoxvtIOierPTOULjyRXKbvsF93DrNc/QKBgQDggDjjIOxsly2sy1xm6dcY5juE+UIKQxeYoSrkg4QnijBvEyX9C6u2jOTep7JEWdejSInAUz+Va4Dv/ESj7W04Lmledw9e2HrGG4LvrH41mz2vEkMtSmHKOmQ3KyaHhtzu3Ry9KWLTyuoOxPcWyXmVMqa8Vd+465cs6Qqf1AMQ3QKBgQCRch82cTYvyUX+MsO5rM42K2VUKO4VT3CqfxHD1VIXBfLjHqSAwUwfnGC4WPXUWR0d4RSLK/DcQE9CV1cxLUHcs0Ik3Wmv6BLPN40LEbrw+LLJ7qR4kYzPIfmxflA2bWjKx1ZLApjCVmEsclMp8jVBu0/U5AOzLjufAg7Vvhj1HQKBgCUgT0gQa8jRf3Yx80f5p96wRGuLJQa5ba+KB6Pes/4t9jCIRCICyhTYXAkORobTLLwOBOnPWpaFPr9xQpCxdPxzgG1k1x/iQPt+FTs5lW2cDKllJcLZqC5fVa9h7W9AOE0wN854h6VqyyZCIHKeQwMPa09+DA9ytn9w0pb34sHpAoGBAMFterhyDe3I6Kulgf804zdU9FrgPhFRFhkGlDiU6Mh/bIhV4Q5XpdSro+vlYz8dKuhKkA4VOcwoVQ+Yfp92Pc/2I+u9mJRByxu/gFKYt8vPoT+IwCoLhQSAJ9ET5xueJ3azgYr65JjAfsSlcKciFiRVevNdI0wNuJnAal2CS/AF";
    public static final String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAs7nYEBtpf4N7k6S6UuVcw8prQevqJjEIwzIFGgGRVbHSMs1SjwOr/XPBST5YbA9A7u6vyznp4yczRrgul/bl0HlejQmzqNLHg3pL4Lra6RVJ7X/IPPSoRe6bkorCMOKWpV6LeCFCGKMmggtQpuabBnAXx2G6T6be7BU2xW2CZUw+xi3+eJImA7SZ+JuFo765RX3V7xOWQzty+qY87iimjr1kpq0BSHvXC6lbB52SHqUdkV08tq4HDuYcRa8ZyGmhFfN1SUTDlS6u3kWvN/ywNPM9LkTgToDHBMhBuTVCt8ZDB2BBDu67bvCXCUPSQXMLJcyzUSkgIOtKi9YfvzHBAQIDAQAB";
    public static final String GATEWAY = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";
    public static final String CHARSET = "UTF-8";
    public static final String FORMAT = "json";
    public static final String SIGN_TYPE = "RSA2";
    public static final String RETURN_URL = "http://localhost:5173/paymentresult";//还没写
    public static final String NOTIFY_URL = "http://afbaky.natappfree.cc/alipay/notify";

    @Bean
    public AlipayClient alipayClient() {
        return new DefaultAlipayClient(
                GATEWAY,
                APP_ID,
                APP_PRIVATE_KEY,
                FORMAT,
                CHARSET,
                ALIPAY_PUBLIC_KEY,
                SIGN_TYPE
        );
    }
}