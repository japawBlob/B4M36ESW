# Zpětná vazba k některým úkolům:

## 02bench

Počty opakování pro samotné měření by měly v reportu být dříve než v závěru a počty opakování k určení warm-upu chybí úplně


 - Bylo by přehlednější znázornit zvolený warm-up i přímo do grafu.: -0.01
 - Zvolený warm-up pro každou implementaci je nutné hledat v textu - souhrná tabulka by přidala na přehlednosti: -0.25
 - Chybí reference na rovnice výpočtů nebo případně samotné vzorce.: -0.1
 - Chybí počty opakování pro určení warm-upu: -0.25
 - Závěr by měl obsahovat alespoň ty nejdůležitější informace - tj. i o kolik/kolikrát je daná implementace nejrychlejší: -0.25
 - V závěru by bylo dobré se zamyslet nad důvodem, proč je násobení s transponovanou maticí o tolik rychlejší: -0.01

celkově -0.87b

## 03epoll_s

- #define pod include, ne uprostřed kódu

bez penalizace

## 08server_c (rust)

- opravdu funguje `Cargo build` a `Cargo run` s velkym `C` na zacatku prikazu?
- project description is incomplete
- missing license
- do not upload generated files (`Cargo.lock`, `esw_server.rs`)
- error handling could be done better, i.e. use `?;` instead of `.unwrap();`

bez penalizace

## 08server_j

 - Funkční řešení: 9
