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

- Se connecter à https://retraite.sgmap.fr
- Saisir "TOTO", "17/11/1954", et "1 22 33 44 555 666" dans les champs (nom, naissance et nir) puis cliquer sur "Etape suivante"
 - Vérifier qu'on passe à l'étape 2
- Coche "indépendant avec 1973" et cliquer sur "Etape suivante"
 - Vérifier qu'on passe à l'étape 3
- Changer l'année de départ en 2018 et cliquer sur "Etape suivante"
 - Vérifier qu'on passe à l'étape 4
- Coche "Non" et cliquer sur "Etape suivante"
 - Vérifier qu'on passe à l'étape 5 avec la checklist et qu'on a quelques données