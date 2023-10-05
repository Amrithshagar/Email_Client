//200037X

//import libraries
import javax.mail.Message;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.*;
import javax.mail.internet.*;

public class Email_Client {

    public static void main(String[] args) throws IOException {
        bdayMail sendWishMail = new bdayMail();
        sendWishMail.wish();

        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("""
                    Enter option type:\s
                    1 - Adding a new recipient
                    2 - Sending an email
                    3 - Printing out all the recipients who have birthdays
                    4 - Printing out details of all the emails sent
                    5 - Printing out the number of recipient objects in the application
                    6 - To exit the application""");

            int option = scanner.nextInt();

            switch (option) {
                case 1 -> {
                    System.out.println("Input recipient details: ");
                    Scanner input = new Scanner(System.in);
                    String clientData = input.nextLine();
                    textHandling writeData = new textHandling();
                    writeData.textWrite(clientData);
                    String[] arr = clientData.split(": ");
                    String firstWord = arr[0];
                    if (Objects.equals(firstWord, "Official")) {
                        String[] arr1 = arr[1].split(",");
                        Official offRec = new Official(arr1[0], arr1[1], arr1[2]);

                    } else if (Objects.equals(firstWord, "Office_friend")) {
                        String[] arr1 = arr[1].split(",");
                        OfficialFriend offFriRec = new OfficialFriend(arr1[0], arr1[1], arr1[2]);

                    } else if (Objects.equals(firstWord, "Personal")) {
                        String[] arr1 = arr[1].split(",");
                        Personal perRec = new Personal(arr1[0], arr1[1], arr1[2], arr1[3]);
                    }


                    // input format - Official: nimal,nimal@gmail.com,ceo
                    // Use a single input to get all the details of a recipient
                    // code to add a new recipient
                    // store details in clientList.txt file
                    // Hint: use methods for reading and writing files
                }
                case 2 -> {
                    System.out.println("Input your content to mail: ");
                    Scanner scanner1 = new Scanner(System.in);
                    String emailText = scanner1.nextLine();
                    String[] arr2 = emailText.split(",");
                    MailHandling sendMail = new MailHandling();
                    sendMail.clientMail(arr2[0], arr2[1], arr2[2]);


                    // input format - email, subject, content
                    // code to send an email
                }
                case 3 -> {
                    System.out.print("Enter the date: ");
                    Scanner bDay = new Scanner(System.in);
                    String clientBday = bDay.nextLine();
                    System.out.println("recipients who have birthday on " + clientBday + " :");
                    textHandling recipientBday = new textHandling();
                    recipientBday.getBirthday(clientBday);

                    // input format - yyyy/MM/dd (ex: 2018/09/17)
                    // code to print recipients who have birthdays on the given date
                }
                case 4 -> {
                    System.out.print("Enter today's date: ");
                    Scanner reqDate = new Scanner(System.in);
                    String todayDate = reqDate.nextLine();
                    MailHandling printSentMails = new MailHandling();
                    printSentMails.sentMailDetails(todayDate);
                    // input format - yyyy/MM/dd (ex: 2018/09/17)
                    // code to print the details of all the emails sent on the input date
                }
                case 5 -> // code to print the number of recipient objects in the application
                        textHandling.countLineBufferedReader();
                case 6 -> {
                    System.out.println("You have exited the program");
                    System.exit(0);
                }
            }

            // start email client
            // code to create objects for each recipient in clientList.txt
            // use necessary variables, methods and classes
        }
    }
}

// create more classes needed for the implementation (remove the  public access modifier from classes when you submit your code)

interface sendBdayMail{
    void wish();
}
class bdayMail implements sendBdayMail {
    List<String> bday_list = new ArrayList<String>();
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    LocalDateTime now = LocalDateTime.now();
    String strDate = dtf.format(now);
    String type,mailAdd, recipient;

    public void wish() {

        try {

            BufferedReader in = new BufferedReader(new FileReader("clientList.txt"));
            String str;

            while ((str = in.readLine()) != null) {
                bday_list .add(str);
            }

            for (String element : bday_list ) {

                String[] arr2 = element.split(": ");
                String[] arr3 = arr2[1].split(",");
                String last_element = arr3[arr3.length - 1];
                if (arr2[0].equals("Office_friend") ){
                    String[] clientBday = last_element.split("/");
                    String[] currDate = strDate.split("/");

                    if (clientBday[1].equals(currDate[1])) {
                        if (clientBday[2].equals(currDate[2])) {

                            type = arr2[0];
                            mailAdd = arr3[arr3.length - 3];
                            recipient = arr3[0];
                            bdayWish bdayMsg = new bdayWish();
                            bdayMsg.sendWish(type, mailAdd, recipient);


                        }
                    }
                }
                else if( (arr2[0].equals("Personal"))){
                    String[] clientBday = last_element.split("/");
                    String[] currDate = strDate.split("/");

                    if (clientBday[1].equals(currDate[1])) {
                        if (clientBday[2].equals(currDate[2])) {

                            type = arr2[0];
                            mailAdd = arr3[arr3.length - 2];
                            recipient = arr3[0];
                            bdayWish bdayMsg = new bdayWish();
                            bdayMsg.sendWish(type, mailAdd, recipient);


                        }
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException | FileNotFoundException ex) {
            System.out.println("Client list exhausted");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}

class bdayWish {
    private String mailId;
    private String mailSubject;
    private String mailBody;

    public void sendWish(String recipient, String mail,String name) throws IOException {

        mailId = mail;
        mailSubject = "Birthday Wish";
        if(recipient.equals("Office_friend")){
            mailBody = "Wish you a Happy Birthday " + name;

        }
        else if(recipient.equals("Personal")){
            mailBody = "Hugs and love on your birthday " + name;

        }
        MailHandling wishMail = new MailHandling();
        wishMail.clientMail(mailId,mailSubject,mailBody);
        serialization emailSer = new serialization();
        emailSer.objSer(mailId +","+ name +","+ mailSubject);

    }

}

class MailHandling extends bdayMail implements java.io.Serializable{

    public  void clientMail(String clientAddress, String mailSubject, String mailBody)  {
        String to = clientAddress;
        String from = "amrithsmart2000@gmail.com";
        String host = "localhost";
        Properties properties = new Properties();
        properties.put("mail.smtp.auth",true);
        properties.put("mail.smtp.starttls.enable",true);
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.smtp.port",587);
        //properties.setProperty("mail.smtp.host",host);

        Session session = Session.getDefaultInstance(properties,new javax.mail.Authenticator()
                {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("amrithsmart2000@gmail.com","sqcc dyej tbyd hgry");
                    }
                }

        );

        try
        {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipients(Message.RecipientType.TO, String.valueOf(new InternetAddress(to)));
            message.setSubject(mailSubject);
            message.setText(mailBody);
            Transport.send(message);
            //System.out.println("Mail is sent successfully");

            FileWriter writeFile = new FileWriter("sentMailList.txt", true);
            PrintWriter out = new PrintWriter(writeFile);
            out.println((strDate + "-" + clientAddress +"-"+ mailSubject));
            out.close();

            serialization emailSer = new serialization();
            emailSer.objSer(clientAddress + mailSubject);
        }
        catch (MessagingException mex)
        {
            mex.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void sentMailDetails(String date) {
        boolean b = false;

        try (BufferedReader reader = new BufferedReader(new FileReader("sentMailList.txt"))) {
            String element;

            while ((element = reader.readLine()) != null) {
                String[] mailArr = element.split("-");
                if (mailArr[0].equals(date)) {
                    System.out.println("Recipient - " + mailArr[1] + " Subject - " + mailArr[2]);
                    b = true;
                }

            }
            if(!b)
                System.out.println("No emails were sent on this date");
        } catch (IOException e) {
            System.out.println("No emails sent yet");
        }
        System.out.println();

    }

}


class serialization extends bdayMail implements java.io.Serializable{
    public void objSer(String text) throws IOException {
        String filename = "file.ser";
        FileOutputStream file = new FileOutputStream(filename,true);
        ObjectOutputStream out = new ObjectOutputStream(file);

        out.writeObject(strDate + "-" + text);

        out.close();
        file.close();



    }
    public void objDeSer(String filename) throws IOException, ClassNotFoundException {

        FileInputStream file = new FileInputStream(filename);
        ObjectInputStream os = new ObjectInputStream(file);

        Object one = os.readObject();

        os.close();

    }
}



class textHandling extends bdayMail {
    List<String> str_list = new ArrayList<String>();

    public void textWrite(String data) throws IOException {
        FileWriter writeFile = new FileWriter("clientList.txt", true);
        PrintWriter out = new PrintWriter(writeFile);
        out.println(data);
        out.close();
    }

    public void getBirthday(String date) throws IOException {
        int b = 0;
        try {
            BufferedReader in = new BufferedReader(new FileReader("clientList.txt"));
            String str;

            while ((str = in.readLine()) != null) {
                str_list.add(str);
            }

            for (String element : str_list) {

                String[] arr2 = element.split(": ");
                String[] arr3 = arr2[1].split(",");
                String last_element = arr3[arr3.length - 1];
                String[] clientBday = last_element.split("/");
                String[] currDate = date.split("/");

                if (clientBday[1].equals(currDate[1])) {
                    if (clientBday[2].equals(currDate[2])) {
                        System.out.println(arr3[0]);
                        b += 1;
                    }
                }

            }

        } catch (ArrayIndexOutOfBoundsException ex){
            System.out.println("Client list exhausted");
        }
        if (b==0) {
            System.out.println("No recipients have birthday today");
        }
    }
    public static void countLineBufferedReader() {

        long lines = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader("clientList.txt"))) {
            while (reader.readLine() != null) lines++;
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(lines);

    }



}
class Recepients {
    private String name;
    private String email;

    public Recepients(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



}
class Official extends Recepients{
    private String designation;

    public Official(String name, String email, String designation) {
        super(name, email);
        this.designation = designation;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }



}

class OfficialFriend extends Recepients{
    private String DOB;

    public OfficialFriend(String name, String email, String DOB) {
        super(name, email);
        this.DOB = DOB;
    }
}

class Personal extends Recepients{
    private String nickName;
    private String DOB;

    public Personal(String name, String email, String nickName, String DOB) {
        super(name, email);
        this.nickName = nickName;
        this.DOB = DOB;
    }
}


