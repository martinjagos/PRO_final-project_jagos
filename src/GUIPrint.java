import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GUIPrint extends JFrame{
    private JTabbedPane tabbedPane1;
    private JPanel panel1;
    private JPanel panelObjednavky;
    private JPanel panelFilamenty;
    private JPanel panelNastaveni;
    private JTextField textFieldObjednavkaID;
    private JComboBox comboBoxFilament;
    private JTextField textFieldObjednavkaDatum;
    private JTextField textFieldObjednavkaSpotřeba;
    private JTextField textFieldObjednavkaEnergie;
    private JCheckBox checkBoxObjednavkaModel;
    private JTable tableObjednavky;
    private JTextField textFieldNazev;
    private JTextField textFieldBarva;
    private JTextField textFieldMaterial;
    private JLabel labelNovyFilament;
    private JTextField textFieldHmotnost;
    private JButton odeslatButton;
    private JButton vybratObrázekButton;
    private JLabel labelObrázek;
    private JTextField textFieldUpravaID;
    private JButton načístButton;
    private JTextField textFieldUpravaNazev;
    private JTextField textFieldUpravaBarva;
    private JTextField textFieldUpravaMaterial;
    private JTextField textFieldUpravaHmotnost;
    private JLabel labelUpravit;
    private JButton odestalUpravaButton;
    private JButton vybratObrazekUpravitButton;
    private JLabel labelUpravitObrazek;
    private JButton odstranitButton;
    private JButton buttonOdeslatObjednavku;
    private JScrollPane scrollPaneTable;
    private JTextField textFieldCena;
    private JTextField textFieldUpravitCena;
    private JLabel labelNovaObjednavka;
    private JLabel labelHistorie;
    private List<Filament> listFilament = new ArrayList<>();
    private List<Objednavka> listObjednavka = new ArrayList<>();
    private JFileChooser chooser = new JFileChooser("");
    private JPanel Textgrid = new JPanel();
    private GridLayout gl = new GridLayout(0, 3, 10, 10);
    private File selectedFileNovy = null;
    private File selectedFileUprava = null;
    private ObjTableModel objTableModel = new ObjTableModel(listObjednavka);
    public GUIPrint(){
        filamenty();
        nastaveni();
        objednavky();

        odeslatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                novyFilament();
            }
        });
        vybratObrázekButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                vyberObrázek();
            }
        });
        načístButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nactiFilament();
            }
        });
        vybratObrazekUpravitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                vyberObrázekUprava();
            }
        });
        odestalUpravaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                upravFilament();
            }
        });
        odstranitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                odstranitFilament();
            }
        });
        buttonOdeslatObjednavku.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                novaObjednavka();
            }
        });
    }
    private void odectiFilament(){
        List<String> novyList = new ArrayList<>();
        List<String> radky = new ArrayList<>();
        try(Scanner scanner = new Scanner(new BufferedReader(new FileReader(new File("./settings/filamentDB.txt"))))){
            while (scanner.hasNextLine()){
                radky.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Chyba při načítání souboru", "Chyba",JOptionPane.ERROR_MESSAGE);
        }
        for(int id=0; id<listFilament.size(); id++){
            if(listFilament.get(id).getNazev() == comboBoxFilament.getSelectedItem()){
                for(int i = 0; i < id; i++){
                    novyList.add(radky.get(i));
                }
                double hmotnost = listFilament.get(id).getHmotnost()-Double.parseDouble(textFieldObjednavkaSpotřeba.getText());
                novyList.add(listFilament.get(id).getId() + ";" + listFilament.get(id).getNazev() + ";" + listFilament.get(id).getBarva() + ";" + listFilament.get(id).getMaterial() + ";" + hmotnost+";"+listFilament.get(id).getObrazek()+";"+listFilament.get(id).getCena());
                for(int i = id+1; i < radky.size(); i++){
                    novyList.add(radky.get(i));
                }
                try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(new File("./settings/filamentDB.txt"))))) {
                    novyList.forEach(s -> pw.println(s));
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Chyba při zapisování do souboru", "Chyba", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        loadFilament();
        vykresliFilament();
        updateComboBox();
        nactiObjednavky();
    }
    private void  nactiObjednavky(){
        listObjednavka.clear();
        String[] radek = null;
        String dalsiRadek = "";
        try(Scanner scanner = new Scanner(new BufferedReader(new FileReader(new File("./settings/historieObjednavekDB.txt"))))){
            while (scanner.hasNextLine()){
                dalsiRadek = scanner.nextLine();
                radek = dalsiRadek.split(";");
                listObjednavka.add(new Objednavka(Integer.parseInt(radek[0]), radek[1], LocalDate.parse(radek[2]), Double.valueOf(radek[3]), Double.valueOf(radek[4]), Boolean.parseBoolean(radek[5])));
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Chyba při načítání souboru", "Chyba",JOptionPane.ERROR_MESSAGE);
        }
        tableObjednavky.repaint();
        tableObjednavky.updateUI();
    }
    private void novaObjednavka() {
        System.out.println(textFieldObjednavkaDatum.getText());
        if(textFieldObjednavkaDatum.getText().isEmpty() || textFieldObjednavkaSpotřeba.getText().isEmpty() || textFieldObjednavkaEnergie.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Vyplňe všechna pole", "Chyba - Spotřeba", JOptionPane.WARNING_MESSAGE);
        } else{
            for (int ide = 0; ide < listFilament.size(); ide++) {
                if (listFilament.get(ide).getNazev() == comboBoxFilament.getSelectedItem()) {
                    if (listFilament.get(ide).getHmotnost() > Double.parseDouble(textFieldObjednavkaSpotřeba.getText())) {
                        List<String> radky = new ArrayList<>();
                        try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(new File("./settings/historieObjednavekDB.txt"))))) {
                            while (scanner.hasNextLine()) {
                                radky.add(scanner.nextLine());
                            }
                        } catch (FileNotFoundException e) {
                            JOptionPane.showMessageDialog(null, "Chyba při načítání souboru", "Chyba", JOptionPane.ERROR_MESSAGE);
                        }
                        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(new File("./settings/historieObjednavekDB.txt"))))) {
                            boolean vlastniModel;
                            if (checkBoxObjednavkaModel.isSelected()) {
                                vlastniModel = true;
                            } else {
                                vlastniModel = false;
                            }
                            int id = listObjednavka.size() + 1;
                            radky.add(id + ";" + comboBoxFilament.getSelectedItem().toString() + ";" + textFieldObjednavkaDatum.getText() + ";" + textFieldObjednavkaSpotřeba.getText() + ";" + textFieldObjednavkaEnergie.getText() + ";" + vlastniModel);
                            radky.forEach(s -> pw.println(s));
                            tableObjednavky.updateUI();

                        } catch (IOException e) {
                            JOptionPane.showMessageDialog(null, "Chyba při vytváření nové objednávky", "Chyba", JOptionPane.ERROR_MESSAGE);
                        }
                        odectiFilament();
                        comboBoxFilament.setSelectedIndex(0);
                        textFieldObjednavkaDatum.setText("");
                        textFieldObjednavkaSpotřeba.setText("");
                        textFieldObjednavkaEnergie.setText("");
                        checkBoxObjednavkaModel.setSelected(false);
                        updateComboBox();
                        nactiObjednavky();
                    } else {
                        JOptionPane.showMessageDialog(null, "Nedostatek filamentu", "Chyba", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }

    }
    private void nactiFilament(){
        try {
            int id = Integer.parseInt(textFieldUpravaID.getText()) - 1;
            if(id > listFilament.size()-1 || id < 0){JOptionPane.showMessageDialog(null, "Neplatné ID", "Chyba", JOptionPane.ERROR_MESSAGE);}
            else {
                textFieldUpravaNazev.setText(listFilament.get(id).getNazev());
                textFieldUpravaBarva.setText(listFilament.get(id).getBarva());
                textFieldUpravaMaterial.setText(listFilament.get(id).getMaterial());
                textFieldUpravaHmotnost.setText("" + listFilament.get(id).getHmotnost());
                textFieldUpravitCena.setText(""+ listFilament.get(id).getCena());
                labelUpravitObrazek.setText(listFilament.get(id).getObrazek());
                selectedFileUprava = null;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Neplatné ID", "Chyba", JOptionPane.ERROR_MESSAGE);
        }

    }
    private void odstranitFilament(){
        List<Filament> novyList = new ArrayList<>();
        List<Filament> radky = new ArrayList<>();
        String[] radek = null;
        String dalsiRadek = "";
        try(Scanner scanner = new Scanner(new BufferedReader(new FileReader(new File("./settings/filamentDB.txt"))))){
            while (scanner.hasNextLine()){
                dalsiRadek = scanner.nextLine();
                radek = dalsiRadek.split(";");
                radky.add(new Filament(Integer.parseInt(radek[0]), radek[1], radek[2], radek[3], Double.valueOf(radek[4]), radek[5], Integer.parseInt(radek[6])));
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Chyba při načítání souboru", "Chyba",JOptionPane.ERROR_MESSAGE);
        }
        File obr = new File("./images/"+textFieldUpravaID.getText()+".png");
        obr.delete();
        for(int i = 0; i < Integer.parseInt(textFieldUpravaID.getText())-1; i++){
            novyList.add(radky.get(i));
        }
        for(int i = Integer.parseInt(textFieldUpravaID.getText()); i < radky.size(); i++){
            File fil = new File("./images/"+radky.get(i).getId()+".png");
            radky.get(i).setId(radky.get(i).getId()-1);
            radky.get(i).setObrazek("./images/"+radky.get(i).getId()+".png");
            File newFil = new File("./images/"+radky.get(i).getId()+".png");
            fil.renameTo(newFil);
            novyList.add(radky.get(i));
        }
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(new File("./settings/filamentDB.txt"))))) {
            for(Filament f : novyList){
                pw.println(
                        f.getId()+";"
                        +f.getNazev()+";"
                        +f.getBarva()+";"
                        +f.getMaterial()+";"
                        +f.getHmotnost()+";"
                        +f.getObrazek()+";"
                        +f.getCena()
                );
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Chyba při zapisování do souboru", "Chyba", JOptionPane.ERROR_MESSAGE);
        }
        loadFilament();
        vykresliFilament();
        updateComboBox();
        nactiObjednavky();

    }
    private void upravFilament(){
        List<String> novyList = new ArrayList<>();
        List<String> radky = new ArrayList<>();
        try(Scanner scanner = new Scanner(new BufferedReader(new FileReader(new File("./settings/filamentDB.txt"))))){
            while (scanner.hasNextLine()){
                radky.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Chyba při načítání souboru", "Chyba",JOptionPane.ERROR_MESSAGE);
        }
        for(int i = 0; i < Integer.parseInt(textFieldUpravaID.getText())-1; i++){
            novyList.add(radky.get(i));
        }
        novyList.add(textFieldUpravaID.getText() + ";" + textFieldUpravaNazev.getText() + ";" + textFieldUpravaBarva.getText() + ";" + textFieldUpravaMaterial.getText() + ";" + textFieldUpravaHmotnost.getText()+";"+"./images/"+textFieldUpravaID.getText()+".png"+";"+textFieldUpravitCena.getText());
        for(int i = Integer.parseInt(textFieldUpravaID.getText()); i < radky.size(); i++){
            novyList.add(radky.get(i));
        }
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(new File("./settings/filamentDB.txt"))))) {
            novyList.forEach(s -> pw.println(s));
            if (selectedFileUprava != null) {
                File sourceFile = selectedFileUprava;
                int nazevSouboru = listFilament.size() + 1;
                File destinationFile = new File("./images/" + textFieldUpravaID.getText() + ".png");
                try {
                    FileInputStream fileInputStream = new FileInputStream(sourceFile);
                    FileOutputStream fileOutputStream = new FileOutputStream(
                            destinationFile);

                    int bufferSize;
                    byte[] bufffer = new byte[512];
                    while ((bufferSize = fileInputStream.read(bufffer)) > 0) {
                        fileOutputStream.write(bufffer, 0, bufferSize);
                    }
                    fileInputStream.close();
                    fileOutputStream.close();
                    labelUpravitObrazek.setText("" + selectedFileUprava);
                } catch (FileNotFoundException e) {
                    JOptionPane.showMessageDialog(null, "Chyba při načínání souboru FileNotFound", "Chyba", JOptionPane.ERROR_MESSAGE);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Chyba při načínání souboru IOException", "Chyba", JOptionPane.ERROR_MESSAGE);
                }
            }

            selectedFileUprava = null;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Chyba při zapisování do souboru", "Chyba", JOptionPane.ERROR_MESSAGE);
        }
        loadFilament();
        vykresliFilament();
        updateComboBox();
        nactiObjednavky();
    }
    private void vyberObrázekUprava() {
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFileUprava = chooser.getSelectedFile();
            labelUpravitObrazek.setText(""+selectedFileUprava);
        }

    }
    private void vyberObrázek(){
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFileNovy = chooser.getSelectedFile();
            labelObrázek.setText(""+selectedFileNovy);
        }

    }
    private void novyFilament(){
        List<String> radky = new ArrayList<>();
        if(textFieldNazev.getText() == "" || textFieldBarva.getText() == "" || textFieldMaterial.getText() == "" || textFieldHmotnost.getText() == "" || labelObrázek.getText() == ""){
            JOptionPane.showMessageDialog(null, "Vyplňe všechna pole", "Chyba", JOptionPane.WARNING_MESSAGE);
        } else {
            try(Scanner scanner = new Scanner(new BufferedReader(new FileReader(new File("./settings/filamentDB.txt"))))){
                while (scanner.hasNextLine()){
                    radky.add(scanner.nextLine());
                }
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(null, "Chyba při načítání souboru", "Chyba",JOptionPane.ERROR_MESSAGE);
            }
            try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(new File("./settings/filamentDB.txt"))))) {
                int obrazekInt = listFilament.size() + 1;
                radky.add(listFilament.size() + 1 + ";" + textFieldNazev.getText() + ";" + textFieldBarva.getText() + ";" + textFieldMaterial.getText() + ";" + textFieldHmotnost.getText()+";"+"./images/"+obrazekInt+".png"+";"+textFieldCena.getText());
                radky.forEach(s -> pw.println(s));
                File sourceFile = selectedFileNovy;
                int nazevSouboru = listFilament.size()+1;
                File destinationFile = new File("./images/"+nazevSouboru+".png");
                try {
                    FileInputStream fileInputStream = new FileInputStream(sourceFile);
                    FileOutputStream fileOutputStream = new FileOutputStream(
                            destinationFile);

                    int bufferSize;
                    byte[] bufffer = new byte[512];
                    while ((bufferSize = fileInputStream.read(bufffer)) > 0) {
                        fileOutputStream.write(bufffer, 0, bufferSize);
                    }
                    fileInputStream.close();
                    fileOutputStream.close();
                } catch (FileNotFoundException e) {
                    JOptionPane.showMessageDialog(null, "Chyba při načínání souboru", "Chyba",JOptionPane.ERROR_MESSAGE);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Chyba při načínání souboru", "Chyba",JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Chyba při zapisování do souboru", "Chyba", JOptionPane.ERROR_MESSAGE);
            }
            textFieldNazev.setText("");
            textFieldBarva.setText("");
            textFieldMaterial.setText("");
            textFieldHmotnost.setText("");
            labelObrázek.setText("");
            selectedFileNovy = null;
            loadFilament();
            vykresliFilament();
            updateComboBox();
            nactiObjednavky();
        }
    }
    private void loadFilament(){
        listFilament.clear();
        String[] radek = null;
        String dalsiRadek = "";
        try(Scanner scanner = new Scanner(new BufferedReader(new FileReader(new File("./settings/filamentDB.txt"))))){
            while (scanner.hasNextLine()){
                dalsiRadek = scanner.nextLine();
                radek = dalsiRadek.split(";");
                listFilament.add(new Filament(Integer.parseInt(radek[0]), radek[1], radek[2], radek[3], Double.valueOf(radek[4]), radek[5], Integer.parseInt(radek[6])));
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Chyba při načítání souboru", "Chyba",JOptionPane.ERROR_MESSAGE);
        }
    }
    private void updateComboBox(){
        comboBoxFilament.removeAllItems();
        listFilament.forEach(f -> filterComboBox(f));
    }
    private void filterComboBox(Filament f){
        if(f.getHmotnost() > 0.1){
            comboBoxFilament.addItem(f.getNazev());
        }
    }
    private void objednavky(){
        labelNovaObjednavka.setFont(new Font(labelNovaObjednavka.getFont().getFontName(), Font.BOLD, 20));
        labelHistorie.setFont(new Font(labelHistorie.getFont().getFontName(), Font.BOLD, 20));
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tableObjednavky.setModel(objTableModel);
        tableObjednavky.getColumnModel().getColumn(0).setMaxWidth(50);
        tableObjednavky.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tableObjednavky.getColumnModel().getColumn(2).setMaxWidth(70);
        tableObjednavky.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        tableObjednavky.getColumnModel().getColumn(3).setMaxWidth(70);
        tableObjednavky.getColumnModel().getColumn(4).setMaxWidth(70);
        tableObjednavky.getColumnModel().getColumn(5).setMaxWidth(120);
        tableObjednavky.getTableHeader().setReorderingAllowed(false);
        updateComboBox();
        nactiObjednavky();
    }
    private void vykresliFilament(){
        Textgrid.removeAll();
        Textgrid.setLayout(gl);
        for (int i=0; i<listFilament.size(); i++){
            if(listFilament.get(i).getHmotnost() > 0.1) {
                JPanel filamentPane = new JPanel();
                filamentPane.setLayout(new BoxLayout(filamentPane, BoxLayout.PAGE_AXIS));

                filamentPane.add(Box.createRigidArea(new Dimension(0, 5)));
                JLabel nazev = new JLabel(listFilament.get(i).getNazev());
                nazev.setFont(new Font(nazev.getFont().getFontName(), Font.BOLD, 16));
                nazev.setAlignmentX(JComponent.CENTER_ALIGNMENT);
                filamentPane.add(nazev);

                ImageIcon icon = new ImageIcon("./images/" + String.valueOf(listFilament.get(i).getId()) + ".png");
                Image image = icon.getImage(); // transform it
                Image newimage = image.getScaledInstance(300, 300, java.awt.Image.SCALE_SMOOTH);
                icon = new ImageIcon(newimage);
                //filamentPane.add(Box.createRigidArea(new Dimension(0, 10)));
                JLabel ikona = new JLabel(icon);
                ikona.setAlignmentX(JComponent.CENTER_ALIGNMENT);
                filamentPane.add(ikona);

                filamentPane.add(Box.createRigidArea(new Dimension(0, 10)));

                JLabel barva = new JLabel("Barva: " + listFilament.get(i).getBarva());
                barva.setFont(new Font(barva.getFont().getFontName(), Font.PLAIN, 15));
                barva.setAlignmentX(JComponent.CENTER_ALIGNMENT);
                filamentPane.add(barva);

                filamentPane.add(Box.createRigidArea(new Dimension(0, 5)));

                JLabel material = new JLabel("Materiál: " + listFilament.get(i).getMaterial());
                material.setFont(new Font(material.getFont().getFontName(), Font.PLAIN, 15));
                material.setAlignmentX(JComponent.CENTER_ALIGNMENT);
                filamentPane.add(material);

                filamentPane.add(Box.createRigidArea(new Dimension(0, 5)));

                JLabel hmotnost = new JLabel("Hmotnost: " + listFilament.get(i).getHmotnost() + " g");
                hmotnost.setFont(new Font(hmotnost.getFont().getFontName(), Font.PLAIN, 15));
                hmotnost.setAlignmentX(JComponent.CENTER_ALIGNMENT);
                filamentPane.add(hmotnost);

                filamentPane.add(Box.createRigidArea(new Dimension(0, 5)));

                JLabel idFilamentu = new JLabel("ID: " + listFilament.get(i).getId());
                idFilamentu.setFont(new Font(idFilamentu.getFont().getFontName(), Font.PLAIN, 15));
                idFilamentu.setAlignmentX(JComponent.CENTER_ALIGNMENT);
                filamentPane.add(idFilamentu);

                filamentPane.add(Box.createRigidArea(new Dimension(0, 5)));

                JLabel cenaFilamentu = new JLabel("Cena: " + listFilament.get(i).getCena() + " Kč");
                cenaFilamentu.setFont(new Font(cenaFilamentu.getFont().getFontName(), Font.PLAIN, 15));
                cenaFilamentu.setAlignmentX(JComponent.CENTER_ALIGNMENT);
                filamentPane.add(cenaFilamentu);

                filamentPane.add(Box.createRigidArea(new Dimension(0, 5)));

                Textgrid.add(filamentPane);
            }
        }

    }
    private void filamenty(){
        panelFilamenty.setLayout(new BorderLayout());
        loadFilament();
        vykresliFilament();
        JScrollPane jsp = new JScrollPane(Textgrid);
        jsp.getVerticalScrollBar().setUnitIncrement(16);
        panelFilamenty.add(jsp);
    }

    private void nastaveni(){
        labelNovyFilament.setFont(new Font(labelNovyFilament.getFont().getFontName(), Font.BOLD, 20));
        labelUpravit.setFont(new Font(labelUpravit.getFont().getFontName(), Font.BOLD, 20));
    }
    public static void main(String[] args) {
        com.formdev.flatlaf.FlatDarkLaf.install();
        GUIPrint g = new GUIPrint();
        g.setContentPane(g.panel1);
        g.setTitle("PrintEvidence");
        g.setSize(1200, 720);
        //g.setExtendedState(g.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        g.setVisible(true);
        g.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private static class ObjTableModel extends AbstractTableModel{

        private final String[] COLUMNS = {"ID", "Filament", "Datum", "Spotřeba", "Energie", "Vlastní Model"};
        private List<Objednavka> objednavky;

        public ObjTableModel(List<Objednavka> objednavky) {
            this.objednavky = objednavky;
        }

        @Override
        public int getRowCount() {
            return objednavky.size();
        }

        @Override
        public int getColumnCount() {
            return COLUMNS.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            String vlastniModel;
            if(objednavky.get(rowIndex).isVlastniModel() == true){
                vlastniModel = "ano";
            } else {vlastniModel = "ne";}
            return switch (columnIndex){
                case 0 -> String.valueOf(objednavky.get(rowIndex).getId());
                case 1 -> objednavky.get(rowIndex).getFilament();
                case 2 -> objednavky.get(rowIndex).getDatum().toString();
                case 3 -> String.valueOf(objednavky.get(rowIndex).getSpotřeba());
                case 4 -> String.valueOf(objednavky.get(rowIndex).getEnergie());
                case 5 -> vlastniModel;
                default -> "-";
            };
        }

        @Override
        public String getColumnName(int column) {
            return COLUMNS[column];
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (getValueAt(0, columnIndex) != null){
                return  getValueAt(0, columnIndex).getClass();
            } else {
                return Object.class;
            }
        }

    }
}
