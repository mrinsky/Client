package gui_interface;

import functional.UserData;
import lang.Strings_EN;
import lang.Strings_RU;
import functional.XmlFileWorking;
import org.jdom2.JDOMException;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.text.ParseException;

import static functional.UserData.loadData;

public class LoginWindow extends JFrame {

    //region Compoments
    String tempFromServer = null;
    XmlFileWorking xmlFileWorking = new XmlFileWorking();

    private JTextField loginTextField;
    private JPasswordField passTextField;
    JLabel regLabelButton;
    JLabel regText;
    JLabel logLabel;
    JLabel passLabel;
    JLabel languageLabel;
    JLabel wrongPassLabel;
    JComboBox langComboBox;

    private JButton okButton = new JButton("OK");
    private JButton guestButton;
    private JButton guestButtonInfo = new JButton("?");
    //endregion

    public LoginWindow() {
        super("Login");   // В дальнейшем сделать константой
        UserData.currentUser = null;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(addComponentsToForm());
        pack();
        setResizable(false);
        setLocationRelativeTo(null);// По центру экрана
        addListener();
        loginTextField.requestFocus();
    }

    public static void main() {
        LoginWindow loginWindow = new LoginWindow();
        loginWindow.setAlwaysOnTop(true);
        loginWindow.setVisible(true);
    }

    public String getLogin() {
        return loginTextField.getText();
    }

    public String getPassword() {
        return String.valueOf(passTextField.getPassword());
    }

    public Box addComponentsToForm() {

        initLabels();

        //region Login Box
        Box boxLogin = Box.createHorizontalBox();
        boxLogin.add(logLabel);
        boxLogin.add(Box.createHorizontalStrut(10));
        loginTextField = new JTextField(15);
        boxLogin.add(loginTextField);
        //endregion

        //region Password Box
        Box boxPass = Box.createHorizontalBox();
        boxPass.add(passLabel);
        boxPass.add(Box.createHorizontalStrut(10));
        passTextField = new JPasswordField(15);
        boxPass.add(passTextField);
        //endregion

        //region lang.Language Box
        Box languageBox = Box.createHorizontalBox();
        languageLabel = new JLabel(Resources.language.getLANGUAGE());
        langComboBox = new JComboBox();
        langComboBox.addItem("English");
        langComboBox.addItem("Русский");
        languageBox.add(languageLabel);
        languageBox.add(Box.createHorizontalStrut(10));
        languageBox.add(langComboBox);
        //endregion

        Box boxButtons = Box.createHorizontalBox();
        boxButtons.add(guestButton);
        boxButtons.add(Box.createHorizontalStrut(1));
        boxButtons.add(guestButtonInfo); //Как уменьшить размер кнопки

        boxButtons.add(Box.createHorizontalGlue());
        boxButtons.add(okButton);

        Box boxRegistration = Box.createHorizontalBox();

        regLabelButton.setFont(new Font("Courier",Font.BOLD, 12));
        regLabelButton.setForeground(Color.blue);

        regLabelButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        Box.createHorizontalGlue();
        boxRegistration.add(regText);
        boxRegistration.add(regLabelButton);

        Box wrongPassBox = Box.createHorizontalBox();
        wrongPassBox.add(wrongPassLabel, Component.CENTER_ALIGNMENT);
        wrongPassLabel.setSize(this.getWidth(), 30);
        wrongPassLabel.setFont(new Font("Courier", Font.ITALIC, 11));

        logLabel.setPreferredSize(passLabel.getPreferredSize()); // чтобы рамка логина  не выступала за рамку  пароля
        languageLabel.setPreferredSize(passLabel.getPreferredSize());
        passLabel.setPreferredSize(logLabel.getPreferredSize());

        Box frameBox = Box.createVerticalBox();
        frameBox.setBorder(new EmptyBorder(6, 6, 6, 6));

        //region FrameBoxAdding
        frameBox.add(boxLogin);
        frameBox.add(Box.createVerticalStrut(7));
        frameBox.add(boxPass);
        frameBox.add(Box.createVerticalStrut(7));
        frameBox.add(languageBox);
        frameBox.add(Box.createVerticalStrut(3));
        frameBox.add(wrongPassBox);
        frameBox.add(Box.createVerticalStrut(10));
        frameBox.add(boxButtons);
        frameBox.add(Box.createVerticalStrut(10));
        frameBox.add(boxRegistration);
        //endregion
        return frameBox;
    }

    private void initLabels() {

        guestButton = new JButton(Resources.language.getGUEST());
        regLabelButton = new JLabel(Resources.language.getCREATE_ACCOUNT());
        regText = new JLabel(Resources.language.getNO_ACCOUNT());
        logLabel = new JLabel(Resources.language.getLOGIN());
        passLabel = new JLabel(Resources.language.getPASSWORD());
        languageLabel = new JLabel(Resources.language.getLANGUAGE());
        wrongPassLabel = new JLabel(" ");
    }

    private void setLabels() {
        guestButton.setText(Resources.language.getGUEST());
        regLabelButton.setText(Resources.language.getCREATE_ACCOUNT());
        regText.setText(Resources.language.getNO_ACCOUNT());
        logLabel.setText(Resources.language.getLOGIN());
        passLabel.setText(Resources.language.getPASSWORD());
        languageLabel.setText(Resources.language.getLANGUAGE());
    }

    public void addListener() {
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//делаем обращение на сервер
                try {
                    String message = "1";
                   /* XmlFileWorking xml = new XmlFileWorking();
                    if (Resources.language instanceof Strings_RU) xml.loadAllRU(Resources.traditions,Resources.countries,Resources.holidays);
                    else xml.loadAllEN(Resources.traditions,Resources.countries,Resources.holidays);
*/                  MyClient.initSystem();
                    Resources.clientOut.println(xmlFileWorking.sendDataLogInToServer_LogIn(getLogin(), getPassword()));
                    if ((tempFromServer = Resources.clientIn.readLine())!= null){
                        System.out.println(tempFromServer);
                        message = tempFromServer;
                        tempFromServer = null;
                    }
                    MyClient.closeSystem();

                    if (!"ok".equals(message)) wrongPassLabel.setText(Resources.language.getLOGIN_OR_PASS_EXCEPTION());

                    else {
                       MyClient.initClient(getLogin());
                        Resources.clientOut.println("loadAllData");
                        Resources.clientOut.println("getCountry");
                        if ((tempFromServer = Resources.clientIn.readLine())!= null){
                            System.out.println(tempFromServer);
                            xmlFileWorking.stringToXML(Resources.TEMP_XML, tempFromServer);
                            Resources.countries = xmlFileWorking.loadCountry(Resources.TEMP_XML);
                            tempFromServer = null;
                        }
                        Resources.clientOut.println("getHoliday");{
                            if ((tempFromServer = Resources.clientIn.readLine())!= null){
                                System.out.println(tempFromServer);
                                xmlFileWorking.stringToXML(Resources.TEMP_XML, tempFromServer);
                                Resources.holidays = xmlFileWorking.loadHoliday(Resources.TEMP_XML);
                                tempFromServer = null;
                            }
                        }
                        Resources.clientOut.println("getTradition");{
                            if ((tempFromServer = Resources.clientIn.readLine())!= null){
                                System.out.println(tempFromServer);
                                xmlFileWorking.stringToXML(Resources.TEMP_XML, tempFromServer);
                                Resources.traditions = xmlFileWorking.loadTradition(Resources.TEMP_XML);
                                tempFromServer = null;
                            }


                        MainWindow.main(false);
                        dispose();
                   }
                }
                }  catch (NullPointerException ex){
                    JOptionPane.showMessageDialog(okButton, "Server off","Error",JOptionPane.INFORMATION_MESSAGE);}
                catch (SAXException e1) {
                    e1.printStackTrace();
                } catch (ParseException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (JDOMException e1) {
                    e1.printStackTrace();
                }

            }
        });

        langComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if ("English".equals(langComboBox.getSelectedItem())) {Resources.language = new Strings_EN();
                    setLabels();
                }
                else {
                    Resources.language = new Strings_RU();
                    setLabels();
                }
                wrongPassLabel.setText(" ");
            }
        });

        guestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = "";

                //loadTableData(message);
                MyClient.initSystem();
                try {
                    Resources.clientOut.println("loadAllData");
                    Resources.clientOut.println("getCountry");
                    if ((tempFromServer = Resources.clientIn.readLine())!= null){
                        System.out.println(tempFromServer);
                        xmlFileWorking.stringToXML(Resources.TEMP_XML, tempFromServer);
                        Resources.countries = xmlFileWorking.loadCountry(Resources.TEMP_XML);
                        tempFromServer = null;
                    }
                    Resources.clientOut.println("getHoliday");{
                        if ((tempFromServer = Resources.clientIn.readLine())!= null){
                            System.out.println(tempFromServer);
                            xmlFileWorking.stringToXML(Resources.TEMP_XML, tempFromServer);
                            Resources.holidays = xmlFileWorking.loadHoliday(Resources.TEMP_XML);
                            tempFromServer = null;
                        }
                    }
                    Resources.clientOut.println("getTradition");
                    {
                        if ((tempFromServer = Resources.clientIn.readLine()) != null) {
                            System.out.println(tempFromServer);
                            xmlFileWorking.stringToXML(Resources.TEMP_XML, tempFromServer);
                            Resources.traditions = xmlFileWorking.loadTradition(Resources.TEMP_XML);
                            tempFromServer = null;
                        }
                    }
                } catch (SAXException e1) {
                    e1.printStackTrace();
                } catch (ParseException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (JDOMException e1) {
                    e1.printStackTrace();
                }

                if (!message.isEmpty()) JOptionPane.showMessageDialog(null, message);
                else {
                    dispose();
                    MainWindow.main(true);
                }
            }
            });
        guestButtonInfo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = "", title = "";
                if (Resources.language instanceof Strings_EN) {
                    message = String.format("In guest mode you can't create or change events. \nFor adding personal events sign or log in.");
                    title = "Information";
                } else {
                    message = String.format("В гостевом режиме нельзя создавать и изменять события. \nДля добавления личных событий пройдите регистрацию или авторизуйтесь.");
                    title = "Информация";
                }

                JOptionPane.showMessageDialog(guestButtonInfo, message, title,
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        regLabelButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if ("English".equals(langComboBox.getSelectedItem())) Resources.language = new Strings_EN();
                else Resources.language = new Strings_RU();
                RegistrationForm registrationForm = new RegistrationForm();
                registrationForm.setVisible(true);
                dispose();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                regLabelButton.setForeground(Color.red);

            }

            @Override
            public void mouseExited(MouseEvent e) {
                regLabelButton.setForeground(Color.blue);
            }
        });

        loginTextField.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                wrongPassLabel.setText(" ");
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {

            }
        });

        passTextField.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                wrongPassLabel.setText(" ");
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {

            }
        });

    }
}
