@echo off
java -Dlog4j.configurationFile=conf/logging.xml --module-path lib/fx --add-modules javafx.controls,javafx.fxml -jar bo-client.jar