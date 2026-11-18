package com.tiketin.util;

import java.util.function.Function;

import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public final class Tabel {
    private Tabel() {
    }

    public static void penuh(TableView<?> tabel) {
        tabel.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        tabel.setPlaceholder(new Label("Belum ada data."));
    }

    public static <T> void chip(TableColumn<T, String> kolom, Function<T, String> label, Function<T, String> gaya) {
        kolom.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(label.apply(data.getValue())));
        kolom.setCellFactory(kol -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getIndex() < 0 || getTableView() == null
                        || getIndex() >= getTableView().getItems().size()) {
                    setText(null);
                    setGraphic(null);
                    return;
                }
                T baris = getTableView().getItems().get(getIndex());
                Label chip = new Label(label.apply(baris));
                chip.getStyleClass().setAll("chip", gaya.apply(baris));
                setGraphic(chip);
                setText(null);
            }
        });
    }
}
