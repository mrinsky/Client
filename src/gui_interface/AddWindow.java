package gui_interface;

import functional.*;
import model.Country;
import model.Holiday;
import model.HolidayType;
import model.Tradition;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;

public class AddWindow extends JFrame {

    // region Components

    private JPanel date;
    private UtilDateModel model;
    private JDatePickerImpl calendar;
    private JDatePanelImpl datePanel;

    private JButton saveButton;
    private JComboBox holidayBox;
    private JComboBox countryBox;
    private JTextField descriptionField;
    private JLabel addTitle;
    private JComboBox typeBox;
    private JLabel notFilled;

    XmlFileWorking xmlFileWorking = new XmlFileWorking();
    String temp = "";
    // endregion

    //region Конструкторы
    public AddWindow() {
        // Конструктор для добавления новой традиции
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setBounds(200, 200, 300, 450);
        this.setResizable(false);
        initComponents();
        saveButton.addActionListener(new AddListener());
    }

    public AddWindow(Tradition tradition, int index) {
        // Конструктор для изменения существующей традиции
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setBounds(200, 200, 300, 450);
        this.setResizable(false);
        initComponents();

        addTitle.setText(Resources.language.getCHANGE());
        // Заполняем поля формы информацией о переданной традиции
        this.holidayBox.setSelectedItem(tradition.getHoliday().getName());
        this.countryBox.setSelectedItem(tradition.getCountry().getName());
        this.descriptionField.setText(tradition.getDescription());
        this.typeBox.setSelectedItem(tradition.getHoliday().getType());
        saveButton.addActionListener(new AddListener(true, index));
    }
    //endregion

    //region Методы main
    public static void main() {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AddWindow().setVisible(true);
            }
        });
    }

    public static void main(final Tradition tradition, final int id) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AddWindow(tradition, id).setVisible(true);
            }
        });
    }
    //endregion

    // Инициализация компонентов
    public void initComponents() {
        BoxLayout box = new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS);
        this.setLayout(box);
        addTitle = new JLabel(Resources.language.getCREATE_TITLE());
        addTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(addTitle);

        initCountryBox();
        initHolidayBox();
        initTypeBox();
        date = new JPanel();
        date.setBorder(BorderFactory.createTitledBorder(Resources.language.getDATE()));
        this.add(date);
        date.setEnabled(false);
        initCalendar();
        this.add(Box.createVerticalStrut(10));
        initField();
        this.add(Box.createVerticalStrut(10));
        initLabel();
        this.add(Box.createVerticalStrut(10));
        initButton();
        this.add(Box.createVerticalStrut(10));
    }

    // Инициализация лейбла о незаполненных полях
    private void initLabel() {
        notFilled = new JLabel(Resources.language.getNOT_FILLED());
        // Изначально невидим
        notFilled.setVisible(false);
        notFilled.setAlignmentX(Component.CENTER_ALIGNMENT);
        notFilled.setForeground(Color.red);
        notFilled.setFont(new Font("Courier", Font.BOLD, 12));
        this.add(notFilled);
    }

    // Инициализация календаря
    private void initCalendar() {
        model = new UtilDateModel();
        Properties pr = new Properties();
        pr.put("text.today", "Today");
        pr.put("text.month", "Month");
        pr.put("text.year", "Year");
        datePanel = new JDatePanelImpl(model, pr);
        calendar = new JDatePickerImpl(datePanel, new DateLabelFormatter());
    }

    // Инициализация списка праздников
    private void initHolidayBox() {
        holidayBox = new JComboBox();

        for (Holiday holiday : Resources.holidays) {
            holidayBox.addItem(holiday.getName());
        }
        // Элемент "Добавить"
        holidayBox.addItem(Resources.language.getADD());
        holidayBox.setSelectedItem(null);
        holidayBox.setBorder(BorderFactory.createTitledBorder(Resources.language.getHOLIDAY_ITEM()));
        this.add(holidayBox);
        holidayBox.addActionListener(new HolidayListener(holidayBox));
    }

    // Инициализация списка стран
    private void initCountryBox() {
        countryBox = new JComboBox();

        for (Country country : Resources.countries) {
            countryBox.addItem(country.getName());
        }
        // Элемент "Добавить"
        countryBox.addItem(Resources.language.getADD());
        countryBox.setSelectedItem(null);
        countryBox.setBorder(BorderFactory.createTitledBorder(Resources.language.getCOUNTRY_ITEM()));
        this.add(countryBox);

        countryBox.addActionListener(new CountryListener(countryBox));
    }

    private void initButton() {
        saveButton = new JButton("OK");
        saveButton.setPreferredSize(new Dimension(50, 50));
        saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(saveButton);
    }

    // Инициализация поля для описания
    private void initField() {
        descriptionField = new JTextField();
        descriptionField.setBorder(BorderFactory.createTitledBorder(Resources.language.getENTER_DESCRIPTION()));
        this.add(descriptionField);
    }

    // Иницализация списка типов
    private void initTypeBox() {
        typeBox = new JComboBox();

        for (HolidayType type : HolidayType.values()) {
            typeBox.addItem(type);
        }

        typeBox.setSelectedItem(null);
        typeBox.setBorder(BorderFactory.createTitledBorder(Resources.language.getTABLE_TYPE()));
        typeBox.setEnabled(false);
        this.add(typeBox);
    }

    // Обработчик событий для списка стран
    private class CountryListener implements ActionListener {

        private JComboBox comboBox;
        // Переменные, фиксирующие последнюю введенную новую страну
        private String last;
        private boolean isItNew;

        public CountryListener(JComboBox comboBox) {
            this.comboBox = comboBox;
            last = "";
            isItNew = false;
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {

            notFilled.setVisible(false);
            // Если выбран элемент "Добавить"
            if (comboBox.getSelectedIndex() == comboBox.getItemCount() - 1) {
                // Флаг новой страны
                isItNew = true;
                // Делаем доступными поля для редактирования и блокируем поля работы с праздниками
                holidayBox.setEditable(false);
                comboBox.setEditable(true);
                comboBox.setSelectedItem(last);
                typeBox.setEnabled(false);
                date.setEnabled(false);
                if (date.getComponentCount() != 0) date.remove(0);

            } else {
                // Сохраняем последнюю введенную строку
                if (isItNew) last = comboBox.getSelectedItem().toString();
                // Сбрасываем флаг
                isItNew = false;
                if (isItem(last, comboBox)) last = "";
                // Возвращаем поля в исходное положение
                comboBox.setEnabled(true);
                holidayBox.setEditable(false);
                comboBox.setEditable(false);
                typeBox.setEnabled(false);
                date.setEnabled(false);
                if (date.getComponentCount() != 0) date.remove(0);
            }
        }
    }

    // Обработчик событий для списка праздников
    private class HolidayListener implements ActionListener {
        private JComboBox comboBox;
        private boolean isItNew;
        private String lastHoliday;

        public HolidayListener(JComboBox comboBox) {
            this.comboBox = comboBox;
            isItNew = false;
            lastHoliday = "";
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            notFilled.setVisible(false);
            if (comboBox.getSelectedIndex() == comboBox.getItemCount() - 1) {

                isItNew = true;
                countryBox.setEditable(false);
                comboBox.setEditable(true);
                comboBox.setSelectedItem(lastHoliday);
                typeBox.setEnabled(true);
                date.setEnabled(true);
                date.add(calendar);

            } else {

                if (isItNew) lastHoliday = comboBox.getSelectedItem().toString();
                isItNew = false;
                if (isItem(lastHoliday, comboBox)) {
                    lastHoliday = "";
                    comboBox.setEditable(false);
                    typeBox.setEnabled(false);
                    date.setEnabled(false);
                    if (date.getComponentCount() != 0) date.remove(0);
                }
            }
        }
    }

    // Проверка на существующий в списке элемент
    private static boolean isItem(String s, JComboBox comboBox) {
        for (int i = 0; i < comboBox.getItemCount() - 1; i++) {
            if (s.equals(comboBox.getItemAt(i).toString())) return true;
        }
        return false;
    }

    // Обработчик нажатия на кнопку
    private class AddListener implements ActionListener {

        // Доступно 2 режима - добавление и изменение
        private boolean isChanging = false;
        private int id;

        public AddListener() {
        }

        public AddListener(boolean check, int id) {
            this.isChanging = check;
            this.id = id;
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {

            try {
                notFilled.setVisible(false);
                // Создаем праздник на основе введенных данных
                Country country;
                if (!isItem(countryBox.getSelectedItem().toString(), countryBox)) {
                    country = new Country(countryBox.getSelectedItem().toString());
                    // Отправляем на сервер команду добавления страны
                    Resources.clientOut.println(xmlFileWorking.sendCountryToServer_ADD(country));
                    if ((temp = Resources.clientIn.readLine()) != null) {
                        xmlFileWorking.stringToXML(Resources.TEMP_XML, temp);
                        Resources.countries = xmlFileWorking.loadCountry(Resources.TEMP_XML);
                        temp = null;
                    }
                } else country = Resources.countries.get(countryBox.getSelectedIndex());
                // Создаем праздник
                Holiday holiday;
                if (!isItem(holidayBox.getSelectedItem().toString(), holidayBox)) {

                    GregorianCalendar cal = (GregorianCalendar) calendar.getJFormattedTextField().getValue();
                    Date date = cal.getTime();

                    holiday = new Holiday(holidayBox.getSelectedItem().toString(), date, typeBox.getSelectedIndex());
                    // Отправляем на сервер команду добавления праздника
                    Resources.clientOut.println(xmlFileWorking.sendHolidaysToServer_ADD(holiday));
                    System.out.println(xmlFileWorking.sendHolidaysToServer_ADD(holiday));
                    if ((temp = Resources.clientIn.readLine()) != null) {
                        System.out.println(temp);
                        xmlFileWorking.stringToXML(Resources.TEMP_XML, temp);
                        Resources.holidays = xmlFileWorking.loadHoliday(Resources.TEMP_XML);
                        temp = null;
                    }
                } else holiday = Resources.holidays.get(holidayBox.getSelectedIndex());

                // Если мы не в режиме изменения, отправляем на сервер команду добавления новой традиции
                if (!isChanging) {
                    Resources.clientOut.println(xmlFileWorking.sendTraditionToServer_ADD(new Tradition(holiday, country, descriptionField.getText())));

                    if ((temp = Resources.clientIn.readLine()) != null) {
                        xmlFileWorking.stringToXML(Resources.TEMP_XML, temp);
                        Resources.traditions = xmlFileWorking.loadTradition(Resources.TEMP_XML);
                        temp = null;
                    }
                }
                else {
                    //Resources.traditions = (ArrayList<Tradition>)Change.editTradition(id, holiday, country, descriptionField.getText(), Resources.traditions);
                    //xmlFileWorking.saveTradition(Resources.traditions,Resources.TEMP_XML);
                    //Resources.clientOut.println(xmlFileWorking.xmlToString(Resources.TEMP_XML));
                    //Сделать изменение
                }

                Container container = saveButton.getParent();
                do {
                    container = container.getParent();
                } while (!(container instanceof AddWindow));

                MainWindow.restart();
                processEvent(new WindowEvent((AddWindow) container, WindowEvent.WINDOW_CLOSING));

            } catch (Exception ex) {
                notFilled.setVisible(true);
            }
        }
    }
}

