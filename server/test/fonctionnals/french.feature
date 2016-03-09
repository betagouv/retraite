# language: fr
Fonctionnalité: Workflow après l'écran d'accueil
 
  #Contexte:
  #  Soit bla bla bla
  #  Et bli bli bli
  
  Scénario: Affiliation unique CNAV
    Soit Une personne avec l'affilitation CNAV
    Lorsque On choisit sa checklist
    Alors La checklist CNAV est sélectionnée
 
  Scénario: Affiliation unique MSA
    Soit Une personne avec l'affilitation MSA
    Lorsque On choisit sa checklist
    Alors La checklist MSA est sélectionnée
 
   Scénario: Affiliation unique RSI
    Soit Une personne avec l'affilitation RSI
    Lorsque On choisit sa checklist
    Alors La checklist RSI est sélectionnée
 
   Scénario: Affiliation double CNAV+RSI
    Soit Une personne avec les affilitations CNAV et RSI
    Et Le statut Salarié Agricole
    Lorsque On choisit sa checklist
    Alors La checklist CNAV est sélectionnée
    Et La condition SA est activée
 
 