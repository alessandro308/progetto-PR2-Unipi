## progetto-PR2
Progetto di realizzazione di un modulo OCaml e Java per il corso di Programmazione 2

#Java: Progettazione e realizzazione di un modulo Java
Si realizzi un modulo Java che simuli la gestione di un sistema di condivisione di documenti digitali. L’applicazione Java deve fornire le funzionalità per la gestione della repository dei documenti. Gli utenti devono poter prendere visione di, copiare, inserire ed eliminare documenti digitali. Ogni utente ha un nome (unico nel sistema) e una password.
Il programma deve essere costituito da un numero adeguato di classi per modellare le varie entità coinvolte: in particolare, ci dovrà essere una classe chiamata DigitalDoc con l’ovvio significato. Le strutture dati e le funzionalità devono essere distribuite tra le varie classi in modo da garantire l’incapsulamento.

Il sistema deve realizzare funzionalità per due tipologie di utenti: l'operatore e i clienti. L'operatore può creare una nuovo utente (assicurando l’unicità del nome e della password) o eliminarne uno già esistente (e di conseguenza tutti i suoi documenti digitali). Gli utenti (che sono i proprietari dei documenti digitali) possono inserire, leggere ed eliminare i propri documenti digitali o condividere un documento digitale con un altro utente. Le varie operazioni possono lanciare opportune eccezioni, i cui nomi sono indicati nell’interfaccia del punto seguente.

#OCaml:  Progettazione e sviluppo di un interprete in OCaml
Si consideri un semplice linguaggio funzionale che oltre a operazioni standard su interi e booleani comprende la definizione di un tipo di dati strutturato tuple, con relative operazioni. Una tupla è una struttura dati immutabile che contiene una sequenze ordinata di elementi di tipo eterogeneo. Ad esempio, la tupla t = [1, “hello”, ”world”, true] contiene quattro elementi: il primo elemento è il valore intero 1 , il secondo elemento è la stringa “hello”, etc. Ogni elemento di una tupla è identificato in modo univoco dalla sua posizione e pertanto si può accedere agli elementi di una tupla singolarmente indicandone la posizione. Ad esempio, t at 2 pemette di selezionare l’elemento in seconda posizione della tupla t, ovvero il valore “hello”. La sintassi concreta del linguaggio è definita dalla grammatica riportata in seguito.

Link alla grammatica completa: http://www.di.unipi.it/~gadducci/progettoAutunnale.pdf
