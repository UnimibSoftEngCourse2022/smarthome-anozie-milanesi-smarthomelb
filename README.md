# SmartHome
Smart Home mira a migliorare la qualità della vita dei residenti attraverso
un sistema che fornisce varie funzionalità, sia manuali che automatiche,
per vivere al meglio nella propria abitazione


## Settori di Competenza
- **Controllo dell'illuminazione** _manuale_ e _automatico_
- **Controllo della temperatura** _manuale_ e _automatico_
- **Controllo della pulizia** _manuale_
- **Controllo della sicurezza** _manuale_ e _automatico_


## Esecuzione del progetto
### Prerequisiti
- [Java 17 o maggiore](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [Maven 3.9.6 o maggiore](https://maven.apache.org/download.cgi)

### Passaggi

- Clonare la repository in locale utilizzando il comando
    ```
    git clone https://github.com/UnimibSoftEngCourse2022/smarthome-anozie-milanesi-smarthomelb.git
    ```

- Eseguire il file `smathome.jar` presente nella root del progetto per avviare l’applicazione 
simulativa di default della Smart Home

## Modifica del comportamento del sistema
Per poter modificare a proprio piacimento il comportamento del sistema e delle sue variabilli, nel 
percorso `src/main/resources` è presente il file `application.properties` che contiene tutte le variabili
del sistema simulativo che possono essere modificate a proprio piacimento. Per poter poi creare una 
nuova applicazione simulativa che si comporti diversamente in base ai cambiamenti effettuati, è necessario
creare un nuovo file `.jar`.

## Creazione di un nuovo file .jar
Da Intellij:
- Eseguire da terminale o con una Maven configuration il comando:
  ```
  mvn clean
  ```
- Nel menu principale vai in `File > Project Structure > Project Settings > Artifacts`, cliccare poi 
sul tasto `+` in alto a sinistra e seleziona `JAR > From moduls with dependecies`.
- Nella schermata che comparirà impostare nella voce `Main Class:` la classe `org.smarthome.App` e 
premere OK.
- Una volta finito premi OK.
- Nel menu principale vai in `Build > Build Artifacts…`, selezionare la build appena creata e premete 
`Build`.
- Verrà creato nel percorso `out/artifacts/smarthome_anozie_milanesi_smarthomelb_jar` il file 
`.jar` desiderato.

Per maggiori informazioni vedi questo [link](https://www.jetbrains.com/help/idea/compiling-applications.html#package_into_jar).
