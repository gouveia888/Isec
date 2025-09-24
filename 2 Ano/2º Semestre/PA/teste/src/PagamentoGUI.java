import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Interface Pagamento
interface Pagamento {
    void processarPagamento(double valor);
}

// Implementação: Pagamento com Cartão
class PagamentoCartao implements Pagamento {
    @Override
    public void processarPagamento(double valor) {
        System.out.println("Pagamento de € " + valor + " realizado via Cartão de Crédito.");
    }
}

// Implementação: Pagamento com PIX
class PagamentoPix implements Pagamento {
    @Override
    public void processarPagamento(double valor) {
        System.out.println("Pagamento de € " + valor + " realizado via MBWay.");
    }
}

// Classe que implementa a interface Printable para imprimir o recibo
class ReciboPrintable implements Printable {
    private String recibo;

    public ReciboPrintable(String recibo) {
        this.recibo = recibo;
    }

    @Override
    public int print(Graphics g, PageFormat pageFormat, int pageIndex) throws PrinterException {
        if (pageIndex > 0) {
            return NO_SUCH_PAGE;
        }
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
        g2d.setFont(new Font("Monospaced", Font.PLAIN, 12));

        String[] lines = recibo.split("\n");
        int y = 15;
        for (String line : lines) {
            g2d.drawString(line, 0, y);
            y += 15;
        }
        return PAGE_EXISTS;
    }

    public static void imprimirReciboFisico(String recibo) {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(new ReciboPrintable(recibo));
        if (job.printDialog()) {
            try {
                job.print();
            } catch (PrinterException ex) {
                JOptionPane.showMessageDialog(null, "Erro durante a impressão: " + ex.getMessage());
            }
        }
    }
}

public class PagamentoGUI {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Sistema de Pagamento");
        frame.setSize(400, 350);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        UIManager.put("Button.background", new Color(150, 150, 255));
        UIManager.put("Button.foreground", Color.WHITE);

        JLabel label = new JLabel("Escolha o método de pagamento:");
        label.setBounds(50, 20, 300, 20);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        frame.add(label);

        JTextField campoValor = new JTextField();
        campoValor.setBounds(50, 50, 300, 30);
        frame.add(campoValor);

        JButton btnCartao = new JButton("Pagar com Cartão");
        btnCartao.setBounds(50, 90, 300, 40);
        frame.add(btnCartao);

        JButton btnPix = new JButton("Pagar com MBWay");
        btnPix.setBounds(50, 140, 300, 40);
        frame.add(btnPix);

        btnCartao.addActionListener(e -> processarPagamento(frame, campoValor, new PagamentoCartao(), "Cartão de Crédito"));
        btnPix.addActionListener(e -> processarPagamento(frame, campoValor, new PagamentoPix(), "MBWay"));

        frame.setVisible(true);
    }

    private static void processarPagamento(JFrame frame, JTextField campoValor, Pagamento metodoPagamento, String metodo) {
        try {
            double valor = Double.parseDouble(campoValor.getText());
            metodoPagamento.processarPagamento(valor);
            JOptionPane.showMessageDialog(frame, "Pagamento de R$ " + valor + " realizado com " + metodo + "!");

            String recibo = formatarRecibo(valor, metodo);
            exibirReciboDialog(frame, recibo);
            ReciboPrintable.imprimirReciboFisico(recibo);
            registrarMovimento(recibo);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Insira um valor válido!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static String formatarRecibo(double valor, String metodo) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String dataHora = LocalDateTime.now().format(formatter);
        return "----- RECIBO DE PAGAMENTO -----\n"
                + "Data e Hora: " + dataHora + "\n"
                + "Método de Pagamento: " + metodo + "\n"
                + "Valor: € " + valor + "\n"
                + "--------------------------------\n"
                + "Obrigado pela preferência!";
    }

    private static void exibirReciboDialog(JFrame parent, String recibo) {
        JTextArea textArea = new JTextArea(recibo);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(350, 200));
        JOptionPane.showMessageDialog(parent, scrollPane, "Recibo", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void registrarMovimento(String recibo) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("movimentos.txt", true))) {
            writer.write(recibo + "\n\n");
        } catch (IOException e) {
            System.err.println("Erro ao registrar movimento: " + e.getMessage());
        }
    }
   private static void save(String filename, MyObject obj) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(
                             new FileOutputStream(filename)))
        {
            oos.writeObject(obj);
        } catch (Exception e) {
            System.err.println("Error saving data");
        }
    }
}