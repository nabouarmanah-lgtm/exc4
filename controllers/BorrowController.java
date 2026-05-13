package controllers;

import dao.BookDAO;
import dao.BorrowDAO;
import dao.StudentDAO;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import models.Book;
import models.Borrow;
import models.Student;

public class BorrowController implements Initializable {

    @FXML private ComboBox<Integer> booksCombobox;
    @FXML private ComboBox<Integer> studentsCombobox;
    @FXML private DatePicker borrowDate;
    @FXML private DatePicker returnDate;
    @FXML private CheckBox status;
    @FXML private TableView<Borrow> table;
    @FXML private TableColumn<Borrow, Integer> borrowIdTC;
    @FXML private TableColumn<Borrow, Integer> bookIdTC;
    @FXML private TableColumn<Borrow, Integer> studentIdTC;
    @FXML private TableColumn<Borrow, String> borrowDateTC;
    @FXML private TableColumn<Borrow, String> returnDateTC;
    @FXML private TableColumn<Borrow, Boolean> statusTC;

    BookDAO bookDAO = new BookDAO();
    StudentDAO studentDAO = new StudentDAO();
    BorrowDAO borrowDAO = new BorrowDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        booksCombobox.getItems().addAll(bookDAO.getAllbooksids());
        studentsCombobox.getItems().addAll(studentDAO.getAllStudentsids());

        borrowIdTC.setCellValueFactory(
                data -> new javafx.beans.property
                        .SimpleIntegerProperty(
                                data.getValue().getBorrowId()
                        ).asObject()
        );

        bookIdTC.setCellValueFactory(
                data -> new javafx.beans.property
                        .SimpleIntegerProperty(
                                data.getValue().getBook().getBookId()
                        ).asObject()
        );

        studentIdTC.setCellValueFactory(
                data -> new javafx.beans.property
                        .SimpleIntegerProperty(
                                data.getValue().getStudent().getStudentId()
                        ).asObject()
        );

        borrowDateTC.setCellValueFactory(
                data -> new javafx.beans.property
                        .SimpleStringProperty(
                                data.getValue().getBorrowDate().toString()
                        )
        );

        returnDateTC.setCellValueFactory(
                data -> new javafx.beans.property
                        .SimpleStringProperty(
                                data.getValue().getReturnDate() != null
                                        ? data.getValue().getReturnDate().toString()
                                        : ""
                        )
        );

        statusTC.setCellValueFactory(
                data -> new javafx.beans.property
                        .SimpleBooleanProperty(
                                data.getValue().isStatus()
                        )
        );
    }

    @FXML
    public void viewHandle() {
        table.getItems().setAll(borrowDAO.findAll());
    }

    @FXML
    public void borrowHandle() {
        if (booksCombobox.getValue() == null
                || studentsCombobox.getValue() == null
                || borrowDate.getValue() == null) {
            showWarning("Missing Data");
            return;
        }

        Student s = studentDAO.findById(studentsCombobox.getValue());
        Book b = bookDAO.findById(booksCombobox.getValue());

        Borrow bor = new Borrow(s, b, borrowDate.getValue(), null, false);

        if (borrowDAO.insert(bor)) {
            showInfo("Borrow Done");
            viewHandle();
        }
    }

    @FXML
    public void returnHandle() {
        Borrow selected = table.getSelectionModel().getSelectedItem();

        if (selected == null || returnDate.getValue() == null) {
            showWarning("Select record + date");
            return;
        }

        selected.setReturnDate(returnDate.getValue());
        selected.setStatus(true);

        if (borrowDAO.update(selected)) {
            showInfo("Returned");
            viewHandle();
        }
    }

    @FXML
    public void deleteHandle() {
        Borrow selected = table.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showWarning("Select record");
            return;
        }

        if (borrowDAO.delete(selected)) {
            showInfo("Deleted");
            viewHandle();
        }
    }

    @FXML
    public void borrowedBooksHandle() {
        table.getItems().setAll(borrowDAO.getBorrowedBooks());
    }

    @FXML
    public void searchbyIds() {
        if (booksCombobox.getValue() == null
                || studentsCombobox.getValue() == null) {
            showWarning("Select both IDs");
            return;
        }

        table.getItems().setAll(
                borrowDAO.searchByIds(
                        booksCombobox.getValue(),
                        studentsCombobox.getValue()
                )
        );
    }

    private void showWarning(String msg) {
        new Alert(AlertType.WARNING, msg).showAndWait();
    }

    private void showInfo(String msg) {
        new Alert(AlertType.INFORMATION, msg).showAndWait();
    }
}