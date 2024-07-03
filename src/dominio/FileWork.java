package dominio;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileWork  implements Serializable{

    private static final long serialVersionUID = 1L;

    private File file;
    private File diretorio;
    private String nome;
    private LocalDateTime ldt;
    private static final DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public FileWork(File file, File diretorio, String nome, LocalDateTime ldt) {
        this.file = file;
        this.diretorio = diretorio;
        this.nome = nome;
        this.ldt = ldt;
    }

    // Método para criar diretório
    public void createDiretory() {
        if (!diretorio.exists()) {
            boolean isCreate = diretorio.mkdir();
            System.out.println("Create diretory: " + isCreate);
        }
    }

    public void createFile() {
        if (!file.exists()) {
            try {
                boolean isCreate = file.createNewFile();
                System.out.println("Create: " + isCreate);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Método para escrever texto no arquivo
        public void writeFile(String textoParaEscrever) {
            try (FileWriter fw = new FileWriter(file, true); // true para append
                 BufferedWriter bw = new BufferedWriter(fw)) {

                bw.write(textoParaEscrever);
                bw.newLine();

                System.out.println("Texto adicionado ao arquivo.");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    // Método para ler conteúdo do arquivo
    public void readFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            String linha;
            while ((linha = br.readLine()) != null) {
                System.out.println(linha);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteFile() {
        if (file.exists()) {
            if (file.delete()) {
                System.out.println("Arquivo excluído com sucesso: " + file.getName());
            } else {
                System.out.println("Não foi possível excluir o arquivo: " + file.getName());
            }
        }
    }

    // Método para renomear o arquivo
    public boolean renameFile(String newName) {
        File newFile = new File(diretorio, newName);
        boolean renamed = file.renameTo(newFile);
        if (renamed) {
            this.file = newFile;
        }
        return renamed;
    }
    public File getDiretorio() {
        return diretorio;
    }

    public void setDiretorio(File diretorio) {
        this.diretorio = diretorio;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDateTime getLdt() {
        return ldt;
    }

    public void setLdt(LocalDateTime ldt) {
        this.ldt = ldt;
    }

    @Override
    public String toString() {
        return "Nome autor: " + nome +
                "\nNome arquivo: " + file.getName() +
                "\nNome diretório: " + diretorio.getName() +
                "\nHorário create/rename: " + sdf.format(ldt);
    }
}