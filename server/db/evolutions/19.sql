# --- !Ups

# Script inutile car données déjà migrées

# Suppression des conditions de type 'carriere-a-reconstituer'

# delete ConditionProps from ChapitreCondition
# left join ConditionProps on ConditionProps.condition_id=ChapitreCondition.id
# where valeur='carriere-a-reconstituer';

# Suppression des conditions sans type
 
# delete ChapitreCondition from ChapitreCondition
# left join ConditionProps on ConditionProps.condition_id=ChapitreCondition.id
# where valeur is null;
# 

# --- !Downs

