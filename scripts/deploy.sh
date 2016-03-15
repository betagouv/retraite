#!/bin/sh

ENV="$1"
REMOTE_USER=$ENV
REMOTE_DIR=$ENV

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
echo
echo "Génération de l'application cliente..."
echo
cd client
grunt build

if [ $? != 0 ]; then
	echo
	echo "Arrêt du déploiement car il y eu une erreur !"
	echo
    exit $?
fi

cd ..

# Tag Git
exit
git tag "deploy.$ENV.$(date +%Y-%m-%d_%H-%M-%S)"

# Copie de l'application client dans le serveur
rm -rf server/www/*
cp -R client/www/ server/www

cd server

# Deploiement
rsync -rv --exclude-from=rsync.exclude.txt --delete . $REMOTE_USER@vm_retraite:/home/$REMOTE_DIR/retraite
ssh $REMOTE_USER@vm_retraite "cd /home/$REMOTE_DIR/retraite && source ../set-retraite-env.sh && /home/deploy/play-1.3.1/play evolutions:apply --%$ENV && /home/deploy/play-1.3.1/play deps --sync && /home/deploy/play-1.3.1/play restart --%$ENV"
cd ..
