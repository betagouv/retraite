echo 
# --- Ascii art : http://patorjk.com/software/taag/#p=display&f=Graceful&t=!!%20PROD%20!!%20PROD%20!!
echo
echo "  _  _    ____  ____   __  ____    _  _    ____  ____   __  ____    _  _   "
echo " / \/ \  (  _ \(  _ \ /  \(    \  / \/ \  (  _ \(  _ \ /  \(    \  / \/ \  "
echo " \_/\_/   ) __/ )   /(  O )) D (  \_/\_/   ) __/ )   /(  O )) D (  \_/\_/  "
echo " (_)(_)  (__)  (__\_) \__/(____/  (_)(_)  (__)  (__\_) \__/(____/  (_)(_)  "
echo
echo "ATTENTION : deploiement de la version 'PROD'"
echo
echo "Deployer [O/n] ?"
read REP
if [ "$REP" = "N" ] || [ "$REP" = "n" ]; then
	exit
fi

echo "Sûr de deployer en PROD [O/n] ?"
read REP
if [ "$REP" = "N" ] || [ "$REP" = "n" ]; then
	exit
fi

./scripts/deploy.sh prod
