description: 'Given some informations should display 5 pages and the user checklist',

steps: [
    /* @Matti : comment juste vérifier l'état d'un bouton qui devrait être disabled
    {
        'HomePageComponent.nextStepButton': disabled
    },
    */
    NextStepComponent.nextStep(),
	HomePageComponent.fill("BBBB", "17/11/1954", "2000406111111"),
    NextStepComponent.nextStep(),
    {
		'QuestionStepDatePageComponent.title': "A QUELLE DATE AVEZ-VOUS PRÉVU DE PRENDRE VOTRE RETRAITE ?"
	},
    QuestionStepDatePageComponent.setYear2018(),
    /* @Matti : je préfèrerai faire ça (voir dans QuestionStep3PageComponent)
    QuestionStep3PageComponent.selectYear(2018),
    */
    NextStepComponent.nextStep(),
    {
		'QuestionStepDepartementPageComponent.title': "OÙ HABITEZ-VOUS ?"
	},
    QuestionStepDepartementPageComponent.selectDepartement05(),
    NextStepComponent.nextStep(),
    {
		'QuestionStepChecklistPageComponent.title': new RegExp('^Mme BBBB,'), 
		'QuestionStepChecklistPageComponent.paragraphe1Chiffre': "1",
		'QuestionStepChecklistPageComponent.paragraphe1Delai': "LE PLUS TÔT POSSIBLE",
		'QuestionStepChecklistPageComponent.paragraphe1Titre': "Je créé mon espace personnel"
	}
]
