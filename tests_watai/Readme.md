Tests End-To-End en utilisant : https://github.com/MattiSG/Watai

# Prérequis

Avoir installé Watai et Selenium Server :

```sh
    npm install --global selenium-server watai
```

Watai est disponible :
```sh
    watai --installed
```

Avoir lancé le serveur Selenium :
```sh
    selenium
```

# Exécution

En étant dans le répertoire `tests_watai` :
- Soit par le script `run.sh`:
```sh
    ./run.sh
```

- Soit en lançant directement `watai`:
```sh
    watai .
```

# Principe du test

- Se connecter à https://retraite.beta.gouv.fr/?test
- Saisir "BBBB", "17/11/1954", et "2000406111111" dans les champs (nom, naissance et nir) puis cliquer sur "Etape suivante"
- Vérifier qu'on passe à l'étape suivante (date de départ)
- Changer l'année de départ en 2018 et cliquer sur "Etape suivante"
- Vérifier qu'on passe à l'étape suivante (département)
- Sélectionner le département 05
- Vérifier qu'on passe à l'étape suivante avec la checklist et qu'on a quelques données