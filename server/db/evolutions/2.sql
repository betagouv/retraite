# --- !Ups

TRUNCATE Chapitre;

ALTER TABLE Chapitre ADD sortIndex integer NOT NULL DEFAULT '0';

# CNAV
INSERT INTO Chapitre (id, checklist_id, sortIndex, titre) VALUES (1, 1, 0, 'Je reconstitue ma carrière');
INSERT INTO Chapitre (id, checklist_id, sortIndex, titre) VALUES (2, 1, 1, 'Je contacte mon/mes régime(s) complémentaire(s)');
INSERT INTO Chapitre (id, checklist_id, sortIndex, titre) VALUES (3, 1, 2, 'Je contacte mes autres régimes (hors régimes alignés)');
INSERT INTO Chapitre (id, checklist_id, sortIndex, titre) VALUES (4, 1, 3, 'Je récupère mon dossier');
INSERT INTO Chapitre (id, checklist_id, sortIndex, titre) VALUES (5, 1, 4, 'Je notifie ma date de départ à mon employeur ou à Pôle emploi');
INSERT INTO Chapitre (id, checklist_id, sortIndex, titre) VALUES (6, 1, 5, 'Je constitue mon dossier');
INSERT INTO Chapitre (id, checklist_id, sortIndex, titre) VALUES (7, 1, 6, 'Je dépose mon dossier');
INSERT INTO Chapitre (id, checklist_id, sortIndex, titre) VALUES (8, 1, 7, 'Je suis l’avancée de mon dossier');
INSERT INTO Chapitre (id, checklist_id, sortIndex, titre) VALUES (9, 1, 8, 'J’obtiens ma retraite');

# RSI
INSERT INTO Chapitre (id, checklist_id, sortIndex, titre) VALUES (10, 2, 0, 'Je prépare ma cessation d’activité');
INSERT INTO Chapitre (id, checklist_id, sortIndex, titre) VALUES (11, 2, 1, 'Je reconstitue ma carrière');
INSERT INTO Chapitre (id, checklist_id, sortIndex, titre) VALUES (12, 2, 2, 'Je contacte mon/mes régime(s) complémentaire(s)');
INSERT INTO Chapitre (id, checklist_id, sortIndex, titre) VALUES (13, 2, 3, 'Je contacte mes autres régimes (hors régimes alignés)');
INSERT INTO Chapitre (id, checklist_id, sortIndex, titre) VALUES (14, 2, 4, 'Je récupère mon dossier');
INSERT INTO Chapitre (id, checklist_id, sortIndex, titre) VALUES (15, 2, 5, 'Je constitue mon dossier');
INSERT INTO Chapitre (id, checklist_id, sortIndex, titre) VALUES (16, 2, 6, 'Je dépose mon dossier');
INSERT INTO Chapitre (id, checklist_id, sortIndex, titre) VALUES (17, 2, 7, 'Je suis l’avancée de mon dossier');
INSERT INTO Chapitre (id, checklist_id, sortIndex, titre) VALUES (18, 2, 8, 'J’obtiens ma retraite');

# MSA
INSERT INTO Chapitre (id, checklist_id, sortIndex, titre) VALUES (19, 3, 0, 'Je prépare ma cessation d’activité');
INSERT INTO Chapitre (id, checklist_id, sortIndex, titre) VALUES (20, 3, 1, 'Je reconstitue ma carrière');
INSERT INTO Chapitre (id, checklist_id, sortIndex, titre) VALUES (21, 3, 2, 'Je contacte mon / mes régime(s) complémentaire(s)');
INSERT INTO Chapitre (id, checklist_id, sortIndex, titre) VALUES (22, 3, 3, 'Je contacte mes autres régimes (hors régimes alignés)');
INSERT INTO Chapitre (id, checklist_id, sortIndex, titre) VALUES (23, 3, 4, 'Je notifie ma date de départ à mon employeur ou à Pôle emploi');
INSERT INTO Chapitre (id, checklist_id, sortIndex, titre) VALUES (24, 3, 5, 'Je récupère mon dossier');
INSERT INTO Chapitre (id, checklist_id, sortIndex, titre) VALUES (25, 3, 6, 'Je constitue mon dossier');
INSERT INTO Chapitre (id, checklist_id, sortIndex, titre) VALUES (26, 3, 7, 'Je dépose mon dossier');
INSERT INTO Chapitre (id, checklist_id, sortIndex, titre) VALUES (27, 3, 8, 'Je suis l’avancée de mon dossier');
INSERT INTO Chapitre (id, checklist_id, sortIndex, titre) VALUES (28, 3, 9, 'J’obtiens ma retraite');

# --- !Downs

ALTER TABLE Chapitre DROP sortIndex;

