package dominio;

import java.io.*;
import java.util.List;

public class FileUtil {

    public static void saveToFile(List<FileWork> list, String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(list);
            System.out.println("Lista de arquivos salva com sucesso.");
        } catch (IOException e) {
            System.err.println("Erro ao salvar a lista de arquivos: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static List<FileWork> loadFromFile(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (List<FileWork>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar a lista de arquivos: " + e.getMessage());
            return null;
        }
    }
}
