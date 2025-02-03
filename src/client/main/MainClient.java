package client.main;

import java.io.*;
import java.net.Socket;
import java.util.Date;
import java.util.List;
import server.model.Profesor;
import server.model.Student;

public class MainClient {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Socket socket = new Socket("localhost", 5555);
        System.out.println("Konektovao sam se na server");

        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            System.out.println("\nOdaberi operaciju:");
            System.out.println("1. getAllStudents");
            System.out.println("2. getAllProfesors");
            System.out.println("3. addProfesor");
            System.out.println("4. updateStudent");
            System.out.println("5. deleteStudent");
            System.out.println("6. findStudent");
            System.out.println("0. Exit");
            System.out.print("Vaš izbor: ");

            String operation = br.readLine();
            switch (operation) {
                case "getAllStudents": {
                    oos.writeUTF("getAllStudents");
                    oos.flush();
                    List<Student> students = (List<Student>) ois.readObject();
                    System.out.println("Studenti sa servera: " + students);
                    break;
                }
                case "getAllProfesors": {
                    oos.writeUTF("getAllProfesors");
                    oos.flush();
                    List<Profesor> profesors = (List<Profesor>) ois.readObject();
                    System.out.println("Profesori sa servera: " + profesors);
                    break;
                }
                case "addProfesor": {
                    oos.writeUTF("addProfesor");
                    oos.flush();

                    System.out.print("Ime: ");
                    String ime = br.readLine();
                    System.out.print("Prezime: ");
                    String prezime = br.readLine();
                    System.out.print("Godina rođenja (YYYY): ");
                    int godina = Integer.parseInt(br.readLine());
                    System.out.print("Mesec rođenja (1-12): ");
                    int mesec = Integer.parseInt(br.readLine()) - 1;
                    System.out.print("Dan rođenja: ");
                    int dan = Integer.parseInt(br.readLine());

                    Profesor profesor = new Profesor();
                    profesor.setIme(ime);
                    profesor.setPrezime(prezime);
                    profesor.setDatumRodjenja(new Date(godina - 1900, mesec, dan));

                    oos.writeObject(profesor);
                    oos.flush();

                    boolean success = ois.readBoolean();
                    System.out.println(success ? "Profesor uspešno dodat" : "Greška pri dodavanju profesora");
                    break;
                }
                case "updateStudent": {
                    oos.writeUTF("updateStudent");
                    oos.flush();

                    System.out.print("Unesite ID studenta za ažuriranje: ");
                    int id = Integer.parseInt(br.readLine());
                    System.out.print("Novo ime: ");
                    String ime = br.readLine();
                    System.out.print("Novo prezime: ");
                    String prezime = br.readLine();

                    Student student = new Student();
                    student.setId(id);
                    student.setIme(ime);
                    student.setPrezime(prezime);

                    oos.writeObject(student);
                    oos.flush();

                    boolean success = ois.readBoolean();
                    System.out.println(success ? "Student uspešno ažuriran" : "Greška pri ažuriranju studenta");
                    break;
                }
                case "deleteStudent": {
                    oos.writeUTF("deleteStudent");
                    oos.flush();

                    System.out.print("Unesite ID studenta za brisanje: ");
                    int id = Integer.parseInt(br.readLine());

                    oos.writeInt(id);
                    oos.flush();

                    boolean success = ois.readBoolean();
                    System.out.println(success ? "Student uspešno obrisan" : "Greška pri brisanju studenta");
                    break;
                }
                case "findStudent": {
                    oos.writeUTF("findStudent");
                    oos.flush();

                    System.out.print("Unesite ime/prezime za pretragu: ");
                    String value = br.readLine();

                    oos.writeUTF(value);
                    oos.flush();

                    List<Student> students = (List<Student>) ois.readObject();
                    System.out.println("Rezultati pretrage: " + students);
                    break;
                }
                case "Exit": {
                    oos.writeUTF("Exit");
                    oos.flush();
                    socket.close();
                    return;
                }
            }
        }
    }
}
