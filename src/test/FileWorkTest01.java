package test;



import dominio.FileUtil;
import dominio.FileWork;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class FileWorkTest01 {

    private static final String FILENAME = "fileworks.dat";

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        List<FileWork> list = loadFileWorks();

        System.out.println("Central de gerenciamento de arquivos:");

        while (true) {
            showMenu();

            String entrada = input.nextLine().trim();


            try {
                int opcao = Integer.parseInt(entrada);

                switch (opcao) {
                    case 1:
                        createFile(input, list);
                        saveFileWorks(list);
                        break;
                    case 2:
                        readFile(input, list);
                        break;
                    case 3:
                        renameFile(input, list);
                        saveFileWorks(list);
                        break;
                    case 4:
                        deleteFile(input, list);
                        saveFileWorks(list);
                    case 5:
                        saveFileWorks(list);
                        System.out.println("Encerrando o programa...");
                        return;
                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Opção inválida. Digite um número válido.");
            }
        }
    }

    private static void showMenu() {
        System.out.println("1- Criar um arquivo");
        System.out.println("2- Ler conteúdo de um arquivo");
        System.out.println("3- Renomear um arquivo");
        System.out.println("4- Excluir um arquivo");
        System.out.println("5- Sair");
        System.out.print("Escolha uma opção: ");
    }


    //criar arquivo
    private static void createFile(Scanner input, List<FileWork> list) {
        System.out.println("Insira o nome do diretório: ");
        String diretorioNome = input.nextLine();
        File diretorio = new File(diretorioNome);

        System.out.println("Insira o nome do arquivo: ");
        String nomeArquivo = input.nextLine();
        File arquivo = new File(diretorio, nomeArquivo);

        System.out.println("Insira o nome do autor da criação:");
        String autor = input.nextLine();

        LocalDateTime dataHora = LocalDateTime.now();

        FileWork fileWork = new FileWork(arquivo, diretorio, autor, dataHora);
        fileWork.createDiretory();
        fileWork.createFile();
        list.add(fileWork);
        FileUtil.saveToFile(list, FILENAME);

        System.out.println("Arquivo criado com sucesso!");

        System.out.println("Deseja escrever no arquivo recém-criado? (sim/não)");
        String resposta = input.nextLine();
        if (resposta.equalsIgnoreCase("sim")) {
            System.out.println("Insira um texto (ou insira 'sair' para encerrar):");
            StringBuilder textoBuilder = new StringBuilder();
            while (true) {
                String linha = input.nextLine();
                if (linha.equalsIgnoreCase("sair")) {
                    break;
                }
                textoBuilder.append(linha).append("\n");
            }
            String texto = textoBuilder.toString();
            fileWork.writeFile(texto);
        }

    }

    //ler arquivo
    private static void readFile(Scanner input, List<FileWork> list) {
        System.out.println("Insira o nome do arquivo:");
        String nomeArquivo = input.nextLine();

        FileWork arquivoParaLer = findFileByName(nomeArquivo, list);
        if (arquivoParaLer != null) {
            arquivoParaLer.readFile();
        } else {
            System.out.println("Arquivo não encontrado.");
        }
    }


    //renomear arquivo
    private static void renameFile(Scanner input, List<FileWork> list) {
        System.out.println("Arquivos na lista:");
        list.forEach(fileWork -> System.out.println("- " + fileWork.getFile().getName())); //itera e define ação de cada elemento com lambda

        System.out.println("Insira o nome do arquivo que deseja renomear:");
        String nomeArquivo = input.nextLine();

        FileWork arquivoParaRenomear = null;
        for (FileWork fileWork : list) {
            if (fileWork.getFile().getName().equals(nomeArquivo)) {
                arquivoParaRenomear = fileWork;
                break;
            }
        }

        if (arquivoParaRenomear == null) {
            System.out.println("Arquivo não encontrado na lista.");
            return;
        }



        System.out.println("Insira o novo nome para o arquivo:");
        String novoNome = input.nextLine();

        File novoArquivo = new File(arquivoParaRenomear.getDiretorio(), novoNome);
        if (arquivoParaRenomear.getFile().renameTo(novoArquivo)) {
            arquivoParaRenomear.setFile(novoArquivo); // seta o arquivo original para o novo
            System.out.println("Arquivo renomeado com sucesso.");
            FileUtil.saveToFile(list, FILENAME);
        } else {
            System.out.println("Não foi possível renomear o arquivo.");
        }
    }


    //deletar arquivos
    private static void deleteFile(Scanner input, List<FileWork> list) {
        System.out.println("Arquivos na lista:");
        list.forEach(fileWork -> System.out.println("- " + fileWork.getFile().getName()));

        System.out.println("Insira o nome do arquivo que deseja excluir:");
        String nomeArquivo = input.nextLine();

        Iterator<FileWork> iterator = list.iterator(); //Iterador de listas
        while (iterator.hasNext()) { //enquanto tiver proximo
            FileWork fileWork = iterator.next(); //o atual se torna o proximo
            if (fileWork.getFile().getName().equals(nomeArquivo)) { //co
                fileWork.deleteFile();
                iterator.remove();
                System.out.println("Arquivo excluído da lista.");
                FileUtil.saveToFile(list, FILENAME);
                return;
            }
        }

        System.out.println("Arquivo não encontrado na lista.");
    }

    private static FileWork findFileByName(String nomeArquivo, List<FileWork> list) {
        for (FileWork fw : list) {
            if (fw.getFile().getName().equals(nomeArquivo)) {
                return fw;
            }
        }
        return null;
    }

    private static void saveFileWorks(List<FileWork> list) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILENAME))) {
            oos.writeObject(list);
            System.out.println("Dados salvos com sucesso.");
        } catch (IOException e) {
            System.err.println("Erro ao salvar os dados: " + e.getMessage());
        }
    }

    private static List<FileWork> loadFileWorks() {
        List<FileWork> list = new ArrayList<>();
        File file = new File(FILENAME);

        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                list = (List<FileWork>) ois.readObject();
                System.out.println("Dados carregados com sucesso.");
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Erro ao carregar os dados: " + e.getMessage());
            }
        }

        return list;
    }
}

