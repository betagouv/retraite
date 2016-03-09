description: 'Given some informations should display 5 pages and the user checklist',

steps: [
    /* @Matti : comment juste vérifier l'état d'un bouton qui devrait être disabled
    {
        'HomePageComponent.nextStepButton': disabled
    },
    */
	HomePageComponent.fill("TOTO", "17/11/1954", "1 22 33 44 555 666"),
    NextStepComponent.nextStep(),
    {
        // @Matti : Je préfèrerai juste vérifier que c'est la bonne page
        // 'QuestionStep2PageComponent.isGoodPage': true,
        
		'QuestionStep2PageComponent.title1': "ETES-VOUS DANS L'UNE OU PLUSIEURS DES SITUATIONS SUIVANTES ?",
        /*'QuestionStep2PageComponent.submitButton': function(nextButton) {
            // @Matti : quel est le type d'objet reçu ?
            return nextButton.getAttribute('disabled')
					.then(function(attribute) {
                        log(attribute);
						assert.equal(attribute, 'disabled', 'Expected disabled for next button');
					});        
        },*/
        
        /* @Matti : ou ici , comment vérifier que la question suivante n'est pas visible (le div est en display=none)
        */
	},
    QuestionStep2PageComponent.checkIndepAvant73(),
    NextStepComponent.nextStep(),
    {
		'QuestionStep3PageComponent.title': "A QUELLE DATE AVEZ-VOUS PRÉVU DE PRENDRE VOTRE RETRAITE ?"
	},
    QuestionStep3PageComponent.setYear2018(),
    /* @Matti : je préfèrerai faire ça (voir dans QuestionStep3PageComponent)
    QuestionStep3PageComponent.selectYear(2018),
    */
    NextStepComponent.nextStep(),
    {
		'QuestionStep4PageComponent.title': "AVEZ-VOUS CONSULTÉ VOTRE RELEVÉ DE CARRIÈRE ?"
	},
    QuestionStep4PageComponent.checkCarriereNon(),
    NextStepComponent.nextStep(),
    {
		'QuestionStep5PageComponent.title': "MES DÉMARCHES PAS À PAS",
		'QuestionStep5PageComponent.paragrapheTitle': "JE PRÉPARE MA CESSATION D’ACTIVITÉ"
	}
]
