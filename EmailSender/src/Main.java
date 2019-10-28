import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.*;

public class Main {

    public static void main(String[] args) throws MessagingException {
        String from = "test756392";         // GMail имя аккаунта (символы перед "@gmail.com")
        String pass = "f716D1dC";           // GMail пароль
        String to = "arizekaz@yandex.ru";// Адрес получателя целиком
        String subject = "Сообщение от автоматической системы."; // заголовок сообщения
        String body;                                        // сообщение

        // тестовые Хэш таблицы (ht_1 - состояние на вчера) (ht_2 - состояние на сегодня)
        HashMap<String,String> testHashMap1 = new HashMap<>();
        testHashMap1.put("URL1","html code 1");//(БЕЗ ИЗМЕНЕНИЙ) URL и html код страницы не изменились
        testHashMap1.put("URL2","html code 2");//(ИСЧЕЗЛА) URL и html код страницы были удалены в след списке
        testHashMap1.put("URL4","html code 4");//(ИЗМЕНИЛАСЬ) html код страницы изменился
        testHashMap1.put("URL10","html code 444");
        testHashMap1.put("URL666","sopostament");
        testHashMap1.put("URL100","staints");

        HashMap<String,String> testHashMap2 = new HashMap<>();
        testHashMap2.put("URL1","html code 1");//(БЕЗ ИЗМЕНЕНИЙ)  URL и html код страницы не изменились
        testHashMap2.put("URL3","html code 3");//(ПОЯВИЛАСЬ) URL и html код страницы были недавно созданы
        testHashMap2.put("URL4","htmlcode444");//(ИЗМЕНИЛАСЬ) html код страницы изменился
        testHashMap2.put("URL10","htmlcode444");
        testHashMap2.put("URL665","sopostament");
        testHashMap2.put("URL110","staints 114");

        //Формирование String сообщения из двух хеш-таблиц ht_1 ht_2
        body = getMessage(testHashMap1,testHashMap2);

        //Отправка сформированного сообщения <body> на адрес <to> с гугл аккаунта <from>
        sendFromGMail(from, pass, to, subject, body);
    }

    // формирование сообщения из полученных хэш таблиц.
    static String getMessage(HashMap<String,String> hashMap1,HashMap<String,String> hashMap2){


        ArrayList<String> newPages = new ArrayList<>();         // массив ПОЯВИВШИХСЯ URLов
        ArrayList<String> delitedPages = new ArrayList<>();     // массив УДАЛЁННЫХ
        ArrayList<String> changedPages = new ArrayList<>();     // массив ИЗМЕНЁННЫХ
        Set<String> keys = hashMap1.keySet();

        for (String key:keys){
            if (hashMap2.containsKey(key)){     //таблица 2 содержит аналогичный ключ из таблицы 2
                if (hashMap1.get(key).equals(hashMap2.get(key))){//html код одного URL НЕ совпадает в обоих таблицах
                    changedPages.add(key);
                }
                else {
                    delitedPages.add(key);
                }
            }
        }

        keys = hashMap2.keySet();

        for (String key:keys){
            if (!hashMap1.containsKey(key)){
                newPages.add(key);
            }
        }

        StringBuilder strBuilder = new StringBuilder();

        String messageText = "Здравствуйте, дорогая и.о. секретаря.\n" +
                "За последние сутки во вверенных Вам сайтах произошли следующие изменения:\n" +
                "Исчезли следующие страницы:\n";

        strBuilder.append(messageText);

        for (String str:delitedPages){
            strBuilder.append(str).append("\n");
        }
        strBuilder.append("\nПоявились следующие новые страницы:\n");
        for (String str:newPages){
            strBuilder.append(str).append("\n");
        }
        strBuilder.append("\nИзменились следующие страницы:\n");

        for (String str:changedPages){
            strBuilder.append(str).append("\n");
        }
        strBuilder.append("\nС уважением, автоматизированная система мониторинга.");
        return strBuilder.toString();
    }

    // Отправка сообщения
    private static void sendFromGMail(String from, String pass, String to, String subject, String body) throws MessagingException {
        Properties props = System.getProperties();
        String host = "smtp.gmail.com";

        props.put("mail.smtp.starttls.enable", "true");

        props.put("mail.smtp.ssl.trust", host);
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.password", pass);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");


        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);
        InternetAddress toAddress;
        Transport transport = null;
        try {
            message.setFrom(new InternetAddress(from));
            toAddress = new InternetAddress(to);
            message.addRecipient(Message.RecipientType.TO, toAddress);

            message.setSubject(subject);
            message.setText(body);

            transport = session.getTransport("smtp");
            transport.connect(host, from, pass);
            transport.sendMessage(message, message.getAllRecipients());
//            transport.close();
        } catch (MessagingException ae) {
            ae.printStackTrace();
        }finally {
            transport.close();
        }
    }

}
