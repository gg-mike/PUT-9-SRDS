# SRDS PROJECT


### Opis aplikacji:

#### 1. Aplikacja z interfejesem REST pozwalająca na żądanie przez klienta pewnej liczby produktu

Brak ograniczeń jeżeli chodzi o zbieranie żądań od klientów. Klient może być informowany o statusie żądania asynchronicznie.

#### 4. Przetwarzanie asynchroniczne - N żądań przetwarzanych cyklicznie w odstępach M sekund

Pozwala na rozłącznie procesu przyjmowania żądań od ich przetwarzania, co w modelu w którym musimy oczekiwać na potwierdzenie czy dany produkt został pomyślnie zarezerwowany może zająć dłuższą chwilę. 
Nie stanowi problemu ze względu na charakterystykę zagadnienia - potwierdzenie zarezerwowania produktu zazwyczaj następuje po dłuższej chwili.

#### 3. Wzorzec select - update - select

Pozwala na uniknięcie sytuacji wynikającej z przetwarzania na wielu węzłach aplikacji, gdzie potencjalnie wiele instancji może próbować zarezerwować ten sam produkt.

Reprezentacja każdego produktu jako wiersza zapobiega problemowi zliczania stanu magazynowego danego produktu.

### Uruchomienie i konfiguracja aplikacji:

Skryptem: ./run.sh {SERVER_PORT} {CASSANDRA_PORT} {APP_ID}
