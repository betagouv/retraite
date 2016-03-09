# --- !Ups

# Pas le même nom de base en local et sur le serveur
# ALTER DATABASE recette CHARACTER SET utf8 COLLATE utf8_unicode_ci;

ALTER TABLE Chapitre CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci;

# --- !Downs

