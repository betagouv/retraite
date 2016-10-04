# --- !Ups

# Script inutile car données déjà migrées

# update Caisse 
# left join CaisseDepartementale on CaisseDepartementale.caisse_id=Caisse.id
# set telephone = null
# where CaisseDepartementale.checklistName = 'CNAV';

# --- !Downs

# Script inutile car données déjà migrées

# update Caisse 
# left join CaisseDepartementale on CaisseDepartementale.caisse_id=Caisse.id
# set telephone = '3960'
# where CaisseDepartementale.checklistName = 'CNAV';