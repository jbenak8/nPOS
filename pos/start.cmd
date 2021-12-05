@echo off
java -Dlog4j.configurationFile=nastaveni/logging.xml --module-path lib/fx --add-modules javafx.controls,javafx.fxml -jar pos.jar
rem heslo nPos
