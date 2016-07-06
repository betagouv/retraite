#!/bin/sh

ENV="$1"
REMOTE_USER=$ENV
REMOTE_DIR=$ENV
# SERVER_NAME=retraite.infra.beta.gouv.fr
SERVER_NAME=vm_retraite

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

# Vérification que la branche courante est la bonne

CURRENT_GIT_BRANCH=$(git branch | grep ^* | sed 's/* //' )
if [[ ($CURRENT_GIT_BRANCH != $ENV) && (($CURRENT_GIT_BRANCH != "master") || ($ENV != "recette")) ]]; then
	echo
	echo "La branche GIT courante '$CURRENT_GIT_BRANCH' n'est pas la bonne pour faire le déploiement '$ENV' !"
	echo
	exit
fi

# Génération de l'application cliente
echo
echo "Génération de l'application cliente ..."
echo
cd client
grunt build
if [ $? != 0 ]; then
    echo
    echo "Il y a eu une erreur : arrêt du déploiement !"
    echo
    exit $?
fi
cd ..

# Copie de l'application client dans le serveur
echo
echo "Copie de l'application client dans le serveur..."
echo
rm -rf server/www/*
cp -R client/www/ server/www
if [ $? != 0 ]; then
    echo
    echo "Il y a eu une erreur : arrêt du déploiement !"
    echo
    exit $?
fi

cd server

# Génération de l'application WEB pour le serveur
echo
echo "Génération de l'application WEB serveur ..."
echo
grunt build
if [ $? != 0 ]; then
    echo
    echo "Il y a eu une erreur : arrêt du déploiement !"
    echo
    exit $?
fi

# Commit Git
echo
echo "Git : commit et tag..."
echo
git status
# read -p "Une touche pour continuer ..."
git add .
git commit -am "deploy_$ENV_$(date +%Y-%m-%d_%H-%M-%S)"

# Tag Git
git tag "deploy.$ENV.$(date +%Y-%m-%d_%H-%M-%S)"

# Deploiement
echo
echo "Déploiement..."
echo
rsync -rv --exclude-from=rsync.exclude.txt --delete . $REMOTE_USER@$SERVER_NAME:/home/$REMOTE_DIR/retraite
if [ $? != 0 ]; then
    echo
    echo "Il y a eu une erreur : arrêt du déploiement !"
    echo
    exit $?
fi
ssh $REMOTE_USER@$SERVER_NAME "cd /home/$REMOTE_DIR/retraite && source ../set-retraite-env.sh && /home/deploy/play-1.4.2/play evolutions:apply --%$ENV && /home/deploy/play-1.4.2/play deps --sync && /home/deploy/play-1.4.2/play restart --%$ENV"
if [ $? != 0 ]; then
    echo
    echo "Il y a eu une erreur : arrêt du déploiement !"
    echo
    exit $?
fi
cd ..

echo "Déploiement terminé avec succès ! :-)"