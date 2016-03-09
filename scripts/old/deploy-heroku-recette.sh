#!/bin/sh

# Vérifications
if [ -n "$(git diff --cached --exit-code)" ]; then
	echo
	git status
	echo
	echo "Il y a des modifications à commiter (a)"
	echo
	exit
fi
if [ -n "$(git diff --exit-code)" ]; then
	echo
	git status
	echo
	echo "Il y a des modifications à commiter (b)"
	echo
	exit
fi

# Génération de l'application cliente
cd client
grunt build
cd ..

# Copie de l'application client dans le serveur
rm -rf server/www/*
cp -R client/www/ server/www

cd server

# Commit Git
git status
read -p "Une touche pour continuer ..."
git add .
git commit -am "deploy_$(date +%Y-%m-%d_%H-%M-%S)"

# Deploiement
git push recette master
heroku run "play evolutions:apply --%heroku-recette"
heroku restart
cd ..
